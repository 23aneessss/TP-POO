package com.esi.smartfarming.ui;

/*
 * COMPILATION ET EXECUTION :
 *
 * 1. Telecharger JavaFX SDK sur https://openjfx.io  (choisir Mac aarch64 ou x64)
 * 2. Extraire dans ~/javafx-sdk/
 * 3. Compiler :
 *    export FX=~/javafx-sdk/lib
 *    find src -name "*.java" | xargs javac --module-path $FX --add-modules javafx.controls -d out
 * 4. Executer :
 *    java --module-path $FX --add-modules javafx.controls -cp out com.esi.smartfarming.ui.SmartFarmingApp
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setTop(buildHeader());
        root.setCenter(buildTabs());

        stage.setTitle("ESI SmartFarming — Interface de gestion");
        stage.setScene(new Scene(root, 1150, 780));
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.show();
    }

    private HBox buildHeader() {
        Label title = new Label("ESI SmartFarming");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");

        Label sub = new Label("Ferme Intelligente — Gestion et Supervision");
        sub.setStyle("-fx-font-size: 12; -fx-text-fill: #a8c4e0; -fx-padding: 0 0 0 14;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label info = new Label("Ferme ESI-Alger   |   15 Mai 2026");
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

        Tab t1 = new Tab("   Dashboard   ", new DashboardView().build());
        Tab t2 = new Tab("   Zones   ",     new ZonesView().build());
        Tab t3 = new Tab("   Capteurs   ",  new CapteursView().build());
        Tab t4 = new Tab("   Alertes   ",   new AlertesView().build());

        tabs.getTabs().addAll(t1, t2, t3, t4);
        return tabs;
    }

    public static void main(String[] args) { launch(args); }
}
