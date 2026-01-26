package less.lgeo.tracer.camera;

import less.lgeo.common.Color;
import less.lgeo.common.Interval;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.HittableList;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Point;
import lombok.Getter;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

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
        Vector3d w = unitVector(settings.position().value().sub(settings.lookAt().value(), new Vector3d()));
        Vector3d u = unitVector(settings.up().cross(w, new Vector3d()));
        Vector3d v = w.cross(u, new Vector3d());

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vector3d viewportU = u.mul(viewportWidth, new Vector3d());    // Vector across viewport horizontal edge
        Vector3d viewportV = v.negate(new Vector3d()).mul(viewportHeight);  // Vector down viewport vertical edge

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        this.pixelDeltaU = viewportU.div(settings.imageWidth(), new Vector3d());
        this.pixelDeltaV = viewportV.div(settings.imageHeight(), new Vector3d());

        // Calculate the location of the upper left pixel.
        Vector3d viewportUpperLeft = settings.position().value()
                .sub(w.mul(settings.focusDist(), new Vector3d()), new Vector3d())
                .sub(viewportU.div(2, new Vector3d()))
                .sub(viewportV.div(2, new Vector3d()));

        // Calculate the camera defocus disk basis vectors.
        double defocusRadius = settings.focusDist() * Math.tan(Math.toRadians(settings.defocusAngle() / 2));
        defocusDiskU = u.mul(defocusRadius, new Vector3d());
        defocusDiskV = v.mul(defocusRadius, new Vector3d());

        // Upper-left pixel center
        this.pixelLocation00 = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV, new Vector3d()).mul(0.5), new Vector3d());
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
                        pixelColor.add(getRayColor(ray, world).toVector3d());
                    }

                    Vector3d sampledPixelColor = pixelColor.mul(settings.pixelSamplesScale(), new Vector3d());
                    writeColor(fileWriter, sampledPixelColor);
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

        Vector3d pixelSampleLocation = pixelLocation00
                .add(pixelDeltaU.mul(i + offset.x(), new Vector3d()), new Vector3d())
                .add(pixelDeltaV.mul(j + offset.y(), new Vector3d()));


        Point rayOrigin = (settings.defocusAngle() <= 0) ? settings.position() : defocusDiskSample();
        Vector3d rayDirection = pixelSampleLocation.sub(rayOrigin.value(), new Vector3d());

        return new Ray(rayOrigin, rayDirection);
    }

    private Point defocusDiskSample() {
        // Returns a random point in the camera defocus disk.
        Vector3d p = randomUnitVectorInDisk();
        return new Point(settings.position().value()
                .add(defocusDiskU.mul(p.x(), new Vector3d()), new Vector3d())
                .add(defocusDiskV.mul(p.y(), new Vector3d())));
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

    private Color getRayColor(Ray ray, HittableList world) {
        return getRayColor(ray, 0, world);
    }

    private Color getRayColor(Ray ray, int rayBounceIteration, HittableList world) {

        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (settings.rayMaxBounces() <= rayBounceIteration) return new Color(0);

        /*
         * There’s also a subtle bug that we need to address.
         * A ray will attempt to accurately calculate the intersection point when it intersects with a surface.
         * Unfortunately for us, this calculation is susceptible to floating point rounding errors which can cause
         * the intersection point to be ever so slightly off. This means that the origin of the next ray, the ray that
         * is randomly scatteredRay off of the surface, is unlikely to be perfectly flush with the surface. It might
         * be just above the surface. It might be just below the surface. If the ray's origin is just below the
         * surface then it could intersect with that surface again. Which means that it will find the nearest
         * surface at 𝑡=0.00000001 or whatever floating point approximation the hit function gives us.
         * The simplest hack to address this is just to ignore hits that are very close to the calculated intersection point
         */
        Optional<HitRecord> optionalHitRecord = world.hit(ray, Interval.of(0.001, Double.POSITIVE_INFINITY));


        if (optionalHitRecord.isPresent()) {

            HitRecord hitRecord = optionalHitRecord.get();
            Optional<ScatterResult> optionalScatterResult = hitRecord.material().scatter(ray, hitRecord);

            return optionalScatterResult.map(scatterResult ->
                    {
                        Color rayColor = getRayColor(scatterResult.scatteredRay(), rayBounceIteration + 1, world);
                        return new Color(rayColor.toVector3d().mul(scatterResult.attenuation()));
                    })
                    .orElse(new Color(0));
        }

        Vector3d unitDirection = unitVector(ray.direction());

        //TODO, document this better
        double a = 0.5 * (unitDirection.y() + 1.0);

        return settings.getBackgroundColor().apply(a);
    }

    private double linearToGammaSpace(double linearComponent) {
        return 0 < linearComponent ? Math.sqrt(linearComponent) : 0;
    }
}
