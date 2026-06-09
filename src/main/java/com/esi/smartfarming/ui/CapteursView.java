package com.esi.smartfarming.ui;

import com.esi.smartfarming.capteur.*;
import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.releve.ReleveGPS;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Vue de gestion des capteurs de la ferme.
 * <p>
 * Panneau gauche : liste de tous les capteurs (toutes zones confondues) avec
 * leurs valeurs actuelles et des boutons d'action rapide.
 * Panneau droit  : graphique d'evolution des relevés sur la journée.
 */
public class CapteursView {

    private VBox capteurListBox;

    public Node build() {
        SplitPane split = new SplitPane();
        split.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        split.getItems().addAll(buildCapteurList(), buildChartPanel());
        split.setDividerPositions(0.42);
        return split;
    }

    // ── Left : liste des capteurs ─────────────────────────────────────────────

    private Node buildCapteurList() {
        DataStore ds = DataStore.getInstance();

        capteurListBox = new VBox(10);
        capteurListBox.setPadding(new Insets(16));
        capteurListBox.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        Label title = new Label("Capteurs (" + ds.getAllCapteurs().size() + ")");
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        capteurListBox.getChildren().add(title);

        refreshCapteurCards(ds);

        ScrollPane scroll = new ScrollPane(capteurListBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";");
        return scroll;
    }

    private void refreshCapteurCards(DataStore ds) {
        // Keep the title label (index 0), replace the rest
        while (capteurListBox.getChildren().size() > 1)
            capteurListBox.getChildren().remove(1);

        Label title = (Label) capteurListBox.getChildren().get(0);
        title.setText("Capteurs (" + ds.getAllCapteurs().size() + ")");

        for (Capteur c : ds.getAllCapteurs()) {
            capteurListBox.getChildren().add(capteurCard(c, ds));
        }
    }

    private VBox capteurCard(Capteur c, DataStore ds) {
        String[] row = ds.toCapteurRow(c);
        // row: [code, type, zone, statut, seuilMin, seuilMax, derniere_valeur, niveau]
        String statut = c.getStatut().name();
        String statusColor = switch (statut) {
            case "ACTIF"      -> SmartFarmingApp.GREEN;
            case "DEFAILLANT" -> SmartFarmingApp.RED;
            default           -> SmartFarmingApp.GRAY;
        };
        String niveauColor = switch (row[7]) {
            case "CRITIQUE"      -> SmartFarmingApp.RED;
            case "AVERTISSEMENT" -> SmartFarmingApp.ORANGE;
            default              -> SmartFarmingApp.GREEN;
        };

        Label code = new Label(c.getCode() + "  (" + row[1] + ")");
        code.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label zone = new Label(c.getZone().getNom());
        zone.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label statLabel = new Label("● " + statut);
        statLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");

        Label valeur = new Label("Valeur : " + row[6]);
        valeur.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label niveau = new Label("Niveau : " + row[7]);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + niveauColor + ";");

        Label seuils = new Label("Seuils : " + row[4] + " — " + row[5]);
        seuils.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        // ── All measurements ─────────────────────────────────────────────────
        Label mesures = buildAllMesures(c);
        mesures.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.TEXT
            + "; -fx-background-color: #f8f9fa; -fx-background-radius: 4; -fx-padding: 4 8;");
        mesures.setWrapText(true);

        // ── Action buttons ───────────────────────────────────────────────────
        HBox actions = buildCapteurActions(c, ds, statLabel, valeur, niveau, seuils);

        VBox card = new VBox(4, code, zone, statLabel, new Separator(), mesures, niveau, seuils, actions);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 8;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.07), 6, 0, 0, 1);" +
            "-fx-border-color: transparent transparent transparent " + statusColor + ";" +
            "-fx-border-width: 0 0 0 3;");
        return card;
    }

    private HBox buildCapteurActions(Capteur c, DataStore ds,
                                      Label statLabel, Label valeur, Label niveau, Label seuils) {
        Button btnBascule = new Button();
        Button btnDefaill = new Button("⚠ Defaillant");
        Button btnReleve  = new Button("↻ Releve");

        styleMiniBtn(btnBascule, SmartFarmingApp.BLUE);
        styleMiniBtn(btnDefaill, SmartFarmingApp.ORANGE);
        styleMiniBtn(btnReleve,  SmartFarmingApp.GREEN);

        updateBasculeLabel(c, btnBascule);

        btnBascule.setOnAction(e -> {
            if (c.getStatut() == StatutCapteur.DEFAILLANT) {
                ds.reactiverCapteur(c);
            } else {
                ds.basculerCapteur(c);
            }
            updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
            updateBasculeLabel(c, btnBascule);
        });

        btnDefaill.setOnAction(e -> {
            if (c.getStatut() == StatutCapteur.DEFAILLANT) {
                info("Deja defaillant", "Ce capteur est deja marque comme defaillant.");
                return;
            }
            ds.marquerDefaillantCapteur(c);
            updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
            updateBasculeLabel(c, btnBascule);
        });

        btnReleve.setOnAction(e -> {
            if (c.getStatut() != StatutCapteur.ACTIF) {
                info("Capteur inactif", "Activez d'abord le capteur pour envoyer un releve.");
                return;
            }
            if (c instanceof CapteurNumerique) {
                var r = ((CapteurNumerique) c).envoyerReleve();
                ds.save();
                updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
                info("Releve envoye", "[" + c.getCode() + "] Valeur = " + String.format("%.2f", r.getValeur())
                    + " " + r.getUnite() + " — Niveau : " + r.getNiveau());
            } else if (c instanceof CapteurGPS) {
                var r = ((CapteurGPS) c).envoyerReleve();
                ds.save();
                info("Releve GPS envoye", "[" + c.getCode() + "] lat=" +
                    String.format("%.5f", r.getLatitude()) + "  lon=" + String.format("%.5f", r.getLongitude()));
            }
        });

        HBox actions = new HBox(6, btnBascule, btnDefaill, btnReleve);
        actions.setPadding(new Insets(4, 0, 0, 0));
        return actions;
    }

    /** Returns a Label showing all current measurements for a capteur. */
    private Label buildAllMesures(Capteur c) {
        StringBuilder sb = new StringBuilder();
        if (c instanceof CapteurEnvironnemental ce) {
            sb.append(String.format("T° : %.1f °C  (seuils: %.1f – %.1f)%n", ce.getTemperature(), ce.getTempMin(), ce.getTempMax()));
            sb.append(String.format("Humidite : %.1f %%  (seuils: %.1f – %.1f)%n", ce.getHumidite(), ce.getHumMin(), ce.getHumMax()));
            sb.append(String.format("Pluviometrie : %.1f mm  (seuils: %.1f – %.1f)", ce.getPluviometrie(), ce.getPluvMin(), ce.getPluvMax()));
        } else if (c instanceof CapteurSol cs) {
            sb.append(String.format("pH : %.2f  (seuils: %.1f – %.1f)%n", cs.getPh(), cs.getPhMin(), cs.getPhMax()));
            sb.append(String.format("Humidite sol : %.1f %%  (seuils: %.1f – %.1f)%n", cs.getHumidite(), cs.getHumMin(), cs.getHumMax()));
            sb.append(String.format("Azote : %.2f mg/kg  (seuils: %.1f – %.1f)", cs.getTeneurAzote(), cs.getAzoteMin(), cs.getAzoteMax()));
        } else if (c instanceof CapteurBiometrique cb) {
            sb.append(String.format("Temp. corp. : %.1f °C  (seuils: %.1f – %.1f)%n", cb.getTemperatureCorporelle(), cb.getTempCorpMin(), cb.getTempCorpMax()));
            sb.append(String.format("Activite : %.1f  (seuils: %.1f – %.1f)%n", cb.getNiveauActivite(), cb.getActiviteMin(), cb.getActiviteMax()));
            sb.append("Animal : ").append(cb.getAnimal() != null ? cb.getAnimal().getEspece() : "—");
        } else if (c instanceof CapteurEau ce) {
            sb.append(String.format("Temp. eau : %.1f °C  (seuils: %.1f – %.1f)%n", ce.getTemperateur(), ce.getTempMin(), ce.getTempMax()));
            sb.append(String.format("Oxygene : %.1f mg/L  (seuils: %.1f – %.1f)%n", ce.getOxygene(), ce.getOxyMin(), ce.getOxyMax()));
            sb.append(String.format("pH : %.2f  (seuils: %.1f – %.1f)", ce.getPh(), ce.getPhMin(), ce.getPhMax()));
        } else if (c instanceof CapteurGPS cg) {
            java.util.List<ReleveGPS> hist = cg.getHistoriqueGPS();
            if (!hist.isEmpty()) {
                ReleveGPS last = hist.get(hist.size() - 1);
                sb.append(String.format("Derniere position :%nlat = %.5f%nlon = %.5f", last.getLatitude(), last.getLongitude()));
            } else {
                sb.append("Aucun releve GPS enregistre");
            }
            if (cg.getAnimal() != null) sb.append("\nAnimal : ").append(cg.getAnimal().getEspece());
        } else {
            sb.append("Aucune mesure disponible");
        }
        return new Label(sb.toString());
    }

    private void updateBasculeLabel(Capteur c, Button btn) {
        switch (c.getStatut()) {
            case ACTIF     -> { btn.setText("⏸ Suspendre"); styleMiniBtn(btn, SmartFarmingApp.GRAY);   }
            case SUSPENDU  -> { btn.setText("▶ Activer");   styleMiniBtn(btn, SmartFarmingApp.GREEN);  }
            case DEFAILLANT-> { btn.setText("↺ Reactiver"); styleMiniBtn(btn, SmartFarmingApp.BLUE);   }
        }
    }

    private void updateCapteurCardLabels(Capteur c, DataStore ds,
                                          Label statLabel, Label valeur, Label niveau, Label seuils) {
        String[] row = ds.toCapteurRow(c);
        String statusColor = switch (c.getStatut().name()) {
            case "ACTIF"      -> SmartFarmingApp.GREEN;
            case "DEFAILLANT" -> SmartFarmingApp.RED;
            default           -> SmartFarmingApp.GRAY;
        };
        String niveauColor = switch (row[7]) {
            case "CRITIQUE"      -> SmartFarmingApp.RED;
            case "AVERTISSEMENT" -> SmartFarmingApp.ORANGE;
            default              -> SmartFarmingApp.GREEN;
        };
        statLabel.setText("● " + c.getStatut().name());
        statLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");
        valeur.setText("Valeur : " + row[6]);
        niveau.setText("Niveau : " + row[7]);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + niveauColor + ";");
        seuils.setText("Seuils : " + row[4] + " — " + row[5]);
    }

    private void styleMiniBtn(Button b, String color) {
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;" +
                   "-fx-background-radius: 4; -fx-padding: 3 10; -fx-font-size: 11; -fx-cursor: hand;");
    }

    // ── Right : graphique d'evolution ─────────────────────────────────────────

    private Node buildChartPanel() {
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        Label title = new Label("Evolution des releves — 15 Mai 2026");
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label sub = new Label("Température ENV-01  |  pH SOL-02  |  Température BIO-02");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        LineChart<String, Number> chart = buildChart();
        VBox.setVgrow(chart, Priority.ALWAYS);

        panel.getChildren().addAll(title, sub, chart, buildLegend());
        return panel;
    }

    @SuppressWarnings("unchecked")
    private LineChart<String, Number> buildChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Heure");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valeur");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        s1.setName("Temp. ENV-01 (°C)");
        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        s2.setName("pH SOL-02");
        XYChart.Series<String, Number> s3 = new XYChart.Series<>();
        s3.setName("Temp. BIO-02 (°C)");

        // Build chart from actual capteur historique, or fall back to DummyData
        List<CapteurNumerique> numeriques = buildNumeriqueList();
        if (numeriques.size() >= 2 && !numeriques.get(0).getHistorique().isEmpty()
                && numeriques.get(0).getHistorique().size() >= 3) {
            fillFromHistorique(s1, numeriques.get(0));
            fillFromHistorique(s2, numeriques.get(1));
            if (numeriques.size() >= 3) fillFromHistorique(s3, numeriques.get(2));
        } else {
            for (int i = 0; i < DummyData.HEURES.length; i++) {
                s1.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.TEMP_ENV01[i]));
                s2.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.PH_SOL02[i]));
                s3.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.TEMP_BIO02[i]));
            }
        }

        chart.getData().addAll(s1, s2, s3);
        return chart;
    }

    private List<CapteurNumerique> buildNumeriqueList() {
        return DataStore.getInstance().getAllCapteurs().stream()
            .filter(c -> c instanceof CapteurNumerique)
            .map(c -> (CapteurNumerique) c)
            .limit(3)
            .collect(java.util.stream.Collectors.toList());
    }

    private void fillFromHistorique(XYChart.Series<String, Number> series, CapteurNumerique c) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        var hist = c.getHistorique();
        int start = Math.max(0, hist.size() - 8);
        for (int i = start; i < hist.size(); i++) {
            var r = hist.get(i);
            series.getData().add(new XYChart.Data<>(sdf.format(r.getDateHeure()), r.getValeur()));
        }
    }

    private HBox buildLegend() {
        HBox legend = new HBox(24);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(8, 12, 8, 12));
        legend.setStyle("-fx-background-color: white; -fx-background-radius: 6;");
        legend.getChildren().addAll(
            legendItem("NORMAL",        SmartFarmingApp.GREEN,  "Releve dans les seuils"),
            legendItem("AVERTISSEMENT", SmartFarmingApp.ORANGE, "Releve proche du seuil"),
            legendItem("CRITIQUE",      SmartFarmingApp.RED,    "Releve hors seuil")
        );
        return legend;
    }

    private HBox legendItem(String label, String color, String desc) {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label d = new Label(" — " + desc);
        d.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        return new HBox(4, dot, lbl, d);
    }

    private void info(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
