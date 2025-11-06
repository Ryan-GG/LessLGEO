package less.lgeo.tracer;

import less.lgeo.primitive.Model;
import less.lgeo.tracer.camera.Camera;

public record RayTracer(Camera camera, Model model) {

    private static final int MAX_NUM_RAY_BOUNCES = 0;


}
