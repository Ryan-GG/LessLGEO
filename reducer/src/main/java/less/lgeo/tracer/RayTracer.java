package less.lgeo.tracer;

import less.lgeo.common.Color;
import less.lgeo.primitive.Model;
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
                    Vector3d pixelCenter = camera.getPixelLocation00().add(
                            i * camera.getPixelDeltaX(),
                            i * camera.getPixelDeltaX(),
                            i * camera.getPixelDeltaX()
                    ).add(
                            j * camera.getPixelDeltaY(),
                            j * camera.getPixelDeltaY(),
                            j * camera.getPixelDeltaY()
                    );

                    Vector3d rayDirection = pixelCenter.sub(camera.getOrigin());
                    Ray ray = new Ray(camera.getOrigin(), rayDirection);

                    Color pixelColor = ray.getColor();

                    fileWriter.write(String.format("%d %d %d\n",
                            (int) (255.999 * pixelColor.r()),
                            (int) (255.999 * pixelColor.g()),
                            (int) (255.999 * pixelColor.b())));
                }
            }
        } catch (IOException e) {
            logger.error("IO Failure", e);
        }
    }
}
