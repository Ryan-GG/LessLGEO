package less.lgeo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color {
    private static final int INHERIT_PRIMARY_COLOR_ID = 16;
    private static final int INHERIT_EDGE_COLOR_ID = 24;
    private int id;
    private String name;
    private String rgb;
    private boolean isTransparent;

    /**
     * @param inheritedColor Possibly inherit the color based on this {@link Color#id}
     * @return {@link Color}
     */
    public Color inheritColor(Optional<Color> inheritedColor) {

        return inheritedColor.map(color -> switch (this.getId()) {
                    case INHERIT_PRIMARY_COLOR_ID, INHERIT_EDGE_COLOR_ID -> color;
                    default -> this;
                })
                .orElse(this);

    }

    @Override
    public String toString() {
        return String.format("(id: %d, name: %s, rgb: %s, isTransparent: %b)",
                id,
                name,
                rgb,
                isTransparent
        );
    }

    //FIXME, i feel like this should just have the actual java.awt.Color value rather than the string
    public int getRed() {
        return java.awt.Color.decode(rgb).getRed();
    }

    public int getGreen() {
        return java.awt.Color.decode(rgb).getGreen();
    }

    public int getBlue() {
        return java.awt.Color.decode(rgb).getBlue();
    }


}
