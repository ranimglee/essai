package esprit.tn.projetspring.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Podcast implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPod;
    String titrePod;
    TypePod typePod;

    @ManyToOne
    private Library libraries;

    @ManyToOne
    private Favoris favoris;


}
