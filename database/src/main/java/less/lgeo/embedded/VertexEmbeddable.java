package less.lgeo.embedded;

import jakarta.persistence.Embeddable;

@Embeddable
public record VertexEmbeddable(double x, double y, double z) {

}
