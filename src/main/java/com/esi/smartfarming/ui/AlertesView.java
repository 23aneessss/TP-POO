package com.esi.smartfarming.ui;

import com.esi.smartfarming.alerte.Alerte;
import com.esi.smartfarming.data.DataStore;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AlertesView {

    private ObservableList<Alerte> allAlertes;
    private FilteredList<Alerte>   filteredAlertes;
    private TableView<Alerte>      table;
    private Label                  badgeLabel;

    public Node build() {
        DataStore ds = DataStore.getInstance();
        allAlertes      = ds.getObservableAlertes();
        filteredAlertes = new FilteredList<>(allAlertes, a -> true);

        VBox root = new VBox(16);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        table = buildTable();

        root.getChildren().addAll(
            buildHeader(),
            buildFilterBar(),
            buildActionBar(),
            table,
            buildFooter()
        );
        return root;
    }

    // l'en-tete du panneau
    private HBox buildHeader() {
        Label title = new Label("Panneau des alertes");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label sub = new Label("Les alertes critiques apparaissent en premier");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        VBox left = new VBox(4, title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        badgeLabel = new Label();
        refreshBadge();

        HBox row = new HBox(left, spacer, badgeLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // la barre de filtres
    private HBox buildFilterBar() {
        Button btnTous  = filterBtn("Toutes",         null);
        Button btnCrit  = filterBtn("CRITIQUE",       "CRITIQUE");
        Button btnAvert = filterBtn("AVERTISSEMENT",  "AVERTISSEMENT");
        Button btnAcq   = filterBtn("Acquittees",     "OUI");

        btnTous.setStyle(btnTous.getStyle() + " -fx-font-weight: bold;");

        btnTous.setOnAction(e  -> filteredAlertes.setPredicate(a -> true));
        btnCrit.setOnAction(e  -> filteredAlertes.setPredicate(a -> a.getNiveauGravite().name().equals("CRITIQUE")));
        btnAvert.setOnAction(e -> filteredAlertes.setPredicate(a -> a.getNiveauGravite().name().equals("AVERTISSEMENT")));
        btnAcq.setOnAction(e   -> filteredAlertes.setPredicate(Alerte::isAcquittee));

        HBox bar = new HBox(10, btnTous, btnCrit, btnAvert, btnAcq);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private Button filterBtn(String label, String filter) {
        Button b = new Button(label);
        String color = filter == null              ? SmartFarmingApp.BLUE
                     : "CRITIQUE".equals(filter)   ? SmartFarmingApp.RED
                     : "AVERTISSEMENT".equals(filter) ? SmartFarmingApp.ORANGE
                     : SmartFarmingApp.GREEN;
        b.setStyle("-fx-background-color: white; -fx-text-fill: " + color + ";" +
                   "-fx-border-color: " + color + "; -fx-border-radius: 6; -fx-background-radius: 6;" +
                   "-fx-padding: 6 14; -fx-font-size: 12; -fx-cursor: hand;");
        return b;
    }

    // la barre des actions (acquitter / supprimer)
    private HBox buildActionBar() {
        Button btnAcquitter = new Button("✔ Acquitter la selection");
        btnAcquitter.setStyle(
            "-fx-background-color: " + SmartFarmingApp.GREEN + "; -fx-text-fill: white;" +
            "-fx-background-radius: 6; -fx-padding: 6 16; -fx-font-size: 12; -fx-cursor: hand;");
        btnAcquitter.setOnAction(e -> acquitterSelection());

        Button btnSupprimer = new Button("✖ Supprimer la selection");
        btnSupprimer.setStyle(
            "-fx-background-color: " + SmartFarmingApp.RED + "; -fx-text-fill: white;" +
            "-fx-background-radius: 6; -fx-padding: 6 16; -fx-font-size: 12; -fx-cursor: hand;");
        btnSupprimer.setOnAction(e -> supprimerSelection());

        HBox bar = new HBox(10, btnAcquitter, btnSupprimer);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    // le tableau des alertes
    @SuppressWarnings("unchecked")
    private TableView<Alerte> buildTable() {
        TableView<Alerte> t = new TableView<>(filteredAlertes);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(t, Priority.ALWAYS);

        TableColumn<Alerte, String> colId   = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(
            "ALT-" + String.format("%03d", d.getValue().getId())));
        colId.setMinWidth(80);

        TableColumn<Alerte, String> colNiv  = niveauCol();
        TableColumn<Alerte, String> colCap  = new TableColumn<>("Capteur");
        colCap.setCellValueFactory(d -> {
            Alerte a = d.getValue();
            String code = (a.getReleve() != null && a.getReleve().getCapteur() != null)
                ? a.getReleve().getCapteur().getCode() : "—";
            return new SimpleStringProperty(code);
        });
        colCap.setMinWidth(90);

        TableColumn<Alerte, String> colZone = new TableColumn<>("Zone");
        colZone.setCellValueFactory(d -> {
            Alerte a = d.getValue();
            String nom = (a.getReleve() != null && a.getReleve().getCapteur() != null)
                ? a.getReleve().getCapteur().getZone().getNom() : "—";
            return new SimpleStringProperty(nom);
        });
        colZone.setMinWidth(100);

        TableColumn<Alerte, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(d -> new SimpleStringProperty(DataStore.descriptionAlerte(d.getValue())));
        colDesc.setMinWidth(250);

        TableColumn<Alerte, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm");
            return new SimpleStringProperty(sdf.format(d.getValue().getDateHeure()));
        });
        colDate.setMinWidth(100);

        TableColumn<Alerte, String> colAcq  = acquitteeCol();

        t.getColumns().addAll(colId, colNiv, colCap, colZone, colDesc, colDate, colAcq);

        t.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Alerte item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setStyle(""); return; }
                if      (item.isAcquittee())                               setStyle("-fx-background-color: #f0fff4;");
                else if ("CRITIQUE".equals(item.getNiveauGravite().name())) setStyle("-fx-background-color: #fff5f5;");
                else                                                        setStyle("-fx-background-color: #fffbf0;");
            }
        });

        t.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        return t;
    }

    private TableColumn<Alerte, String> niveauCol() {
        TableColumn<Alerte, String> c = new TableColumn<>("Niveau");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNiveauGravite().name()));
        c.setMinWidth(130);
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if ("CRITIQUE".equals(v))
                    setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                else
                    setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
            }
        });
        return c;
    }

    private TableColumn<Alerte, String> acquitteeCol() {
        TableColumn<Alerte, String> c = new TableColumn<>("Acquittee");
        c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isAcquittee() ? "Oui" : "Non"));
        c.setMinWidth(90);
        c.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setText(null); setStyle(""); return; }
                setText(v);
                if ("Oui".equals(v))
                    setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + "; -fx-font-weight: bold;");
                else
                    setStyle("-fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
            }
        });
        return c;
    }

    // methodes applicatives appelees par les ecouteurs
    private void acquitterSelection() {
        Alerte selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Aucune selection", "Selectionnez une alerte dans le tableau.");
            return;
        }
        if (selected.isAcquittee()) {
            showInfo("Deja acquittee", "Cette alerte est deja acquittee.");
            return;
        }
        DataStore.getInstance().acquitterAlerte(selected);
        refreshTable();
        refreshBadge();
    }

    private void supprimerSelection() {
        Alerte selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Aucune selection", "Selectionnez une alerte dans le tableau.");
            return;
        }
        DataStore.getInstance().getAlertes().remove(selected);
        DataStore.getInstance().save();
        allAlertes.remove(selected);
        refreshBadge();
    }

    private void refreshTable() {
        table.refresh();
    }

    private void refreshBadge() {
        long actives = DataStore.getInstance().countAlertesActives();
        badgeLabel.setText(actives + " alerte(s) active(s)");
        badgeLabel.setStyle("-fx-background-color: #fde8e8; -fx-text-fill: " + SmartFarmingApp.RED + ";" +
                            "-fx-font-weight: bold; -fx-font-size: 12; -fx-background-radius: 6; -fx-padding: 6 14;");
    }

    // le pied de page (legende)
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

    // methode utilitaire pour afficher un message
    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
