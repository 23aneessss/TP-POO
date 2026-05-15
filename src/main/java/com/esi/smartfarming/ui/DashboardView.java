package com.esi.smartfarming.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

public class DashboardView {

    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        content.getChildren().addAll(
            sectionTitle("Vue d'ensemble"),
            buildStatCards(),
            sectionTitle("Zones de la ferme"),
            buildZoneCards(),
            sectionTitle("Alertes recentes"),
            buildRecentAlerts()
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";");
        return scroll;
    }

    // ── Stat cards ─────────────────────────────────────────────────────────────

    private HBox buildStatCards() {
        HBox row = new HBox(16);
        row.getChildren().addAll(
            statCard("Zones actives",       "2 / 3", SmartFarmingApp.GREEN),
            statCard("Capteurs actifs",     "6 / 8", SmartFarmingApp.BLUE),
            statCard("Alertes actives",     "3",     SmartFarmingApp.RED),
            statCard("Releves aujourd'hui", "24",    SmartFarmingApp.PURPLE)
        );
        return row;
    }

    private VBox statCard(String label, String value, String color) {
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        VBox card = new VBox(4, val, lbl);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle(cardStyle());
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ── Zone cards ──────────────────────────────────────────────────────────────

    private HBox buildZoneCards() {
        HBox row = new HBox(16);
        for (String[] z : DummyData.ZONES) {
            row.getChildren().add(zoneCard(z));
        }
        return row;
    }

    private VBox zoneCard(String[] z) {
        // z: [code, nom, type, statut, entites, nb_capteurs]
        boolean active = "ACTIVE".equals(z[3]);
        String couleur = active ? SmartFarmingApp.GREEN : SmartFarmingApp.GRAY;
        String bgStat  = active ? "#eafaf1" : "#f2f3f4";

        Label nom = new Label(z[1]);
        nom.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label type = new Label(z[2]);
        type.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label statut = new Label("● " + z[3]);
        statut.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + couleur + ";" +
                        "-fx-background-color: " + bgStat + "; -fx-background-radius: 4; -fx-padding: 3 10;");

        Separator sep = new Separator();
        sep.setStyle("-fx-opacity: 0.3;");

        Label entites = new Label(z[4]);
        entites.setStyle("-fx-font-size: 14; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label capteurs = new Label(z[5] + " capteur(s)");
        capteurs.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label code = new Label("Code : " + z[0]);
        code.setStyle("-fx-font-size: 11; -fx-text-fill: #bdc3c7;");

        VBox card = new VBox(8, nom, type, statut, sep, entites, capteurs, code);
        card.setPadding(new Insets(16));
        card.setStyle(cardStyle() + " -fx-border-color: transparent transparent transparent " + couleur + ";" +
                      "-fx-border-width: 0 0 0 4;");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ── Recent alerts ───────────────────────────────────────────────────────────

    private VBox buildRecentAlerts() {
        VBox box = new VBox(8);
        int count = Math.min(3, DummyData.ALERTES.length);
        for (int i = 0; i < count; i++) {
            box.getChildren().add(alertRow(DummyData.ALERTES[i]));
        }
        return box;
    }

    private HBox alertRow(String[] a) {
        // a: [id, niveau, capteur, zone, description, date, acquittee]
        boolean critique = "CRITIQUE".equals(a[1]);
        String color = critique ? SmartFarmingApp.RED    : SmartFarmingApp.ORANGE;
        String bg    = critique ? "#fde8e8"              : "#fef9e7";

        Label niveau = new Label(a[1]);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                        "-fx-background-color: " + bg + "; -fx-background-radius: 4; -fx-padding: 3 10;");
        niveau.setMinWidth(120);

        Label qui = new Label(a[2] + "  —  " + a[3]);
        qui.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        HBox.setHgrow(qui, Priority.ALWAYS);

        Label desc = new Label(a[4]);
        desc.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        HBox.setHgrow(desc, Priority.ALWAYS);

        Label date = new Label(a[5]);
        date.setStyle("-fx-font-size: 11; -fx-text-fill: #bdc3c7;");

        HBox row = new HBox(14, niveau, qui, desc, date);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setStyle(cardStyle());
        return row;
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    private Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private String cardStyle() {
        return "-fx-background-color: white; -fx-background-radius: 8;" +
               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.07), 8, 0, 0, 2);";
    }
}
