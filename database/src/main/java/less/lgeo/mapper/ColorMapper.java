package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    // --- Entity <-> Domain ---
    Color toDomain(ColorEntity entity);

    ColorEntity toEntity(Color domain);

    // --- Domain <-> DTO ---
    /*@Mapping(target = "monthlyPayment", expression = "java(loan.calculateMonthlyPayment())")
    LoanDTO toDto(Loan loan);*/

    /*@InheritInverseConfiguration(name = "toDto")
    Loan toDomain(LoanDTO dto);*/

    // --- List mappings (optional) ---
    //List<LoanDTO> toDtoList(List<Loan> loans);

    List<Color> toDomainList(List<ColorEntity> entities);

}
