package esprit.tn.projetspring.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    long idMeal;
    String imgMeals;
    String nameMeal;
    float prixMeals;
    String description;

    @ManyToMany(mappedBy = "meals")
    private List<Ceremony> ceremonies;

}
