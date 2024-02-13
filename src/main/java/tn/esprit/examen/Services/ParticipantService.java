package tn.esprit.examen.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.examen.Entity.Evenement;
import tn.esprit.examen.Entity.Participant;
import tn.esprit.examen.Entity.Tache;
import tn.esprit.examen.Interfaces.ParticipantInterface;
import tn.esprit.examen.Repository.EvenementRepository;
import tn.esprit.examen.Repository.LogistiqueRepository;
import tn.esprit.examen.Repository.ParticipantRepository;

import java.util.Set;
@Slf4j
@AllArgsConstructor
@Service
public class ParticipantService implements ParticipantInterface {

    ParticipantRepository pr;
    EvenementRepository er;
    LogistiqueRepository lr;


    @Override
    public Participant ajouterParticipant(Participant p) {
        return pr.save(p) ;
    }

    @Override
    public Set<Participant> getParReservLogis() {
        return pr.participReservLogis(true, Tache.ORGANISATEUR);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void calculCout() {
        float cout = 0;
        Set<Evenement> evenements= (Set<Evenement>) er.findAll();
        for (Evenement ev : evenements)
        {
            cout = cout + lr.calculPrixLogistiquesReserves(true);
            ev.setCout(cout);
            er.save(ev);
           log.info("le cout de l'evenement : " + ev +" est:" +cout +
                    "il est mis à jour dans la base");
        }
    }
}
