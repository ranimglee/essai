package esprit.tn.projetspring.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Livraison implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idLivraison;
    LocalDate dateLivraison;
    boolean statusLiv;
    String adresseLiv;
    float prixLiv;

    @ManyToMany
    private List<User> users;

    @ManyToOne
    private Commande commande;



}
