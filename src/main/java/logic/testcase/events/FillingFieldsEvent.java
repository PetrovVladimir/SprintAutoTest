package logic.testcase.events;

import application.ApplicationConfig;
import models.ElementWithFieldValue;
import models.ElementWithStringValue;
import models.EventParams;
import models.testcase.Value;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FillingFieldsEvent extends EventWrapper {
    private List<ElementWithFieldValue> elementWithFieldValues;

    public FillingFieldsEvent(EventParams params, List<ElementWithFieldValue> elementWithFieldValues) {
        super(params);
        this.elementWithFieldValues = elementWithFieldValues;
    }

    @Override
    protected void doWork(){
        elementWithFieldValues.parallelStream()
                .forEachOrdered(elementWithFieldValue -> {
                    String value = Objects.requireNonNull(convertValueToString(elementWithFieldValue.getValue()));

                    System.out.println("        ввод значения " + elementWithFieldValue.getValue() + " для элемента " +
                            Optional.ofNullable(elementWithFieldValue.getElement().getName()).orElse(elementWithFieldValue.getElement().getLocator()));
                    seleniumServise().enterSingleValuesToWebField(new ElementWithStringValue(elementWithFieldValue.getElement(), value));
                });
    }

    private String convertValueToString(Value value) {
        return switch (value.getType()) {
            case (2) -> (String) seleniumServise().jsReturnsValue(ApplicationConfig.getJsFunctionByName(value.getValue()));
            case (3) -> {
                try {
                    break (String) dataBaseService().getQueryFirstResult(value.getValue());
                } catch (SQLException e) {
                    break null;
                }
            }
            default -> value.getValue();
        };
    }
}
