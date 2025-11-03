package less.lgeo.mapper;

import less.lgeo.embedded.MatrixEmbeddable;
import less.lgeo.entity.ModelEntity;
import less.lgeo.entity.SubFileRefEntity;
import less.lgeo.primitive.SubFileReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SubFileReferenceMapper {

    private final ColorMapper colorMapper;

    public SubFileReferenceMapper(
            ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    public SubFileReference toDomain(SubFileRefEntity entity, ModelMapper modelMapper) {
        return new SubFileReference(
                colorMapper.toDomain(entity.getColor()),
                entity.getMatrix().toDomain(),
                modelMapper.toDomain(entity.getSubModel()),
                entity.getFileName(),
                //FIXME, Connections don't exist in Database yet
                Optional.empty()
        );
    }

    private SubFileRefEntity toEntity(SubFileReference domain, ModelEntity modelEntity, ModelMapper modelMapper) {
        SubFileRefEntity entity = new SubFileRefEntity();
        //Don't set Id since sequence
        entity.setColor(colorMapper.toEntity(domain.color()));
        entity.setSubModel(modelMapper.toEntity(domain.subModel()));
        entity.setFileName(domain.fileName());
        //FIXME, Connections don't exist in Database yet
        entity.setConnectionId(0L);
        entity.setMatrix(new MatrixEmbeddable(domain.matrix()));
        entity.setModel(modelEntity);
        return entity;
    }
    
    public List<SubFileRefEntity> toEntityList(List<SubFileReference> domainList, ModelEntity modelEntity, ModelMapper modelMapper) {
        return domainList.stream().map(domain -> toEntity(domain, modelEntity, modelMapper)).toList();
    }

    public List<SubFileReference> toDomainList(List<SubFileRefEntity> entityList, ModelMapper modelMapper) {
        return entityList.stream().map(entity -> toDomain(entity, modelMapper)).toList();
    }

}
