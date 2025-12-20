package less.lgeo.reducer;

import less.lgeo.hittable.HittableList;
import less.lgeo.material.Dielectric;
import less.lgeo.material.Lambertian;
import less.lgeo.material.Material;
import less.lgeo.material.Metal;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Sphere;
import less.lgeo.tracer.camera.Camera;
import org.joml.Vector3d;
import org.springframework.stereotype.Component;

import static less.lgeo.tracer.camera.Camera.ASPECT_RATIO_16_9;

/**
 * <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html">Ray Tracing in One Weekend</a>
 */
@Component
public class Reducer {

    public Model reduce(Model model) {

        HittableList world = new HittableList();

        Material materialGround = new Lambertian(new Vector3d(0.8, 0.8, 0.0));
        Material materialCenter = new Lambertian(new Vector3d(0.1, 0.2, 0.5));
        Material materialLeft = new Dielectric(1.0);
        Material materialBubble = new Dielectric(1.0 / 1.5);
        Material materialRight = new Metal(new Vector3d(0.8, 0.6, 0.2), 1.0);

        world.add(new Sphere(new Vector3d(0.0, -100.5, -1.0), 100.0, materialGround));
        world.add(new Sphere(new Vector3d(0.0, 0.0, -1.2), 0.5, materialCenter));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.5, materialLeft));
        world.add(new Sphere(new Vector3d(-1.0, 0.0, -1.0), 0.4, materialBubble));
        world.add(new Sphere(new Vector3d(1.0, 0.0, -1.0), 0.5, materialRight));

        Camera camera = new Camera(
                new Vector3d(0, 0, 0),
                ASPECT_RATIO_16_9,
                400
        );

        camera.render(world);

        return model;
    }
}
