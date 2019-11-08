package ar.edu.itba.relif.application.view;

import javafx.scene.control.TextField;

public class NumericField extends TextField {
    public NumericField() {
        /*DecimalFormat format = new DecimalFormat("0");

        setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty())
                return c;

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length())
                return null;
            else
                return c;
        })); */
    }

    public double getDoubleValue() {
        String value = getText();
        if(value.isEmpty()) {
            setValue(0);
            return 0;
        }

        return Double.valueOf(value.replace(',', '.'));
    }

    public int getIntValue() {
        return (int) getDoubleValue();
    }

    public void setValue(double value) {
        textProperty().setValue(String.valueOf(value));
    }
}
