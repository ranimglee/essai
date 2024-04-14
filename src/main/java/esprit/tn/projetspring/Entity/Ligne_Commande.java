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
public class Ligne_Commande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idLigneCom;
    float quantite;
    float prix_total_product;

    @ManyToOne
    private Commande commande;
    @ManyToOne
    private Product product;



}
