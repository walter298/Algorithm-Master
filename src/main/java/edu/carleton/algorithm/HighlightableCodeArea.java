package edu.carleton.algorithm;

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

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import org.fxmisc.richtext.Selection;
import org.fxmisc.richtext.SelectionImpl;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;

public class HighlightableCodeArea extends InlineCssTextArea {
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
    private Selection<String, String, String> specifiedSelection;
    private boolean clickedOnSpecifiedSelection = false;

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
        setMouseOverTextDelay(Duration.ofMillis(1)); //if you don't call this function, there is infinite delay
        
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
        // addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, event -> {
        //     currTempHighlight.deselect();
        // });
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

            if (specifiedSelection.getRange().equals(currTempHighlightRange)) {
                clickedOnSpecifiedSelection = true;
                specifiedSelection.deselect();
                permanentHighlight.deselect();
            } else if (permanentHighlight.getRange().equals(currTempHighlightRange)) {
                permanentHighlight.deselect();
                currTempHighlight.deselect(); //change
            } else {
                currTempHighlight.deselect();
                permanentHighlight.selectRange(start, end);
            }
        });
    }

    public HighlightableCodeArea(String cppFilePath, JSONArray stepIntervals) throws IOException {
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
        specifiedSelection = new SelectionImpl<>("specified_selection", this,
            path -> {
                path.setStrokeWidth(0);
                path.setFill(Color.PINK);
            }
        );
        
        if (!addSelection(specifiedSelection)) {
            System.out.println("Error: could not add specifiedSelection");
            System.exit(-1);
        }
        if (!addSelection(currTempHighlight)) {
            System.out.println("Error: could not add currTempHighlight");
            System.exit(-1);
        }
        
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        replaceText(cppFileText);
        setStyle("-fx-font-size: 20px;");
        setEditable(false);

        generatePossiblePermanentHighlights();
        setupReactiveTemporaryHighlighting();
        setupReactivePermanentHighlighting();
    }

    private Optional<Interval> getCharInterval(int firstLine, int secondLine) {
        if (firstLine >= lineBeginIndices.size() || secondLine >= lineBeginIndices.size()) {
            return Optional.empty();
        }

        var charInterval = new Interval(
            lineBeginIndices.get(firstLine), 
            lineBeginIndices.get(secondLine)
        );
        return Optional.of(charInterval);
    }

    //[firstLine, secondLine)
    public boolean hasBreakpoint(int firstLine, int secondLine) {
        var charInterval = getCharInterval(firstLine, secondLine);
        if (charInterval.isEmpty()) {
            return false;
        }
        if (!currPermanentHighlights.containsKey(charInterval.get())) {
            return false;
        }

        return currPermanentHighlights.get(charInterval.get()).getRange().getLength() != 0;
    }

    public void overlayOverPermanentHighlight(int firstLine, int secondLine) {
        System.out.println("Highlighting: " + firstLine + ", " + secondLine);

        var charInterval = getCharInterval(firstLine, secondLine);
        if (charInterval.isEmpty()) {
            System.out.println("Error: not hoverable: " + firstLine + ", " + secondLine);
            return;
        }
        var charIntervalV = charInterval.get();
        var permanentHighlight = currPermanentHighlights.get(charIntervalV);
        permanentHighlight.deselect();
        //currTempHighlight.selectRange(charIntervalV.first, charIntervalV.second);
        specifiedSelection.selectRange(charIntervalV.first, charIntervalV.second);
    }

    public void deoverlayOverPermanentHighlight() {
        if (clickedOnSpecifiedSelection) {
            clickedOnSpecifiedSelection = false;
            return; 
        }

        int charBegin = specifiedSelection.getRange().getStart();
        int charEnd = specifiedSelection.getRange().getEnd();
        specifiedSelection.deselect();

        if (!currPermanentHighlights.containsKey(new Interval(charBegin, charEnd))) {
            System.out.println("Error: " + charBegin + " and " + charEnd + " not contained");
            for (var kv : currPermanentHighlights.entrySet()) {
                var interval = kv.getKey();
                System.out.println(interval.first + ", " + interval.second);
            }
            System.exit(-1);
        }
        var permanentHighlight = currPermanentHighlights.get(new Interval(charBegin, charEnd));
        permanentHighlight.selectRange(charBegin, charEnd);

        currTempHighlight.deselect();
    }
}