package less.lgeo.tracer.camera;

import less.lgeo.common.Interval;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.HittableList;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import lombok.Getter;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static less.lgeo.common.Vector3dUtils.*;


//FIXME, this really needs a good refactor with tests

@Getter
public class Camera {

    private static final Logger logger = LoggerFactory.getLogger(Camera.class);
    private final CameraSettings settings;

    private final Vector3d pixelDeltaU; // Offset to pixel below
    private final Vector3d pixelDeltaV; // Offset to pixel below

    private final Vector3d defocusDiskU; // Defocus disk horizontal radius
    private final Vector3d defocusDiskV; // Defocus disk vertical radius

    private final Vector3d pixelLocation00; // Location of pixel 0, 0

    public Camera(CameraSettings settings) {
        this.settings = settings;

        double theta = Math.toRadians(settings.verticalFOV());
        double h = Math.tan(theta / 2);
        double viewportHeight = 2 * h * settings.focusDist();
        double viewportWidth = viewportHeight * ((double) (settings.imageWidth()) / settings.imageHeight());

        // Calculate the u,v,w unit basis vectors for the camera coordinate frame.
        Vector3d w = unitVector(new Vector3d(settings.position()).sub(settings.lookAt()));
        Vector3d u = unitVector(new Vector3d(settings.up()).cross(w));
        Vector3d v = new Vector3d(w).cross(u);

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vector3d viewportU = new Vector3d(u).mul(viewportWidth);    // Vector across viewport horizontal edge
        Vector3d viewportV = new Vector3d(v).negate().mul(viewportHeight);  // Vector down viewport vertical edge

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        this.pixelDeltaU = new Vector3d(viewportU).div(settings.imageWidth());
        this.pixelDeltaV = new Vector3d(viewportV).div(settings.imageHeight());

        // Calculate the location of the upper left pixel.
        Vector3d viewportUpperLeft = new Vector3d(settings.position())
                .sub(new Vector3d(w).mul(settings.focusDist()))
                .sub(new Vector3d(viewportU).div(2))
                .sub(new Vector3d(viewportV).div(2));

        // Calculate the camera defocus disk basis vectors.
        double defocusRadius = settings.focusDist() * Math.tan(Math.toRadians(settings.defocusAngle() / 2));
        defocusDiskU = new Vector3d(u).mul(defocusRadius);
        defocusDiskV = new Vector3d(v).mul(defocusRadius);

        // Upper-left pixel center
        this.pixelLocation00 = new Vector3d(viewportUpperLeft)
                .add(new Vector3d(pixelDeltaU).add(pixelDeltaV).mul(0.5));


        logger.info(
                """
                        theta: {}
                        h: {}
                        viewportHeight: {}
                        viewportWidth: {}
                        w: {}
                        u: {}
                        v: {}
                        viewportU: {}
                        viewportV: {}
                        pixelDeltaU: {}
                        pixelDeltaV: {}
                        viewportUpperLeft: {}
                        pixelLocation00: {}
                        """,
                theta,
                h,
                viewportHeight,
                viewportWidth,
                w,
                u,
                v,
                viewportU,
                viewportV,
                pixelDeltaU,
                pixelDeltaV,
                viewportUpperLeft,
                pixelLocation00
        );
    }

    public void render(HittableList world) {

        try (FileWriter fileWriter = new FileWriter("test.ppm")) {
            fileWriter.write(String.format("""
                            P3
                            %d %d
                            255
                            """,
                    settings.imageWidth(),
                    settings.imageHeight()));

            for (int j = 0; j < settings.imageHeight(); j++) {
                logger.info("Scanlines remaining: {}", settings.imageHeight() - j);
                for (int i = 0; i < settings.imageWidth(); i++) {
                    Vector3d pixelColor = new Vector3d(0);
                    for (int sample = 0; sample < settings.samplesPerPixel(); sample++) {
                        Ray ray = getRay(i, j);
                        pixelColor.add(getRayColor(ray, world));
                    }
                    writeColor(fileWriter, pixelColor.mul(settings.pixelSamplesScale()));
                }
            }
        } catch (IOException e) {
            logger.error("IO Failure", e);
        }
    }

    /**
     * @param i delta U
     * @param j delta V
     * @return a camera ray originating from the defocus disk and directed at a randomly sampled point around the pixel location i, j.
     */
    private Ray getRay(int i, int j) {
        Vector3d offset = sampleSquare();

        Vector3d pixelSampleLocation = new Vector3d(pixelLocation00)
                .add(new Vector3d(pixelDeltaU).mul(i + offset.x()))
                .add(new Vector3d(pixelDeltaV).mul(j + offset.y()));


        Vector3d rayOrigin = (settings.defocusAngle() <= 0) ? new Vector3d(settings.position()) : defocusDiskSample();
        Vector3d rayDirection = new Vector3d(pixelSampleLocation).sub(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    private Vector3d defocusDiskSample() {
        // Returns a random point in the camera defocus disk.
        Vector3d p = randomUnitVectorInDisk();
        return new Vector3d(settings.position())
                .add(new Vector3d(defocusDiskU).mul(p.x()))
                .add(new Vector3d(defocusDiskV).mul(p.y()));
    }

    /**
     * @return a vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
     */
    private Vector3d sampleSquare() {

        Random random = new Random();
        return new Vector3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, 0);
    }

    private void writeColor(FileWriter fileWriter, Vector3d pixelColor) throws IOException {

        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();

        // Apply a linear to gamma transform for gamma 2
        r = linearToGammaSpace(r);
        g = linearToGammaSpace(g);
        b = linearToGammaSpace(b);

        // Translate the [0,1] component values to the byte range [0,255].
        Interval intensity = Interval.of(0.000, 0.999);
        int rbyte = (int) (256 * intensity.clamp(r));
        int gbyte = (int) (256 * intensity.clamp(g));
        int bbyte = (int) (256 * intensity.clamp(b));

        fileWriter.write(String.format("%d %d %d\n", rbyte, gbyte, bbyte));
    }

    private Vector3d getRayColor(Ray ray, HittableList world) {
        return getRayColor(ray, 0, world);
    }

    private Vector3d getRayColor(Ray ray, int rayBounceIteration, HittableList world) {

        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (settings.rayMaxBounces() <= rayBounceIteration) return new Vector3d(0);

        HitRecord rec = new HitRecord();

        /*
         * There’s also a subtle bug that we need to address.
         * A ray will attempt to accurately calculate the intersection point when it intersects with a surface.
         * Unfortunately for us, this calculation is susceptible to floating point rounding errors which can cause
         * the intersection point to be ever so slightly off. This means that the origin of the next ray, the ray that
         * is randomly scattered off of the surface, is unlikely to be perfectly flush with the surface. It might
         * be just above the surface. It might be just below the surface. If the ray's origin is just below the
         * surface then it could intersect with that surface again. Which means that it will find the nearest
         * surface at 𝑡=0.00000001 or whatever floating point approximation the hit function gives us.
         * The simplest hack to address this is just to ignore hits that are very close to the calculated intersection point
         */
        boolean hasRayHitSurface = world.hit(ray, Interval.of(0.001, Double.POSITIVE_INFINITY), rec);

        if (hasRayHitSurface) {
            ScatterResult materialScatterResult = rec.getMaterial().scatter(ray, rec);
            if (materialScatterResult.isScattered()) {
                return getRayColor(
                        materialScatterResult.scattered(),
                        rayBounceIteration + 1,
                        world)
                        .mul(materialScatterResult.attenuation());
            }
            return new Vector3d(0, 0, 0);
        }

        Vector3d unitDirection = unitVector(ray.direction());

        double a = 0.5 * (unitDirection.y() + 1.0);

        Vector3d colorOne = new Vector3d(1.0, 1.0, 1.0);
        Vector3d colorTwo = new Vector3d(0.5, 0.7, 1.0);

        // result = (1 - a) * colorOne + a * colorTwo
        return lerp(
                colorOne,
                colorTwo,
                a
        );
    }

    private double linearToGammaSpace(double linearComponent) {
        return 0 < linearComponent ? Math.sqrt(linearComponent) : 0;
    }
}
