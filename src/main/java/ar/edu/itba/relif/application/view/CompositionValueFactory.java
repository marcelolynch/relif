package ar.edu.itba.relif.application.view;

import ar.edu.itba.relif.core.RelifSolution;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositionValueFactory implements Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>> {
    private final Map<String, Map<String, List<String>>> cycles;
    private final String atom;

    public CompositionValueFactory(RelifSolution rs, String atom) {
        this.cycles = rs.getCycles();
        this.atom = atom;
    }

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<String, String> param) {
        String composedWith = param.getValue();
        List<String> inComposition = cycles.getOrDefault(atom, Collections.emptyMap()).getOrDefault(composedWith, Collections.emptyList());
        StringBuilder sb = new StringBuilder();
        for(String c: inComposition) {
            sb.append(c);
            sb.append(" ");
        }

        return new ReadOnlyObjectWrapper<>(sb.toString());
    }
}
