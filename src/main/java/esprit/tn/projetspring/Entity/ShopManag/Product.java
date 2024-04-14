package esprit.tn.projetspring.Entity.ShopManag;

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



    @OneToMany(mappedBy = "product")
    private List<CommandLine> ligneCommandes;

    @OneToMany
    private List<Rating> ratings;



}
