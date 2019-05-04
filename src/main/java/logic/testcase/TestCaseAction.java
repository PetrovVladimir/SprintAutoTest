package logic.testcase;

import logic.testcase.events.EventService;

import java.util.List;
import java.util.Objects;

public final class TestCaseAction implements Comparable<TestCaseAction>{

    private int orderNumber;
    private String actionName;
    private int repeatsOnError;
    private List<EventService> eventService;
    private List<EventService> exceptionBlock;

    public TestCaseAction(int orderNumber, String actionName, int repeatsOnError, List<EventService> eventService, List<EventService> exceptionBlock) {
        this.orderNumber = orderNumber;
        this.actionName = actionName;
        this.repeatsOnError = repeatsOnError;
        this.eventService = eventService;
        this.exceptionBlock = exceptionBlock;
    }

    public List<EventService> getEventService() {
        return eventService;
    }

    public String getActionName() {
        return actionName;
    }

    public List<EventService> getExceptionBlock() {
        return exceptionBlock;
    }

    final boolean runAction(TestCaseActionResultCollector testCaseActionResultCollector) {
        System.out.println("Выполняется: " + actionName); //для лога
        boolean eventResult = true;

        while (repeatsOnError >= 0) {
            for (EventService event : eventService) {
                System.out.println("    " + event.getEventOrderNumberAndType()); //для лога

                eventResult = event.runEvent();
                testCaseActionResultCollector.collectActionResult(actionName,
                        event.getEventOrderNumberAndType(),
                        eventResult,
                        System.nanoTime(),
                        event.getStackTrace());

                //переход в блок исключений
                if (!eventResult && event.hasExceptionBlock() && repeatsOnError > 0) {
                    repeatsOnError -= 1;
                    boolean eventExceptionResult;
                    if (!(exceptionBlock == null || exceptionBlock.isEmpty())) {

                        for (EventService exceptionEvent : exceptionBlock) {
                            System.out.println("    " + exceptionEvent.getEventOrderNumberAndType()); //для лога

                            eventExceptionResult = exceptionEvent.runEvent();
                            testCaseActionResultCollector.collectActionResult(actionName,
                                    exceptionEvent.getEventOrderNumberAndType() + "/exception",
                                    eventExceptionResult,
                                    System.nanoTime(),
                                    exceptionEvent.getStackTrace());

                            if (!eventExceptionResult) {
                                System.out.println("Тест закончился неудачно на действии " + orderNumber + " " + actionName + ", событие " + event.getEventOrderNumberAndType() +
                                        " при выполнении действий при ошибке. " +
                                        "При описании данных действий, Вы должны обеспечить их стопроцентную отработку!");
                                return false;
                            }
                        }
                    }
                    //если действия блока исключений успешно выполнились, то выполняем events сначала
                    break;
                }

                //грохаем если действие завершилось с ошибкой
                if (!eventResult) {
                    System.out.println("Тест закончился неудачно на действии " + orderNumber + " " + actionName + ", событие " + event.getEventOrderNumberAndType());
                    return false;
                }
            } //конец цикла for для events
            if (eventResult) return true; //в случае успеха
        } //конец while repeatsOnError >= 0
        return false; //ну и если как-то добрались сюда, значит неудачно
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseAction that = (TestCaseAction) o;
        return orderNumber == that.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }

    @Override
    public int compareTo(TestCaseAction o) {
        return Integer.compare(orderNumber, o.orderNumber);
    }
}
