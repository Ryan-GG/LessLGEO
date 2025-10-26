package less.lgeo.primitive;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3Utils;
import lombok.Data;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;

@Data
public class Quadrilateral {

    private final LineType type = LineType.QUADRILATERAL;
    private final Long id = null;
    private final int colorId;
    private final Vector3d p1;
    private final Vector3d p2;
    private final Vector3d p3;
    private final Vector3d p4;

    public Quadrilateral(
            int colorId,
            Vector3d p1,
            Vector3d p2,
            Vector3d p3,
            Vector3d p4) {
        this.colorId = colorId;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public List<Vector3d> getVertices() {
        return List.of(p1, p2, p3, p4);
    }

    public Quadrilateral transform(
            Optional<Matrix> transformationMatrix,
            Optional<Integer> inheritedColorId) {

        return new Quadrilateral(
                getColor(inheritedColorId, colorId),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p2, matrix)).orElse(p2),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p3, matrix)).orElse(p3),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p4, matrix)).orElse(p4));
    }

    public List<Triangle> tessellate() {
        Triangle bottomLeft = new Triangle(colorId, p1, p2, p4);
        Triangle topRight = new Triangle(colorId, p2, p3, p4);
        return List.of(bottomLeft, topRight);
    }

}
