package ar.edu.itba.relif.application.view;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Title extends Text {
    public Title(String text) {
        super(text);
        setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 13));
    }
}
