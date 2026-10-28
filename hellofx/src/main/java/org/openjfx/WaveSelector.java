package org.openjfx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class WaveSelector extends HBox {
    
    private Waveform waveform;
    private final Label nameLabel = new Label();

    public WaveSelector() {

        Button leftArrow = new Button("◀");
        Button rightArrow = new Button("▶");

        leftArrow.setOnAction(e -> setWaveform(waveform.previous()));
        rightArrow.setOnAction(e -> setWaveform(waveform.next()));

        // Fixed width so the box doesn't resize when the name changes
        nameLabel.setMinWidth(90);
        nameLabel.setAlignment(Pos.CENTER);

        getChildren().addAll(leftArrow, nameLabel, rightArrow);
        setAlignment(Pos.CENTER);
        setSpacing(10);

        // Stop the parent layout from stretching this to fill the window
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        // Styling: makes the whole thing look like one button
        setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 10; -fx-padding: 8 12;");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        String arrowStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand";
        leftArrow.setStyle(arrowStyle);
        rightArrow.setStyle(arrowStyle);

        setWaveform(Waveform.SINE);
    }

    public Waveform getWaveform() {
        return waveform;
    }

    public void setWaveform(Waveform waveform) {
        this.waveform = waveform;
        nameLabel.setText(waveform.getDisplayName());
    }
}