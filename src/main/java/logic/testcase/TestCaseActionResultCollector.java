package logic.testcase;

import models.TestCaseResultModel;

import java.util.List;

public interface TestCaseActionResultCollector {
    void collectActionResult(String actionName, String eventOrderNumberAndType, boolean success, long executionTime, String stackTrace);
    List<TestCaseResultModel> getResult();
}
