package com.esi.smartfarming.ui;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.data.DataStore;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CapteursView {

    public Node build() {
        SplitPane split = new SplitPane();
        split.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        split.getItems().addAll(buildCapteurList(), buildChartPanel());
        split.setDividerPositions(0.35);
        return split;
    }

    // ── Left : liste des capteurs ─────────────────────────────────────────────

    private Node buildCapteurList() {
        DataStore ds = DataStore.getInstance();

        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        Label title = new Label("Capteurs (" + ds.getAllCapteurs().size() + ")");
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        box.getChildren().add(title);

        for (Capteur c : ds.getAllCapteurs()) {
            box.getChildren().add(capteurCard(ds.toCapteurRow(c)));
        }

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";");
        return scroll;
    }

    private VBox capteurCard(String[] c) {
        // c: [code, type, zone, statut, seuilMin, seuilMax, derniere_valeur, niveau]
        String statut = c[3];
        String color;
        if      ("ACTIF".equals(statut))      color = SmartFarmingApp.GREEN;
        else if ("DEFAILLANT".equals(statut)) color = SmartFarmingApp.RED;
        else                                  color = SmartFarmingApp.GRAY;

        String niveauReleve = c[7];
        String niveauColor;
        if      ("CRITIQUE".equals(niveauReleve))      niveauColor = SmartFarmingApp.RED;
        else if ("AVERTISSEMENT".equals(niveauReleve)) niveauColor = SmartFarmingApp.ORANGE;
        else                                           niveauColor = SmartFarmingApp.GREEN;

        Label code = new Label(c[0] + "  (" + c[1] + ")");
        code.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label zone = new Label(c[2]);
        zone.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label statLabel = new Label("● " + statut);
        statLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label valeur = new Label("Derniere valeur : " + c[6]);
        valeur.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label niveau = new Label("Niveau : " + niveauReleve);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + niveauColor + ";");

        Label seuils = new Label("Seuils : " + c[4] + " — " + c[5]);
        seuils.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        VBox card = new VBox(4, code, zone, statLabel, new Separator(), valeur, niveau, seuils);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8;" +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.07), 6, 0, 0, 1);" +
                      "-fx-border-color: transparent transparent transparent " + color + ";" +
                      "-fx-border-width: 0 0 0 3;");
        return card;
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

        for (int i = 0; i < DummyData.HEURES.length; i++) {
            s1.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.TEMP_ENV01[i]));
            s2.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.PH_SOL02[i]));
            s3.getData().add(new XYChart.Data<>(DummyData.HEURES[i], DummyData.TEMP_BIO02[i]));
        }

        chart.getData().addAll(s1, s2, s3);
        return chart;
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
}
