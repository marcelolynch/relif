package ar.edu.itba.relif.application;

import ar.edu.itba.relif.application.controller.Controller;
import ar.edu.itba.relif.application.view.ViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class RelifApplication extends Application {

    private final static String FXML_PATH = "fxml/application.fxml";
    private final static double HEIGHT = 700;
    private final static double WIDTH = 800;

    @Override
    public void start(final Stage primaryStage) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        final ViewLoader loader = new ViewLoader(FXML_PATH);
        final Scene scene = loader.getScene();
        final Controller ctrl = loader.getController();
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style/highlighting.css").toExternalForm());

        ctrl.setStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
