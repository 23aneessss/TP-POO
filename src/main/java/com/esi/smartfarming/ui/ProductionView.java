package com.esi.smartfarming.ui;

import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.historique.ProductionRecord;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class ProductionView {

    private ObservableList<ProductionRecord> allProductions;
    private FilteredList<ProductionRecord> filtered;
    private TableView<ProductionRecord> table;
    private DatePicker dpDebut;
    private DatePicker dpFin;
    private Predicate<ProductionRecord> typePredicate = p -> true;

    public Node build() {
        DataStore ds = DataStore.getInstance();

        List<ProductionRecord> source = ds.getProductions();
        ObservableList<ProductionRecord> reversed = FXCollections.observableArrayList();
        for (int i = source.size() - 1; i >= 0; i--) reversed.add(source.get(i));
        allProductions = reversed;
        filtered = new FilteredList<>(allProductions, p -> true);

        VBox root = new VBox(18);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        table = buildTable();

        root.getChildren().addAll(
            buildHeader(),
            buildStatCards(ds),
            buildFilterBar(),
            buildDateFilterBar(),
            table,
            buildFooter(ds)
        );
        return root;
    }

    private VBox buildHeader() {
        Label title = new Label("Journal de production");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        Label sub = new Label("Toutes les productions et rendements enregistres dans la ferme");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        return new VBox(4, title, sub);
    }

    private HBox buildStatCards(DataStore ds) {
        double totalLait = 0;
        int totalOeufs = 0;
        int nbRendements = 0;
        for (ProductionRecord pr : ds.getProductions()) {
            if (pr.getType().equals("Lait")) totalLait += pr.getValeur();
            else if (pr.getType().equals("Oeufs")) totalOeufs += (int) pr.getValeur();
            else if (pr.getType().equals("Rendement culture")) nbRendements++;
        }

        HBox row = new HBox(16);
        row.getChildren().addAll(
            statCard("Lait collecte", String.format("%.0f L", totalLait), SmartFarmingApp.BLUE),
            statCard("Oeufs collectes", totalOeufs + "", SmartFarmingApp.ORANGE),
            statCard("Rendements saisis", nbRendements + "", SmartFarmingApp.GREEN),
            statCard("Total enregistrements", ds.getProductions().size() + "", SmartFarmingApp.PURPLE)
        );
        return row;
    }

    private VBox statCard(String label, String value, String color) {
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        VBox card = new VBox(4, val, lbl);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8;" +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.07), 8, 0, 0, 2);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private HBox buildFilterBar() {
        Button btnTous = filterBtn("Tous", SmartFarmingApp.BLUE);
        Button btnLait = filterBtn("Lait", SmartFarmingApp.BLUE);
        Button btnOeufs = filterBtn("Oeufs", SmartFarmingApp.ORANGE);
        Button btnRend = filterBtn("Rendement culture", SmartFarmingApp.GREEN);

        btnTous.setOnAction(e -> { typePredicate = p -> true; applyFilters(); });
        btnLait.setOnAction(e -> { typePredicate = p -> p.getType().equals("Lait"); applyFilters(); });
        btnOeufs.setOnAction(e -> { typePredicate = p -> p.getType().equals("Oeufs"); applyFilters(); });
        btnRend.setOnAction(e -> { typePredicate = p -> p.getType().equals("Rendement culture"); applyFilters(); });

        HBox bar = new HBox(10, btnTous, btnLait, btnOeufs, btnRend);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private HBox buildDateFilterBar() {
        Label lbl = new Label("Periode :");
        lbl.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        dpDebut = new DatePicker();
        dpFin   = new DatePicker();
        dpDebut.setPromptText("Du");
        dpFin.setPromptText("Au");
        dpDebut.setPrefWidth(150);
        dpFin.setPrefWidth(150);
        dpDebut.setOnAction(e -> applyFilters());
        dpFin.setOnAction(e -> applyFilters());

        Button btnReset = new Button("Effacer");
        btnReset.setOnAction(e -> { dpDebut.setValue(null); dpFin.setValue(null); applyFilters(); });

        HBox bar = new HBox(10, lbl, dpDebut, dpFin, btnReset);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private void applyFilters() {
        filtered.setPredicate(p -> typePredicate.test(p) && dansIntervalle(p.getDate()));
    }

    private boolean dansIntervalle(Date d) {
        if (dpDebut == null || d == null) return true;
        LocalDate jour = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate debut = dpDebut.getValue();
        LocalDate fin = dpFin.getValue();
        if (debut != null && jour.isBefore(debut)) return false;
        if (fin != null && jour.isAfter(fin)) return false;
        return true;
    }

    private Button filterBtn(String label, String color) {
        Button b = new Button(label);
        b.setStyle("-fx-background-color: white; -fx-text-fill: " + color + ";" +
                   "-fx-border-color: " + color + "; -fx-border-radius: 6; -fx-background-radius: 6;" +
                   "-fx-padding: 6 14; -fx-font-size: 12; -fx-cursor: hand;");
        return b;
    }

    @SuppressWarnings("unchecked")
    private TableView<ProductionRecord> buildTable() {
        TableView<ProductionRecord> t = new TableView<>(filtered);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(t, Priority.ALWAYS);

        t.getColumns().addAll(
            col("Date", 0), col("Zone", 1), typeCol(), col("Detail", 3), col("Valeur", 4)
        );
        t.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        Label vide = new Label("Aucune production enregistree pour le moment.\nUtilisez 'Enregistrer production' ou 'Enregistrer rendement' dans l'onglet Zones.");
        vide.setStyle("-fx-text-fill: " + SmartFarmingApp.SUBTEXT + "; -fx-text-alignment: center;");
        vide.setWrapText(true);
        t.setPlaceholder(vide);
        return t;
    }

    private TableColumn<ProductionRecord, String> col(String titre, int idx) {
        TableColumn<ProductionRecord, String> c = new TableColumn<>(titre);
        c.setCellValueFactory(d -> new SimpleStringProperty(
            DataStore.getInstance().toProductionRow(d.getValue())[idx]));
        return c;
    }

    private TableColumn<ProductionRecord, String> typeCol() {
        TableColumn<ProductionRecord, String> c = new TableColumn<>("Type");
        c.setCellValueFactory(d -> new SimpleStringProperty(
            DataStore.getInstance().toProductionRow(d.getValue())[2]));
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if (v.equals("Lait"))
                    setStyle("-fx-text-fill: " + SmartFarmingApp.BLUE + "; -fx-font-weight: bold;");
                else if (v.equals("Oeufs"))
                    setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                else
                    setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + "; -fx-font-weight: bold;");
            }
        });
        return c;
    }

    private HBox buildFooter(DataStore ds) {
        StringBuilder sb = new StringBuilder();
        for (com.esi.smartfarming.zone.ZoneElevage z : ds.getZonesElevage()) {
            if (sb.length() > 0) sb.append("     |     ");
            sb.append(z.getNom()).append(" : ").append(z.getProgrammeAlimentation().getTypeAliment())
              .append(" (").append(z.getProgrammeAlimentation().getQuantiteParRepas()).append(" kg/repas)");
        }
        for (com.esi.smartfarming.zone.ZoneAquacole z : ds.getZonesAquacole()) {
            if (sb.length() > 0) sb.append("     |     ");
            sb.append(z.getNom()).append(" : ").append(z.getProgrammeAlimentation().getTypeAliment())
              .append(" (").append(z.getProgrammeAlimentation().getQuantiteParRepas()).append(" kg/repas)");
        }
        Label info = new Label(sb.toString());
        info.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        HBox footer = new HBox(info);
        footer.setPadding(new Insets(8, 12, 8, 12));
        footer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6;");
        return footer;
    }
}
