package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import less.lgeo.embedded.MatrixEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "models_sub_file_references")
public class SubFileRefEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_sub_file_reference_seq_gen")
    @SequenceGenerator(
            name = "model_sub_file_reference_seq_gen",
            sequenceName = "model_sub_file_reference_seq",
            allocationSize = 50
    )
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "color_id", referencedColumnName = "id", unique = false)
    private ColorEntity color;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_model_id", nullable = false)
    private ModelEntity model;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_model_id", referencedColumnName = "id", unique = false, nullable = true)
    private ModelEntity subModel;

    private String fileName;

    private Long connectionId;

    @Embedded
    private MatrixEmbeddable matrix;
}
