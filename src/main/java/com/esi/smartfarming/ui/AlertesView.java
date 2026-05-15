package com.esi.smartfarming.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AlertesView {

    private FilteredList<String[]> filteredData;

    public Node build() {
        ObservableList<String[]> allData = FXCollections.observableArrayList(DummyData.ALERTES);
        filteredData = new FilteredList<>(allData, a -> true);

        VBox root = new VBox(16);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        root.getChildren().addAll(
            buildHeader(),
            buildFilterBar(),
            buildTable(),
            buildFooter()
        );
        return root;
    }

    // ── Header ──────────────────────────────────────────────────────────────────

    private HBox buildHeader() {
        Label title = new Label("Panneau des alertes");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label sub = new Label("Les alertes critiques apparaissent en premier");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        VBox left = new VBox(4, title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label("3 alertes actives");
        badge.setStyle("-fx-background-color: #fde8e8; -fx-text-fill: " + SmartFarmingApp.RED + ";" +
                       "-fx-font-weight: bold; -fx-font-size: 12; -fx-background-radius: 6; -fx-padding: 6 14;");

        HBox row = new HBox(left, spacer, badge);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ── Filter bar ──────────────────────────────────────────────────────────────

    private HBox buildFilterBar() {
        Button btnTous   = filterBtn("Toutes (4)",         null);
        Button btnCrit   = filterBtn("CRITIQUE (2)",       "CRITIQUE");
        Button btnAvert  = filterBtn("AVERTISSEMENT (2)",  "AVERTISSEMENT");
        Button btnAcq    = filterBtn("Acquittees (1)",     "OUI");

        // Highlight "Toutes" by default
        btnTous.setStyle(btnTous.getStyle() + "-fx-font-weight: bold;");

        btnTous.setOnAction(e  -> filteredData.setPredicate(a -> true));
        btnCrit.setOnAction(e  -> filteredData.setPredicate(a -> "CRITIQUE".equals(a[1])));
        btnAvert.setOnAction(e -> filteredData.setPredicate(a -> "AVERTISSEMENT".equals(a[1])));
        btnAcq.setOnAction(e   -> filteredData.setPredicate(a -> "Oui".equals(a[6])));

        HBox bar = new HBox(10, btnTous, btnCrit, btnAvert, btnAcq);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private Button filterBtn(String label, String filter) {
        Button b = new Button(label);
        String color = filter == null     ? SmartFarmingApp.BLUE
                     : "CRITIQUE".equals(filter)       ? SmartFarmingApp.RED
                     : "AVERTISSEMENT".equals(filter)  ? SmartFarmingApp.ORANGE
                     : SmartFarmingApp.GREEN;
        b.setStyle("-fx-background-color: white; -fx-text-fill: " + color + ";" +
                   "-fx-border-color: " + color + "; -fx-border-radius: 6; -fx-background-radius: 6;" +
                   "-fx-padding: 6 14; -fx-font-size: 12; -fx-cursor: hand;");
        return b;
    }

    // ── Table ───────────────────────────────────────────────────────────────────

    private TableView<String[]> buildTable() {
        TableView<String[]> table = new TableView<>(filteredData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Colonnes
        table.getColumns().addAll(
            simpleCol("ID",          0, 90),
            niveauCol(),
            simpleCol("Capteur",     2, 100),
            simpleCol("Zone",        3, 110),
            simpleCol("Description", 4, 260),
            simpleCol("Date",        5, 110),
            acquitteeCol()
        );

        // Couleur de fond des lignes selon niveau
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("CRITIQUE".equals(item[1])) {
                    setStyle("-fx-background-color: #fff5f5;");
                } else if ("AVERTISSEMENT".equals(item[1])) {
                    setStyle("-fx-background-color: #fffbf0;");
                } else {
                    setStyle("");
                }
            }
        });

        table.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        return table;
    }

    private TableColumn<String[], String> simpleCol(String header, int idx, double minW) {
        TableColumn<String[], String> c = new TableColumn<>(header);
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
        c.setMinWidth(minW);
        return c;
    }

    private TableColumn<String[], String> niveauCol() {
        TableColumn<String[], String> c = new TableColumn<>("Niveau");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        c.setMinWidth(130);
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if ("CRITIQUE".equals(v)) {
                    setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                } else if ("AVERTISSEMENT".equals(v)) {
                    setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                }
            }
        });
        return c;
    }

    private TableColumn<String[], String> acquitteeCol() {
        TableColumn<String[], String> c = new TableColumn<>("Acquittee");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[6]));
        c.setMinWidth(90);
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if ("Oui".equals(v)) {
                    setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + "; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
                }
            }
        });
        return c;
    }

    // ── Footer ──────────────────────────────────────────────────────────────────

    private HBox buildFooter() {
        Label info = new Label(
            "● CRITIQUE : action immediate requise     " +
            "● AVERTISSEMENT : surveillance necessaire     " +
            "● Acquittee : prise en charge confirmee"
        );
        info.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        HBox footer = new HBox(info);
        footer.setPadding(new Insets(8, 12, 8, 12));
        footer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6;");
        return footer;
    }
}
