package less.lgeo.service;

import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.mapper.ModelMapper;
import less.lgeo.primitive.Model;
import less.lgeo.repository.ColorRepository;
import less.lgeo.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service layer for interacting with {@link less.lgeo.entity.ModelEntity} from
 * the database.
 * Handles the Type conversion from {@link ModelId} to {@link Long} based on
 * {@link ModelRepository}
 */
@Service
public class ModelService {

    @Autowired
    private final ModelRepository modelRepository;

    @Autowired
    private final ColorRepository colorRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public ModelService(
            ModelRepository modelRepository,
            ColorRepository colorRepository,
            ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.colorRepository = colorRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * @return database entity by Model Id throws if not found
     */
    public ModelEntity getModelById(ModelId id) throws NoSuchElementException {
        return modelRepository.findById(id.getValue()).orElseThrow();
    }

    @Transactional
    public ModelId insertModel(Model model) {
        ModelEntity entity = modelMapper.toEntity(model);
        return modelRepository.save(entity).getId();
    }

    public List<ModelId> getAllParentModelIds() {
        return modelRepository.findAllParentModels().stream().map(ModelEntity::getId).toList();
    }

}
