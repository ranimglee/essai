package esprit.tn.projetspring.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;
    private String nomUser;
    private String prenomUser;
    private String EmailUser;
    private String mdpUser;
    private String adressUser;
    private int numTel;
    private String imageUser;
    private boolean etat;

    private LocalDate dateNaiss;
    private LocalDate dateMort;

    @ManyToMany

    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    @JsonIgnore
    @ManyToOne
    private Groupe groupe;

    @JsonIgnore
    @ManyToOne
    private Shop shop;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Commande> commandes;

    @JsonIgnore
    @ManyToMany
    private List<Event> events;

    @JsonIgnore
    @ManyToMany
    private List<Library> libraries;

    @JsonIgnore
    @OneToOne
    private Favoris favoris;

    @JsonIgnore
    @ManyToMany
    private List<Medicament> medicaments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Diagnostic> diagnostics;

    @JsonIgnore
    @OneToOne
    private Ceremony ceremony;




}
