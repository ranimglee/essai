package esprit.tn.projetspring.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;
    private String descPost;
    private Date dateCreation;
    private String imagePost;

    @ManyToOne
    private User user;



}
