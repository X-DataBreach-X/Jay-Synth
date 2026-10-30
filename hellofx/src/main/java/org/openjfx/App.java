package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

/**
 * JavaFX App
 */
public class App extends Application {

    private final Oscillator oscillator = new Oscillator();

    @Override
    public void start(Stage stage) {
        WaveSelector waveSelector = new WaveSelector();
        waveSelector.setOnWaveformChanged(oscillator::setWaveform);

        VBox root = new VBox(20, waveSelector);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 640, 480);

        KeyboardController keyboard = new KeyboardController(oscillator);
        keyboard.attach(scene);

        // If the window loses focus while a key is held, we'd never hear the release
        stage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                keyboard.allNotesOff();
            }
        });

        try {
            oscillator.start();
        } catch (LineUnavailableException ex) {
            System.err.println("Could not open audio output: " + ex.getMessage());
        }

        stage.setTitle("Jay-Synth");
        stage.setScene(scene);
        stage.show();
    }

    // Called by JavaFX when the window closes
    @Override
    public void stop() {
        oscillator.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}
