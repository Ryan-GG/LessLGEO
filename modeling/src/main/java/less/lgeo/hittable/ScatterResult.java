package less.lgeo.hittable;

import less.lgeo.common.Ray;
import org.joml.Vector3d;

public record ScatterResult(Vector3d attenuation, Ray scatteredRay) {
}
