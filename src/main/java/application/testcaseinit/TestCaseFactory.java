package application.testcaseinit;

import application.ApplicationConfig;
import logic.testcase.TestCase;
import models.ModelService;
import models.testcase.Action;
import models.testcase.Event;
import models.testcase.Field;
import models.webpackage.Form;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;


public class TestCaseFactory {
    public static TestCase createFromConfig(ModelService modelService) {
        TestCase testCase = new TestCase();

        modelService.getTestCaseActions().parallelStream()
                .forEachOrdered(action -> {
                    validateParametrs(modelService.getFormByName(action.getRunConfigurations().getFormName()) , action);
                    testCase.addAction(TestCaseActionFactory.createFromConfig(
                            modelService.getFormByName(action.getRunConfigurations().getFormName()),
                            modelService.getActionByOrderNumber(action.getOrderNumber())));
                });
        return testCase;
    }

    /**Проверяем корректность данных, которые должны подтянуться с webPackage*/
    private static void validateParametrs(Form form, Action action) {
        Optional.ofNullable(form).orElseThrow(() -> new NullPointerException("В указаном вами webPackage в action " + action.getName() + " отсутствует форма " +
                action.getRunConfigurations().getFormName()));

        action.getRunConfigurations().getEvents().parallelStream()
                .forEach(event -> validateEvent(event, action, form));

        if (Objects.nonNull(action.getRunConfigurations().getExceptionBlock())) {
            action.getRunConfigurations().getExceptionBlock().parallelStream()
                    .forEach(event -> validateEvent(event, action, form));
        }
    }

    private static void validateEvent(Event event, Action action, Form form) {
        String elementName = event.getElementName();
        //проверка существования элемента в корне ивента
        if (Objects.nonNull(elementName) && !elementName.isBlank()) { //если задан именно элемент по имени
            Optional.ofNullable(form.getElementByName(event.getElementName()))
                    .orElseThrow(() -> new NullPointerException("В указаном вами webPackage отсутствует элемент указанный в action " +
                            action.getOrderNumber() + " " + action.getName() + " на event " +
                            event.getOrderNumber() + " в теге elementName - элемент:" + event.getElementName()));
        }

        //проверка существования элемента при указании в fields
        Predicate<Field> checkElementExistInWebPackage = field -> Objects.isNull(form.getElementByName(field.getElement())) && (Objects.isNull(field.getXpath()) || field.getXpath().isBlank());
        Predicate<Field> checkJSFunctionExist = field -> Objects.nonNull(field.getValue()) &&
                (field.getValue().getType() == 2 && Objects.isNull(ApplicationConfig.getJsFunctionByName(field.getValue().getValue())));
        if (Objects.nonNull(event.getFields())) {
            event.getFields().parallelStream()
                    .filter(checkElementExistInWebPackage.or(checkJSFunctionExist))
                    .forEach(field -> {
                        throw new NullPointerException("В указаном вами webPackage отсутствует элемент указанный в action " +
                                action.getOrderNumber() + " " +
                                action.getName() + " на event " +
                                event.getOrderNumber() +
                                " в тегaх Field - элемент:" + (Objects.isNull(field.getElement()) ? field.getXpath() : field.getElement()) + " или отсутствуе js функция");
                    });
        }
    }
}
