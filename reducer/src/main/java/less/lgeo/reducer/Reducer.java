package less.lgeo.reducer;

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

    public Model reduce(Model model) {

        HittableList world = new HittableList();

        Material material_ground = new Lambertian(new Vector3d(0.8, 0.8, 0.0));
        Material material_center = new Lambertian(new Vector3d(0.1, 0.2, 0.5));
        Material material_left = new Dielectric(1.50);
        Material material_bubble = new Dielectric(1.00 / 1.50);
        Material material_right = new Metal(new Vector3d(0.8, 0.6, 0.2), 1.0);

        world.add(new Sphere(new Vector3d(0.0, -100.5, -1.0), 100.0, material_ground));
        world.add(new Sphere(new Vector3d(0.0, 0.0, -1.2), 0.5, material_center));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.5, material_left));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.4, material_bubble));
        world.add(new Sphere(new Vector3d(1.0, 0.0, -1.0), 0.5, material_right));

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
                3.4
        );
        Camera camera = new Camera(settings);

        camera.render(world);

        return model;
    }
}
