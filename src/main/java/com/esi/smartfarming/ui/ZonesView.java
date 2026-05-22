package com.esi.smartfarming.ui;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.animal.Ruminant;
import com.esi.smartfarming.animal.Volaille;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.enums.StageCroissance;
import com.esi.smartfarming.enums.StatutAnimal;
import com.esi.smartfarming.enums.TypeFamille;
import com.esi.smartfarming.zone.Zone;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Date;
import java.util.stream.Collectors;

public class ZonesView {

    private ObservableList<String[]> culturesData;
    private ObservableList<String[]> animauxData;
    private Label lblZoneNordStatut;
    private Label lblZoneEstStatut;
    private Label lblZoneSudStatut;

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

    // ── ZoneCulture ───────────────────────────────────────────────────────────

    private Node buildCultureTab() {
        DataStore ds = DataStore.getInstance();
        culturesData = FXCollections.observableArrayList(
            ds.getZoneNord().getCultures().stream()
                .map(ds::toCultureRow).collect(Collectors.toList()));

        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        lblZoneNordStatut = new Label();
        HBox header = zoneHeader("Zone Nord — ZoneCulture", ds.getZoneNord(), lblZoneNordStatut);

        Button btnSuspendre = actionBtn("Suspendre / Activer", SmartFarmingApp.GRAY);
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneNord());
            refreshZoneStatut(ds.getZoneNord(), lblZoneNordStatut);
        });

        Button btnAjouter = actionBtn("+ Ajouter culture", SmartFarmingApp.GREEN);
        btnAjouter.setOnAction(e -> showAddCultureDialog());

        HBox actions = new HBox(10, btnAjouter, btnSuspendre);
        actions.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> table = cultureTable();

        box.getChildren().addAll(
            header, actions,
            sectionLabel("Cultures (" + ds.getZoneNord().getCultures().size() + ")"),
            table,
            sectionLabel("Capteurs (" + ds.getZoneNord().getCapteurs().size() + ")"),
            capteurTable("Zone Nord")
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── ZoneElevage ───────────────────────────────────────────────────────────

    private Node buildElevageTab() {
        DataStore ds = DataStore.getInstance();
        animauxData = FXCollections.observableArrayList(
            ds.getZoneEst().getAnimaux().stream()
                .map(ds::toAnimalRow).collect(Collectors.toList()));

        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        lblZoneEstStatut = new Label();
        HBox header = zoneHeader("Zone Est — ZoneElevage", ds.getZoneEst(), lblZoneEstStatut);

        Button btnSuspendre = actionBtn("Suspendre / Activer", SmartFarmingApp.GRAY);
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneEst());
            refreshZoneStatut(ds.getZoneEst(), lblZoneEstStatut);
        });

        Button btnAjouter = actionBtn("+ Ajouter animal", SmartFarmingApp.GREEN);
        btnAjouter.setOnAction(e -> showAddAnimalDialog());

        HBox actions = new HBox(10, btnAjouter, btnSuspendre);
        actions.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(
            header, actions,
            sectionLabel("Animaux"),
            animauxTable(),
            sectionLabel("Programme d'alimentation"),
            infoCard("Aliment",       ds.getZoneEst().getProgrammeAlimentation().getTypeAliment(),
                     "Quantite / repas", ds.getZoneEst().getProgrammeAlimentation().getQuantiteParRepas() + " kg"),
            sectionLabel("Capteurs (" + ds.getZoneEst().getCapteurs().size() + ")"),
            capteurTable("Zone Est")
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── ZoneAquacole ──────────────────────────────────────────────────────────

    private Node buildAquacoleTab() {
        DataStore ds = DataStore.getInstance();

        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        lblZoneSudStatut = new Label();
        HBox header = zoneHeader("Zone Sud — ZoneAquacole", ds.getZoneSud(), lblZoneSudStatut);

        Button btnSuspendre = actionBtn("Suspendre / Activer", SmartFarmingApp.GRAY);
        btnSuspendre.setOnAction(e -> {
            ds.basculerZone(ds.getZoneSud());
            refreshZoneStatut(ds.getZoneSud(), lblZoneSudStatut);
        });

        HBox actions = new HBox(10, btnSuspendre);
        actions.setAlignment(Pos.CENTER_LEFT);

        String espece = ds.getZoneSud().getEspeces().isEmpty() ? "—"
            : ds.getZoneSud().getEspeces().get(0).getEspece();
        String nombre = ds.getZoneSud().getEspeces().isEmpty() ? "—"
            : String.valueOf(ds.getZoneSud().getEspeces().get(0).getNombre());

        box.getChildren().addAll(
            header, actions,
            sectionLabel("Especes aquacoles (" + ds.getZoneSud().getEspeces().size() + ")"),
            infoCard("Espece", espece, "Nombre d'individus", nombre),
            sectionLabel("Programme d'alimentation"),
            infoCard("Aliment",       ds.getZoneSud().getProgrammeAlimentation().getTypeAliment(),
                     "Quantite / repas", ds.getZoneSud().getProgrammeAlimentation().getQuantiteParRepas() + " kg"),
            sectionLabel("Capteurs (" + ds.getZoneSud().getCapteurs().size() + ")"),
            capteurTable("Zone Sud")
        );

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle(scrollStyle());
        return scroll;
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private void showAddCultureDialog() {
        DataStore ds = DataStore.getInstance();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une culture");
        dialog.setHeaderText("Nouvelle culture — Zone Nord");

        ButtonType okBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 120, 10, 10));

        TextField tfNom    = new TextField(); tfNom.setPromptText("Ex: Ble dur");
        ComboBox<TypeFamille>     cbFamille = new ComboBox<>(FXCollections.observableArrayList(TypeFamille.values()));
        ComboBox<StageCroissance> cbStade   = new ComboBox<>(FXCollections.observableArrayList(StageCroissance.values()));
        cbFamille.getSelectionModel().selectFirst();
        cbStade.getSelectionModel().selectFirst();
        TextField tfPhMin  = new TextField("6.0"); TextField tfPhMax  = new TextField("7.5");
        TextField tfHumMin = new TextField("40");  TextField tfHumMax = new TextField("70");

        grid.add(new Label("Nom :"),          0, 0); grid.add(tfNom,    1, 0);
        grid.add(new Label("Famille :"),      0, 1); grid.add(cbFamille, 1, 1);
        grid.add(new Label("Stade :"),        0, 2); grid.add(cbStade,   1, 2);
        grid.add(new Label("pH min :"),       0, 3); grid.add(tfPhMin,   1, 3);
        grid.add(new Label("pH max :"),       0, 4); grid.add(tfPhMax,   1, 4);
        grid.add(new Label("Humidite min :"), 0, 5); grid.add(tfHumMin,  1, 5);
        grid.add(new Label("Humidite max :"), 0, 6); grid.add(tfHumMax,  1, 6);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn != okBtn) return;
            try {
                Culture c = new Culture(
                    ds.nextCultureId(),
                    tfNom.getText().trim(),
                    cbFamille.getValue(),
                    new Date(), new Date(),
                    Double.parseDouble(tfPhMin.getText()),
                    Double.parseDouble(tfPhMax.getText()),
                    Double.parseDouble(tfHumMin.getText()),
                    Double.parseDouble(tfHumMax.getText())
                );
                c.setStageCroissance(cbStade.getValue());
                ds.ajouterCulture(c);
                refreshCultures();
            } catch (Exception ex) {
                showError("Erreur de saisie", ex.getMessage());
            }
        });
    }

    private void showAddAnimalDialog() {
        DataStore ds = DataStore.getInstance();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un animal");
        dialog.setHeaderText("Nouvel animal — Zone Est");

        ButtonType okBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 120, 10, 10));

        ComboBox<String>     cbType  = new ComboBox<>(FXCollections.observableArrayList("Ruminant", "Volaille"));
        cbType.getSelectionModel().selectFirst();
        TextField            tfEspece = new TextField(); tfEspece.setPromptText("Ex: Vache Normande");
        TextField            tfAge    = new TextField("2");
        TextField            tfPoids  = new TextField("400");
        ComboBox<StatutAnimal> cbSante = new ComboBox<>(FXCollections.observableArrayList(StatutAnimal.values()));
        cbSante.getSelectionModel().selectFirst();

        grid.add(new Label("Type :"),   0, 0); grid.add(cbType,   1, 0);
        grid.add(new Label("Espece :"), 0, 1); grid.add(tfEspece, 1, 1);
        grid.add(new Label("Age :"),    0, 2); grid.add(tfAge,    1, 2);
        grid.add(new Label("Poids :"),  0, 3); grid.add(tfPoids,  1, 3);
        grid.add(new Label("Sante :"),  0, 4); grid.add(cbSante,  1, 4);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn != okBtn) return;
            try {
                int    id    = ds.nextAnimalId();
                double poids = Double.parseDouble(tfPoids.getText());
                int    age   = Integer.parseInt(tfAge.getText());
                Animal a = "Ruminant".equals(cbType.getValue())
                    ? new Ruminant(id, tfEspece.getText().trim(), age, poids)
                    : new Volaille(id, tfEspece.getText().trim(), age, poids);
                a.setEtatSante(cbSante.getValue());
                ds.ajouterAnimal(a);
                refreshAnimaux();
            } catch (Exception ex) {
                showError("Erreur de saisie", ex.getMessage());
            }
        });
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    private void refreshCultures() {
        DataStore ds = DataStore.getInstance();
        culturesData.setAll(
            ds.getZoneNord().getCultures().stream()
                .map(ds::toCultureRow).collect(Collectors.toList()));
    }

    private void refreshAnimaux() {
        DataStore ds = DataStore.getInstance();
        animauxData.setAll(
            ds.getZoneEst().getAnimaux().stream()
                .map(ds::toAnimalRow).collect(Collectors.toList()));
    }

    private void refreshZoneStatut(Zone z, Label lbl) {
        boolean active = z.getStatut().name().equals("ACTIVE");
        String color   = active ? SmartFarmingApp.GREEN : SmartFarmingApp.GRAY;
        String bg      = active ? "#eafaf1" : "#f2f3f4";
        lbl.setText("● " + z.getStatut().name());
        lbl.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                     "-fx-background-color: " + bg + "; -fx-background-radius: 4; -fx-padding: 3 10;");
    }

    // ── Tables ────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private TableView<String[]> cultureTable() {
        TableView<String[]> t = new TableView<>(culturesData);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setMaxHeight(150);
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
        t.setMaxHeight(200);
        t.getColumns().addAll(
            col("#", 0), col("Espece", 1), col("Type", 2),
            col("Age", 3), col("Poids", 4), santeCol(5)
        );
        return t;
    }

    @SuppressWarnings("unchecked")
    private TableView<String[]> capteurTable(String zoneNom) {
        DataStore ds = DataStore.getInstance();
        ObservableList<String[]> data = FXCollections.observableArrayList(
            ds.getAllCapteurs().stream()
                .filter(c -> zoneNom.equals(c.getZone().getNom()))
                .map(ds::toCapteurRow)
                .collect(Collectors.toList()));
        TableView<String[]> t = new TableView<>(data);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setMaxHeight(160);
        t.getColumns().addAll(
            col("Code", 0), col("Type", 1), statutCol(3),
            col("Seuil min", 4), col("Seuil max", 5), col("Derniere valeur", 6), niveauCol(7)
        );
        return t;
    }

    // ── Column factories ──────────────────────────────────────────────────────

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
                switch (v) {
                    case "SEMIS"       -> setStyle("-fx-text-fill: " + SmartFarmingApp.PURPLE + ";");
                    case "GERMINATION" -> setStyle("-fx-text-fill: " + SmartFarmingApp.BLUE + ";");
                    case "CROISSANCE"  -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + ";");
                    case "MATURITE"    -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                    case "RECOLTE"     -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED + "; -fx-font-weight: bold;");
                    default -> setStyle("");
                }
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
                switch (v) {
                    case "SAIN"        -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                    case "MALADE"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                    case "QUARANTAINE" -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
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
                    case "ACTIF"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN + "; -fx-font-weight: bold;");
                    case "DEFAILLANT" -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED   + "; -fx-font-weight: bold;");
                    case "SUSPENDU"   -> setStyle("-fx-text-fill: " + SmartFarmingApp.GRAY  + "; -fx-font-weight: bold;");
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
                    case "NORMAL"        -> setStyle("-fx-text-fill: " + SmartFarmingApp.GREEN  + "; -fx-font-weight: bold;");
                    case "AVERTISSEMENT" -> setStyle("-fx-text-fill: " + SmartFarmingApp.ORANGE + "; -fx-font-weight: bold;");
                    case "CRITIQUE"      -> setStyle("-fx-text-fill: " + SmartFarmingApp.RED    + "; -fx-font-weight: bold;");
                    default -> setStyle("");
                }
            }
        });
        return c;
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private HBox zoneHeader(String nom, Zone z, Label statutLabel) {
        Label name = new Label(nom);
        name.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        refreshZoneStatut(z, statutLabel);
        HBox row = new HBox(16, name, statutLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Button actionBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;" +
                   "-fx-background-radius: 6; -fx-padding: 6 16; -fx-font-size: 12; -fx-cursor: hand;");
        return b;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        return l;
    }

    private HBox infoCard(String k1, String v1, String k2, String v2) {
        Label l1  = new Label(k1 + " : ");
        l1.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        Label lv1 = new Label(v1);
        lv1.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        Region sep = new Region(); sep.setMinWidth(32);
        Label l2  = new Label(k2 + " : ");
        l2.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");
        Label lv2 = new Label(v2);
        lv2.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");
        HBox card = new HBox(4, l1, lv1, sep, l2, lv2);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12, 18, 12, 18));
        card.setStyle("-fx-background-color: #eaf4fb; -fx-background-radius: 6;");
        return card;
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg != null ? msg : "Saisie invalide.");
        alert.showAndWait();
    }

    private String scrollStyle() {
        return "-fx-background-color: " + SmartFarmingApp.BG + "; -fx-background: " + SmartFarmingApp.BG + ";";
    }

}
