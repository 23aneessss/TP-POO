package com.esi.smartfarming.ui;

import com.esi.smartfarming.animal.*;
import com.esi.smartfarming.capteur.*;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.enums.*;
import com.esi.smartfarming.zone.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ZonesView {

    // ── Observable table data (refreshed after mutations) ─────────────────────
    private ObservableList<String[]> culturesData;
    private ObservableList<String[]> animauxData;
    private ObservableList<String[]> especesData;
    private ObservableList<String[]> capteurNordData;
    private ObservableList<String[]> capteurEstData;
    private ObservableList<String[]> capteurSudData;

    // ── Status labels ─────────────────────────────────────────────────────────
    private Label lblNordStatut;
    private Label lblEstStatut;
    private Label lblSudStatut;

    // ── Entry point ───────────────────────────────────────────────────────────

    public Node build() {
        DataStore ds = DataStore.getInstance();

        culturesData    = obs(ds.getZoneNord().getCultures().stream().map(ds::toCultureRow).collect(Collectors.toList()));
        animauxData     = obs(ds.getZoneEst().getAnimaux().stream().map(ds::toAnimalRow).collect(Collectors.toList()));
        especesData     = obs(ds.getZoneSud().getEspeces().stream().map(ds::toEspeceRow).collect(Collectors.toList()));
        capteurNordData = obs(ds.getZoneNord().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));
        capteurEstData  = obs(ds.getZoneEst().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));
        capteurSudData  = obs(ds.getZoneSud().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));

        lblNordStatut = new Label(); lblEstStatut = new Label(); lblSudStatut = new Label();

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            new Tab("  Zone Nord (Culture)  ",  buildCultureTab()),
            new Tab("  Zone Est  (Elevage)  ",  buildElevageTab()),
            new Tab("  Zone Sud  (Aquacole) ",  buildAquacoleTab())
        );
        return tabs;
    }

    // =========================================================================
    // ZONE CULTURE
    // =========================================================================

    private Node buildCultureTab() {
        DataStore ds = DataStore.getInstance();
        refreshZoneStatut(ds.getZoneNord(), lblNordStatut);

        // ── Header ──────────────────────────────────────────────────────────
        Label name = boldLabel("Zone Nord — ZoneCulture", 16);
        Button btnSuspendre = grayBtn("Suspendre / Activer");
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneNord());
            refreshZoneStatut(ds.getZoneNord(), lblNordStatut);
        });
        HBox header = hrow(name, lblNordStatut, spacer(), btnSuspendre);

        // ── Actions cultures ─────────────────────────────────────────────────
        Button btnAjouterC    = greenBtn("+ Ajouter culture");
        Button btnChangerStade = blueBtn("Changer stade");
        Button btnRendement    = blueBtn("Enregistrer rendement");
        btnAjouterC.setOnAction(e   -> { showAddCultureDialog();          refreshCultures(); });
        btnChangerStade.setOnAction(e -> showChangeStageCultureDialog());
        btnRendement.setOnAction(e    -> showEnregistrerRendementDialog());
        HBox actCultures = hrow(btnAjouterC, btnChangerStade, btnRendement);

        // ── Actions capteurs ─────────────────────────────────────────────────
        Button btnAddEnv     = blueBtn("+ Capteur ENV");
        Button btnAddSol     = blueBtn("+ Capteur Sol");
        Button btnReleves    = orangeBtn("Envoyer releves");
        btnAddEnv.setOnAction(e   -> { showAddCapteurEnvDialog();    refreshCapteurNord(); });
        btnAddSol.setOnAction(e   -> { showAddCapteurSolDialog();    refreshCapteurNord(); });
        btnReleves.setOnAction(e  -> envoyerRelevesZone(ds.getZoneNord()));
        HBox actCapteurs = hrow(btnAddEnv, btnAddSol, btnReleves);

        // ── Layout ───────────────────────────────────────────────────────────
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            header,
            sectionLabel("Cultures (" + ds.getZoneNord().getCultures().size() + ")"),
            actCultures,
            cultureTable(),
            sectionLabel("Capteurs (" + ds.getZoneNord().getCapteurs().size() + ")"),
            actCapteurs,
            capteurTable(capteurNordData)
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── Culture dialogs ───────────────────────────────────────────────────────

    private void showAddCultureDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Ajouter une culture", "Nouvelle culture — Zone Nord");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField tfNom = tf("Ex: Ble dur");
        ComboBox<TypeFamille>     cbFam   = combo(TypeFamille.values());
        ComboBox<StageCroissance> cbStade = combo(StageCroissance.values());
        TextField tfPhMin = tf("6.0"), tfPhMax = tf("7.5");
        TextField tfHMin  = tf("40"),  tfHMax  = tf("70");

        GridPane g = grid();
        addRow(g, 0, "Nom :",           tfNom);
        addRow(g, 1, "Famille :",        cbFam);
        addRow(g, 2, "Stade :",          cbStade);
        addRow(g, 3, "pH min :",         tfPhMin);
        addRow(g, 4, "pH max :",         tfPhMax);
        addRow(g, 5, "Humidite min % :", tfHMin);
        addRow(g, 6, "Humidite max % :", tfHMax);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                Culture c = new Culture(ds.nextCultureId(), tfNom.getText().trim(),
                    cbFam.getValue(), new Date(), new Date(),
                    Double.parseDouble(tfPhMin.getText()),
                    Double.parseDouble(tfPhMax.getText()),
                    Double.parseDouble(tfHMin.getText()),
                    Double.parseDouble(tfHMax.getText()));
                c.setStageCroissance(cbStade.getValue());
                ds.ajouterCulture(c);
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showChangeStageCultureDialog() {
        DataStore ds = DataStore.getInstance();
        if (ds.getZoneNord().getCultures().isEmpty()) { info("Aucune culture", "Ajoutez d'abord une culture."); return; }

        Dialog<ButtonType> d = dialog("Changer stade", "Mettre a jour le stade de croissance");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        List<Culture> cultures = ds.getZoneNord().getCultures();
        ComboBox<Culture> cbC = new ComboBox<>(FXCollections.observableArrayList(cultures));
        cbC.setCellFactory(lv -> cultCell()); cbC.setButtonCell(cultCell());
        cbC.getSelectionModel().selectFirst();
        ComboBox<StageCroissance> cbStade = combo(StageCroissance.values());
        cbC.setOnAction(e -> {
            Culture sel = cbC.getValue();
            if (sel != null) cbStade.setValue(sel.getStageCroissance());
        });
        if (!cultures.isEmpty()) cbStade.setValue(cultures.get(0).getStageCroissance());

        GridPane g = grid();
        addRow(g, 0, "Culture :", cbC);
        addRow(g, 1, "Nouveau stade :", cbStade);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok || cbC.getValue() == null) return;
            ds.changerStageCulture(cbC.getValue(), cbStade.getValue());
            refreshCultures();
        });
    }

    private void showEnregistrerRendementDialog() {
        Dialog<ButtonType> d = dialog("Rendement", "Enregistrer le rendement — Zone Nord");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField tfR = tf("Ex: 4.5");
        GridPane g = grid();
        addRow(g, 0, "Rendement (t/ha) :", tfR);
        d.getDialogPane().setContent(g);
        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try { DataStore.getInstance().enregistrerRendement(Double.parseDouble(tfR.getText())); info("Succes", "Rendement enregistre."); }
            catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showAddCapteurEnvDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Capteur Environnemental", "Nouveau capteur ENV — Zone Nord");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField tfT = tf("22.5"), tfH = tf("55.0"), tfPluv = tf("0.0");
        TextField tfTMin = tf("15.0"), tfTMax = tf("35.0");
        TextField tfHMin = tf("30.0"), tfHMax = tf("70.0");
        TextField tfPMin = tf("0.0"),  tfPMax = tf("50.0");

        GridPane g = grid();
        addRow(g, 0, "Temperature °C :",         tfT);
        addRow(g, 1, "Humidite % :",              tfH);
        addRow(g, 2, "Pluviometrie mm :",          tfPluv);
        addRow(g, 3, "Seuil min T °C :",           tfTMin);
        addRow(g, 4, "Seuil max T °C :",           tfTMax);
        addRow(g, 5, "Seuil min H % :",            tfHMin);
        addRow(g, 6, "Seuil max H % :",            tfHMax);
        addRow(g, 7, "Seuil min Pluv mm :",        tfPMin);
        addRow(g, 8, "Seuil max Pluv mm :",        tfPMax);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                String code = ds.nextCapteurCode("CE-");
                CapteurEnvironnemental c = new CapteurEnvironnemental(
                    code, ds.getZoneNord(), "°C",
                    p(tfT), p(tfH), p(tfPluv),
                    p(tfTMin), p(tfTMax), p(tfHMin), p(tfHMax), p(tfPMin), p(tfPMax));
                ds.ajouterCapteurEnv(c);
                info("Succes", "Capteur [" + code + "] ajoute a Zone Nord.");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showAddCapteurSolDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Capteur Sol", "Nouveau capteur Sol — Zone Nord");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField tfPh = tf("6.8"), tfH = tf("55.0"), tfAz = tf("2.1");
        TextField tfPhMin = tf("6.0"), tfPhMax = tf("7.5");
        TextField tfHMin = tf("30.0"), tfHMax = tf("70.0");
        TextField tfAMin = tf("1.0"), tfAMax = tf("5.0");

        GridPane g = grid();
        addRow(g, 0, "pH :",                    tfPh);
        addRow(g, 1, "Humidite sol % :",         tfH);
        addRow(g, 2, "Teneur azote mg/kg :",     tfAz);
        addRow(g, 3, "Seuil min pH :",           tfPhMin);
        addRow(g, 4, "Seuil max pH :",           tfPhMax);
        addRow(g, 5, "Seuil min H % :",          tfHMin);
        addRow(g, 6, "Seuil max H % :",          tfHMax);
        addRow(g, 7, "Seuil min Azote mg/kg :", tfAMin);
        addRow(g, 8, "Seuil max Azote mg/kg :", tfAMax);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                String code = ds.nextCapteurCode("CS-");
                CapteurSol c = new CapteurSol(
                    code, ds.getZoneNord(), "pH",
                    p(tfPh), p(tfH), p(tfAz),
                    p(tfPhMin), p(tfPhMax), p(tfHMin), p(tfHMax), p(tfAMin), p(tfAMax));
                ds.ajouterCapteurSol(c);
                info("Succes", "Capteur Sol [" + code + "] ajoute a Zone Nord.");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    // =========================================================================
    // ZONE ELEVAGE
    // =========================================================================

    private Node buildElevageTab() {
        DataStore ds = DataStore.getInstance();
        refreshZoneStatut(ds.getZoneEst(), lblEstStatut);

        // ── Header ──────────────────────────────────────────────────────────
        Label name = boldLabel("Zone Est — ZoneElevage", 16);
        Button btnSuspendre = grayBtn("Suspendre / Activer");
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneEst());
            refreshZoneStatut(ds.getZoneEst(), lblEstStatut);
        });
        HBox header = hrow(name, lblEstStatut, spacer(), btnSuspendre);

        // ── Programme alimentation ────────────────────────────────────────
        Label progLabel = progLabel(ds);
        Button btnModifProg = grayBtn("Modifier alimentation");
        btnModifProg.setOnAction(e -> { showModifierAlimentationElevageDialog(); progLabel.setText(progText(ds)); });
        HBox progRow = hrow(progLabel, spacer(), btnModifProg);
        progRow.setPadding(new Insets(6, 10, 6, 10));
        progRow.setStyle("-fx-background-color: #eaf4fb; -fx-background-radius: 6;");

        // ── Actions animaux ───────────────────────────────────────────────
        Button btnAjout   = greenBtn("+ Ajouter animal");
        Button btnDetails = blueBtn("Voir details");
        Button btnProd    = blueBtn("Enregistrer production");
        Button btnEvt     = blueBtn("Evenement sanitaire");
        Button btnLimites = orangeBtn("Verifier limites");
        btnAjout.setOnAction(e   -> { showAddAnimalDialog();              refreshAnimaux(); });
        btnDetails.setOnAction(e -> showDetailsAnimalDialog());
        btnProd.setOnAction(e    -> showEnregistrerProductionDialog());
        btnEvt.setOnAction(e     -> showEvenementSanitaireDialog());
        btnLimites.setOnAction(e -> showVerifierLimitesDialog());
        HBox actAnimaux = hrow(btnAjout, btnDetails, btnProd, btnEvt, btnLimites);

        // ── Actions capteurs ──────────────────────────────────────────────
        Button btnBio  = blueBtn("+ Capteur Biometrique");
        Button btnGPS  = blueBtn("+ Capteur GPS");
        btnBio.setOnAction(e -> { showAddCapteurBioDialog(); refreshCapteurEst(); });
        btnGPS.setOnAction(e -> { showAddCapteurGPSDialog(); refreshCapteurEst(); });
        HBox actCapteurs = hrow(btnBio, btnGPS);

        // ── Layout ───────────────────────────────────────────────────────
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            header, progRow,
            sectionLabel("Animaux"),
            actAnimaux,
            animauxTable(),
            sectionLabel("Capteurs (" + ds.getZoneEst().getCapteurs().size() + ")"),
            actCapteurs,
            capteurTable(capteurEstData)
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── Elevage dialogs ───────────────────────────────────────────────────────

    private void showAddAnimalDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Ajouter un animal", "Nouvel animal — Zone Est");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        ComboBox<String>      cbType  = new ComboBox<>(FXCollections.observableArrayList("Ruminant", "Volaille"));
        cbType.getSelectionModel().selectFirst();
        TextField tfEspece = tf("Ex: Vache Normande");
        TextField tfAge    = tf("2");
        TextField tfPoids  = tf("400");
        ComboBox<StatutAnimal> cbSante = combo(StatutAnimal.values());

        GridPane g = grid();
        addRow(g, 0, "Type :",   cbType);
        addRow(g, 1, "Espece :", tfEspece);
        addRow(g, 2, "Age :",    tfAge);
        addRow(g, 3, "Poids :",  tfPoids);
        addRow(g, 4, "Sante :",  cbSante);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                int    id    = ds.nextAnimalId();
                double poids = Double.parseDouble(tfPoids.getText());
                int    age   = Integer.parseInt(tfAge.getText());
                Animal a = "Ruminant".equals(cbType.getValue())
                    ? new Ruminant(id, tfEspece.getText().trim(), age, poids)
                    : new Volaille(id, tfEspece.getText().trim(), age, poids);
                a.setEtatSante(cbSante.getValue());
                ds.ajouterAnimal(a);
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showEnregistrerProductionDialog() {
        DataStore ds = DataStore.getInstance();
        List<Animal> animaux = ds.getZoneEst().getAnimaux();
        if (animaux.isEmpty()) { info("Aucun animal", "Ajoutez d'abord un animal."); return; }

        Dialog<ButtonType> d = dialog("Production", "Enregistrer une production");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        ComboBox<Animal> cbA = animalCombo(animaux);
        Label lblUnite = new Label("Litres (lait) :");
        TextField tfVal = tf("0");
        cbA.setOnAction(e -> {
            Animal sel = cbA.getValue();
            lblUnite.setText(sel instanceof Ruminant ? "Litres de lait :" : "Nombre d'oeufs :");
        });
        if (!animaux.isEmpty()) lblUnite.setText(animaux.get(0) instanceof Ruminant ? "Litres de lait :" : "Nombre d'oeufs :");

        GridPane g = grid();
        addRow(g, 0, "Animal :", cbA);
        g.add(lblUnite, 0, 1); g.add(tfVal, 1, 1);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok || cbA.getValue() == null) return;
            try {
                Animal a = cbA.getValue();
                if (a instanceof Ruminant)
                    ds.enregistrerProductionRuminant((Ruminant) a, Double.parseDouble(tfVal.getText()));
                else if (a instanceof Volaille)
                    ds.enregistrerProductionVolaille((Volaille) a, Integer.parseInt(tfVal.getText()));
                info("Production enregistree", "Production enregistree pour " + a.getEspece() + ".");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showEvenementSanitaireDialog() {
        DataStore ds = DataStore.getInstance();
        List<Animal> animaux = ds.getZoneEst().getAnimaux();
        if (animaux.isEmpty()) { info("Aucun animal", "Ajoutez d'abord un animal."); return; }

        Dialog<ButtonType> d = dialog("Evenement sanitaire", "Nouvel evenement de sante");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        ComboBox<Animal> cbA  = animalCombo(animaux);
        TextField tfDesc  = tf("Ex: Vaccination contre la grippe");
        TextField tfPoids = tf("0");

        GridPane g = grid();
        addRow(g, 0, "Animal :",        cbA);
        addRow(g, 1, "Description :",   tfDesc);
        addRow(g, 2, "Nouveau poids :", tfPoids);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok || cbA.getValue() == null) return;
            try {
                ds.ajouterEvenementSanitaire(cbA.getValue(), tfDesc.getText().trim(),
                    Double.parseDouble(tfPoids.getText()));
                refreshAnimaux();
                info("Succes", "Evenement enregistre. Poids mis a jour.");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showVerifierLimitesDialog() {
        String rapport = DataStore.getInstance().verifierLimitesElevage();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Verification des limites");
        a.setHeaderText("Capacite — Zone Est");
        TextArea ta = new TextArea(rapport);
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefRowCount(8);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    private void showDetailsAnimalDialog() {
        DataStore ds = DataStore.getInstance();
        List<Animal> animaux = ds.getZoneEst().getAnimaux();
        if (animaux.isEmpty()) { info("Aucun animal", "Ajoutez d'abord un animal."); return; }

        Dialog<ButtonType> d = dialog("Details animal", "Details complets — Zone Est");
        d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        ComboBox<Animal> cbA = animalCombo(animaux);
        TextArea ta = new TextArea(buildAnimalDetails(animaux.get(0)));
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefRowCount(16); ta.setPrefWidth(480);
        ta.setStyle("-fx-font-family: monospace; -fx-font-size: 12;");

        cbA.setOnAction(e -> {
            Animal sel = cbA.getValue();
            if (sel != null) ta.setText(buildAnimalDetails(sel));
        });

        VBox box = new VBox(10, cbA, ta);
        box.setPadding(new Insets(10));
        d.getDialogPane().setContent(box);
        d.showAndWait();
    }

    private String buildAnimalDetails(Animal a) {
        StringBuilder sb = new StringBuilder();
        String type = (a instanceof Ruminant) ? "Ruminant" : "Volaille";
        sb.append("═══ ").append(type).append(" #").append(a.getNumero())
          .append(" — ").append(a.getEspece()).append(" ═══\n");
        sb.append("  Age          : ").append(a.getAge()).append(" an(s)\n");
        sb.append("  Poids        : ").append(String.format("%.1f", a.getPoids())).append(" kg\n");
        sb.append("  Etat sante   : ").append(a.getEtatSante()).append("\n\n");

        if (a.getCapteurBiometrique() != null) {
            var cb = a.getCapteurBiometrique();
            sb.append("  Capteur Bio  : [").append(cb.getCode()).append("]\n");
            sb.append("    Temp corp  : ").append(cb.getTemperatureCorporelle()).append(" °C");
            sb.append("  (seuils: ").append(cb.getTempCorpMin()).append(" - ").append(cb.getTempCorpMax()).append(")\n");
            sb.append("    Activite   : ").append(cb.getNiveauActivite());
            sb.append("  (seuils: ").append(cb.getActiviteMin()).append(" - ").append(cb.getActiviteMax()).append(")\n");
            sb.append("    Statut     : ").append(cb.getStatut()).append("\n");
            if (!cb.getHistorique().isEmpty()) {
                var last = cb.getHistorique().get(cb.getHistorique().size() - 1);
                sb.append("    Dernier releve : ").append(String.format("%.2f", last.getValeur()))
                  .append(" ").append(last.getUnite()).append(" → ").append(last.getNiveau()).append("\n");
            } else {
                sb.append("    Dernier releve : aucun\n");
            }
        } else {
            sb.append("  Capteur Bio  : aucun\n");
        }

        sb.append("\n");
        if (a.getCapteurGPS() != null) {
            var cg = a.getCapteurGPS();
            sb.append("  Capteur GPS  : [").append(cg.getCode()).append("]\n");
            sb.append("    Statut     : ").append(cg.getStatut()).append("\n");
            if (!cg.getHistoriqueGPS().isEmpty()) {
                var last = cg.getHistoriqueGPS().get(cg.getHistoriqueGPS().size() - 1);
                sb.append(String.format("    Position   : lat=%.5f  lon=%.5f%n",
                    last.getLatitude(), last.getLongitude()));
                sb.append("    Releves enreg. : ").append(cg.getHistoriqueGPS().size()).append("\n");
            } else {
                sb.append("    Aucun releve GPS\n");
            }
        } else {
            sb.append("  Capteur GPS  : aucun\n");
        }

        sb.append("\n");
        if (a.getEvenements().isEmpty()) {
            sb.append("  Evenements sanitaires : aucun\n");
        } else {
            sb.append("  Evenements sanitaires (").append(a.getEvenements().size()).append(") :\n");
            for (var ev : a.getEvenements()) {
                sb.append("    • ").append(ev.getDescription())
                  .append("  (nouveau poids : ").append(ev.getNouveauPoids()).append(" kg)\n");
            }
        }
        return sb.toString();
    }

    private void showAddCapteurBioDialog() {
        DataStore ds = DataStore.getInstance();
        List<Animal> animaux = ds.getZoneEst().getAnimaux();
        if (animaux.isEmpty()) { info("Aucun animal", "Ajoutez d'abord un animal."); return; }

        Dialog<ButtonType> d = dialog("Capteur Biometrique", "Nouveau capteur Bio — Zone Est");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        ComboBox<Animal> cbA = animalCombo(animaux);
        TextField tfT     = tf("38.5"), tfAct  = tf("70.0");
        TextField tfTMin  = tf("37.5"), tfTMax = tf("39.5");
        TextField tfAMin  = tf("30.0"), tfAMax = tf("100.0");

        GridPane g = grid();
        addRow(g, 0, "Animal :",                  cbA);
        addRow(g, 1, "Temperature corporelle °C :", tfT);
        addRow(g, 2, "Niveau activite (0-100) :", tfAct);
        addRow(g, 3, "Seuil min T corp °C :",     tfTMin);
        addRow(g, 4, "Seuil max T corp °C :",     tfTMax);
        addRow(g, 5, "Seuil min activite :",      tfAMin);
        addRow(g, 6, "Seuil max activite :",      tfAMax);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok || cbA.getValue() == null) return;
            try {
                String code = ds.nextCapteurCode("BIO-");
                CapteurBiometrique c = new CapteurBiometrique(
                    code, ds.getZoneEst(), "°C", cbA.getValue(),
                    p(tfT), p(tfAct), p(tfTMin), p(tfTMax), p(tfAMin), p(tfAMax));
                ds.ajouterCapteurBio(cbA.getValue(), c);
                info("Succes", "Capteur [" + code + "] associe a " + cbA.getValue().getEspece() + ".");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showAddCapteurGPSDialog() {
        DataStore ds = DataStore.getInstance();
        List<Animal> animaux = ds.getZoneEst().getAnimaux();
        if (animaux.isEmpty()) { info("Aucun animal", "Ajoutez d'abord un animal."); return; }

        Dialog<ButtonType> d = dialog("Capteur GPS", "Nouveau capteur GPS — Zone Est");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        ComboBox<Animal> cbA = animalCombo(animaux);
        GridPane g = grid();
        addRow(g, 0, "Animal :", cbA);
        Label lblNote = new Label("Les coordonnees GPS sont simulees automatiquement.");
        lblNote.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        g.add(lblNote, 0, 1, 2, 1);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok || cbA.getValue() == null) return;
            String code = ds.nextCapteurCode("GPS-");
            CapteurGPS c = new CapteurGPS(code, ds.getZoneEst(), cbA.getValue());
            c.envoyerReleve();
            ds.ajouterCapteurGPS(cbA.getValue(), c);
            info("Succes", "Capteur GPS [" + code + "] ajoute a " + cbA.getValue().getEspece() + ".");
        });
    }

    private void showModifierAlimentationElevageDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Alimentation", "Modifier le programme — Zone Est");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField tfAliment  = new TextField(ds.getZoneEst().getProgrammeAlimentation().getTypeAliment());
        TextField tfQuantite = new TextField(String.valueOf(ds.getZoneEst().getProgrammeAlimentation().getQuantiteParRepas()));
        GridPane g = grid();
        addRow(g, 0, "Type d'aliment :",    tfAliment);
        addRow(g, 1, "Quantite / repas :", tfQuantite);
        d.getDialogPane().setContent(g);
        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try { ds.modifierProgAlimentationElevage(tfAliment.getText().trim(), Double.parseDouble(tfQuantite.getText())); }
            catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    // =========================================================================
    // ZONE AQUACOLE
    // =========================================================================

    private Node buildAquacoleTab() {
        DataStore ds = DataStore.getInstance();
        refreshZoneStatut(ds.getZoneSud(), lblSudStatut);

        Label name = boldLabel("Zone Sud — ZoneAquacole", 16);
        Button btnSuspendre = grayBtn("Suspendre / Activer");
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneSud());
            refreshZoneStatut(ds.getZoneSud(), lblSudStatut);
        });
        HBox header = hrow(name, lblSudStatut, spacer(), btnSuspendre);

        // ── Alimentation ─────────────────────────────────────────────────
        Label progLbl = progLabelAquacole(ds);
        Button btnModifProg = grayBtn("Modifier alimentation");
        btnModifProg.setOnAction(e -> { showModifierAlimentationAquacoleDialog(); progLbl.setText(progTextAquacole(ds)); });
        HBox progRow = hrow(progLbl, spacer(), btnModifProg);
        progRow.setPadding(new Insets(6, 10, 6, 10));
        progRow.setStyle("-fx-background-color: #eaf4fb; -fx-background-radius: 6;");

        // ── Actions especes ───────────────────────────────────────────────
        Button btnAjoutE = greenBtn("+ Ajouter espece");
        btnAjoutE.setOnAction(e -> { showAddEspeceDialog(); refreshEspeces(); });
        HBox actEsp = hrow(btnAjoutE);

        // ── Actions capteurs ──────────────────────────────────────────────
        Button btnAddEau  = blueBtn("+ Capteur Eau");
        Button btnReleves = orangeBtn("Envoyer releves");
        btnAddEau.setOnAction(e  -> { showAddCapteurEauDialog(); refreshCapteurSud(); });
        btnReleves.setOnAction(e -> envoyerRelevesZone(ds.getZoneSud()));
        HBox actCapteurs = hrow(btnAddEau, btnReleves);

        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            header, progRow,
            sectionLabel("Especes aquacoles (" + ds.getZoneSud().getEspeces().size() + ")"),
            actEsp,
            especesTable(),
            sectionLabel("Capteurs Eau (" + ds.getZoneSud().getCapteurs().size() + ")"),
            actCapteurs,
            capteurTable(capteurSudData)
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── Aquacole dialogs ──────────────────────────────────────────────────────

    private void showAddEspeceDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Ajouter espece", "Nouvelle espece aquacole — Zone Sud");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField tfNom = tf("Ex: Carpe");
        TextField tfNb  = tf("100");
        GridPane g = grid();
        addRow(g, 0, "Espece :",            tfNom);
        addRow(g, 1, "Nombre d'individus :", tfNb);
        d.getDialogPane().setContent(g);
        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                ds.ajouterEspeceAquacole(new com.esi.smartfarming.animal.EspeceAquacole(
                    ds.nextEspeceId(), tfNom.getText().trim(), Integer.parseInt(tfNb.getText())));
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showAddCapteurEauDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Capteur Eau", "Nouveau capteur Eau — Zone Sud");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField tfT = tf("20.0"), tfOxy = tf("7.0"), tfPh = tf("7.2"), tfType = tf("Polyvalent");
        TextField tfTMin = tf("18.0"), tfTMax = tf("28.0");
        TextField tfOMin = tf("5.0"),  tfOMax = tf("10.0");
        TextField tfPhMin = tf("6.5"), tfPhMax = tf("8.5");

        GridPane g = grid();
        addRow(g, 0, "Temperature eau °C :", tfT);
        addRow(g, 1, "Taux oxygene mg/L :", tfOxy);
        addRow(g, 2, "pH :",                 tfPh);
        addRow(g, 3, "Type capteur :",       tfType);
        addRow(g, 4, "Seuil min T °C :",    tfTMin);
        addRow(g, 5, "Seuil max T °C :",    tfTMax);
        addRow(g, 6, "Seuil min O mg/L :", tfOMin);
        addRow(g, 7, "Seuil max O mg/L :", tfOMax);
        addRow(g, 8, "Seuil min pH :",      tfPhMin);
        addRow(g, 9, "Seuil max pH :",      tfPhMax);
        d.getDialogPane().setContent(g);

        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try {
                String code = ds.nextCapteurCode("EAU-");
                CapteurEau c = new CapteurEau(
                    code, ds.getZoneSud(), "°C",
                    p(tfT), p(tfOxy), p(tfPh), tfType.getText().trim(),
                    p(tfTMin), p(tfTMax), p(tfOMin), p(tfOMax), p(tfPhMin), p(tfPhMax));
                ds.ajouterCapteurEau(c);
                info("Succes", "Capteur Eau [" + code + "] ajoute a Zone Sud.");
            } catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    private void showModifierAlimentationAquacoleDialog() {
        DataStore ds = DataStore.getInstance();
        Dialog<ButtonType> d = dialog("Alimentation", "Modifier le programme — Zone Sud");
        ButtonType ok = okBtn();
        d.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField tfAliment  = new TextField(ds.getZoneSud().getProgrammeAlimentation().getTypeAliment());
        TextField tfQuantite = new TextField(String.valueOf(ds.getZoneSud().getProgrammeAlimentation().getQuantiteParRepas()));
        GridPane g = grid();
        addRow(g, 0, "Type d'aliment :",    tfAliment);
        addRow(g, 1, "Quantite / repas :", tfQuantite);
        d.getDialogPane().setContent(g);
        d.showAndWait().ifPresent(btn -> {
            if (btn != ok) return;
            try { ds.modifierProgAlimentationAquacole(tfAliment.getText().trim(), Double.parseDouble(tfQuantite.getText())); }
            catch (Exception ex) { erreur(ex.getMessage()); }
        });
    }

    // =========================================================================
    // RELEVES (common)
    // =========================================================================

    private void envoyerRelevesZone(com.esi.smartfarming.zone.Zone zone) {
        List<String> rapport = DataStore.getInstance().envoyerRelevesZone(zone);
        if (rapport.isEmpty()) {
            info("Releves", "Aucun capteur actif dans cette zone.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : rapport) sb.append(line).append("\n");
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Releves envoyes — " + zone.getNom());
        a.setHeaderText(rapport.size() + " capteur(s) interroge(s)");
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefRowCount(Math.min(rapport.size() + 2, 10));
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    // =========================================================================
    // TABLES
    // =========================================================================

    @SuppressWarnings("unchecked")
    private TableView<String[]> cultureTable() {
        TableView<String[]> t = new TableView<>(culturesData);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPrefHeight(160);
        t.getColumns().addAll(
            col("Nom", 1), col("Famille", 2), stadeCol(3),
            col("pH min", 4), col("pH max", 5), col("Hum. min", 6), col("Hum. max", 7)
        );
        return t;
    }

    @SuppressWarnings("unchecked")
    private TableView<String[]> animauxTable() {
        TableView<String[]> t = new TableView<>(animauxData);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPrefHeight(200);
        t.getColumns().addAll(
            col("#", 0), col("Espece", 1), col("Type", 2),
            col("Age", 3), col("Poids", 4), santeCol(5), col("Capteurs", 6)
        );
        return t;
    }

    @SuppressWarnings("unchecked")
    private TableView<String[]> especesTable() {
        TableView<String[]> t = new TableView<>(especesData);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPrefHeight(130);
        t.getColumns().addAll(col("ID", 0), col("Espece", 1), col("Nombre", 2));
        return t;
    }

    @SuppressWarnings("unchecked")
    private TableView<String[]> capteurTable(ObservableList<String[]> data) {
        TableView<String[]> t = new TableView<>(data);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPrefHeight(160);
        t.getColumns().addAll(
            col("Code", 0), col("Type", 1), statutCol(3),
            col("Seuil min", 4), col("Seuil max", 5), col("Derniere valeur", 6), niveauCol(7)
        );
        return t;
    }

    // =========================================================================
    // REFRESH
    // =========================================================================

    private void refreshCultures() {
        DataStore ds = DataStore.getInstance();
        culturesData.setAll(ds.getZoneNord().getCultures().stream().map(ds::toCultureRow).collect(Collectors.toList()));
    }

    private void refreshAnimaux() {
        DataStore ds = DataStore.getInstance();
        animauxData.setAll(ds.getZoneEst().getAnimaux().stream().map(ds::toAnimalRow).collect(Collectors.toList()));
    }

    private void refreshEspeces() {
        DataStore ds = DataStore.getInstance();
        especesData.setAll(ds.getZoneSud().getEspeces().stream().map(ds::toEspeceRow).collect(Collectors.toList()));
    }

    private void refreshCapteurNord() {
        DataStore ds = DataStore.getInstance();
        capteurNordData.setAll(ds.getZoneNord().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));
    }

    private void refreshCapteurEst() {
        DataStore ds = DataStore.getInstance();
        capteurEstData.setAll(ds.getZoneEst().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));
    }

    private void refreshCapteurSud() {
        DataStore ds = DataStore.getInstance();
        capteurSudData.setAll(ds.getZoneSud().getCapteurs().stream().map(ds::toCapteurRow).collect(Collectors.toList()));
    }

    private void refreshZoneStatut(Zone z, Label lbl) {
        boolean active = "ACTIVE".equals(z.getStatut().name());
        lbl.setText("● " + z.getStatut().name());
        lbl.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: "
            + (active ? SmartFarmingApp.GREEN : SmartFarmingApp.GRAY) + ";"
            + "-fx-background-color: " + (active ? "#eafaf1" : "#f2f3f4") + ";"
            + "-fx-background-radius: 4; -fx-padding: 3 10;");
    }

    // =========================================================================
    // COLUMN FACTORIES
    // =========================================================================

    private TableColumn<String[], String> col(String h, int idx) {
        TableColumn<String[], String> c = new TableColumn<>(h);
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        return c;
    }

    private TableColumn<String[], String> stadeCol(int idx) {
        TableColumn<String[], String> c = new TableColumn<>("Stade");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                switch (v) {
                    case "SEMIS"       -> setStyle("-fx-text-fill: " + SmartFarmingApp.PURPLE + ";");
                    case "GERMINATION" -> setStyle("-fx-text-fill: " + SmartFarmingApp.BLUE + ";");
                    case "CROISSANCE"  -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + ";");
                    case "MATURITE"    -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight:bold;");
                    case "RECOLTE"     -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED + "; -fx-font-weight:bold;");
                    default -> setStyle("");
                }
            }
        });
        return c;
    }

    private TableColumn<String[], String> santeCol(int idx) {
        TableColumn<String[], String> c = new TableColumn<>("Sante");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                switch (v) {
                    case "SAIN"        -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight:bold;");
                    case "MALADE"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight:bold;");
                    case "QUARANTAINE" -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight:bold;");
                    default -> setStyle("");
                }
            }
        });
        return c;
    }

    private TableColumn<String[], String> statutCol(int idx) {
        TableColumn<String[], String> c = new TableColumn<>("Statut");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                switch (v) {
                    case "ACTIF"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + "; -fx-font-weight:bold;");
                    case "DEFAILLANT" -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED   + "; -fx-font-weight:bold;");
                    case "SUSPENDU"   -> setStyle("-fx-text-fill: " + SmartFarmingApp.GRAY  + "; -fx-font-weight:bold;");
                    default -> setStyle("");
                }
            }
        });
        return c;
    }

    private TableColumn<String[], String> niveauCol(int idx) {
        TableColumn<String[], String> c = new TableColumn<>("Niveau");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                switch (v) {
                    case "NORMAL"        -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight:bold;");
                    case "AVERTISSEMENT" -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight:bold;");
                    case "CRITIQUE"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight:bold;");
                    default -> setStyle("");
                }
            }
        });
        return c;
    }

    // =========================================================================
    // UI HELPERS
    // =========================================================================

    private Label sectionLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private Label boldLabel(String t, int sz) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size: " + sz + "; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private HBox hrow(Node... nodes) {
        HBox b = new HBox(10); b.setAlignment(Pos.CENTER_LEFT);
        b.getChildren().addAll(nodes); return b;
    }

    private Region spacer() { Region r = new Region(); HBox.setHgrow(r, Priority.ALWAYS); return r; }

    private Button greenBtn(String t)  { return styledBtn(t, SmartFarmingApp.GREEN);  }
    private Button blueBtn(String t)   { return styledBtn(t, SmartFarmingApp.BLUE);   }
    private Button orangeBtn(String t) { return styledBtn(t, SmartFarmingApp.ORANGE); }
    private Button grayBtn(String t)   { return styledBtn(t, SmartFarmingApp.GRAY);   }

    private Button styledBtn(String t, String color) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;"
            + "-fx-background-radius:6;-fx-padding:6 14;-fx-font-size:12;-fx-cursor:hand;");
        return b;
    }

    private Dialog<ButtonType> dialog(String title, String header) {
        Dialog<ButtonType> d = new Dialog<>();
        d.setTitle(title); d.setHeaderText(header); return d;
    }

    private ButtonType okBtn() { return new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE); }

    private GridPane grid() {
        GridPane g = new GridPane();
        g.setHgap(12); g.setVgap(10); g.setPadding(new Insets(20, 140, 10, 10));
        return g;
    }

    private void addRow(GridPane g, int row, String label, Node field) {
        Label l = new Label(label);
        l.setStyle("-fx-font-size:12;");
        g.add(l, 0, row); g.add(field, 1, row);
    }

    private TextField tf(String prompt) {
        TextField t = new TextField(); t.setPromptText(prompt); return t;
    }

    private <T> ComboBox<T> combo(T[] values) {
        ComboBox<T> c = new ComboBox<>(FXCollections.observableArrayList(values));
        c.getSelectionModel().selectFirst(); return c;
    }

    private ComboBox<Animal> animalCombo(List<Animal> animaux) {
        ComboBox<Animal> cb = new ComboBox<>(FXCollections.observableArrayList(animaux));
        cb.setCellFactory(lv -> animalCell());
        cb.setButtonCell(animalCell());
        cb.getSelectionModel().selectFirst();
        return cb;
    }

    private ListCell<Animal> animalCell() {
        return new ListCell<>() {
            @Override protected void updateItem(Animal a, boolean empty) {
                super.updateItem(a, empty);
                setText((a == null || empty) ? null : "#" + a.getNumero() + " " + a.getEspece()
                    + " [" + (a instanceof Ruminant ? "Ruminant" : "Volaille") + "]");
            }
        };
    }

    private ListCell<Culture> cultCell() {
        return new ListCell<>() {
            @Override protected void updateItem(Culture c, boolean empty) {
                super.updateItem(c, empty);
                setText((c == null || empty) ? null : c.getNom() + " [" + c.getStageCroissance() + "]");
            }
        };
    }

    private double p(TextField tf) { return Double.parseDouble(tf.getText().trim().replace(',', '.')); }

    private void info(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private void erreur(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erreur"); a.setHeaderText("Saisie invalide");
        a.setContentText(msg != null ? msg : "Verifiez les valeurs saisies."); a.showAndWait();
    }

    private String scrollStyle() {
        return "-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";";
    }

    private ObservableList<String[]> obs(java.util.Collection<String[]> c) {
        return FXCollections.observableArrayList(c);
    }

    private Label progLabel(DataStore ds) {
        Label l = new Label(progText(ds));
        l.setStyle("-fx-font-size:12; -fx-text-fill:" + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private String progText(DataStore ds) {
        var p = ds.getZoneEst().getProgrammeAlimentation();
        return "Alimentation : " + p.getTypeAliment() + "  |  " + p.getQuantiteParRepas() + " kg/repas";
    }

    private Label progLabelAquacole(DataStore ds) {
        Label l = new Label(progTextAquacole(ds));
        l.setStyle("-fx-font-size:12; -fx-text-fill:" + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private String progTextAquacole(DataStore ds) {
        var p = ds.getZoneSud().getProgrammeAlimentation();
        return "Alimentation : " + p.getTypeAliment() + "  |  " + p.getQuantiteParRepas() + " kg/repas";
    }
}
