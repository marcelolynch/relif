package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.application.view.NumericField;
import ar.edu.itba.relif.core.RelifSolution;
import ar.edu.itba.relif.core.Representation;
import ar.edu.itba.relif.core.RepresentationFinder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.Options;
import kodkod.engine.satlab.SATFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RepresentationWindowController implements Controller {
    @FXML
    public HBox controls;

    @FXML
    public VBox representations;


    private Stage stage;
    private RelifSolution solution;

    public RepresentationWindowController() {}


    private void tryFind(int bound) {
        representations.getChildren().clear();
        RepresentationFinder finder = new RepresentationFinder(solution, bound);
        Iterator<Representation> all = finder.findAll();

        if(!all.hasNext()) {
            System.out.println("No solution");
        }

        if(all.hasNext()) {
            Representation next = all.next();

            representations.getChildren().add(new Text("===== REPRESENTATION: ========"));
            representations.getChildren().add(new Text("===== BACKING SET ========"));
            representations.getChildren().add(new Text(reprToText("X", next.getBackingSet())));

            representations.getChildren().add(new Text("===== ATOMS ========"));
            for(Pair<String, List<Pair<String, String>>> e: next.getAtoms()) {
                representations.getChildren().add(new Text(reprToText(e.getKey(), e.getValue())));
            }

            representations.getChildren().add(new Text("===== USER DEFINED RELATIONS ========"));

            for(Pair<String, List<Pair<String, String>>> e: next.getUserDefinedRelations()) {
                representations.getChildren().add(new Text(reprToText(e.getKey(), e.getValue())));
            }

        }
    }

    private String reprToText(String name, List<Pair<String, String>> values) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = {");
        for(Pair<String, String> p: values) {
            sb.append("(");
            sb.append(p.getKey());
            sb.append(",");
            sb.append(p.getValue());
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

        Button findButton = new Button(" Find");

        controls.getChildren().add(new Text("Bound: "));
        controls.getChildren().add(numericField);
        controls.getChildren().add(findButton);

        findButton.setOnAction(e -> {
            tryFind(numericField.getIntValue());
        });
    }


    public void setSolution(RelifSolution solution) {
        this.solution = solution;
    }




}
