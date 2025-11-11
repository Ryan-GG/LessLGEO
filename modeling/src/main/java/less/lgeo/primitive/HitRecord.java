package less.lgeo.primitive;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3d;

@Setter
@Getter
public class HitRecord {

    private Vector3d point;
    private Vector3d normal;
    private double t;
}
