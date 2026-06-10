package com.esi.smartfarming.ui;

import com.esi.smartfarming.capteur.*;
import com.esi.smartfarming.data.DataStore;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.releve.ReleveGPS;
import com.esi.smartfarming.releve.ReleveNumerique;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CapteursView {

    private Label listTitle;
    private VBox capteurListBox;
    private ComboBox<String> cbZoneFilter;

    private LineChart<String, Number> chart;
    private Label chartTitle;
    private Label chartSub;
    private Label chartNoData;
    private VBox chartContainer;
    private ComboBox<CapteurNumerique> cbCapteur;
    private DatePicker dpDebut;
    private DatePicker dpFin;

    public Node build() {
        SplitPane split = new SplitPane();
        split.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");
        split.getItems().addAll(buildCapteurList(), buildChartPanel());
        split.setDividerPositions(0.42);
        return split;
    }

    private Node buildCapteurList() {
        DataStore ds = DataStore.getInstance();

        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        listTitle = new Label();
        listTitle.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        cbZoneFilter = new ComboBox<>(FXCollections.observableArrayList(
            "Toutes zones", "Zone Nord", "Zone Est", "Zone Sud"));
        cbZoneFilter.getSelectionModel().selectFirst();
        cbZoneFilter.setMaxWidth(Double.MAX_VALUE);
        cbZoneFilter.setOnAction(e -> refreshCapteurCards(ds));

        Label filterLbl = new Label("Filtrer par zone :");
        filterLbl.setStyle("-fx-font-size: 12; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        capteurListBox = new VBox(10);

        box.getChildren().addAll(listTitle, filterLbl, cbZoneFilter, capteurListBox);
        refreshCapteurCards(ds);

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:" + SmartFarmingApp.BG + ";-fx-background:" + SmartFarmingApp.BG + ";");
        return scroll;
    }

    private void refreshCapteurCards(DataStore ds) {
        capteurListBox.getChildren().clear();
        String zoneFiltre = cbZoneFilter.getValue();
        int count = 0;
        for (Capteur c : ds.getAllCapteurs()) {
            if (zoneFiltre != null && !zoneFiltre.equals("Toutes zones")
                && !c.getZone().getNom().equals(zoneFiltre)) continue;
            capteurListBox.getChildren().add(capteurCard(c, ds));
            count++;
        }
        listTitle.setText("Capteurs (" + count + ")");
    }

    private VBox capteurCard(Capteur c, DataStore ds) {
        String[] row = ds.toCapteurRow(c);
        String statut = c.getStatut().name();

        String statusColor;
        if (statut.equals("ACTIF"))           statusColor = SmartFarmingApp.GREEN;
        else if (statut.equals("DEFAILLANT")) statusColor = SmartFarmingApp.RED;
        else                                  statusColor = SmartFarmingApp.GRAY;

        String niveauColor;
        if (row[7].equals("CRITIQUE"))           niveauColor = SmartFarmingApp.RED;
        else if (row[7].equals("AVERTISSEMENT")) niveauColor = SmartFarmingApp.ORANGE;
        else                                     niveauColor = SmartFarmingApp.GREEN;

        Label code = new Label(c.getCode() + "  (" + row[1] + ")");
        code.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: " + SmartFarmingApp.TEXT + ";");

        Label zone = new Label(c.getZone().getNom());
        zone.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label statLabel = new Label("● " + statut);
        statLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");

        Label valeur = new Label();
        Label niveau = new Label("Niveau : " + row[7]);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + niveauColor + ";");
        Label seuils = new Label("Seuils : " + row[4] + " — " + row[5]);
        seuils.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.SUBTEXT + ";");

        Label mesures = buildAllMesures(c);
        mesures.setStyle("-fx-font-size: 11; -fx-text-fill: " + SmartFarmingApp.TEXT
            + "; -fx-background-color: #f8f9fa; -fx-background-radius: 4; -fx-padding: 4 8;");
        mesures.setWrapText(true);

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
            if (c.getStatut() == StatutCapteur.DEFAILLANT) ds.reactiverCapteur(c);
            else                                           ds.basculerCapteur(c);
            updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
            updateBasculeLabel(c, btnBascule);
        });

        btnDefaill.setOnAction(e -> {
            if (c.getStatut() == StatutCapteur.DEFAILLANT) {
                info("Deja defaillant", "Ce capteur est deja marque comme defaillant."); return;
            }
            ds.marquerDefaillantCapteur(c);
            updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
            updateBasculeLabel(c, btnBascule);
        });

        btnReleve.setOnAction(e -> {
            if (c.getStatut() != StatutCapteur.ACTIF) {
                info("Capteur inactif", "Activez d'abord le capteur pour envoyer un releve."); return;
            }
            if (c instanceof CapteurNumerique) {
                CapteurNumerique cn = (CapteurNumerique) c;
                ReleveNumerique r = cn.envoyerReleve();
                ds.save();
                updateCapteurCardLabels(c, ds, statLabel, valeur, niveau, seuils);
                if (cbCapteur != null && cbCapteur.getValue() == c) refreshChart();
                info("Releve envoye", "[" + c.getCode() + "] Valeur = "
                    + String.format("%.2f", r.getValeur()) + " " + r.getUnite()
                    + " — Niveau : " + r.getNiveau());
            } else if (c instanceof CapteurGPS) {
                CapteurGPS cg = (CapteurGPS) c;
                ReleveGPS r = cg.envoyerReleve();
                ds.save();
                info("Releve GPS envoye", "[" + c.getCode() + "] lat="
                    + String.format("%.5f", r.getLatitude()) + "  lon="
                    + String.format("%.5f", r.getLongitude()));
            }
        });

        HBox actions = new HBox(6, btnBascule, btnDefaill, btnReleve);
        actions.setPadding(new Insets(4, 0, 0, 0));
        return actions;
    }

    private Label buildAllMesures(Capteur c) {
        StringBuilder sb = new StringBuilder();
        if (c instanceof CapteurEnvironnemental) {
            CapteurEnvironnemental ce = (CapteurEnvironnemental) c;
            sb.append(String.format("T° : %.1f °C  (seuils: %.1f – %.1f)%n",
                ce.getTemperature(), ce.getTempMin(), ce.getTempMax()));
            sb.append(String.format("Humidite : %.1f %%  (seuils: %.1f – %.1f)%n",
                ce.getHumidite(), ce.getHumMin(), ce.getHumMax()));
            sb.append(String.format("Pluviometrie : %.1f mm  (seuils: %.1f – %.1f)",
                ce.getPluviometrie(), ce.getPluvMin(), ce.getPluvMax()));
        } else if (c instanceof CapteurSol) {
            CapteurSol cs = (CapteurSol) c;
            sb.append(String.format("pH : %.2f  (seuils: %.1f – %.1f)%n",
                cs.getPh(), cs.getPhMin(), cs.getPhMax()));
            sb.append(String.format("Humidite sol : %.1f %%  (seuils: %.1f – %.1f)%n",
                cs.getHumidite(), cs.getHumMin(), cs.getHumMax()));
            sb.append(String.format("Azote : %.2f mg/kg  (seuils: %.1f – %.1f)",
                cs.getTeneurAzote(), cs.getAzoteMin(), cs.getAzoteMax()));
        } else if (c instanceof CapteurBiometrique) {
            CapteurBiometrique cb = (CapteurBiometrique) c;
            sb.append(String.format("Temp. corp. : %.1f °C  (seuils: %.1f – %.1f)%n",
                cb.getTemperatureCorporelle(), cb.getTempCorpMin(), cb.getTempCorpMax()));
            sb.append(String.format("Activite : %.1f  (seuils: %.1f – %.1f)%n",
                cb.getNiveauActivite(), cb.getActiviteMin(), cb.getActiviteMax()));
            sb.append("Animal : ").append(cb.getAnimal() != null ? cb.getAnimal().getEspece() : "—");
        } else if (c instanceof CapteurEau) {
            CapteurEau ce = (CapteurEau) c;
            sb.append(String.format("Temp. eau : %.1f °C  (seuils: %.1f – %.1f)%n",
                ce.getTemperateur(), ce.getTempMin(), ce.getTempMax()));
            sb.append(String.format("Oxygene : %.1f mg/L  (seuils: %.1f – %.1f)%n",
                ce.getOxygene(), ce.getOxyMin(), ce.getOxyMax()));
            sb.append(String.format("pH : %.2f  (seuils: %.1f – %.1f)",
                ce.getPh(), ce.getPhMin(), ce.getPhMax()));
        } else if (c instanceof CapteurGPS) {
            CapteurGPS cg = (CapteurGPS) c;
            List<ReleveGPS> hist = cg.getHistoriqueGPS();
            if (!hist.isEmpty()) {
                ReleveGPS last = hist.get(hist.size() - 1);
                sb.append(String.format("Derniere position :%nlat = %.5f%nlon = %.5f",
                    last.getLatitude(), last.getLongitude()));
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
            case ACTIF:
                btn.setText("⏸ Suspendre");
                styleMiniBtn(btn, SmartFarmingApp.GRAY);
                break;
            case SUSPENDU:
                btn.setText("▶ Activer");
                styleMiniBtn(btn, SmartFarmingApp.GREEN);
                break;
            case DEFAILLANT:
                btn.setText("↺ Reactiver");
                styleMiniBtn(btn, SmartFarmingApp.BLUE);
                break;
        }
    }

    private void updateCapteurCardLabels(Capteur c, DataStore ds,
                                          Label statLabel, Label valeur, Label niveau, Label seuils) {
        String[] row = ds.toCapteurRow(c);
        String statut = c.getStatut().name();

        String statusColor;
        if (statut.equals("ACTIF"))           statusColor = SmartFarmingApp.GREEN;
        else if (statut.equals("DEFAILLANT")) statusColor = SmartFarmingApp.RED;
        else                                  statusColor = SmartFarmingApp.GRAY;

        String niveauColor;
        if (row[7].equals("CRITIQUE"))           niveauColor = SmartFarmingApp.RED;
        else if (row[7].equals("AVERTISSEMENT")) niveauColor = SmartFarmingApp.ORANGE;
        else                                     niveauColor = SmartFarmingApp.GREEN;

        statLabel.setText("● " + statut);
        statLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");
        valeur.setText("Valeur : " + row[6]);
        niveau.setText("Niveau : " + row[7]);
        niveau.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: " + niveauColor + ";");
        seuils.setText("Seuils : " + row[4] + " — " + row[5]);
    }

    private void styleMiniBtn(Button b, String color) {
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;" +
                   "-fx-background-radius:4;-fx-padding:3 10;-fx-font-size:11;-fx-cursor:hand;");
    }

    private Node buildChartPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: " + SmartFarmingApp.BG + ";");

        chartTitle = new Label("Historique des releves");
        chartTitle.setStyle("-fx-font-size:14;-fx-font-weight:bold;-fx-text-fill:" + SmartFarmingApp.TEXT + ";");

        chartSub = new Label("Selectionnez un capteur pour afficher son evolution");
        chartSub.setStyle("-fx-font-size:12;-fx-text-fill:" + SmartFarmingApp.SUBTEXT + ";");

        List<CapteurNumerique> numeriques = getAllNumeriques();
        cbCapteur = new ComboBox<>(FXCollections.observableArrayList(numeriques));
        cbCapteur.setCellFactory(lv -> capteurCell());
        cbCapteur.setButtonCell(capteurCell());
        cbCapteur.setMaxWidth(Double.MAX_VALUE);
        cbCapteur.setPromptText("-- Choisir un capteur --");

        Button btnRefresh = new Button("↻ Rafraichir");
        btnRefresh.setStyle("-fx-background-color:" + SmartFarmingApp.BLUE + ";-fx-text-fill:white;" +
                            "-fx-background-radius:6;-fx-padding:6 14;-fx-cursor:hand;");

        HBox selector = new HBox(10, cbCapteur, btnRefresh);
        selector.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cbCapteur, Priority.ALWAYS);
        btnRefresh.setOnAction(e -> refreshChart());

        dpDebut = new DatePicker();
        dpFin = new DatePicker();
        dpDebut.setPromptText("Du");
        dpFin.setPromptText("Au");
        dpDebut.setPrefWidth(150);
        dpFin.setPrefWidth(150);
        Button btnReset = new Button("Effacer");
        btnReset.setStyle("-fx-background-color:" + SmartFarmingApp.GRAY + ";-fx-text-fill:white;" +
                          "-fx-background-radius:6;-fx-padding:6 14;-fx-cursor:hand;");
        btnReset.setOnAction(e -> { dpDebut.setValue(null); dpFin.setValue(null); refreshChart(); });
        dpDebut.setOnAction(e -> refreshChart());
        dpFin.setOnAction(e -> refreshChart());

        Label periodeLbl = new Label("Periode :");
        periodeLbl.setStyle("-fx-font-size:12;-fx-text-fill:" + SmartFarmingApp.SUBTEXT + ";");
        HBox periode = new HBox(10, periodeLbl, dpDebut, dpFin, btnReset);
        periode.setAlignment(Pos.CENTER_LEFT);

        chartNoData = new Label("Aucun releve disponible.\nCliquez sur '↻ Releve' sur un capteur ou\n'Envoyer releves' dans une zone pour remplir l'historique.");
        chartNoData.setStyle("-fx-font-size:13;-fx-text-fill:" + SmartFarmingApp.SUBTEXT
            + ";-fx-alignment:center;-fx-text-alignment:center;");
        chartNoData.setWrapText(true);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Heure");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valeur");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setStyle("-fx-background-color:white;-fx-background-radius:8;");
        VBox.setVgrow(chart, Priority.ALWAYS);

        chartContainer = new VBox(chart);
        chartContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(chartContainer, Priority.ALWAYS);

        Label seuilInfo = new Label();
        seuilInfo.setStyle("-fx-font-size:11;-fx-text-fill:" + SmartFarmingApp.SUBTEXT
            + ";-fx-background-color:#f8f9fa;-fx-background-radius:4;-fx-padding:4 10;");

        cbCapteur.setOnAction(e -> {
            refreshChart();
            CapteurNumerique sel = cbCapteur.getValue();
            if (sel != null) {
                String[] row = DataStore.getInstance().toCapteurRow(sel);
                seuilInfo.setText("Seuil MIN : " + row[4] + "   |   Seuil MAX : " + row[5]
                    + "   |   Unite : " + sel.getUnite());
            } else {
                seuilInfo.setText("");
            }
        });

        panel.getChildren().addAll(chartTitle, chartSub, selector, periode, chartContainer, seuilInfo, buildLegend());

        if (!numeriques.isEmpty()) {
            CapteurNumerique first = numeriques.get(0);
            for (CapteurNumerique cn : numeriques) {
                if (!cn.getHistorique().isEmpty()) { first = cn; break; }
            }
            cbCapteur.setValue(first);
            refreshChart();
            String[] row = DataStore.getInstance().toCapteurRow(first);
            seuilInfo.setText("Seuil MIN : " + row[4] + "   |   Seuil MAX : " + row[5]
                + "   |   Unite : " + first.getUnite());
        }

        return panel;
    }

    @SuppressWarnings("unchecked")
    private void refreshChart() {
        CapteurNumerique sel = cbCapteur.getValue();
        chartContainer.getChildren().clear();

        if (sel == null) {
            chartContainer.getChildren().add(chartNoData);
            chartTitle.setText("Historique des releves");
            chartSub.setText("Selectionnez un capteur");
            return;
        }

        List<ReleveNumerique> hist = new ArrayList<>();
        for (ReleveNumerique r : sel.getHistorique()) {
            if (dansIntervalle(r.getDateHeure())) hist.add(r);
        }

        chartTitle.setText("Evolution — " + sel.getCode() + " (" + sel.getClass().getSimpleName().replace("Capteur","") + ")");

        if (hist.isEmpty()) {
            chartSub.setText("Aucun releve dans la periode choisie — utilisez '↻ Releve' ou changez la periode");
            chartContainer.getChildren().add(chartNoData);
            return;
        }

        chartSub.setText(hist.size() + " releve(s) affiche(s)");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Heure");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(sel.getUnite());

        chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setStyle("-fx-background-color:white;-fx-background-radius:8;");
        VBox.setVgrow(chart, Priority.ALWAYS);

        XYChart.Series<String, Number> serieValeurs = new XYChart.Series<>();
        serieValeurs.setName(sel.getCode() + " (" + sel.getUnite() + ")");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        int start = Math.max(0, hist.size() - 20);
        for (int i = start; i < hist.size(); i++) {
            ReleveNumerique r = hist.get(i);
            serieValeurs.getData().add(new XYChart.Data<>(sdf.format(r.getDateHeure()), r.getValeur()));
        }
        chart.getData().add(serieValeurs);

        String[] row = DataStore.getInstance().toCapteurRow(sel);
        try {
            double sMin = Double.parseDouble(row[4]);
            double sMax = Double.parseDouble(row[5]);
            XYChart.Series<String, Number> serieMin = new XYChart.Series<>();
            serieMin.setName("Seuil min (" + sMin + ")");
            XYChart.Series<String, Number> serieMax = new XYChart.Series<>();
            serieMax.setName("Seuil max (" + sMax + ")");
            for (int i = start; i < hist.size(); i++) {
                String label = sdf.format(hist.get(i).getDateHeure());
                serieMin.getData().add(new XYChart.Data<>(label, sMin));
                serieMax.getData().add(new XYChart.Data<>(label, sMax));
            }
            chart.getData().addAll(serieMin, serieMax);

            javafx.application.Platform.runLater(() -> {
                for (int s = 1; s < chart.getData().size(); s++) {
                    XYChart.Series<String, Number> serie = chart.getData().get(s);
                    if (s == 1)
                        serie.getNode().setStyle("-fx-stroke: " + SmartFarmingApp.ORANGE + "; -fx-stroke-dash-array: 8 4; -fx-stroke-width: 1.5;");
                    else
                        serie.getNode().setStyle("-fx-stroke: " + SmartFarmingApp.RED + "; -fx-stroke-dash-array: 8 4; -fx-stroke-width: 1.5;");
                    for (XYChart.Data<String, Number> d : serie.getData()) {
                        if (d.getNode() != null) d.getNode().setVisible(false);
                    }
                }
            });
        } catch (NumberFormatException ignored) {}

        chartContainer.getChildren().add(chart);
    }

    private boolean dansIntervalle(Date d) {
        if (dpDebut == null) return true;
        LocalDate jour = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate debut = dpDebut.getValue();
        LocalDate fin = dpFin.getValue();
        if (debut != null && jour.isBefore(debut)) return false;
        if (fin != null && jour.isAfter(fin)) return false;
        return true;
    }

    private List<CapteurNumerique> getAllNumeriques() {
        List<CapteurNumerique> liste = new ArrayList<>();
        for (Capteur c : DataStore.getInstance().getAllCapteurs()) {
            if (c instanceof CapteurNumerique) {
                liste.add((CapteurNumerique) c);
            }
        }
        return liste;
    }

    private ListCell<CapteurNumerique> capteurCell() {
        return new ListCell<>() {
            @Override protected void updateItem(CapteurNumerique c, boolean empty) {
                super.updateItem(c, empty);
                if (c == null || empty) { setText(null); return; }
                int nbReleves = c.getHistorique().size();
                setText(c.getCode() + " — " + c.getClass().getSimpleName().replace("Capteur", "")
                    + " (" + c.getZone().getNom() + ")  [" + nbReleves + " relevé(s)]");
            }
        };
    }

    private HBox buildLegend() {
        HBox legend = new HBox(24);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(8, 12, 8, 12));
        legend.setStyle("-fx-background-color:white;-fx-background-radius:6;");
        legend.getChildren().addAll(
            legendItem("NORMAL",        SmartFarmingApp.GREEN,  "Releve dans les seuils"),
            legendItem("AVERTISSEMENT", SmartFarmingApp.ORANGE, "--- Seuil min"),
            legendItem("CRITIQUE",      SmartFarmingApp.RED,    "--- Seuil max")
        );
        return legend;
    }

    private HBox legendItem(String label, String color, String desc) {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill:" + color + ";-fx-font-size:14;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size:12;-fx-font-weight:bold;-fx-text-fill:" + color + ";");
        Label d = new Label(" — " + desc);
        d.setStyle("-fx-font-size:11;-fx-text-fill:" + SmartFarmingApp.SUBTEXT + ";");
        return new HBox(4, dot, lbl, d);
    }

    private void info(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
