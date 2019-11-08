package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.application.view.ViewLoader;
import ar.edu.itba.relif.core.iterator.RelifSolutionIterator;
import ar.edu.itba.relif.core.SolutionFinder;
import ar.edu.itba.relif.parser.SpecificationParser;
import ar.edu.itba.relif.parser.ast.Specification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.StringReader;
import java.util.Optional;

public class MainWindowController implements Controller {
    private static final String SOLVER_WINDOW_FXML_PATH = "fxml/solver.fxml";

    @FXML
    private CodeArea codeArea;

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
    private void handleOpen(ActionEvent actionEvent) {
        Optional<Pair<File, String>> openedFile = loadController.load();
        currentFile = openedFile.map(Pair::getKey).orElse(null);
        openedFile.ifPresent(c -> codeArea.replaceText(c.getValue()));
    }


    @FXML
    private void handleSave(ActionEvent actionEvent) {
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
    private void handleRun(ActionEvent actionEvent) {

        try {
            // TODO: Exceptions -- syntax errors
            Specification specification = SpecificationParser.getSpecification(new StringReader(codeArea.getText()));
            RelifSolutionIterator solutions = SolutionFinder.findSolutions(specification);
            ViewLoader loader = new ViewLoader(SOLVER_WINDOW_FXML_PATH);
            final Scene scene = loader.getScene();
            final SolveWindowController ctrl = loader.getController();
            ctrl.setSolution(solutions);
            Stage stage = new Stage();
            stage.setScene(scene);
            ctrl.setStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
