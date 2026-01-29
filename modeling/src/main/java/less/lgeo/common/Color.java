package less.lgeo.common;

import lombok.Builder;
import org.joml.Vector3d;

import java.util.Optional;

//TODO, #61 - Migrate from Rebrickable Color back to LDraw implementation of !Colour definition
@Builder
public record Color(int id, String name, double r, double g, double b, boolean isTransparent) {
    private static final int INHERIT_PRIMARY_COLOR_ID = 16;
    private static final int INHERIT_EDGE_COLOR_ID = 24;

    /**
     * Wrapper for {@link Vector3d}
     *
     * @param r Red, {@link Vector3d#x()}
     * @param g Green, {@link Vector3d#y()}
     * @param b Blue, {@link Vector3d#z()}
     */
    public static Color of(double r, double g, double b, boolean isTransparent) {
        return new Color(-1, "Vector3", r, g, b, isTransparent);
    }

    public static Color of(double r, double g, double b) {
        return of(r, g, b, false);
    }

    public static Color of(Vector3d vector3d) {
        return of(vector3d.x(), vector3d.y(), vector3d.z());
    }

    public static Color of(double val) {
        return of(val, val, val);
    }


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

    public Color interpolate(Color other, Double t) {
        return Color.of(toVector3d().lerp(other.toVector3d(), t, new Vector3d()));
    }

}
