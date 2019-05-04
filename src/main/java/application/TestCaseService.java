package application;

import application.testcaseinit.TestCaseFactory;
import logic.testcase.TestCase;
import logic.testcase.TestCaseActionResultCollector;
import logic.testcase.TestCaseResult;
import logic.testcase.events.EventService;
import models.ActionWithEvents;
import models.ModelService;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class TestCaseService {
    private ModelService modelService;
    private TestCase testCase;

    TestCaseService(ModelService modelService) {
        this.modelService = modelService;
    }

    TestCaseActionResultCollector executeTestCase() {
        testCase = TestCaseFactory.createFromConfig(modelService);

        System.out.println("Время формирования TestCase: "+ TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - Main.startTime) + " сек");
        Main.startTime = System.nanoTime();
        return testCase.execute(new TestCaseResult());
    }

   List<ActionWithEvents> getActionsWithEventsList() {
        List<ActionWithEvents> actionsWithEventsList = new ArrayList<>();
//Даже не знаю, без стримов выглядит нагляднее
//        for (TestCaseAction action : testCase.getActions()) {
//            List<String> eventList = new ArrayList<>();
//            for (EventService event : action.getEventService()) {
//                eventList.add(event.getEventOrderNumberAndType());
//            }
//            if (action.getExceptionBlock() != null) {
//                for (EventService event : action.getExceptionBlock()) {
//                    eventList.add(event.getEventOrderNumberAndType() + "/exception");
//                }
//            }
//
//            actionsWithEventsList.add(new ActionWithEvents(action.getActionName(), eventList));
//        }

        testCase.getActions().forEach(testCaseAction -> {
                    List<String> eventList = testCaseAction.getEventService().stream()
                            .map(EventService::getEventOrderNumberAndType)
                            .collect(Collectors.toList());
                    if (Objects.nonNull(testCaseAction.getExceptionBlock())) {
                        eventList.addAll(testCaseAction.getExceptionBlock().stream()
                                .map(eventService -> eventService.getEventOrderNumberAndType() + "/exception")
                                .collect(Collectors.toList())
                        );
                    }
                    actionsWithEventsList.add(new ActionWithEvents(testCaseAction.getActionName(), eventList));
                });

        return actionsWithEventsList;
    }
}
