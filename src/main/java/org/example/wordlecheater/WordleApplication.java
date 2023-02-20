package org.example.wordlecheater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// The functional entry point for the entire application.
// Not technically the entry point, but it doesn't really matter because the true entry point just calls this.
public class WordleApplication extends Application {
    // Overriding the start function of the application class to set the UI up
    @Override
    public void start(Stage stage) throws IOException {
        // Create a new backend instance to share around the application
        Backend backend = new Backend();

        FXMLLoader loader = new FXMLLoader(WordleApplication.class.getResource("/org.example.wordlecheater/main.fxml"));

        loader.setControllerFactory(Helper.getControllerFactory(backend));

        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Wordle Cheater");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

