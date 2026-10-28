package org.openjfx;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override 
    public void start(Stage stage) {
        StackPane root = new StackPane(new WaveSelector());
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Jay-Synth");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }

}