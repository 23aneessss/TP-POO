# TP-POO
export JAVA=/opt/homebrew/opt/openjdk/bin
export FX=/Users/mac/Downloads/javafx-sdk-26.0.1/lib

# Compiler (une seule fois, ou après modification) :
find src -name "*.java" | xargs $JAVA/javac --module-path $FX --add-modules javafx.controls -d out

# Lancer l'interface :
$JAVA/java --module-path $FX --add-modules javafx.controls -cp out com.esi.smartfarming.ui.SmartFarmingApp

# Lancer le terminal (Main interactif) :
$JAVA/java -cp out com.esi.smartfarming.Main