package less.lgeo.reducer;

import less.lgeo.LDrawUnitsUtil;
import less.lgeo.primitive.Model;
import less.lgeo.tracer.RayTracer;
import less.lgeo.tracer.camera.Camera;
import org.joml.Vector3d;
import org.springframework.stereotype.Component;

import static less.lgeo.tracer.camera.Camera.ASPECT_RATIO_16_9;

@Component
public class Reducer {

    public Model reduce(Model model) {
        Camera camera = new Camera(
                new Vector3d(0, 0, -1 * LDrawUnitsUtil.BRICK_TO_LDU),
                ASPECT_RATIO_16_9,
                400
        );

        RayTracer rayTracer = new RayTracer(camera, model);
        rayTracer.render();

        return model;
    }
}
