package org.example.wordlecheater;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// This class is used to control the dialog that creates a new worker.
public class AddDialogController {
    // A reference to our backend instance
    private final Backend backend;

    public VBox inputBox;

    // All the types of workers available
    ObservableList<String> workerTypes = FXCollections.observableArrayList("New York Times", "Wordle Unlimited");

    // Fields to fill out before creating a worker

    public TextField workerName;

    // See workerTypes
    public ChoiceBox<String> workerType;

    // Should the bot keep solving the puzzle on loop?
    public CheckBox repeat;

    // Start the bot immediately after creating the worker?
    public CheckBox startOnCreate;

    // A constructor passing in our backend
    public AddDialogController(Backend backend) {
        this.backend = backend;
    }

    // Set up UI for user interaction
    @FXML
    private void initialize() {
        workerType.setItems(workerTypes);
        workerType.setValue(workerTypes.get(0));
    }

    // When the user clicks the add button
    @FXML
    public void onAdd(ActionEvent event) {
        closeStage(event);

        // Create a new wordle instance
        Wordle newWordle;

        // Decide on a worker type based on the selection, otherwise fall back to WordleUnlimited
        if (workerTypes.get(0).equalsIgnoreCase(workerType.getValue())) newWordle = new NyTimes();
        else if (workerTypes.get(1).equalsIgnoreCase(workerType.getValue())) newWordle = new WordleUnlimited();
        else newWordle = new WordleUnlimited();

        // Create the new worker
        Worker newWorker = new Worker(backend, newWordle, workerName.getText(), repeat.isSelected());

        // Add the worker to the backend
        backend.addWorker(newWorker);

        if (startOnCreate.isSelected()) newWorker.startWorker();
    }

    // Close popup window
    public void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}
