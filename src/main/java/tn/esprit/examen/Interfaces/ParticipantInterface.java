package tn.esprit.examen.Interfaces;

import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.examen.Entity.Participant;

import java.util.Set;

public interface ParticipantInterface {
    public Participant ajouterParticipant (Participant p);
    public Set<Participant> getParReservLogis ( );

    @Scheduled(fixedRate = 60000)
    void calculCout();
}
