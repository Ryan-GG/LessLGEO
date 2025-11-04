package less.lgeo.connection;

import less.lgeo.common.Comment;
import less.lgeo.common.Matrix;
import less.lgeo.common.MetaCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.CommonOps_DDRM;
import org.joml.Vector3d;

import java.util.List;

import static less.lgeo.common.Matrix.dMatrixToMatrix;
import static less.lgeo.common.Matrix.matrixToDMatrix;

/**
 * I’ve been playing around with these values and compared it with the other
 * connectivity elements. What I’ve found out so far:
 * <p>
 * First block: 0 (always the same)
 * Second block: PE_CONN (always the same, mostly likely a meta command; Part
 * Editor Connectivity maybe)
 * Third block: ID1 (group ID see figure 1.2 below)
 * Fourth block: ID2 (element ID see figure 1.2 below)
 * Fifth block: 1 0 0 0 1 0 0 0 1 (always the same, Transformation matrix)*
 * Sixth block: XYZ (Position of the element)
 * Seventh block: 2 2 (always the same; geometry data aka visual representation
 * of the element)**
 * Eight block: 3:1,0:4,3:1,0:4,10:4,0:4,3:1,0:4,3:1 (always the same; geometry
 * data aka visual representation of the element)***
 * <p>
 * For other elements not always the identity matrix; see figure 1.2
 * * Lateral size. If you change it from 2 2 to 1 1 it has half the size. But
 * any other value does not work
 * ** Most complicated one. I guess it describes or are related the geometry of
 * the connectivity element itself
 * (squares in the corner, disc in the center and the connection lines). If you
 * change 10:4 to 20:4 for example the disc changes
 * its form from a disc to a ring. Some of the numbers fit roughly to the size
 * of the connectivity element.
 */
@Data
public class Connection {

    public static String COL_EXT = ".col";
    public static String DAT_EXT = ".dat";
    // Part Extension defines connections via PE_CONN meta command
    // This is because traditional .conn files are proprietary and cannot be parsed
    // normally
    public static String PART_EXT = ".part";
        
    private List<Comment> comments;
    private List<MetaCommand> commands;
    private List<PartConnection> partConnections;

    public Connection(List<Comment> comments, List<MetaCommand> commands, List<PartConnection> partConnections) {
        this.comments = comments;
        this.commands = commands;
        this.partConnections = partConnections;
    }

    public static String changeFileExtension(String subFileName, String extension) {
        String fileName = subFileName.substring(0, subFileName.lastIndexOf("."));
        return fileName.concat(extension);
    }

    /**
     * @return Set of rendered connection points as {@link Vector3}
     */
    /*public static Set<Vector3> getConnectionPoints(Connection connection) {
        return connection.getPartConnections().stream()
                .flatMap(partConnection -> switch (partConnection.getGroupId()) {
                    case GroupId.GROUP_ZERO -> Stream.empty();
                    case GroupId.GROUP_ONE -> Stream.empty();
                    case GroupId.GROUP_STUD -> getGroupStudVertices(partConnection);
                    case GroupId.GROUP_FOUR -> Stream.empty();
                    case GroupId.GROUP_SIX -> Stream.empty();
                    default -> Stream.empty();
                }).collect(Collectors.toSet());
    }*/

    /**
     * @return Gets the stud vertices for a part connection. Creates points by the
     * rotation and
     * translation of the connection matrix
     */
    /*private static Stream<Vector3> getGroupStudVertices(PartConnection partConnection) {
        GroupStud studGeometry = partConnection.getGroupStud();

        int zStuds = studGeometry.getZWidthHalfStud() / 2;
        int xStuds = studGeometry.getXWidthHalfStud() / 2;

        Stream<Vector3> connectionVertices = Stream.empty();

        for (int z = 0; z < zStuds; z++) {
            for (int x = 0; x < xStuds; x++) {

                int index = (2 * z + 1) * (studGeometry.getXWidthHalfStud() + 1) + (2 * x + 1);
                if (!studGeometry.getStudGrid(index)) {
                    continue;
                }

                double xStudOffset = -xStuds / 2.0;
                double zStudOffset = zStuds / 2.0;

                // This is to translate so that stud centers align
                double xTranslation = x + 0.5;
                double zTranslation = -z - 0.5;

                double xOffset = (xStudOffset + xTranslation) * BRICK_TO_LDU;
                double zOffset = (zStudOffset + zTranslation) * BRICK_TO_LDU;

                Vector3 center = transformConnectionVector3(xOffset, -STUD_HEIGHT,
                        zOffset,
                        partConnection.getMatrix());

                connectionVertices = Stream.concat(connectionVertices,
                        Stream.of(center));
            }
        }

        return connectionVertices;
    }*/
    private static Vector3d transformConnectionVector3(double xOffset, double yOffset, double zOffset,
                                                       Matrix partconnectionMatrix) {

        DMatrixRMaj transformVector = new DMatrixRMaj(4, 1);
        transformVector.set(0, 0, xOffset);
        transformVector.set(1, 0, yOffset);
        transformVector.set(2, 0, zOffset);
        transformVector.set(3, 0, partconnectionMatrix.scale());

        DMatrixRMaj resultVector = new DMatrixRMaj(4, 1);
        CommonOps_DDRM.mult(new DMatrixRMaj(matrixToDMatrix(partconnectionMatrix)), transformVector,
                resultVector);

        double x = resultVector.get(0, 0);
        double y = resultVector.get(1, 0);
        double z = resultVector.get(2, 0);

        return new Vector3d(x, y, z);
    }

    /**
     * @param transformationMatrix 'dat' / 'piece' matrix
     * @return Transformed connected by 'piece' transformation matrix
     */
    public Connection transformConnection(Matrix transformationMatrix) {

        List<PartConnection> transformedPartConnections = getPartConnections().stream()
                .map(partConnection -> {
                    DMatrix4x4 result = new DMatrix4x4();
                    CommonOps_DDF4.mult(matrixToDMatrix(transformationMatrix),
                            matrixToDMatrix(partConnection.getMatrix()),
                            result);
                    Matrix resulted = dMatrixToMatrix(result);

                    return new PartConnection(partConnection.groupId, partConnection.elementId, resulted, partConnection.groupStud);
                })
                .toList();

        return new Connection(
                this.getComments(),
                this.getCommands(),
                transformedPartConnections
        );
    }

    public enum GroupId {
        GROUP_ZERO,
        GROUP_ONE,
        GROUP_STUD,
        GROUP_FOUR,
        GROUP_SIX;

        public static GroupId fromValue(int i) {

            return switch (i) {
                case 0 -> GROUP_ZERO;
                case 1 -> GROUP_ONE;
                case 2 -> GROUP_STUD;
                case 3 -> GROUP_FOUR;
                case 4 -> GROUP_SIX;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }

    }

    @Data
    @Builder
    public static class PartConnection {
        private final GroupId groupId;
        private final int elementId;
        private final Matrix matrix;
        // FIXME, This should be a oneof like in GPB
        private final GroupStud groupStud;

    }

    @Data
    @AllArgsConstructor
    public static class GroupStud {
        private int xWidthHalfStud;
        private int zWidthHalfStud;

        // flat 2d array, representing a Z by X grid where true means its a
        // connection point for a stud
        private List<Boolean> studGrid;
    }
}