package less.lgeo.reducer;

import less.lgeo.common.Color;
import less.lgeo.hittable.HittableList;
import less.lgeo.material.Dielectric;
import less.lgeo.material.Lambertian;
import less.lgeo.material.Material;
import less.lgeo.material.Metal;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Point;
import less.lgeo.primitive.Sphere;
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
                CameraSettings.ASPECT_RATIO_16_9,
                100,
                30,
                400,
                20,
                new Point(-2, 2, 1),
                new Point(0, 0, -1),
                new Vector3d(0, 1, 0),
                10.0,
                3.4,
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

        Material materialGround = new Lambertian(new Color(0.8, 0.8, 0.0));
        Material materialCenter = new Lambertian(new Color(0.1, 0.2, 0.5));
        Material materialLeft = new Dielectric(1.50);
        Material materialBubble = new Dielectric(1.00 / 1.50);
        Material materialRight = new Metal(new Color(0.8, 0.6, 0.2), 1.0);

        world.add(new Sphere(new Vector3d(0.0, -100.5, -1.0), 100.0, materialGround));
        world.add(new Sphere(new Vector3d(0.0, 0.0, -1.2), 0.5, materialCenter));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.5, materialLeft));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.4, materialBubble));
        world.add(new Sphere(new Vector3d(1.0, 0.0, -1.0), 0.5, materialRight));

        Camera camera = getCamera();

        camera.render(world);

        return model;
    }
}
