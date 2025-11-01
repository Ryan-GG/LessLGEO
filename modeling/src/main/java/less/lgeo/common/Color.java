package less.lgeo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color {
    private int id;
    private String name;
    private String rgb;
    private boolean isTransparent;

    @Override
    public String toString() {
        return String.format("(id: %d, name: %s, rgb: %s, isTransparent: %b)",
                id,
                name,
                rgb,
                isTransparent
        );
    }
}
