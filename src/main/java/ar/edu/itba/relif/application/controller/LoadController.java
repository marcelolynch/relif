package ar.edu.itba.relif.application.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.Optional;

public class LoadController {

    private FileChooser fileChooser;
    private Stage stage;

    public LoadController(Stage stage) {
        this.stage = stage;
        this.fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Relif specifications", "*.rls"),
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
    }

    public Optional<Pair<File, String>> load() {
        File file = fileChooser.showOpenDialog(stage);

        if (file == null)
            return Optional.empty();

        String contents = readFile(file);
        return Optional.of(new Pair<File, String>(file, contents));
    }

    private String readFile(File file) {
        try {
            return tryReadFile(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String tryReadFile(File file) throws FileNotFoundException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.lines().forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();
    }

}

