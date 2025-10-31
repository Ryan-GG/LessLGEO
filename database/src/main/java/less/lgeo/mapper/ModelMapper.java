package less.lgeo.mapper;

import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * This create a generated implementation using the MapStruct dependency
 */
@Mapper(componentModel = "spring", uses = {ColorMapper.class})
public interface ModelMapper {

    // --- Entity <-> Domain ---
    Model toDomain(ModelEntity entity);

    ModelEntity toEntity(Model domain);

    // --- Domain <-> DTO ---
    /*@Mapping(target = "monthlyPayment", expression = "java(loan.calculateMonthlyPayment())")
    LoanDTO toDto(Loan loan);*/

    /*@InheritInverseConfiguration(name = "toDto")
    Loan toDomain(LoanDTO dto);*/

    // --- List mappings (optional) ---
    //List<LoanDTO> toDtoList(List<Loan> loans);

    List<ModelEntity> toDomainList(List<ModelEntity> entities);
}
