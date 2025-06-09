package less.lgeo.utils;

import less.lgeo.common.Vertex;
import org.fxyz3d.geometry.Point3D;

public class RenderUtils {

  public static Point3D gpbToPoint3D( Vertex point ) {
    return new Point3D( point.getX(), point.getY(), point.getZ() );
  }
}
