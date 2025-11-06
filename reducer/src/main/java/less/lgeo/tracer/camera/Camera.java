package less.lgeo.tracer.camera;

import org.joml.Vector3d;

public record Camera(
        Vector3d position,
        Vector3d orientation,
        Vector3d direction,
        int horizontalFov,
        int verticalFov) {
    
}
