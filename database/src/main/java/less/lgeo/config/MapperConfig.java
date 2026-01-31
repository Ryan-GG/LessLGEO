package less.lgeo.config;

import less.lgeo.mapper.*;
import less.lgeo.service.ColorService;
import org.springframework.context.annotation.Bean;

public class MapperConfig {

    @Bean
    public ColorMapper colorMapper(ColorService colorService) {
        return new ColorMapper(colorService);
    }

    @Bean
    public LineMapper lineMapper(ColorMapper colorMapper) {
        return new LineMapper(colorMapper);
    }

    @Bean
    public TriangleMapper triangleMapper(ColorMapper colorMapper) {
        return new TriangleMapper(colorMapper);
    }

    @Bean
    public QuadrilateralMapper quadrilateralMapper(ColorMapper colorMapper) {
        return new QuadrilateralMapper(colorMapper);
    }

    @Bean
    public OptionalLineMapper optionalLineMapper(ColorMapper colorMapper) {
        return new OptionalLineMapper(colorMapper);
    }

    @Bean
    public SubFileReferenceMapper subFileRefMapper(ColorMapper colorMapper) {
        return new SubFileReferenceMapper(colorMapper);
    }

    @Bean
    public ModelMapper modelMapper(
            LineMapper lineMapper,
            TriangleMapper triangleMapper,
            QuadrilateralMapper quadrilateralMapper,
            OptionalLineMapper optionalLineMapper,
            SubFileReferenceMapper subFileReferenceMapper
    ) {
        return new ModelMapper(lineMapper, triangleMapper, quadrilateralMapper, optionalLineMapper, subFileReferenceMapper);
    }
}
