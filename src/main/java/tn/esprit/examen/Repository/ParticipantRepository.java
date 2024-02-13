package tn.esprit.examen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.Entity.Participant;
import tn.esprit.examen.Entity.Tache;

import java.util.Date;
import java.util.Set;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findByNomAndPrenomAndTache(String nom, String prenom, Tache tache);

    @Query("SELECT p from Participant p join p.evenements evnts where evnts.dateDebut>=?1")
    Set<Participant> participByDateEvent (Date dateDebut);


    @Query("SELECT DISTINCT p from Participant p join p.evenements evnts join evnts.logistiques logis where logis.reserve=?1 AND p.tache=?2")
    Set <Participant> participReservLogis (Boolean state,Tache tache);


}
