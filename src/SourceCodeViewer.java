package src;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.fxmisc.richtext.Selection;
import org.fxmisc.richtext.SelectionImpl;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import org.fxmisc.richtext.model.TwoDimensional.Bias;

public class SourceCodeViewer {
    private static int HEIGHT = 700;

    private static class Interval {
        public int first;
        public int second;
        public Interval(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true; 
            }
            if (other == null || getClass() != other.getClass()) {
                return false; 
            }
            var otherBounds = (Interval) other;
            return otherBounds.first == first && otherBounds.second == second;
        }

        @Override
        public String toString() {
            return Integer.toString(first) + Integer.toString(second);
        }
    }

    private static Integer selectionIDCount = Integer.MIN_VALUE;

    private static class HighlightableArea extends InlineCssTextArea {
        private static ArrayList<Interval> parseLineBounds(JSONArray IntervalJson) {
            var Interval = new ArrayList<Interval>();
            for (int i = 0; i < IntervalJson.length(); i++) {
                var j = IntervalJson.getJSONObject(i);
                Interval.add(new Interval(j.getInt("start"), j.getInt("end")));
            }
            return Interval;
        }

        private static ArrayList<Integer> makeLineBeginIndices(String text) {
            var ret = new ArrayList<Integer>();
            
            int charIdx = 0;
            var newLine = System.getProperty("os.name").startsWith("Windows") ? "\r\n" : "\n";
    
            var lines = text.split(newLine);
            
            for (var line : lines) {
                ret.add(charIdx);
                charIdx += line.length() + 1;
            }
    
            return ret;
        }

        private ArrayList<Interval> highlightableLineBounds;
        private ArrayList<Integer> lineBeginIndices;
        private HashMap<Interval, Selection<String, String, String>> currPermanentHighlights;
        private Selection<String, String, String> currTempHighlight;

        private void generatePossiblePermanentHighlights() {
            for (var lineBounds : highlightableLineBounds) {
                var begin = lineBeginIndices.get(lineBounds.first);
                var end = lineBeginIndices.get(lineBounds.second);
                setStyle(begin, end, "-fx-font-weight: bold");
                var newSelection = new SelectionImpl<>(lineBounds.toString(), this, path -> {
                    path.setStrokeWidth(0);
                    path.setFill(Color.LIGHTBLUE);
                });
                addSelection(newSelection);
                currPermanentHighlights.put(new Interval(begin, end), newSelection);
            }
        }

        private Optional<Interval> getHoveredInterval(int charIdx) {
            var lineIdx = offsetToPosition(charIdx, Bias.Backward).getMajor();
            return highlightableLineBounds.stream().filter((bounds) -> {
                return bounds.first <= lineIdx && bounds.second > lineIdx;
            }).findFirst();
        }

        private void setupReactiveTemporaryHighlighting() {
            setMouseOverTextDelay(Duration.ofMillis(1));
            
            addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
                var charIdx = event.getCharacterIndex();
                
                //find line that is hovered over
                var hoveredInterval = getHoveredInterval(charIdx);
                
                if (hoveredInterval.isEmpty()) {
                    currTempHighlight.deselect();
                    return;
                }

                var hoveredIntervalV = hoveredInterval.get();
                currTempHighlight.selectRange(
                    lineBeginIndices.get(hoveredIntervalV.first), lineBeginIndices.get(hoveredIntervalV.second)
                );
            });
        }

        private void setupReactivePermanentHighlighting() {
            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                var currTempHighlightRange = currTempHighlight.getRange();
                var start = currTempHighlightRange.getStart();
                var end = currTempHighlightRange.getEnd();

                var interval = new Interval(start, end);
                if (!currPermanentHighlights.containsKey(interval)) {
                    return;
                }
                
                var permanentHighlight = currPermanentHighlights.get(interval);

                //if we are clicking on something that is already selected, deselect it
                if (permanentHighlight.getRange().equals(currTempHighlightRange)) {
                    permanentHighlight.deselect();
                } else {
                    currTempHighlight.deselect();
                    permanentHighlight.selectRange(start, end);
                }
            });
        }

        public HighlightableArea(String cppFilePath, JSONArray stepIntervals) throws IOException {
            super();

            var cppFileText = Files.readString(Paths.get(cppFilePath));

            highlightableLineBounds = parseLineBounds(stepIntervals);
            lineBeginIndices = makeLineBeginIndices(cppFileText);
            currPermanentHighlights = new HashMap<>();
            currTempHighlight = new SelectionImpl<>("curr_highlight", this,
                path -> {
                    path.setStrokeWidth(0);
                    path.setFill(Color.GREEN);
                }
            );
            
            addSelection(currTempHighlight);
            setParagraphGraphicFactory(LineNumberFactory.get(this));
            replaceText(cppFileText);
            setStyle("-fx-font-size: 20px;");
            setEditable(false);

            generatePossiblePermanentHighlights();
            setupReactiveTemporaryHighlighting();
            setupReactivePermanentHighlighting();
        }
    }

    public static HBox generate(String cppFilePath, JSONArray stepIntervals) throws IOException {
        var layoutRoot = new HBox();
        layoutRoot.setPadding(new Insets(0, 0, 0, 120));
        
        var area = new HighlightableArea(cppFilePath, stepIntervals);
        LayoutUtil.setSize(area, 900, 700);
        
        layoutRoot.getChildren().add(area);

        return layoutRoot;
    }
}
