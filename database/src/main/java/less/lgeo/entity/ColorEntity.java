package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colors")
public class ColorEntity {

    @Id
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String rgb;

    @Column(nullable = false)
    private boolean isTrans;

    @Column(nullable = false)
    private int numParts;

    @Column(nullable = false)
    private int numSets;

    @Column(name = "y1")
    private Integer startYear;

    @Column(name = "y2")
    private Integer endYear;

}
