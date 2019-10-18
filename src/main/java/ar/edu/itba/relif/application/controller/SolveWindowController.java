package ar.edu.itba.relif.application.controller;

import javafx.stage.Stage;

public class SolveWindowController implements Controller {

    private Stage stage;

    public SolveWindowController() {}

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
