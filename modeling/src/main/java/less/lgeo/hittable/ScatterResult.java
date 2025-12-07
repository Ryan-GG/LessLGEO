package less.lgeo.hittable;

import less.lgeo.primitive.Ray;
import org.joml.Vector3d;

//FIXME, fix for mutation reference but could cleanup more
public record ScatterResult(Vector3d attenuation, Ray scattered, boolean isScattered) {
}
