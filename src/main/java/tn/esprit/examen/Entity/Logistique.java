package tn.esprit.examen.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Logistique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLog;
    private String description;
    private boolean reserve;
    private float prixUnit;
    private int quantite;

}
