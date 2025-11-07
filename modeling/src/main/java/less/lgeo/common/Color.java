package less.lgeo.common;

import lombok.Builder;
import org.joml.Vector3d;

import java.util.Optional;

@Builder
public record Color(int id, String name, double r, double g, double b, boolean isTransparent) {
    private static final int INHERIT_PRIMARY_COLOR_ID = 16;
    private static final int INHERIT_EDGE_COLOR_ID = 24;

    /**
     * @param inheritedColor Possibly inherit the color based on this {@link Color#id}
     * @return {@link Color}
     */
    public Color inheritColor(Optional<Color> inheritedColor) {

        return inheritedColor.map(color -> switch (this.id()) {
                    case INHERIT_PRIMARY_COLOR_ID, INHERIT_EDGE_COLOR_ID -> color;
                    default -> this;
                })
                .orElse(this);

    }

    @Override
    public String toString() {
        return String.format("(id: %d, name: %s, rgb: %02X%02X%02X, isTransparent: %b)",
                id,
                name,
                (int) (255.999 * r),
                (int) (255.999 * g),
                (int) (255.999 * b),
                isTransparent
        );
    }

    public Vector3d toVector3d() {
        return new Vector3d(r, g, b);
    }


}
