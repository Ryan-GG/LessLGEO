package less.lgeo.embedded;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Data
public class ModelId implements Serializable {

    @JsonValue
    private Long value;

    public static ModelId of(Long value) {
        return new ModelId(value);
    }
}

