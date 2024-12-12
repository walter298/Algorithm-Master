package src;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlgorithmPage {
    public static String getPath(String filename) {
        return "algorithm_data/" + filename;
    }
    
    VBox layoutRoot;
    
    private Text makeText(String str, int fontSize) {
        var text = new Text(str);
        text.setFont(new Font(fontSize));
        return text;
    }

    private Text makeText(String str) {
        final int FONT_SIZE = 21;
        return makeText(str, FONT_SIZE);
    }

    private static StackPane centerNode(Node n) {
        var centeredPane = new StackPane(n);
        centeredPane.setAlignment(Pos.TOP_CENTER); 
        //centeredPane;
        return centeredPane;
    }

    private void setSize(Region r, int width, int height) {
        r.setPrefSize(width, height);
        r.setMinSize(width, height);
        r.setMaxSize(width, height);
    }   

    private void writeDescription(JSONObject jsonRoot) {
        var descriptionTitle = makeText("Description");
        descriptionTitle.setUnderline(true);
        layoutRoot.getChildren().add(centerNode(descriptionTitle));
        var description = makeText(jsonRoot.getString("description"));
        layoutRoot.getChildren().add(centerNode(description));
    }

    private void writeTitle(JSONObject jsonRoot) {
        var str = jsonRoot.getString("name") + "\n";
        var text = makeText(str, 50);
        text.setUnderline(true);
        layoutRoot.getChildren().add(centerNode(text));
    }

    private void writeSteps(JSONObject jsonRoot) {
        var stepsTitle = makeText("Steps");
        stepsTitle.setUnderline(true);
        layoutRoot.getChildren().add(centerNode(stepsTitle));
        
        JSONArray stepsJson = jsonRoot.getJSONArray("steps");
        
        var stepsVBox = new VBox();
        stepsVBox.setPadding(new Insets(0, 0, 0, 120));

        for (int i = 0; i < stepsJson.length(); i++) { 
            var text = makeText("Step " + Integer.toString(i + 1) + ": " + stepsJson.getString(i) + "\n");
            stepsVBox.getChildren().add(text);
        }
        layoutRoot.getChildren().add(stepsVBox);
    }

    private void makeSubmitButton(TextField field) {
        var submitButton = new Button("Execute");
        submitButton.setOnMouseClicked(event -> {
            var input = field.getText();
            var numStrs = input.split(" ");
        });
        layoutRoot.getChildren().add(submitButton);
    }

    private void makeListInput() {
        //create input field
        var listInput = new TextField("Enter a list of numbers separated by spaces");
        setSize(listInput, 250, 30);
        layoutRoot.getChildren().addAll(centerNode(listInput));
        makeSubmitButton(listInput);
    }

    AlgorithmPage(String jsonPath) {
        layoutRoot = new VBox();
       
        try {
            String fileStr = Files.readString(Paths.get(jsonPath)); 
            JSONObject jsonRoot = new JSONObject(fileStr);
            writeTitle(jsonRoot);
            writeDescription(jsonRoot);
            writeSteps(jsonRoot);
            makeListInput();
        } catch (Exception e) { //FileIOException or JSONException
            System.out.println(System.getProperty("user.dir"));
            e.printStackTrace();
        }
    }

    public void run(Stage stage) {
        stage.setFullScreen(true);
        
        var scene = new Scene(layoutRoot);
        stage.setScene(scene);
        stage.show();
    }
}
