package ar.edu.itba.relif.application.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class ViewLoader {
    private FXMLLoader loader;
    private Scene scene;

    public ViewLoader(String path) {
        URL fxmlPath = getClass().getClassLoader().getResource(path);
        loader = new FXMLLoader(fxmlPath);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load " + fxmlPath);
        }

        scene = new Scene(loader.getRoot());
    }

    public Scene getScene() {
        return scene;
    }
    public <T> T getController() {
        return loader.getController();
    }
}
