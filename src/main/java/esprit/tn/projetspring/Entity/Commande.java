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

public class Commande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idCom;
    Long numCom;
    Long nbProduit;
    float totalPrix;
    float remise;
    boolean statusCom;
    LocalDate date_commande;
    ModePayement modePayement;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Ligne_Commande> ligneCommandes;

    @OneToMany(mappedBy = "commande")
    private List<Livraison> livraisons;






}
