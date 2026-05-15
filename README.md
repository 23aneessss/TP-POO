# TP-POO
# compiler + executer le tp
mkdir -p out && find src -name "*.java" | xargs javac -d out && java -cp out com.esi.smartfarming.Main


# complier le tp 
mkdir -p out && find src -name "*.java" | xargs javac -d out

# executer le tp
java -cp out com.esi.smartfarming.Main
