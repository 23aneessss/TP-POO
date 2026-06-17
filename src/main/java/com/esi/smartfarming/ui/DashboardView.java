package com.esi.smartfarming.ui;

import com.esi.smartfarming.alerte.Alerte;
import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.zone.Zone;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

public class DashboardView {

    public Node build() {
        DataStore ds = DataStore.getInstance();

        VBox content = new VBox(24);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        content.getChildren().addAll(
            sectionTitle("Vue d'ensemble"),
            buildStatCards(ds),
            sectionTitle("Zones de la ferme"),
            buildZoneCards(ds),
            sectionTitle("Alertes recentes"),
            buildRecentAlerts(ds)
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";");
        return scroll;
    }

    private HBox buildStatCards(DataStore ds) {
        int totalZones    = ds.getZones().size();
        int totalCapteurs = ds.getAllCapteurs().size();
        int totalAnimaux  = 0;
        for (com.esi.smartfarming.zone.ZoneElevage z : ds.getZonesElevage()) totalAnimaux += z.getAnimaux().size();

        HBox row = new HBox(16);
        row.getChildren().addAll(
            statCard("Zones actives",       ds.countZonesActives()   + " / " + totalZones,    SmartFarmingApp.GREEN),
            statCard("Capteurs actifs",     ds.countCapteursActifs() + " / " + totalCapteurs, SmartFarmingApp.BLUE),
            statCard("Alertes actives",     String.valueOf(ds.countAlertesActives()),          SmartFarmingApp.RED),
            statCard("Total animaux",       String.valueOf(totalAnimaux), SmartFarmingApp.PURPLE)
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

    private HBox buildZoneCards(DataStore ds) {
        HBox row = new HBox(16);
        for (Zone z : ds.getZones()) {
            row.getChildren().add(zoneCard(ds.toZoneRow(z)));
        }
        return row;
    }

    private VBox zoneCard(String[] z) {
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

    private VBox buildRecentAlerts(DataStore ds) {
        VBox box = new VBox(8);
        int count = 0;
        for (Alerte a : ds.getAlertes()) {
            if (count >= 3) break;
            box.getChildren().add(alertRow(ds.toAlerteRow(a)));
            count++;
        }
        return box;
    }

    private HBox alertRow(String[] a) {
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
