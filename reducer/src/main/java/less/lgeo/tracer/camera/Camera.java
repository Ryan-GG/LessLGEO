package less.lgeo.tracer.camera;

import lombok.Getter;
import org.joml.Vector3d;

/**
 * https://raytracing.github.io/books/RayTracingInOneWeekend.html
 */
@Getter
public class Camera {

    public static final double ASPECT_RATIO_16_9 = 16.0 / 9.0;
    private final Vector3d origin;
    private final int imageWidth;
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

}
