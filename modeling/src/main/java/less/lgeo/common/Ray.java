package less.lgeo.common;

import org.joml.Vector3d;

public record Ray(Vector3d origin, Vector3d direction, double scale) {
}
