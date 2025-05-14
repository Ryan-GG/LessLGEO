package less.lgeo.primitive;

public class Comment {

  private final int lineNumber;
  private final String comment;

  public Comment(int lineNumber, String comment) {
    this.lineNumber = lineNumber;
    this.comment = comment;
  }

  @Override
  public String toString() {
    return this.comment;
  }
}
