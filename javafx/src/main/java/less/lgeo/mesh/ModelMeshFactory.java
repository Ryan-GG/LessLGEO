package less.lgeo.mesh;

import less.lgeo.primitive.Model;
import less.lgeo.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMeshFactory {
    @Autowired
    private ColorService colorService;

    public ModelMesh create(Model model) {
        return new ModelMesh(model, colorService);
    }
} 