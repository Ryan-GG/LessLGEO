package less.lgeo.common;

import static less.lgeo.connectivity.GroupId.GROUP_FOUR;
import static less.lgeo.connectivity.GroupId.GROUP_ONE;
import static less.lgeo.connectivity.GroupId.GROUP_SIX;
import static less.lgeo.connectivity.GroupId.GROUP_STUD;
import static less.lgeo.connectivity.GroupId.GROUP_ZERO;
import static less.lgeo.connectivity.GroupId.UNRECOGNIZED;

import java.util.Optional;
import less.lgeo.connectivity.GroupId;
import org.ejml.data.DMatrix4x4;

public class CommonUtils {

  public static String COL_EXT = ".col";
  public static String DAT_EXT = ".dat";
  // Part Extension defines connections via PE_CONN meta command
  // This is because traditional .conn files are proprietary and cannot be parsed normally
  public static String PART_EXT = ".part";

  /**
   * TODO: Come back and verify if this is need or the gpb can be modified
   *
   * <p>
   * This weird switch statement is because GPB wants the unknown value as 0, cause that's the
   * default value assigned to the empty gpb
   * </p>
   *
   * @param commandValue Line's Command Value
   * @return Returns the corrected LineType Enum based on the scene line value
   */
  public static LineType getLineType(int commandValue) {
    switch (commandValue) {
      case 0 -> {
        return LineType.COMMENT_OR_META_CMD;
      }
      case 1 -> {
        return LineType.SUB_FILE_REF;
      }
      case 2 -> {
        return LineType.LINE;
      }
      case 3 -> {
        return LineType.TRIANGLE;
      }
      case 4 -> {
        return LineType.QUADRILATERAL;
      }
      case 5 -> {
        return LineType.OPTIONAL_LINE;
      }
      default -> {
        return LineType.LINE_TYPE_UNKNOWN;
      }
    }
  }


  public static GroupId getGroupId(int value) {
    return switch (value) {
      case 0 -> GROUP_ZERO;
      case 1 -> GROUP_ONE;
      case 2, 3 -> GROUP_STUD;
      case 4 -> GROUP_FOUR;
      case 6 -> GROUP_SIX;
      default -> UNRECOGNIZED;
    };
  }

  public static String changeFileExtension(String subFileName, String extension) {
    String fileName = subFileName.substring(0, subFileName.lastIndexOf("."));
    return fileName.concat(extension);
  }

  /**
   * @formatter:off
   * / a b c x \
   * | d e f y |
   * | g h i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  public static DMatrix4x4 gpbToDMatrix(Matrix matrix) {
    return new DMatrix4x4(
        matrix.getA(), matrix.getB(), matrix.getC(), matrix.getX(),
        matrix.getD(), matrix.getE(), matrix.getF(), matrix.getY(),
        matrix.getG(), matrix.getH(), matrix.getI(), matrix.getZ(),
        0.0, 0.0, 0.0, matrix.getScale()
    );
  }

  public static Matrix dMatrixToGpb(DMatrix4x4 matrix) {
    return Matrix.newBuilder()
        .setA(matrix.a11)
        .setB(matrix.a12)
        .setC(matrix.a13)
        .setX(matrix.a14)

        .setD(matrix.a21)
        .setE(matrix.a22)
        .setF(matrix.a23)
        .setY(matrix.a24)

        .setG(matrix.a31)
        .setH(matrix.a32)
        .setI(matrix.a33)
        .setZ(matrix.a34)

        .setScale(matrix.a44)
        .build();
  }

  /**
   * @formatter:off
   * / a b c x \
   * | d e f y |
   * | g h i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  public static String gpbMatrixToString(Matrix m) {
    return String.format(
        """
            \n
            / %.4f %.4f %.4f %.4f\\
            | %.4f %.4f %.4f %.4f |
            | %.4f %.4f %.4f %.4f |
            \\ 0    0    0   %.4f /
            """,
        m.getA(), m.getB(), m.getC(), m.getX(),
        m.getD(), m.getE(), m.getF(), m.getY(),
        m.getG(), m.getH(), m.getI(), m.getZ(), m.getScale()
    );
  }

  /**
   * @param inheritedColor Possibly inherit the color based on the subPartColor
   * @param subPartColor   subPart's color dictates if inheritedColor is needed based on reserved
   *                       color codes 16,24
   * @return Color GPB
   */
  public static Color getColor(Optional<Color> inheritedColor, Color subPartColor) {

    return inheritedColor.map(color -> switch (subPartColor.getId()) {
          case 16 -> {
            Color.Builder builder = subPartColor.toBuilder();

            builder.setValue(color.getValue());

            if (builder.hasAlpha()) {
              builder.setAlpha(color.getAlpha());
            }

            if (builder.hasLuminance()) {
              builder.setLuminance(color.getLuminance());
            }

            if (builder.hasFinish()) {
              builder.setFinish(color.getFinish());
            }

            yield builder.build();
          }
          case 24 -> {
            Color.Builder builder = subPartColor.toBuilder();

            builder.setEdge(color.getEdge());

            if (builder.hasAlpha()) {
              builder.setAlpha(color.getAlpha());
            }

            if (builder.hasLuminance()) {
              builder.setLuminance(color.getLuminance());
            }

            if (builder.hasFinish()) {
              builder.setFinish(color.getFinish());
            }

            yield builder.build();
          }
          default -> subPartColor;
        })
        .orElse(subPartColor);


  }
}
