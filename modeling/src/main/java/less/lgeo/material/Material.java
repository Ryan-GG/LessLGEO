package less.lgeo.material;

import less.lgeo.common.Color;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;

import java.util.Optional;

public interface Material {

    /**
     * @param color LDraw defined Color
     * @return Corresponding {@link Material} implementation based on color LDraw properties
     */
    static Material fromColor(Color color) {
        if (color.isTransparent()) {
            return new Dielectric(1.5);
        }
        return new Lambertian(color);
    }

    Optional<ScatterResult> scatter(Ray rayIn, HitRecord record);

}
