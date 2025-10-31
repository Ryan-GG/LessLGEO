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
}
