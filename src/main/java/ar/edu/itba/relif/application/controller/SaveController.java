package ar.edu.itba.relif.application.controller;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;


public class SaveController implements Controller {

    private final FileChooser fileChooser;
    private Stage stage;

    public SaveController(Stage stage) {
        this.stage = stage;
        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Relif specification", "*.rls"),
                new ExtensionFilter("All Files", "*.*"));
    }

    public Optional<File> saveAs(CodeArea codeArea) {
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            save(codeArea, file);
        }

        return Optional.ofNullable(file);
    }


    public void save(CodeArea codeArea, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(codeArea.getText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
