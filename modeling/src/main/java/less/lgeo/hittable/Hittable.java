package less.lgeo.hittable;

import less.lgeo.common.Interval;
import less.lgeo.common.Ray;

import java.util.Optional;

public interface Hittable {

    /**
     * @param ray             Ray being cast
     * @param rayTimeInterval time of ray during cast interval
     * @return Empty if nothing was hit, otherwise return the {@link HitRecord} which defines what was hit
     */
    Optional<HitRecord> hit(Ray ray, Interval rayTimeInterval);

}
