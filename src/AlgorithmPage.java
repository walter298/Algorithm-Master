package src;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AlgorithmPage {
    public static String getAlgorithmDirectory(String algorithmName) {
        return "algorithm_data/" + algorithmName + "/";
    }
    
    private Scene scene;
    private ScrollPane scrollPane;
    private VBox layoutRoot;
    private ListUI currInput;
    private HBox algorithmSection;
    private Button submitButton;
    private HBox watchWindowSection;
    private ArrayList<Supplier<String>> paramValues;
    private AlgorithmGenerator algorithmGenerator;
    private volatile AlgorithmStepList algorithmStepList;

    private void showAlgorithm() {
        var timeline = new Timeline(
            new KeyFrame(Duration.millis(50), event -> {
                if (!algorithmStepList.isEmpty() && algorithmStepList.run()) {
                    var newWatchWindow = algorithmStepList.getVariableWindow();
                    if (newWatchWindow.isPresent()) {
                        watchWindowSection.getChildren().clear();
                        watchWindowSection.getChildren().add(newWatchWindow.get());
                    }
                } else {
                    submitButton.setDisable(false);
                    scrollPane.setDisable(false);
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private Text makeText(String str, double wrapping, int fontSize) {
        var text = new Text(str);
        text.setFont(new Font(fontSize));
        return text;
    }

    private Text makeText(Region r, String str) {
        final int FONT_SIZE = 21;
        return makeText(str, Double.MAX_VALUE, FONT_SIZE);
    }

    private Button makeBackButton(Stage stage, Scene homepage) {
        var button = new Button("Home");
        button.setOnMouseClicked(event -> {
            currInput.clear(algorithmSection);
            stage.setScene(homepage);
        });
        return button;
    }

    private void writeTitle(JSONObject jsonRoot) {
        var text = new Text(jsonRoot.getString("name"));
        text.setFont(new Font(50));
        layoutRoot.getChildren().add(LayoutUtil.centerNode(text));
    }

    private void writeDescription(JSONObject jsonRoot) {
        var textFlow = new TextFlow();
        textFlow.setPadding(new Insets(0, 0, 0, 120));
        textFlow.prefWidthProperty().bind(scrollPane.viewportBoundsProperty().map(bounds -> bounds.getWidth()));

        var description = new Text(jsonRoot.getString("description"));
        description.setFont(new Font(23));
        textFlow.getChildren().addAll(description);
        layoutRoot.getChildren().add(textFlow);
    }

    private static final int INPUT_HEIGHT = 50;

    private Optional<ArrayList<Integer>> parseIntegerInput(String inputStr) {
        try {
            var intStrs = inputStr.split("\\D+");
            var nums = new ArrayList<Integer>();
            for (var intStr : intStrs) {
                nums.add(Integer.parseInt(intStr));
            }
            return Optional.of(nums);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void reparseCurrNumInput(String inputStr) {
        var nums = parseIntegerInput(inputStr);
        currInput.clear(algorithmSection);
        if (!nums.isEmpty()) {
            currInput = new ListUI(algorithmSection, nums.get());
        }
    }

    private void makeListInput(HBox inputParamsBox) {
        //create input field
        var listInput = new TextField();
        listInput.setPromptText("Enter a list of numbers");

        LayoutUtil.setSize(listInput, 350, INPUT_HEIGHT);
        listInput.setFont(new Font(23));

        listInput.textProperty().addListener((observable, oldValue, newValue) -> {
            reparseCurrNumInput(newValue);
        });

        inputParamsBox.getChildren().add(listInput);
    }

    private ComboBox<String> makeDropdown(JSONArray values) {
        var dropdown = new ComboBox<String>();
        for (int i = 0; i < values.length(); i++) {
            dropdown.getItems().add(values.getString(i));
        }
        dropdown.setValue(values.getString(0));
        LayoutUtil.setSize(dropdown, 115, INPUT_HEIGHT);
        paramValues.add(() -> { return dropdown.getValue(); });
        return dropdown;
    }

    private TextField makeNumberInput() {
        var input = new TextField();

        LayoutUtil.setSize(input, 75, INPUT_HEIGHT);

        //restrict input to only numbers
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            input.setText(newValue.replaceAll("[^0-9]+", ""));
        });

        paramValues.add(() -> { return input.getText(); });

        return input;
    }

    private void makeAlgorithmParams(JSONObject jsonRoot, HBox inputParamsBox) {
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

    private void makeSubmitButton(HBox inputParamsBox) {
        submitButton = new Button("Execute");
        LayoutUtil.setSize(submitButton, 140, INPUT_HEIGHT);
        submitButton.setOnMouseClicked(event -> {
            var args = new ArrayList<String>();
            for (var input : paramValues) {
                args.add(input.get());
            }
            
            algorithmStepList = algorithmGenerator.generate(currInput, currInput.toIntegers(), args);
            algorithmSection.requestFocus(); //prevent automatic scrolling down when numbers move up
            submitButton.setDisable(true);
        });
        inputParamsBox.getChildren().add(submitButton);
    }

    private void makeAlgorithmForm(JSONObject jsonRoot) {
        var inputParamsBox = new HBox();
        inputParamsBox.setPadding(new Insets(40, 0, 0, 120));

        makeListInput(inputParamsBox);
        makeAlgorithmParams(jsonRoot, inputParamsBox);
        makeSubmitButton(inputParamsBox);

        layoutRoot.getChildren().add(inputParamsBox);
    }

    private void writeSteps(JSONObject jsonRoot) {
        var stepsTitle = makeText(layoutRoot, "Steps");
        stepsTitle.setUnderline(true);
        layoutRoot.getChildren().add(LayoutUtil.centerNode(stepsTitle));
        
        JSONArray stepsJson = jsonRoot.getJSONArray("steps");
        
        var stepsVBox = new VBox();
        stepsVBox.setPadding(new Insets(0, 0, 0, 120));

        for (int i = 0; i < stepsJson.length(); i++) { 
            var text = makeText(layoutRoot, "Step " + Integer.toString(i + 1) + ": " + stepsJson.getString(i) + "\n");
            stepsVBox.getChildren().add(text);
        }
        layoutRoot.getChildren().add(stepsVBox);
    }

    private void viewSourceCode(VBox layoutRoot, String filePath) throws IOException {
        var content = Files.readString(Paths.get(filePath));
        var lines = content.split("\n");
        var lineCountStr = Integer.toString(lines.length);

        var numberedLines = new String();

        for (int i = 0; i < lines.length; i++) {
            var idxStr = new StringBuilder(Integer.toString(i));
            for (int j = 0; j < lineCountStr.length() - idxStr.length(); j++) {
                idxStr.append(" ");
            }
            
            numberedLines = numberedLines + "\n" + idxStr.toString()  + "   " + lines[i];
        }
        
        var textArea = new TextArea(numberedLines);
        textArea.setEditable(false);

        //make text area have black background and white text
        textArea.setStyle("-fx-control-inner-background: black;");
        
        LayoutUtil.setSize(textArea, 700, 700);
        
        var hbox = new HBox();
        hbox.setPadding(new Insets(0, 0, 0, 120));
        hbox.getChildren().add(textArea);

        layoutRoot.getChildren().add(hbox);
    }

    AlgorithmPage(String algorithmName, AlgorithmGenerator algorithmGenerator, Stage stage, Scene homepage) throws Exception {
        //initialize data members
        layoutRoot = new VBox();
        scrollPane = new ScrollPane(layoutRoot);
        currInput = new ListUI();
        algorithmSection = new HBox(20); //20 is spacing between numbers
        algorithmSection.setAlignment(Pos.CENTER);
        algorithmStepList = new AlgorithmStepList();
        watchWindowSection = new HBox();
        watchWindowSection.setPadding(new Insets(0, 0, 0, 120));
        paramValues = new ArrayList<Supplier<String>>();
        this.algorithmGenerator = algorithmGenerator;
        
        String jsonFile = Files.readString(Paths.get(getAlgorithmDirectory(algorithmName) + "/" + algorithmName + ".json")); 
        JSONObject jsonRoot = new JSONObject(jsonFile);

        //create scene elements
        layoutRoot.getChildren().add(makeBackButton(stage, homepage));
        writeTitle(jsonRoot);
        writeDescription(jsonRoot);
        makeAlgorithmForm(jsonRoot);
        
        layoutRoot.getChildren().addAll(algorithmSection, watchWindowSection);

        scene = new Scene(scrollPane);

        showAlgorithm();

        viewSourceCode(layoutRoot, getAlgorithmDirectory(algorithmName) + "/" + algorithmName + ".hpp");
    }

    public void run(Stage stage) {
        stage.setScene(scene);
        stage.show();
    }
}
