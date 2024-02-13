package tn.esprit.examen.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description ;
    private Date dateDebut;
    private Date dateFin;
    private float cout;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    private Set<Participant> participants=new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @OneToMany
    private Set <Logistique> logistiques=new HashSet<>();



}
