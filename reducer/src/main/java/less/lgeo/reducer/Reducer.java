package less.lgeo.reducer;

import less.lgeo.hittable.HittableList;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Sphere;
import less.lgeo.tracer.camera.Camera;
import org.joml.Vector3d;
import org.springframework.stereotype.Component;

import static less.lgeo.tracer.camera.Camera.ASPECT_RATIO_16_9;

@Component
public class Reducer {

    public Model reduce(Model model) {

        HittableList world = new HittableList();

        world.add(new Sphere(new Vector3d(0, 0, -1), 0.5));
        world.add(new Sphere(new Vector3d(0, -100.5, -1), 100));

        Camera camera = new Camera(
                new Vector3d(0, 0, 0),
                ASPECT_RATIO_16_9,
                400
        );

        camera.render(world);

        return model;
    }
}
