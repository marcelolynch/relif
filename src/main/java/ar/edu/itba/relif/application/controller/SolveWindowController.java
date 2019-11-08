package ar.edu.itba.relif.application.controller;

import ar.edu.itba.relif.application.view.CompositionValueFactory;
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

public class SolveWindowController implements Controller {

    private static final String REPRESENTATION_WINDOW_FXML_PATH = "fxml/representation.fxml";
    @FXML
    public TableView atomTable;

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
        TableColumn<String, String> nameColumn = new TableColumn<>(" ; ");
        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue()));
        atomTable.getColumns().add(nameColumn);

        List<String> sortedAtoms = new ArrayList<>();
        sortedAtoms.addAll(rs.getIdentities());
        sortedAtoms.addAll(rs.getSymmetrics());
        sortedAtoms.addAll(rs.getAsymmetrics());

        rs.getAtoms().forEach(System.out::print);

        for (String atom: sortedAtoms) {
            TableColumn<String, String> col = new TableColumn<>(atom);
            col.setCellValueFactory(new CompositionValueFactory(rs, atom));
            col.setSortable(false);
            atomTable.getColumns().add(col);
        }

        for (String atom: sortedAtoms) {
            atomTable.getItems().add(atom);
        }

        Text title = new Text("Relations: ");
        title.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 13));
        userRelations.getChildren().add(title);


        for(Map.Entry<String, List<String>> e : rs.getUserRelations().entrySet()) {
            Text text = new Text(userRelation(e.getKey(), e.getValue()));
            text.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            userRelations.getChildren().add(new Text("   "));
            userRelations.getChildren().add(text);
            userRelations.getChildren().add(new Text("   "));
        }

        current = rs;
    }

    private String userRelation(String key, List<String> value) {
        StringBuilder sb = new StringBuilder(key);
        sb.append(" = { ");
        for(String c: value) {
            sb.append(c);
            sb.append(" ");
        }
        sb.append('}');
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
