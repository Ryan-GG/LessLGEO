package less.lgeo.tracer.camera;

import less.lgeo.common.Interval;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.HittableList;
import less.lgeo.primitive.Ray;
import lombok.Getter;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static less.lgeo.common.Vector3dUtils.*;

@Getter
public class Camera {

    public static final double ASPECT_RATIO_16_9 = 16.0 / 9.0; // Ratio of image width over height
    private static final int SAMPLES_PER_PIXEL = 100; // Count of random samples for each pixel
    private static final int RAY_MAX_BOUNCES = 50;   // Maximum number of ray bounces into scene
    private static final double PIXEL_SAMPLES_SCALE = (double) 1 / SAMPLES_PER_PIXEL;
    private static final Logger logger = LoggerFactory.getLogger(Camera.class);
    private final Vector3d origin;
    private final int imageWidth; // Rendered image width in pixel count
    private final int imageHeight;
    private final Vector3d pixelDeltaU;
    private final Vector3d pixelDeltaV;
    private final Vector3d pixelLocation00;

    public Camera(Vector3d origin,
                  double aspectRatio,
                  int imageWidth) {
        this.origin = new Vector3d(origin);
        this.imageWidth = imageWidth;

        this.imageHeight = Math.max((int) (imageWidth / aspectRatio), 1);

        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double) imageWidth / imageHeight);

        // Viewport edge vectors
        Vector3d viewportU = new Vector3d(viewportWidth, 0, 0);
        //FIXME, this may become positive viewportHeight
        Vector3d viewportV = new Vector3d(0, -viewportHeight, 0);

        // Pixel deltas
        this.pixelDeltaU = new Vector3d(viewportU).div(imageWidth);
        this.pixelDeltaV = new Vector3d(viewportV).div(imageHeight);

        // Viewport upper left corner
        Vector3d focalOffset = new Vector3d(0, 0, focalLength);
        Vector3d viewportUpperLeft = new Vector3d(origin)
                .sub(focalOffset)
                .sub(new Vector3d(viewportU).div(2))
                .sub(new Vector3d(viewportV).div(2));

        // Upper-left pixel center
        this.pixelLocation00 = new Vector3d(viewportUpperLeft)
                .add(new Vector3d(pixelDeltaU).add(pixelDeltaV).mul(0.5));
    }

    public void render(HittableList world) {

        try (FileWriter fileWriter = new FileWriter("test.ppm")) {
            fileWriter.write(String.format("""
                            P3
                            %d %d
                            255
                            """,
                    imageWidth, imageHeight));

            for (int j = 0; j < imageHeight; j++) {
                logger.info("Scanlines remaining: {}", imageHeight - j);
                for (int i = 0; i < imageWidth; i++) {
                    Vector3d pixelColor = new Vector3d(0);
                    for (int sample = 0; sample < SAMPLES_PER_PIXEL; sample++) {
                        Ray ray = getRay(i, j);
                        pixelColor.add(getRayColor(ray, world));
                    }
                    writeColor(fileWriter, pixelColor.mul(PIXEL_SAMPLES_SCALE));
                }
            }
        } catch (IOException e) {
            logger.error("IO Failure", e);
        }
    }

    /**
     * @param i delta U
     * @param j delta V
     * @return a camera ray originating from the origin and directed at randomly sampled point around the pixel location i, j.
     */
    private Ray getRay(int i, int j) {
        Vector3d offset = sampleSquare();

        Vector3d pixelSampleLocation = new Vector3d(pixelLocation00)
                .add(new Vector3d(pixelDeltaU).mul(i + offset.x()))
                .add(new Vector3d(pixelDeltaV).mul(j + offset.y()));

        Vector3d rayOrigin = new Vector3d(origin);
        Vector3d rayDirection = new Vector3d(pixelSampleLocation).sub(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
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
        if (RAY_MAX_BOUNCES <= rayBounceIteration) return new Vector3d(0);

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
            Vector3d direction = new Vector3d(rec.getNormal()).add(randomUnitVector());
            Ray bouncingRay = new Ray(rec.getPoint(), direction);
            return getRayColor(bouncingRay, rayBounceIteration + 1, world).mul(0.5);
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
}
