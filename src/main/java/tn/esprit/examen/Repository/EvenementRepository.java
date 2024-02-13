package tn.esprit.examen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.Entity.Evenement;

import java.util.Date;
import java.util.Set;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement,Integer> {
    Evenement findByDescriptionLike(String description);
    Set<Evenement> findByDateDebutBetween(Date dateDebut, Date dateFin);

}
