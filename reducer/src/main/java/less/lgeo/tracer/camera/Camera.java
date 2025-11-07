package less.lgeo.tracer.camera;

import less.lgeo.LDrawUnitsUtil;
import lombok.Getter;
import org.joml.Vector3d;

/**
 * https://raytracing.github.io/books/RayTracingInOneWeekend.html
 */
public class Camera {

    public static final double ASPECT_RATIO_16_9 = 16.0 / 9.0;

    @Getter
    private final Vector3d origin;
    @Getter
    private final int imageWidth;
    @Getter
    private final int imageHeight;
    @Getter
    private final double pixelDeltaX;
    @Getter
    private final double pixelDeltaY;
    @Getter
    private final Vector3d viewportUpperLeft;
    @Getter
    private final Vector3d pixelLocation00;

    public Camera(Vector3d origin,
                  double aspectRatio,
                  int imageWidth) {
        this.origin = origin;
        this.imageWidth = imageWidth;

        // Calculate the image height, and ensure that it's at least 1.
        this.imageHeight = Math.max((int) (imageWidth / aspectRatio), 1);

        int focal_length = LDrawUnitsUtil.BRICK_TO_LDU;
        // Viewport widths less than one are ok since they are real valued.
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * (double) (this.imageWidth) / this.imageHeight;

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vector3d viewportX = new Vector3d(viewportWidth, 0, 0);
        Vector3d viewportY = new Vector3d(0, viewportHeight, 0);

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        this.pixelDeltaX = viewportX.div(imageWidth).x();
        this.pixelDeltaY = viewportY.div(imageHeight).y();


        // Calculate the location of the upper left pixel.
        // Camera Center - focal_length - viewportX/2 - viewportY/2
        this.viewportUpperLeft = this.origin.sub(new Vector3d(0, 0, focal_length))
                .sub(viewportX.div(2))
                .sub(viewportY.div(2));

        // viewportUpperLeft + 0.5 * (pixelDeltaX + pixelDeltaY)
        this.pixelLocation00 = viewportUpperLeft.add(0.5, 0.5, 0.5).mul(pixelDeltaX + pixelDeltaY);

    }

}
