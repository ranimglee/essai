package tn.esprit.examen.Services;

import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.Entity.Evenement;
import tn.esprit.examen.Entity.Participant;
import tn.esprit.examen.Interfaces.EvenementInterface;
import tn.esprit.examen.Repository.EvenementRepository;
import tn.esprit.examen.Repository.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EvenementService implements EvenementInterface {

    ParticipantRepository pr;
    EvenementRepository er;

    @Transactional
    public Evenement ajoutAffectEvenParticip(Evenement e) {

        return er.save(e);
 }


}
