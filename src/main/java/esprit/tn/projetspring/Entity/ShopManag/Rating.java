package esprit.tn.projetspring.Entity.ShopManag;

import esprit.tn.projetspring.Entity.UserManag.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idRate;
    int nbrStars;

    @ManyToOne
    private User user;
}
