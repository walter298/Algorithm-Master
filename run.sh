javac --module-path "/Users/chloe/javafx-sdk-23.0.1/lib" --add-modules javafx.controls -cp lib/org_json.jar @sources.txt
java --module-path "/Users/chloe/javafx-sdk-23.0.1/lib" --add-modules javafx.controls -cp lib/org_json.jar:. src/Main.java