package logic.testcase.events;

import application.ApplicationConfig;
import models.ElementWithFieldValue;
import models.EventParams;
import models.testcase.Value;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CheckInputValuesEvent extends EventWrapper {
    private List<ElementWithFieldValue> elementWithFieldValues;

    public CheckInputValuesEvent(EventParams params, List<ElementWithFieldValue> elementWithFieldValues) {
        super(params);
        this.elementWithFieldValues = elementWithFieldValues;
    }

    @Override
    protected void doWork(){
        elementWithFieldValues.parallelStream()
                .forEach(elementWithFieldValue -> {
                    String checkValue = null;
                    try {
                        checkValue = convertValueToString(elementWithFieldValue.getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("        проверка соответствия значения атрибута для элемента " +
                            Optional.ofNullable(elementWithFieldValue.getElement().getName()).orElse(elementWithFieldValue.getElement().getLocator()) +
                            " значению " + elementWithFieldValue.getValue());
                    String factValue = seleniumServise().getInputValue(elementWithFieldValue.getElement());

                    if (!factValue.equals(checkValue)) {
                        throw new RuntimeException("Значение в Input элементе " + elementWithFieldValue.getElement().toString() +
                                " не соответствует ожидаемому.\nОжидаемое значение: " + checkValue +
                                "\nФактическое значение: " + factValue);
                    }
                });
    }

    private String convertValueToString(Value value) throws SQLException {
        switch (value.getType()) {
            case (1):
                return value.getValue();
            case (2):
                String js = ApplicationConfig.getJsFunctionByName(value.getValue());
                return (String) seleniumServise().jsReturnsValue(js);
            case (3):
                return (String) dataBaseService().getQueryFirstResult(value.getValue());
            default: return value.getValue();
        }
    }
}
