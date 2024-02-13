package tn.esprit.examen.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.Entity.Logistique;
import tn.esprit.examen.Interfaces.LogistiqueInterface;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("logistique")
public class LogistiqueController {
    LogistiqueInterface li;

@PostMapping("/ajoutAffectLogEvnm/{description}")
public Logistique ajoutAffectLogEvnm(@RequestBody Logistique l,
                                     @PathVariable("description") String description){
    return li.ajoutAffectLogEvnm(l,description);
}

    @GetMapping("/getLogistiquesDates/{dateDebut}/{dateFin}")
    public Set<Logistique> getLogistiquesDates (@PathVariable("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date dateDebut, @PathVariable("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date dateFin)
    {
        return li.getLogistiquesDates(dateDebut,dateFin);
    }
}
