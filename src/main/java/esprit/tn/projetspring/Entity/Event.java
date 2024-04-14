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
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idEvent;
    String titreEvent;
    LocalDate DateEvent;
    String imageEvent;
    int nrParticipants;
    int nbTotalPlace;
    String adresseEvent;
    TypeEvent typeEvent;
    MeansTransport meansTransport;

    @ManyToMany(mappedBy = "events")
    private List<User> users;
}
