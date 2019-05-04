package logic.testcase;

import models.TestCaseResultModel;
import java.util.ArrayList;
import java.util.List;

public final class TestCaseResult implements TestCaseActionResultCollector {
    private List<TestCaseResultModel> resultsList;

    public TestCaseResult() {
        this.resultsList = new ArrayList<>();
    }

    @Override
    public void collectActionResult(String actionName, String eventOrderNumber, boolean success, long executionTime, String stackTrace) {
        resultsList.add(new TestCaseResultModel(actionName, eventOrderNumber, success, executionTime, stackTrace));
    }

    @Override
    public List<TestCaseResultModel> getResult() {
        return resultsList;
    }
}
