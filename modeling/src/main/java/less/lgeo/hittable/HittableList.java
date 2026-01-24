package less.lgeo.hittable;

import less.lgeo.common.Interval;
import less.lgeo.common.Ray;

import java.util.ArrayList;
import java.util.List;

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
    public boolean hit(Ray ray, Interval rayTimeInterval, HitRecord hitRecord) {

        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestsoFar = rayTimeInterval.max();

        for (Hittable hittable : hittableList) {
            boolean hasRayHitSurface = hittable.hit(ray, Interval.of(rayTimeInterval.min(), closestsoFar), tempRec);
            if (hasRayHitSurface) {
                hitAnything = true;
                closestsoFar = tempRec.getT();

                // FIXME, This for some reason doesn't mutate the pointer ref so i need to set the values
                hitRecord.setT(tempRec.getT());
                hitRecord.setPoint(tempRec.getPoint());
                hitRecord.setNormal(tempRec.getNormal());
                hitRecord.setFrontFace(tempRec.isFrontFace());
                hitRecord.setMaterial(tempRec.getMaterial());
            }
        }

        return hitAnything;
    }
}
