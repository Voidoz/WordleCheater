package org.example.wordlecheater;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

// Entry point for the UI part of the application
public class MainController extends FXMLController {
    // Variables for UI elements
    public TableView<Worker> workerTable;
    public TableColumn<Worker, String> nameColumn;
    public TableColumn<Worker, String> statusColumn;
    public TableColumn<Worker, String> progressColumn;
    public TableColumn<Worker, String > winsColumn;
    public TableColumn<Worker, String> lossesColumn;

    public MainController(Backend backend) {
        super(backend);

        // The function passed into the subscribe call will be run every time backend.updateWorkers() is called
        backend.getWorker().subscribe(workers -> {
            workerTable.getItems().setAll(workers);
        });
    }

    public void initialize() {
        // Define a function to run when a row is created in order to build it
        workerTable.setRowFactory(tableView -> {
            final TableRow<Worker> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem startItem = new MenuItem("Start");

            startItem.setOnAction(e -> {
                row.getItem().startWorker();
            });

            MenuItem stopItem = new MenuItem("Stop");

            stopItem.setOnAction(e -> {
                row.getItem().stopWorker();
            });

            CheckMenuItem repeatItem = new CheckMenuItem("Repeat");

            repeatItem.setOnAction(e -> {
                row.getItem().repeat = !row.getItem().repeat;
            });

            rowMenu.getItems().addAll(startItem, stopItem, repeatItem);

            row.setOnContextMenuRequested(e -> {
                startItem.setDisable(row.getItem().status == WorkerStatus.Running);

                stopItem.setDisable(row.getItem().status != WorkerStatus.Running);

                repeatItem.setSelected(row.getItem().repeat);
            });

            row.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY && row.getItem() != null) {
                    rowMenu.show(tableView, e.getScreenX(), e.getScreenY());
                }
            });

            row.setContextMenu(rowMenu);

            return row;
        });

        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        progressColumn.setCellValueFactory(cell -> new SimpleStringProperty("W.I.P"));

        winsColumn.setCellValueFactory(cell -> new SimpleStringProperty("W.I.P"));

        lossesColumn.setCellValueFactory(cell -> new SimpleStringProperty("W.I.P"));
    }

    // Function to open the add worker dialog
    @FXML
    public void onAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordleApplication.class.getResource("/org.example.wordlecheater/addDialog.fxml"));

        fxmlLoader.setControllerFactory(Helper.getControllerFactory(backend));

        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent, 350, 300);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
