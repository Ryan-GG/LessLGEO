package less.lgeo.hittable;

import less.lgeo.common.Ray;
import org.joml.Vector3d;

//FIXME, fix for mutation reference but could cleanup more
//fixme, should just return optional for the isScattered boolean
public record ScatterResult(Vector3d attenuation, Ray scatteredRay) {
}
