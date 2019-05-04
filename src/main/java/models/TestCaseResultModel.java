package models;

public class TestCaseResultModel {
    private String actionName;
    private  String eventOrderNumberAndType;
    private boolean success;
    private long executionTime;
    private String stackTrace;

    public TestCaseResultModel(String actionName, String eventOrderNumberAndType, boolean success, long executionTime, String stackTrace) {
        this.actionName = actionName;
        this.eventOrderNumberAndType = eventOrderNumberAndType;
        this.success = success;
        this.executionTime = executionTime;
        this.stackTrace = stackTrace;
    }

    public String getActionName() {
        return actionName;
    }

    public String getEventOrderNumberAndType() {
        return eventOrderNumberAndType;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
