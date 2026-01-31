package less.lgeo.reducer;

import less.lgeo.common.Color;
import less.lgeo.hittable.HittableList;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Point;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import less.lgeo.tracer.camera.Camera;
import less.lgeo.tracer.camera.CameraSettings;
import org.joml.Vector3d;
import org.springframework.stereotype.Component;


/**
 * <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html">Ray Tracing in One Weekend</a>
 */
@Component
public class Reducer {

    private static Camera getCamera() {

        CameraSettings settings = new CameraSettings(
                1.0,
                100,
                50,
                400,
                80,
                Point.of(0, 0, 9),
                Point.of(0),
                new Vector3d(0, 1, 0),
                0.0,
                9,
                (time) -> {
                    //TODO, these should be settings that can be controlled
                    Color colorOne = Color.of(1.0, 1.0, 1.0);
                    Color colorTwo = Color.of(0.5, 0.7, 1.0);
                    return colorOne.interpolate(colorTwo, time);
                }
        );
        return new Camera(settings);
    }

    public Model reduce(Model model) {

        HittableList world = new HittableList();

        Color leftRed = Color.of(1.0, 0.2, 0.2);
        Quadrilateral left = new Quadrilateral(
                leftRed,
                Point.of(-3, -2, 5),
                Point.of(-3, -2, 1),
                Point.of(-3, 2, 1),
                Point.of(-3, 2, 5)
        );


        Color backGreen = Color.of(0.2, 1.0, 0.2);
        Triangle back = new Triangle(
                backGreen,
                Point.of(2, -2, 0),
                Point.of(-2, -2, 0),
                Point.of(2, 2, 0)
        );

        Color rightBlue = Color.of(0, 0, 1, false);

        Quadrilateral right = new Quadrilateral(
                rightBlue,
                Point.of(3, -2, 5),
                Point.of(3, -2, 1),
                Point.of(3, 2, 1),
                Point.of(3, 2, 5)
        );

        world.addAll(left, back, right);

        Camera camera = getCamera();

        camera.render(world);

        return model;
    }
}
