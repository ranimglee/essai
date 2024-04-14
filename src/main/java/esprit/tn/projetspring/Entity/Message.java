package esprit.tn.projetspring.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idMessage ;
    String Contenu;
    LocalDate DateMsg;

    @ManyToOne
    private User user;

    @ManyToOne
    private Groupe groupe;
}
