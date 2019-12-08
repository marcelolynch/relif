package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.application.view.CompositionValueFactory;
import ar.edu.itba.relif.application.view.Title;
import ar.edu.itba.relif.application.view.ViewLoader;
import ar.edu.itba.relif.core.RelifSolution;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;

public class SolveWindowController implements Controller {

    private static final String REPRESENTATION_WINDOW_FXML_PATH = "fxml/representation.fxml";

    @FXML
    public TableView atomTable;

    @FXML
    public TableView converseTable;


    @FXML
    public VBox userRelations;
    private Stage stage;
    private Iterator<RelifSolution> solutions;
    private List<RelifSolution> history;
    private RelifSolution current = null;

    public SolveWindowController() {
        history = new ArrayList<>();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getScene().getStylesheets()
                .add(getClass().getClassLoader()
                        .getResource("style/tables2.css").toExternalForm());
        stage.setHeight(400);

        atomTable.setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
        converseTable.setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
    }

    public void doFindNext(ActionEvent actionEvent) {
        nextSolution();
    }

    private void nextSolution() {
        if (solutions.hasNext()) {
            RelifSolution next = solutions.next();
            history.add(next);
            updateView(next);
        } else {
            emptyTable();
        }
    }

    private void emptyTable() {
        atomTable.getColumns().clear();
        atomTable.getItems().clear();
        converseTable.getColumns().clear();
        converseTable.getItems().clear();
        userRelations.getChildren().clear();
        current = null;
    }

    public void setSolution(Iterator<RelifSolution> solutions) {
        history.clear();
        this.solutions = solutions;
        nextSolution();
    }

    private void updateView(RelifSolution rs) {
        emptyTable();

        atomTable.setPrefHeight(32 * (rs.getAtoms().size() + 1));
        int tableLength = rs.getCycles().values().stream().mapToInt(m -> m.values().stream().mapToInt(l -> l.size() + 2).max().orElse(1)).sum();
        atomTable.setPrefWidth(40 + 15 * tableLength);

        converseTable.setPrefHeight(30 * (rs.getAtoms().size() + 1));

        // Atom tble
        TableColumn<String, String> nameColumn = new TableColumn<>(";");
        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue()));
        nameColumn.getStyleClass().add("name-column");
        nameColumn.setSortable(false);
        atomTable.getColumns().add(nameColumn);

        List<String> sortedAtoms = new ArrayList<>();
        sortedAtoms.addAll(rs.getIdentities());
        sortedAtoms.addAll(rs.getSymmetrics());
        sortedAtoms.addAll(rs.getAsymmetrics());

        for (String atom: sortedAtoms) {
            TableColumn<String, String> col = new TableColumn<>(atom);
            col.setCellValueFactory(new CompositionValueFactory(rs, atom));
            col.setSortable(false);
            atomTable.getColumns().add(col);
        }

        for (String atom: sortedAtoms) {
            atomTable.getItems().add(atom);
        }

        atomTable.refresh();



        // Converse table
        TableColumn<String, String> atomColumn = new TableColumn<>("Atom");
        atomColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue()));
        atomColumn.setSortable(false);

        TableColumn<String, String> converseColumn = new TableColumn<>("Converse");
        converseColumn.getStyleClass().add("converse-table");
        converseColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(rs.getConverse().getOrDefault(p.getValue(), "")));
        converseColumn.setSortable(false);

        converseTable.getColumns().add(atomColumn);
        converseTable.getColumns().add(converseColumn);
        converseTable.getItems().addAll(sortedAtoms);

        converseTable.refresh();

        if (!rs.getUserRelations().isEmpty()) {
            Text title = new Title("User defined relations: ");
            userRelations.getChildren().add(title);
            userRelations.getChildren().add(new Text("   "));
            for (Map.Entry<String, List<String>> e : rs.getUserRelations().entrySet()) {
                Text text = new Text(userRelationToText(e.getKey(), e.getValue()));
                text.setFont(Font.font("Lucida Console", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                userRelations.getChildren().add(text);
            }
        }
        current = rs;
    }

    private String userRelationToText(String name, List<String> atoms) {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" =  ");

        for (int i = 0; i < atoms.size() - 1; i++) {
            sb.append(atoms.get(i));
            sb.append(" + ");
        }

        if (atoms.size() > 0) {
            sb.append(atoms.get(atoms.size() - 1));
        }

        return sb.toString();
    }

    public void doFindRepresentation(ActionEvent actionEvent) {
        if (current != null) {
            ViewLoader loader = new ViewLoader(REPRESENTATION_WINDOW_FXML_PATH);

            final Scene scene = loader.getScene();
            final RepresentationWindowController ctrl = loader.getController();
            ctrl.setSolution(current);
            Stage stage = new Stage();
            stage.setScene(scene);
            ctrl.setStage(stage);
            stage.show();
        }
    }
}
