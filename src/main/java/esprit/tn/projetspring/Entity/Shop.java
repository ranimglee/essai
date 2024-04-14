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
public class Shop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idShop;
    TypeShop typeShop;

    @OneToMany(mappedBy = "shop")
    private List<User> users;

    @OneToMany(mappedBy = "shop")
    private List<Product> products;



}
