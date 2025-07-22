package less.lgeo.mesh;

import less.lgeo.service.ColorService;
import less.lgeo.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMeshFactory {

  @Autowired
  private ColorService colorService;

  @Autowired
  private ModelService modelService;

  public ModelMesh create( String id ) {
    return new ModelMesh( id, modelService, colorService );
  }
} 