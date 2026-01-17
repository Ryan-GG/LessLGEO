package less.lgeo.tracer.camera;

import org.joml.Vector3d;

/**
 * @param aspectRatio     Ratio of image width over height
 * @param samplesPerPixel Count of random samples for each pixel
 * @param rayMaxBounces   Maximum number of ray bounces a ray can have in a scene
 * @param imageWidth      Rendered image width(Pixels)
 * @param verticalFOV     Field of lookAt - vertically
 * @param position        Position of the camera(Origin)
 * @param lookAt          Point camera is looking at
 * @param up              Camera relative up direction
 * @param defocusAngle    Variation angle of rays through each pixel
 * @param focusDist       Distance from camera {@code position} point to plane of perfect focus
 */
public record CameraSettings(
        double aspectRatio,
        int samplesPerPixel,
        int rayMaxBounces,
        int imageWidth,
        double verticalFOV,
        Vector3d position,
        Vector3d lookAt,
        Vector3d up,
        double defocusAngle,
        double focusDist
) {
    public static final double ASPECT_RATIO_16_9 = 16.0 / 9.0;

    public double pixelSamplesScale() {
        return 1.0 / samplesPerPixel;
    }

    public int imageHeight() {
        int imageHeight = (int) (imageWidth / aspectRatio);
        return Math.max(1, imageHeight);
    }

    public double focalLength() {
        return new Vector3d(position).sub(lookAt).length();
    }
}
