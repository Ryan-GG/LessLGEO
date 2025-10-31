package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class MatrixEmbeddable {

    private double a;
    private double b;
    private double c;
    private double x;
    private double d;
    private double e;
    private double f;
    private double y;
    private double g;
    private double h;
    private double i;
    private double z;
    private double scale;

}