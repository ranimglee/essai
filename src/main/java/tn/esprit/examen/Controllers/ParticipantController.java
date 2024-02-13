package tn.esprit.examen.Controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.Entity.Participant;
import tn.esprit.examen.Interfaces.ParticipantInterface;

import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("participant")
public class ParticipantController {

    ParticipantInterface pi;
    @PostMapping("/ajouterParticipant")

    public Participant ajouterParticipant(@RequestBody Participant p) {

        return pi.ajouterParticipant(p);
    }

    @GetMapping("/getParReservLogis")
    public Set<Participant> getParReservLogis()
    {
        return pi.getParReservLogis();
    }
}
