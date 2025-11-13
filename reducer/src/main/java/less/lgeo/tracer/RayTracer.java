package less.lgeo.tracer;

import less.lgeo.common.Color;
import less.lgeo.hittable.HittableList;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Ray;
import less.lgeo.primitive.Sphere;
import less.lgeo.tracer.camera.Camera;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public record RayTracer(Camera camera, Model model) {

    //FIXME, put this into application.yaml
    private static final int MAX_NUM_RAY_BOUNCES = 0;

    private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);

    public void render() {
        int imageWidth = camera().getImageWidth();
        int imageHeight = camera().getImageHeight();


        HittableList world = new HittableList();

        world.add(new Sphere(new Vector3d(0, 0, -1), 0.5));
        world.add(new Sphere(new Vector3d(0, -100.5, -1), 100));

        try (FileWriter fileWriter = new FileWriter("test.ppm")) {
            fileWriter.write(String.format("""
                            P3
                            %d %d
                            255
                            """,
                    imageWidth, imageHeight));

            for (int j = 0; j < imageHeight; j++) {
                logger.info("Scanline's remaining: {}", imageHeight - j);
                for (int i = 0; i < imageWidth; i++) {

                    //pixel00_loc + (i * pixel_delta_u) + (j * pixel_delta_v);
                    Vector3d pixelCenter = new Vector3d(camera.getPixelLocation00())
                            .add(new Vector3d(camera.getPixelDeltaU()).mul(i))
                            .add(new Vector3d(camera.getPixelDeltaV()).mul(j));

                    Vector3d rayDirection = new Vector3d(pixelCenter).sub(camera.getOrigin());
                    Ray ray = new Ray(camera.getOrigin(), rayDirection);

                    Color pixelColor = ray.getColor(world);

                    fileWriter.write(String.format("%d %d %d\n",
                            (int) (255.999 * pixelColor.r()),
                            (int) (255.999 * pixelColor.g()),
                            (int) (255.999 * pixelColor.b())));
                }
            }
            logger.info("Done!");
        } catch (IOException e) {
            logger.error("IO Failure", e);
        }
    }
}
