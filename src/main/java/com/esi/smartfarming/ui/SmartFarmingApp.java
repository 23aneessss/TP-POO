package com.esi.smartfarming.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SmartFarmingApp extends Application {

    static final String BG       = "#f0f4f8";
    static final String HEADER   = "#1a3c5e";
    static final String GREEN    = "#27ae60";
    static final String ORANGE   = "#e67e22";
    static final String RED      = "#e74c3c";
    static final String GRAY     = "#7f8c8d";
    static final String BLUE     = "#2980b9";
    static final String PURPLE   = "#8e44ad";
    static final String TEXT     = "#2c3e50";
    static final String SUBTEXT  = "#7f8c8d";
    static final String WHITE    = "white";

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setTop(buildHeader());
        root.setCenter(buildTabs());

        Scene scene = new Scene(root, 1150, 780);

        primaryStage.setTitle("ESI SmartFarming — Interface de gestion");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    private HBox buildHeader() {
        Label title = new Label("ESI SmartFarming");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");

        Label sub = new Label("Ferme Intelligente — Gestion et Supervision");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: #a8c4e0; -fx-padding: 0 0 0 14;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String dateDuJour = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH).format(new Date());
        Label info = new Label("Ferme ESI-Alger   |   " + dateDuJour);
        info.setStyle("-fx-font-size: 12; -fx-text-fill: #a8c4e0;");

        HBox header = new HBox(title, sub, spacer, info);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: " + HEADER + ";");
        return header;
    }

    private TabPane buildTabs() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab t1 = new Tab("   Dashboard   ");
        Tab t2 = new Tab("   Zones   ");
        Tab t3 = new Tab("   Capteurs   ");
        Tab t4 = new Tab("   Production   ");
        Tab t5 = new Tab("   Alertes   ");

        t1.setContent(new DashboardView().build());
        t2.setContent(new ZonesView().build());
        t3.setContent(new CapteursView().build());
        t4.setContent(new ProductionView().build());
        t5.setContent(new AlertesView().build());

        tabs.getTabs().addAll(t1, t2, t3, t4, t5);

        tabs.getSelectionModel().selectedItemProperty().addListener((obs, ancien, courant) -> {
            if (courant == t1) t1.setContent(new DashboardView().build());
            else if (courant == t2) t2.setContent(new ZonesView().build());
            else if (courant == t3) t3.setContent(new CapteursView().build());
            else if (courant == t4) t4.setContent(new ProductionView().build());
            else if (courant == t5) t5.setContent(new AlertesView().build());
        });

        return tabs;
    }

    public static void main(String[] args) { launch(args); }
}
