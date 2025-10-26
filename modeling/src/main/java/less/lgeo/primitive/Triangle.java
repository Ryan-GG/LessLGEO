package less.lgeo.primitive;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import lombok.Data;

import java.util.List;
import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;

@Data
public class Triangle {

    private final LineType type = LineType.TRIANGLE;
    private final Long id = null;
    private final int colorId;
    private final Vector3 p1;
    private final Vector3 p2;
    private final Vector3 p3;

    public Triangle(
            int colorId,
            Vector3 p1,
            Vector3 p2,
            Vector3 p3) {
        this.colorId = colorId;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Vector3 getCentroid() {
        double xCentroid = (p1.getX() + p2.getX() + p3.getX()) / 3;
        double yCentroid = (p1.getY() + p2.getY() + p3.getY()) / 3;
        double zCentroid = (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

        return new Vector3(xCentroid, yCentroid, zCentroid);
    }

    public List<Vector3> getVertices() {
        return List.of(p1, p2, p3);
    }

    public Triangle transform(
            Optional<Matrix> transformationMatrix,
            Optional<Integer> inheritedColorId) {
        return new Triangle(
                getColor(inheritedColorId, colorId),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3));
    }

}
