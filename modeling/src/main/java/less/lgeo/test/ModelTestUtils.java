package less.lgeo.test;

import less.lgeo.common.Color;
import less.lgeo.common.Matrix;
import less.lgeo.primitive.*;

import java.util.List;
import java.util.Optional;


/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {

    public static final Color BLACK = new Color(0, "Black", 0, 0, 0, false);
    public static final Color RED = new Color(4, "Red", 255, 0, 0, false);

    public static final Color INHERIT_PARENT_COLOR = new Color(16, "inherit_parent", 0, 0, 0, false);
    public static final Color INHERIT_EDGE_COLOR = new Color(24, "inherit_edge", 0, 0, 0, false);


    /**
     * See BoundingBox for ordering
     *
     * @return {@link Model} shaped as a Cube
     */
    public static Model cube() {

        Point a = Point.of(0, -1, 0);
        Point b = Point.of(1, -1, 0);
        Point c = Point.of(1, -1, 1);
        Point d = Point.of(0, -1, 1);

        Point e = Point.of(0, 0, 0);
        Point f = Point.of(1, 0, 0);
        Point g = Point.of(1, 0, 1);
        Point h = Point.of(0, 0, 1);

        Quadrilateral top = new Quadrilateral(RED, a, b, c, d);
        Quadrilateral bottom = new Quadrilateral(RED, e, f, g, h);

        Quadrilateral front = new Quadrilateral(RED, e, f, b, a);
        Quadrilateral back = new Quadrilateral(RED, g, h, d, c);

        Quadrilateral left = new Quadrilateral(RED, h, e, a, d);
        Quadrilateral right = new Quadrilateral(RED, f, g, c, b);

        List<Quadrilateral> cube = List.of(top, bottom, front, back, left, right);

        return new Model(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                cube,
                List.of(),
                List.of());
    }

    private static Model subCubeModel() {
        Model model = cube();
        return new Model(
                model.comments(),
                model.commands(),
                model.lines(),
                model.triangles(),
                model.quadrilaterals().stream().map(quadrilateral ->
                        new Quadrilateral(
                                INHERIT_PARENT_COLOR,
                                quadrilateral.p1(),
                                quadrilateral.p2(),
                                quadrilateral.p3(),
                                quadrilateral.p4())).toList(),
                model.optionalLines(),
                model.pieces());
    }

    public static Model pyramid() {

        Point a = Point.of(0, 0, 0);
        Point b = Point.of(0, 0, 1);
        Point c = Point.of(1, 0, 1);
        Point d = Point.of(1, 0, 0);
        Point e = Point.of(0.5, -1, 0.5);

        Quadrilateral bottom = new Quadrilateral(BLACK, a, b, c, d);
        Triangle front = new Triangle(BLACK, a, e, d);
        Triangle back = new Triangle(BLACK, b, e, c);
        Triangle left = new Triangle(BLACK, a, e, b);
        Triangle right = new Triangle(BLACK, d, e, c);

        List<Triangle> pyramid = List.of(front, back, left, right);

        return new Model(
                List.of(),
                List.of(),
                List.of(),
                pyramid,
                List.of(bottom),
                List.of(),
                List.of()
        );
    }

    public static Model nestedCubes() {
        Model nestedCubes = new Model(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new SubFileReference(BLACK, Matrix.IDENTITY_MATRIX, subCubeModel(), "cube1", Optional.empty()),
                        new SubFileReference(RED, Matrix.IDENTITY_MATRIX, subCubeModel(), "cube2", Optional.empty())
                )
        );
        return nestedCubes.transformModel();
    }


}
