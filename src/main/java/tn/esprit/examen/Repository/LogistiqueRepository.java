package tn.esprit.examen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.Entity.Logistique;

@Repository
public interface LogistiqueRepository extends JpaRepository<Logistique,Integer> {

    @Query("Select SUM (log.prixUnit * log.quantite) from Logistique log where log.reserve=:state ")
    public float calculPrixLogistiquesReserves (boolean state);

}
