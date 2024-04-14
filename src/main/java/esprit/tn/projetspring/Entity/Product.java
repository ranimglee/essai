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
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idProduit;
    String nomProduit;
    LocalDate DateFabrication;
    LocalDate dateExpiration;
    String description;
    float prixProduit;
    String imageProduit;
    int nbrProduit;
    boolean statusProd;

    @ManyToOne
    private Shop shop;

    @OneToMany(mappedBy = "product")
    private List<Ligne_Commande> ligneCommandes;



}
