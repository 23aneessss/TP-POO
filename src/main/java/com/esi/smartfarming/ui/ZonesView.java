package com.esi.smartfarming.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class ZonesView {

    public Node build() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            new Tab("  Zone Nord (Culture)  ",  buildCultureTab()),
            new Tab("  Zone Est  (Elevage)  ",  buildElevageTab()),
            new Tab("  Zone Sud  (Aquacole) ",  buildAquacoleTab())
        );
        return tabs;
    }

    // ── ZoneCulture ─────────────────────────────────────────────────────────────

    private Node buildCultureTab() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            zoneHeader("Zone Nord — ZoneCulture", "ACTIVE"),
            sectionLabel("Cultures (2)"),
            cultureTable(),
            sectionLabel("Capteurs (3)"),
            capteurTable("Zone Nord")
        );
        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── ZoneElevage ─────────────────────────────────────────────────────────────

    private Node buildElevageTab() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            zoneHeader("Zone Est — ZoneElevage", "ACTIVE"),
            sectionLabel("Animaux (5)"),
            animauxTable(),
            sectionLabel("Programme d'alimentation"),
            infoCard("Aliment", "Mais broye + luzerne", "Quantite / repas", "15.0 kg"),
            sectionLabel("Capteurs (4)"),
            capteurTable("Zone Est")
        );
        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── ZoneAquacole ────────────────────────────────────────────────────────────

    private Node buildAquacoleTab() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        box.getChildren().addAll(
            zoneHeader("Zone Sud — ZoneAquacole", "SUSPENDUE"),
            sectionLabel("Especes aquacoles (1)"),
            infoCard("Espece", "Tilapia", "Nombre d'individus", "250"),
            sectionLabel("Programme d'alimentation"),
            infoCard("Aliment", "Granules flottants", "Quantite / repas", "2.5 kg"),
            sectionLabel("Capteurs (1)"),
            capteurTable("Zone Sud")
        );
        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── Tables ──────────────────────────────────────────────────────────────────

    private TableView<String[]> cultureTable() {
        TableView<String[]> t = new TableView<>(FXCollections.observableArrayList(DummyData.CULTURES));
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setMaxHeight(130);

        t.getColumns().addAll(
            col("Nom", 1),
            col("Famille", 2),
            stadeCol(3),
            col("pH min", 4),
            col("pH max", 5),
            col("Hum. min", 6),
            col("Hum. max", 7)
        );
        return t;
    }

    private TableView<String[]> animauxTable() {
        TableView<String[]> t = new TableView<>(FXCollections.observableArrayList(DummyData.ANIMAUX));
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setMaxHeight(180);

        t.getColumns().addAll(
            col("#", 0),
            col("Espece", 1),
            col("Type", 2),
            col("Age", 3),
            col("Poids", 4),
            santeCol(5)
        );
        return t;
    }

    private TableView<String[]> capteurTable(String zone) {
        List<String[]> filtered = new ArrayList<>();
        for (String[] c : DummyData.CAPTEURS) {
            if (zone.equals(c[2])) filtered.add(c);
        }
        TableView<String[]> t = new TableView<>(FXCollections.observableArrayList(filtered));
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setMaxHeight(160);

        t.getColumns().addAll(
            col("Code", 0),
            col("Type", 1),
            statutCol(3),
            col("Seuil min", 4),
            col("Seuil max", 5),
            col("Derniere valeur", 6),
            niveauCol(7)
        );
        return t;
    }

    // ── Column factories ────────────────────────────────────────────────────────

    private TableColumn<String[], String> col(String header, int idx) {
        TableColumn<String[], String> c = new TableColumn<>(header);
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
                if      ("SEMIS".equals(v))       setStyle("-fx-text-fill: " + SmartFarmingApp.PURPLE + ";");
                else if ("GERMINATION".equals(v)) setStyle("-fx-text-fill: " + SmartFarmingApp.BLUE + ";");
                else if ("CROISSANCE".equals(v))  setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + ";");
                else if ("MATURITE".equals(v))    setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                else if ("RECOLTE".equals(v))     setStyle("-fx-text-fill: " + SmartFarmingApp.RED + "; -fx-font-weight: bold;");
                else setStyle("");
            }
        });
        return c;
    }

    private TableColumn<String[], String> santeCol(int idx) {
        TableColumn<String[], String> c = new TableColumn<>("Etat sante");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if      ("SAIN".equals(v))        setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                else if ("MALADE".equals(v))      setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                else if ("QUARANTAINE".equals(v)) setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                else setStyle("");
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
                if      ("ACTIF".equals(v))      setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                else if ("DEFAILLANT".equals(v)) setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                else if ("SUSPENDU".equals(v))   setStyle("-fx-text-fill: " + SmartFarmingApp.GRAY   + "; -fx-font-weight: bold;");
                else setStyle("");
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
                if      ("NORMAL".equals(v))        setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                else if ("AVERTISSEMENT".equals(v)) setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                else if ("CRITIQUE".equals(v))      setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                else setStyle("");
            }
        });
        return c;
    }

    // ── UI helpers ───────────────────────────────────────────────────────────────

    private HBox zoneHeader(String nom, String statut) {
        boolean active = "ACTIVE".equals(statut);
        String color = active ? SmartFarmingApp.GREEN : SmartFarmingApp.GRAY;
        String bgStat = active ? "#eafaf1" : "#f2f3f4";

        Label name = new Label(nom);
        name.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label stat = new Label("● " + statut);
        stat.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                      "-fx-background-color: " + bgStat + "; -fx-background-radius: 4; -fx-padding: 3 10;");

        HBox row = new HBox(16, name, stat);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private HBox infoCard(String k1, String v1, String k2, String v2) {
        Label l1 = new Label(k1 + " : ");
        l1.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        Label lv1 = new Label(v1);
        lv1.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Region sep = new Region();
        sep.setMinWidth(32);

        Label l2 = new Label(k2 + " : ");
        l2.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        Label lv2 = new Label(v2);
        lv2.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        HBox card = new HBox(4, l1, lv1, sep, l2, lv2);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12, 18, 12, 18));
        card.setStyle("-fx-background-color: #eaf4fb; -fx-background-radius: 6;");
        return card;
    }

    private String scrollStyle() {
        return "-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";";
    }
}
