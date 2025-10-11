package less.lgeo;

import less.lgeo.common.LineType;
import less.lgeo.common.Vertex;
import less.lgeo.embedded.ModelId;
import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.entity.*;
import less.lgeo.primitive.*;
import less.lgeo.service.ModelService;
import less.lgeo.tracer.BoundingVolumeHierarchy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static less.lgeo.common.VertexUtils.toVertex;
import static less.lgeo.primitive.ModelUtils.IDENTITY_MATRIX;
import static less.lgeo.primitive.ModelUtils.splitIntoTriangles;

@SpringBootApplication
public class ReducerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

    @Autowired
    private final ModelService modelService;

    public ReducerHandler(ModelService modelService) {
        this.modelService = modelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReducerHandler.class);
    }


    /**
     * See {@link less.lgeo.consumer.ReducerConsumer}
     */
    public void consume(ModelId modelId) {
        ModelEntity model = modelService.getModelById(modelId);

        Model converted = toGpbModel(model);
        logger.info("converted: {}", converted);


        List<Triangle> triangles = splitIntoTriangles(converted);
        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);
        List<Line> boundingBoxesLines = bvh.getBoundingBoxes();
        Model justBoundBoxes = converted.toBuilder()
                .clearTriangle()
                .clearQuadrilateral()
                .clearLine()
                .clearOptionalLine()
                .clearPiece()
                .addAllLine(boundingBoxesLines)
                .build();

        ModelId newBoundBoxId = modelService.insertModel(justBoundBoxes);
        logger.info("Inserting model with bounding boxes, {}", newBoundBoxId);
        logger.info("BVH: {}", bvh);

    }

    private Vertex toGpb(VertexEmbeddable vertexEmbeddable) {
        return toVertex(vertexEmbeddable.getX(), vertexEmbeddable.getY(), vertexEmbeddable.getZ());
    }

    private Triangle toGpb(TriangleEntity triangleEntity) {
        return Triangle.newBuilder()
                .setType(LineType.TRIANGLE)
                .setColorId(triangleEntity.getColor().getId())
                .setP1(toGpb(triangleEntity.getP1()))
                .setP2(toGpb(triangleEntity.getP2()))
                .setP3(toGpb(triangleEntity.getP3()))
                .build();
    }

    private Line toGpb(LineEntity lineEntity) {
        return Line.newBuilder()
                .setType(LineType.LINE)
                .setColorId(lineEntity.getColor().getId())
                .setP1(toGpb(lineEntity.getP1()))
                .setP2(toGpb(lineEntity.getP2()))
                .build();
    }

    private Quadrilateral toGpb(QuadrilateralEntity quadrilateralEntity) {
        return Quadrilateral.newBuilder()
                .setType(LineType.QUADRILATERAL)
                .setColorId(quadrilateralEntity.getColor().getId())
                .setP1(toGpb(quadrilateralEntity.getP1()))
                .setP2(toGpb(quadrilateralEntity.getP2()))
                .setP3(toGpb(quadrilateralEntity.getP3()))
                .setP4(toGpb(quadrilateralEntity.getP4()))
                .build();
    }

    private OptionalLine toGpb(OptionalLineEntity optionalLineEntity) {
        return OptionalLine.newBuilder()
                .setType(LineType.OPTIONAL_LINE)
                .setColorId(optionalLineEntity.getColor().getId())
                .setP1(toGpb(optionalLineEntity.getP1()))
                .setP2(toGpb(optionalLineEntity.getP2()))
                .setP3(toGpb(optionalLineEntity.getP3()))
                .setP4(toGpb(optionalLineEntity.getP4()))
                .build();
    }

    private Model toGpbModel(ModelEntity modelEntity) {
        return Model.newBuilder()
                .addAllLine(modelEntity.getLines().stream().map(this::toGpb).toList())
                .addAllTriangle(modelEntity.getTriangles().stream().map(this::toGpb).toList())
                .addAllQuadrilateral(modelEntity.getQuadrilaterals().stream().map(this::toGpb).toList())
                .addAllOptionalLine(modelEntity.getOptionalLines().stream().map(this::toGpb).toList())
                .addAllPiece(modelEntity.getChildren().stream().map(this::toGpbSubFileReference).toList())
                .build();
    }

    private SubFileReference toGpbSubFileReference(ModelEntity modelEntity) {
        return SubFileReference.newBuilder()
                .setType(LineType.SUB_FILE_REF)
                .setMatrix(IDENTITY_MATRIX)
                .setSubModel(toGpbModel(modelEntity))
                .build();
    }
}