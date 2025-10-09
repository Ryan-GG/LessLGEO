package less.lgeo.primitive;

import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

import java.util.List;
import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;
import static less.lgeo.primitive.TriangleUtils.toTriangle;

public class QuadrilateralUtils {


    public static Quadrilateral toQuadrilateral(int colorId, Vertex p1, Vertex p2, Vertex p3,
                                                Vertex p4) {
        return Quadrilateral.newBuilder()
                .setColorId(colorId)
                .setP1(p1)
                .setP2(p2)
                .setP3(p3)
                .setP4(p4)
                .build();
    }

    public static List<Vertex> getVertices(Quadrilateral quadrilateral) {
        return List.of(quadrilateral.getP1(),
                quadrilateral.getP2(),
                quadrilateral.getP3(),
                quadrilateral.getP4());
    }

    public static Quadrilateral transformQuadrilateral(Quadrilateral quadrilateral,
                                                       Optional<Matrix> transformationMatrix,
                                                       Optional<Integer> inheritedColorId) {
        Vertex p1 = quadrilateral.getP1();
        Vertex p2 = quadrilateral.getP2();
        Vertex p3 = quadrilateral.getP3();
        Vertex p4 = quadrilateral.getP4();

        return toQuadrilateral(
                getColor(inheritedColorId, quadrilateral.getColorId()),
                transformationMatrix.map(value -> VertexUtils.transform(p1, value)).orElse(p1),
                transformationMatrix.map(value -> VertexUtils.transform(p2, value)).orElse(p2),
                transformationMatrix.map(value -> VertexUtils.transform(p3, value)).orElse(p3),
                transformationMatrix.map(value -> VertexUtils.transform(p4, value)).orElse(p4)
        );
    }

    public static List<Triangle> splitIntoTriangles(Quadrilateral quadrilateral) {
        Vertex p1 = quadrilateral.getP1();
        Vertex p2 = quadrilateral.getP2();
        Vertex p3 = quadrilateral.getP3();
        Vertex p4 = quadrilateral.getP4();

        Triangle bottomLeft = toTriangle(quadrilateral.getColorId(), p1, p2, p4);
        Triangle topRight = toTriangle(quadrilateral.getColorId(), p2, p3, p4);
        return List.of(bottomLeft, topRight);
    }

}
