package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;


class ColorMapperTest {

    @Mock
    private ColorService colorService;

    private final ColorMapper colorMapper = new ColorMapper(colorService);

    @Nested
    class ToDomain {
        private final ColorEntity unknownColorEntity = new ColorEntity(
                -1,
                "[Unknown]",
                "0033B2",
                false,
                17,
                2,
                2000,
                2000
        );

        private final ColorEntity blackColorEntity = new ColorEntity(
                0,
                "Black",
                "05131D",
                false,
                758317,
                210285,
                1957,
                2025
        );

        private final ColorEntity darkPinkColorEntity = new ColorEntity(
                5,
                "Dark Pink",
                "C870A0",
                false,
                13243,
                4236,
                2003,
                2025
        );

        private final ColorEntity transRedColorEntity = new ColorEntity(
                36,
                "Trans-Red",
                "C91A09",
                true,
                19580,
                6839,
                1969,
                2025
        );

        private final ColorEntity invalidColorEntity = new ColorEntity(
                0,
                "invalid",
                "invalid",
                false,
                0,
                0,
                0,
                0
        );

        @Test
        void mapUnknownToDomain() {
            Color result = colorMapper.toDomain(unknownColorEntity);

            assertEquals(unknownColorEntity.getId(), result.id());
            assertEquals(unknownColorEntity.getName(), result.name());
            assertEquals(0, result.r());
            assertEquals(51, result.g());
            assertEquals(178, result.b());
            assertFalse(result.isTransparent());
        }

        @Test
        void mapBlackToDomain() {
            Color result = colorMapper.toDomain(blackColorEntity);

            assertEquals(blackColorEntity.getId(), result.id());
            assertEquals(blackColorEntity.getName(), result.name());
            assertEquals(5, result.r());
            assertEquals(19, result.g());
            assertEquals(29, result.b());
            assertFalse(result.isTransparent());
        }

        @Test
        void mapDarkPinkToDomain() {
            Color result = colorMapper.toDomain(darkPinkColorEntity);

            assertEquals(darkPinkColorEntity.getId(), result.id());
            assertEquals(darkPinkColorEntity.getName(), result.name());
            assertEquals(200, result.r());
            assertEquals(112, result.g());
            assertEquals(160, result.b());
            assertFalse(result.isTransparent());
        }

        @Test
        void mapTransparent() {
            Color result = colorMapper.toDomain(transRedColorEntity);

            assertEquals(transRedColorEntity.getId(), result.id());
            assertEquals(transRedColorEntity.getName(), result.name());
            assertEquals(201, result.r());
            assertEquals(26, result.g());
            assertEquals(9, result.b());
            assertTrue(result.isTransparent());
        }

        @Test
        void mapInvalid() {
            assertThrowsExactly(NumberFormatException.class, () -> colorMapper.toDomain(invalidColorEntity));
        }
    }
}
