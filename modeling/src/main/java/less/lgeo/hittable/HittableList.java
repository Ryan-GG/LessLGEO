package less.lgeo.hittable;

import less.lgeo.common.Interval;
import less.lgeo.common.Ray;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HittableList extends Hittable {

    private final List<Hittable> hittableList;

    public HittableList() {
        hittableList = new ArrayList<>();
    }

    public HittableList(Hittable hittable) {
        hittableList = new ArrayList<>();
        hittableList.add(hittable);
    }

    public void clear() {
        hittableList.clear();
    }

    public boolean add(Hittable hittable) {
        return hittableList.add(hittable);
    }


    @Override
    public Optional<HitRecord> hit(Ray ray, Interval rayTimeInterval) {

        Optional<HitRecord> resultRecord = Optional.empty();
        double closestSoFar = rayTimeInterval.max();

        for (Hittable hittable : hittableList) {
            Optional<HitRecord> optionalHitRecord = hittable.hit(ray, Interval.of(rayTimeInterval.min(), closestSoFar));

            //FIXME, this could be refactored, i think
            if (optionalHitRecord.isPresent()) {
                HitRecord hitRecord = optionalHitRecord.get();
                closestSoFar = hitRecord.time();
                resultRecord = Optional.of(hitRecord);
            }
        }

        return resultRecord;
    }
}
