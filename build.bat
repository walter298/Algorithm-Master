javac --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls -cp lib/org_json.jar @sources.txt
java --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls -cp lib/org_json.jar;. @sources.txt