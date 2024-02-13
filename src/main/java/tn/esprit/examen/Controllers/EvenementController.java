package tn.esprit.examen.Controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.Entity.Evenement;
import tn.esprit.examen.Interfaces.EvenementInterface;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/event")
public class EvenementController {
    EvenementInterface ei;
    @PostMapping("/ajoutAffectEvenParticip")

    public Evenement ajoutAffectEvenParticip(@RequestBody Evenement e) {

        return ei.ajoutAffectEvenParticip(e);
    }

}
