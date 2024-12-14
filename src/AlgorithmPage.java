package src;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Supplier;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class AlgorithmPage {
    public static String getPath(String filename) {
        return "algorithm_data/" + filename;
    }
    
    private VBox layoutRoot;
    private Group algorithmSection;
    private ArrayList<Supplier<String>> paramValues;
    private AlgorithmGenerator algorithm;
    private AlgorithmGenerator algorithmGenerator;
    private volatile AlgorithmStepList algorithmStepList;

    private void showAlgorithm() {
        var timeline = new Timeline(
            new KeyFrame(Duration.millis(50), event -> {
                if (!algorithmStepList.isEmpty()) {
                    algorithmStepList.run();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

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

    private void makeListInput(HBox inputParamsBox) {
        //create input field
        var listInput = new TextField("Enter a list of numbers separated by spaces");
        setSize(listInput, 250, 30);
        inputParamsBox.getChildren().add(listInput);
        paramValues.add(() -> { return listInput.getText(); });
    }

    private ComboBox<String> makeDropdown(JSONArray values) {
        var dropdown = new ComboBox<String>();
        for (int i = 0; i < values.length(); i++) {
            dropdown.getItems().add(values.getString(i));
        }
        dropdown.setValue(values.getString(0));
        paramValues.add(() -> { return dropdown.getValue(); });
        return dropdown;
    }

    private TextField makeNumberInput() {
        var input = new TextField();
        
        //restrict input to only numbers
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            input.setText(newValue.replaceAll("[^0-9]+", ""));
        });
        paramValues.add(() -> { return input.getText(); });

        return input;
    }

    private void makeAlgorithmParams(JSONObject jsonRoot, HBox inputParamsBox) {
        System.out.println("Adding parameters...");
        var params = jsonRoot.getJSONArray("params");
        for (int i = 0; i < params.length(); i++) {
            var param = params.getJSONObject(i);
            String type = param.getString("type");
            if (type.equals("dropdown")) {
                inputParamsBox.getChildren().add(makeDropdown(param.getJSONArray("values")));
            } else {
               inputParamsBox.getChildren().add(makeNumberInput());
            }
        }
    }

    private void makeSubmitButton() {
        var submitButton = new Button("Execute");
        submitButton.setOnMouseClicked(event -> {
            var args = new ArrayList<String>();
            for (var input : paramValues) {
                args.add(input.get());
            }
            
            algorithmStepList = algorithmGenerator.generate(algorithmSection, args);
            
            submitButton.setDisable(true);
        });
        layoutRoot.getChildren().add(centerNode(submitButton));
    }

    private void makeAlgorithmForm(JSONObject jsonRoot) {
        var inputParamsBox = new HBox();

        makeListInput(inputParamsBox);
        makeAlgorithmParams(jsonRoot, inputParamsBox);

        layoutRoot.getChildren().add(inputParamsBox);
        inputParamsBox.setAlignment(Pos.CENTER);
    }

    AlgorithmPage(String jsonPath, AlgorithmGenerator algorithmGenerator) {
        layoutRoot = new VBox();
        algorithmSection = new Group();
        algorithmStepList = new AlgorithmStepList();

        paramValues = new ArrayList<Supplier<String>>();
        this.algorithmGenerator = algorithmGenerator;
        
        try {
            String fileStr = Files.readString(Paths.get(getPath(jsonPath))); 
            JSONObject jsonRoot = new JSONObject(fileStr);

            //create scene elements
            writeTitle(jsonRoot);
            writeDescription(jsonRoot);
            writeSteps(jsonRoot);
            makeAlgorithmForm(jsonRoot);
            makeSubmitButton();

            layoutRoot.getChildren().add(algorithmSection);
            VBox.setMargin(algorithmSection, new Insets(50, 0, 0, 0));

            showAlgorithm();
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
