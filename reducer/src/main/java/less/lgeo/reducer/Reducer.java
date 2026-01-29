package less.lgeo.reducer;

import less.lgeo.common.Color;
import less.lgeo.hittable.HittableList;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Point;
import less.lgeo.primitive.Quadrilateral;
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
                new Point(0, 0, 9),
                new Point(0, 0, 0),
                new Vector3d(0, 1, 0),
                0.0,
                9,
                (time) -> {
                    //TODO, these should be settings that can be controlled
                    Color colorOne = new Color(new Vector3d(1.0, 1.0, 1.0));
                    Color colorTwo = new Color(new Vector3d(0.5, 0.7, 1.0));
                    return colorOne.interpolate(colorTwo, time);
                }
        );
        return new Camera(settings);
    }

    public Model reduce(Model model) {

        HittableList world = new HittableList();
        
        Color leftRed = new Color(1.0, 0.2, 0.2);

        world.add(
                new Quadrilateral(
                        leftRed,
                        new Point(-3, -2, 5),
                        new Point(-3, -2, 1),
                        new Point(-3, 2, 1),
                        new Point(-3, 2, 5)
                ));

        Camera camera = getCamera();

        camera.render(world);

        return model;
    }
}
