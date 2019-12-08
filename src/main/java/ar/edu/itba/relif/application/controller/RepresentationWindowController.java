package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.application.view.NumericField;
import ar.edu.itba.relif.application.view.Title;
import ar.edu.itba.relif.core.RelifSolution;
import ar.edu.itba.relif.core.Representation;
import ar.edu.itba.relif.core.RepresentationFinder;
import ar.edu.itba.relif.util.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.List;

public class RepresentationWindowController implements Controller {
    @FXML
    public HBox controls;

    @FXML
    public VBox representations;


    private Stage stage;
    private RelifSolution solution;

    private Iterator<Representation> current = null;
    private Button findNextButton;

    public RepresentationWindowController() {}


    private void tryFind(int bound) {
        representations.getChildren().clear();
        representations.getChildren().add(new Text("Searching..."));

        RepresentationFinder finder = new RepresentationFinder(solution, bound);
        current = finder.findAll();
        tryFindNext();
    }

    private void tryFindNext() {
        representations.getChildren().clear();
        representations.getChildren().add(new Text("Searching..."));

        if (current == null || !current.hasNext()) {
            representations.getChildren().clear();
            representations.getChildren().add(new Text("No solution"));
            findNextButton.setDisable(true);
            return;
        }

        representations.getChildren().clear();
        findNextButton.setDisable(false);
        Representation next = current.next();

        representations.getChildren().add(new Title("Base set:"));

        representations.getChildren().add(new Text(setToText("X", next.getBackingSet())));
        representations.getChildren().add(new Text(reprToText("Unit", next.getUnit())));

        representations.getChildren().add(new Text("\n"));
        representations.getChildren().add(new Title("Atoms:"));
        for (Pair<String, List<Pair<String, String>>> e : next.getAtoms()) {
            representations.getChildren().add(new Text(reprToText(e.fst(), e.snd())));
        }

        representations.getChildren().add(new Text("\n"));

        if (!next.getUserDefinedRelations().isEmpty()) {
            representations.getChildren().add(new Title("User defined relations:"));

            for (Pair<String, List<Pair<String, String>>> e : next.getUserDefinedRelations()) {
                representations.getChildren().add(new Text(reprToText(e.fst(), e.snd())));
            }
        }
    }


    private String setToText(String name, List<String> set) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = { ");
        for(String e: set) {
            sb.append(e);
            sb.append(" ");
        }
        sb.append("}");

        return sb.toString();    }

    private String reprToText(String name, List<Pair<String, String>> values) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = { ");
        for(Pair<String, String> p: values) {
            sb.append("(");
            sb.append(p.fst());
            sb.append(",");
            sb.append(p.snd());
            sb.append(") ");
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        NumericField numericField = new NumericField();
        numericField.setValue(3);

        numericField.promptTextProperty().setValue("Maximum number of elements in the backing set");


        controls.getChildren().add(new Text("Bound: "));
        controls.getChildren().add(numericField);

        Button findButton = new Button("Find");
        controls.getChildren().add(findButton);

        findNextButton = new Button("Find next");
        findNextButton.setDisable(true);
        controls.getChildren().add(findNextButton);


        findButton.setOnAction(e -> {
            tryFind(numericField.getIntValue());
        });

        findNextButton.setOnAction(e -> tryFindNext());
    }


    public void setSolution(RelifSolution solution) {
        this.solution = solution;
    }




}
