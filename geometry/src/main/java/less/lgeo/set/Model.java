package less.lgeo.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import less.lgeo.primitive.Comment;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.MetaCommand;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;

public class Model {

  private final List<Comment> comments;
  private final List<MetaCommand> commands;
  private final List<Line> lines;
  private final List<Triangle> triangles;
  private final List<Quadrilateral> quadrilaterals;
  private final List<OptionalLine> optionalLines;
  private final List<SubFileReference> pieces;

  public Model(List<Comment> comments, List<MetaCommand> commands, List<Line> lines,
      List<Triangle> triangles, List<Quadrilateral> quadrilaterals,
      List<OptionalLine> optionalLines, List<SubFileReference> pieces) {
    this.comments = comments;
    this.commands = commands;
    this.lines = lines;
    this.triangles = triangles;
    this.quadrilaterals = quadrilaterals;
    this.optionalLines = optionalLines;
    this.pieces = pieces;
  }

  public List<SubFileReference> getPieces() {
    return this.pieces;
  }

  @Override
  public String toString() {
    return String.format("""
            Model {
              comments='%s',
              commands='%s,
              lines='%s',
              triangles='%s,
              quadrilaterals='%s',
              optionalLines='%s',
              pieces='%s'
            }
            """,
        this.comments,
        this.commands,
        this.lines,
        this.triangles,
        this.quadrilaterals,
        this.optionalLines,
        this.pieces
    );
  }

  public static class Builder {

    private final List<Comment> builderComments = new ArrayList<>();
    private final List<MetaCommand> builderCommands = new ArrayList<>();
    private final List<Line> builderLines = new ArrayList<>();
    private final List<Triangle> builderTriangles = new ArrayList<>();
    private final List<Quadrilateral> builderQuadrilaterals = new ArrayList<>();
    private final List<OptionalLine> builderOptionalLines = new ArrayList<>();
    private final List<SubFileReference> builderPieces = new ArrayList<>();

    private Builder() {
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder addComment(Comment comment) {
      builderComments.add(comment);
      return this;
    }

    public Builder addCommand(MetaCommand command) {
      builderCommands.add(command);
      return this;
    }

    public Builder addLine(Line line) {
      builderLines.add(line);
      return this;
    }

    public Builder addLines(Collection<Line> linesToAdd) {
      builderLines.addAll(linesToAdd);
      return this;
    }

    public Builder addTriangle(Triangle triangle) {
      builderTriangles.add(triangle);
      return this;
    }

    public Builder addTriangles(Collection<Triangle> trianglesToAdd) {
      builderTriangles.addAll(trianglesToAdd);
      return this;
    }

    public Builder addQuadrilateral(Quadrilateral quadrilateral) {
      builderQuadrilaterals.add(quadrilateral);
      return this;
    }

    public Builder addQuadrilaterals(Collection<Quadrilateral> quadsToAdd) {
      builderQuadrilaterals.addAll(quadsToAdd);
      return this;
    }

    public Builder addOptionalLine(OptionalLine line) {
      builderOptionalLines.add(line);
      return this;
    }

    public Builder addOptionalLines(Collection<OptionalLine> linesToAdd) {
      builderOptionalLines.addAll(linesToAdd);
      return this;
    }

    public Builder addPiece(SubFileReference piece) {
      builderPieces.add(piece);
      return this;
    }

    public Builder addPieces(Collection<SubFileReference> piecesToAdd) {
      builderPieces.addAll(piecesToAdd);
      return this;
    }

    public Model build() {
      return new Model(builderComments, builderCommands, builderLines, builderTriangles,
          builderQuadrilaterals, builderOptionalLines, builderPieces);
    }
  }
}
