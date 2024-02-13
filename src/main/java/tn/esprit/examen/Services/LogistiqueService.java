package tn.esprit.examen.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.Entity.Evenement;
import tn.esprit.examen.Entity.Logistique;
import tn.esprit.examen.Interfaces.LogistiqueInterface;
import tn.esprit.examen.Repository.EvenementRepository;
import tn.esprit.examen.Repository.LogistiqueRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class LogistiqueService implements LogistiqueInterface {

    EvenementRepository er;
    LogistiqueRepository lr;
    @Transactional
    public Logistique ajoutAffectLogEvnm(Logistique l, String description) {
   Evenement e=er.findByDescriptionLike(description);
   Set<Logistique> list = new HashSet<>();
   list.add(l);
   if(e.getLogistiques()==null)
   {
       e.setLogistiques(list);

   }
   else {
       e.getLogistiques().add(l);
   }
         return lr.save(l);

    }

    @Override
    public Set<Logistique> getLogistiquesDates(Date dateDebut, Date dateFin) {
        Set<Evenement> events =er.findByDateDebutBetween(dateDebut,dateFin);
        Set<Logistique> allLogists= new HashSet<>();
        for (Evenement e: events)
        {
            for(Logistique l: e.getLogistiques())
            {if (l.isReserve())
                allLogists.add(l);
            }
        }

        return null;
    }
}
