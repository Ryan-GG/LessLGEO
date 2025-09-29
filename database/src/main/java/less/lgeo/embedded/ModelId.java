package less.lgeo.embedded;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;

/**
 * Strongly Typed Id for {@link less.lgeo.entity.ModelEntity}
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModelId implements Serializable {

    @JsonValue
    private Long value;

    public static ModelId of(Long value) {
        return new ModelId(value);
    }
}

