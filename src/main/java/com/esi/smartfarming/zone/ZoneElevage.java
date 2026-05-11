package com.esi.smartfarming.zone;

import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.historique.HistoriqueLait;
import com.esi.smartfarming.historique.HistoriqueOeuf;

import java.util.ArrayList;
import java.util.List;

public class ZoneElevage extends Zone {
    private List<Animal> animaux;
    private ProgrammeAlimentation programmeAlimentation;
    private HistoriqueLait historiqueLait;
    private HistoriqueOeuf historiqueOeuf;
    
    public ZoneElevage(String code, String nom, ProgrammeAlimentation programmeAlimentation) {
        super(code, nom);
        this.animaux = new ArrayList<>();
        this.programmeAlimentation = programmeAlimentation;
    }
}
