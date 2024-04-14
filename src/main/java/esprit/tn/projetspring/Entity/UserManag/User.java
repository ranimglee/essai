package esprit.tn.projetspring.Entity.UserManag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esprit.tn.projetspring.Entity.EventManag.Event;
import esprit.tn.projetspring.Entity.EventManag.Favoris;
import esprit.tn.projetspring.Entity.EventManag.Library;
import esprit.tn.projetspring.Entity.ForumManag.Comment;
import esprit.tn.projetspring.Entity.ForumManag.Groupe;
import esprit.tn.projetspring.Entity.ForumManag.Message;
import esprit.tn.projetspring.Entity.ForumManag.Post;
import esprit.tn.projetspring.Entity.FuneralManag.Ceremony;
import esprit.tn.projetspring.Entity.FuneralManag.TypeReligion;
import esprit.tn.projetspring.Entity.HealthManag.Diagnostic;
import esprit.tn.projetspring.Entity.HealthManag.Medicament;
import esprit.tn.projetspring.Entity.ShopManag.Orderr;
import esprit.tn.projetspring.Entity.ShopManag.Rating;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;
    private String nomUser;
    private String prenomUser;
    private String emailUser;
    private String mdpUser;
    private String adressUser;
    private String numTel;
    private String imageUser;
    private boolean etat;
    private String confirmationToken;
    private boolean accountConfirmed;
   // private boolean banni;

    @Enumerated(EnumType.STRING)
    private TypeReligion religion;
    private String sexe;

    private Date dateNaiss;
    private Date dateMort;

    public String getResetCode() {
        return resetCode;
    }
    @Column(name = "reset_code")
    private String resetCode; // New field for storing the reset code

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Message> messages;

    @ManyToOne
    @JsonIgnore
    private Groupe groupe;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Orderr> commandes;

    @ManyToMany
    @JsonIgnore
    private List<Event> events;

    @ManyToMany
    @JsonIgnore
    private List<Library> libraries;

    @OneToOne
    @JsonIgnore
    private Favoris favoris;

    @ManyToMany
    @JsonIgnore
    private List<Medicament> medicaments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Diagnostic> diagnostics;

    @OneToOne
    @JsonIgnore
    private Ceremony ceremony;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Rating> ratings;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority>  autorities=roles.stream().map(role -> {
            return  new SimpleGrantedAuthority(role.getRoleName().toString());
        }).collect(Collectors.toList()) ;
        return autorities;
    }

    @Override
    public String getPassword() {
        return mdpUser;
    }


    @Override
    public String getUsername() {
        return emailUser;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
