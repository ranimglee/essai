package tn.esprit.examen.Interfaces;

import tn.esprit.examen.Entity.Logistique;

import java.util.Date;
import java.util.Set;

public interface LogistiqueInterface {
    public Logistique ajoutAffectLogEvnm (Logistique l, String descriptionEvnmt);
    public Set<Logistique> getLogistiquesDates (Date dateDebut, Date dateFin);
}
