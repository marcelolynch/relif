package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.core.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainWindowController implements Controller {
    public CodeArea codeArea;

    private SaveController saveController;
    private LoadController loadController;
    private File currentFile;

    public MainWindowController() {

    }

    @Override
    public void setStage(Stage stage) {
        loadController = new LoadController(stage);
        saveController = new SaveController(stage);
    }

    @FXML
    public void handleOpen(ActionEvent actionEvent) {
        Optional<Pair<File, String>> openedFile = loadController.load();
        currentFile = openedFile.map(Pair::getKey).orElse(null);
        openedFile.ifPresent(c -> codeArea.replaceText(c.getValue()));
    }


    @FXML
    public void handleSave(ActionEvent actionEvent) {
        if (currentFile == null) {
            handleSaveAs(actionEvent);
        } else {
            saveController.save(codeArea, currentFile);
        }
    }

    @FXML
    private void handleSaveAs(ActionEvent actionEvent) {
        currentFile = saveController.saveAs(codeArea).orElse(null);
    }


    @FXML
    public void handleRun(ActionEvent actionEvent) {
       Main.run(codeArea.getText());
    }
}
