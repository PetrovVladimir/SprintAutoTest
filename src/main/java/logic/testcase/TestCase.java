package logic.testcase;

import java.util.ArrayList;
import java.util.List;

public final class TestCase {
    private List<TestCaseAction> actions;

    public TestCase() {
        actions = new ArrayList<>();
    }

    public final TestCaseActionResultCollector execute(TestCaseActionResultCollector testCaseActionResultCollector) {

        for (TestCaseAction action : getActions()) {
            if (!action.runAction(testCaseActionResultCollector)) return testCaseActionResultCollector;
        }

        System.out.println("Тест завершился без ошибок!");
        return testCaseActionResultCollector;
    }

    public List<TestCaseAction> getActions() {
        return actions;
    }

    public void addAction(TestCaseAction testCaseAction) {
        actions.add(testCaseAction);
    }
}
