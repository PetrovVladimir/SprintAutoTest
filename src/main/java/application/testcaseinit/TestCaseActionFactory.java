package application.testcaseinit;

import logic.testcase.TestCaseAction;
import logic.testcase.events.*;
import models.ElementWithFieldValue;
import models.EventParams;
import models.testcase.Action;
import models.testcase.Event;
import models.testcase.Field;
import models.webpackage.Element;
import models.webpackage.Form;
import models.webpackage.Locator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestCaseActionFactory {
    static TestCaseAction createFromConfig(Form form, Action action) {
        List<EventService> eventList;
        List<EventService> exceptionEventList;

        Function<List<Event>, List<EventService>> makeEvents = events -> Objects.nonNull(events) ? events.stream().map(event -> makeEvent(form, event)).collect(Collectors.toList()) : null;
        eventList = makeEvents.apply(action.getRunConfigurations().getEvents());
        exceptionEventList = makeEvents.apply(action.getRunConfigurations().getExceptionBlock());

        //добавляем ивенты, если нужна валидация при открытии и/или закрытии
        Function<List<Element>, List<Element>> filterAlwaysVisibleElements = elements -> elements.parallelStream().filter(Element::getAlwaysVisible).collect(Collectors.toList());

        if (action.getRunConfigurations().isOpenValidation()) eventList.add(
                    action.getRunConfigurations().getEvents().get(0).getType().equalsIgnoreCase("goToURL") ? 1 : 0 , //позиция для старта
                    (new CheckElementsVisibilityEvent(new EventParams("0 openValidation", false, false),
                            filterAlwaysVisibleElements.apply(form.getElements()))));

        if (action.getRunConfigurations().isCloseValidation()) eventList.add(
                new CheckElementsInVisibilityEvent(new EventParams("# closeValidation", false, false),
                        filterAlwaysVisibleElements.apply(form.getElements())));

        return new TestCaseAction(action.getOrderNumber(), action.getName(), action.getRunConfigurations().getRepeatsOnError(), eventList, exceptionEventList);
    }

    private static EventService makeEvent(Form form, Event event) {
        TestCaseEventType eventType = TestCaseEventType.getEventType(event.getType());
        EventParams params = new EventParams(event.getOrderNumber() + " " + event.getType(), event.isInvertResult(), event.isHasExceptionBlock());

        //вспомогательное
        Predicate<Field> checkElementOrXpathIsPresent = field -> Objects.nonNull(field.getXpath()) && !field.getXpath().isEmpty();
        Function<Field, Element> createNewElementWithXpath = field -> {
            Element element = new Element();
            element.setLocator(new Locator(field.getXpath(), (byte) 2));
            return element;
        };
        Function<Field, Element> setNewElement = field -> checkElementOrXpathIsPresent.test(field) ? createNewElementWithXpath.apply(field) : form.getElementByName(field.getElement());

        return switch (eventType) {
            case GOTO_URL -> new GoToURLEvent(params, event.getUrl());
            case CHECK_ELEMENTS_VISIBILITY -> new CheckElementsVisibilityEvent(params,
                    event.getFields().stream().map(setNewElement).collect(Collectors.toCollection(ArrayList::new)));
            case CHECK_ELEMENT_INVISIBILITY -> new CheckElementsInVisibilityEvent(params,
                    event.getFields().stream().map(setNewElement).collect(Collectors.toCollection(ArrayList::new)));
            case FILLING_FIELDS -> new FillingFieldsEvent(params,
                    event.getFields().stream()
                            .map(field -> new ElementWithFieldValue(form.getElementByName(field.getElement()), field.getValue()))
                            .collect(Collectors.toCollection(ArrayList::new)));
            case CLICK_ELEMENT -> new ClickElementEvent(params,
                    form.getElementByName(event.getElementName()));
            case WAIT -> new WaitEvent(params,
                    Long.parseLong(event.getUtilityValue()));
            case SCROLL_DOWN -> new ScrollDownEvent(params,
                    form.getElementByName(event.getElementName()));
            case DB_UPDATE -> new DBUpdateEvent(params,
                    event.getDbRequest());
            case CHECK_INPUT_VALUES -> new CheckInputValuesEvent(params,
                    event.getFields().stream()
                            .map(field -> new ElementWithFieldValue(form.getElementByName(field.getElement()), field.getValue()))
                            .collect(Collectors.toCollection(ArrayList::new)));
            case CHECK_QUERY_RESULTS_WITH_UTILITY_VALUE -> new CheckQueryResultWithUtilityValueEvent(params,
                    event.getUtilityValue(),
                    event.getDbRequest());
            case CHECK_FIELDS_PRESENCE_BY_QUERY_RESULT -> new CheckFieldsPresenceByQueryResultEvent(params,
                    event.getUtilityValue(),
                    event.getDbRequest());
            case USER_INPUT -> new UserInputEvent(params,
                    form.getElementByName(event.getElementName()));
        };
    }

    private enum TestCaseEventType {
        GOTO_URL("goToURL"), //переход по URL
        CHECK_ELEMENTS_VISIBILITY("checkElementsVisibility"), //проверка видимости элемента
        CHECK_ELEMENT_INVISIBILITY("checkElementsInVisibility"), //проверка что элемент невидим/или отсутствует в DOM - т.е. что его нет
        FILLING_FIELDS("fillingFields"), //заполнение поле
        CLICK_ELEMENT("clickElement"), //клик на элеамента
        DB_UPDATE("dbUpdate"), //выполнить апдейт в БД
        CHECK_INPUT_VALUES("checkInputValues"), //проверка значение input элементов
        CHECK_QUERY_RESULTS_WITH_UTILITY_VALUE("checkQueryResultWithUtilityValue"), //проверка введенного пользователем значения с значением из БД
        CHECK_FIELDS_PRESENCE_BY_QUERY_RESULT("checkFieldsPresenceByQueryResult"), //проверка наличия элементов на форме по xpath по паттерну //*[text()[contains(normalize-space(.), "ЗНАЧЕНИЕ")]], полученных запросом
        WAIT("wait"), //ожидание - усыпить поток на милисекунды
        SCROLL_DOWN("scrollDown"), //скрольнуть вниз от указанного элемента
        USER_INPUT("userInput"); //ввод данных пользователем с последующим заполнением указанного поля

        private String value;

        TestCaseEventType(String value) {
            this.value = value;
        }

        static TestCaseEventType getEventType(String value) {
            return Stream.of(TestCaseEventType.values())
                    .parallel()
                    .filter(testCaseEventType -> value.equalsIgnoreCase(testCaseEventType.value))
                    .findFirst().orElseThrow(() -> new NullPointerException("Нет такого типа события для тестКейса: " + value));
        }
    }

}