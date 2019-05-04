package logic.testcase.events;

import models.EventParams;

public class CheckQueryResultWithUtilityValueEvent extends EventWrapper {
    private String checkValue;
    private String query;

    public CheckQueryResultWithUtilityValueEvent(EventParams params, String checkValue, String dbValue) {
        super(params);
        this.checkValue = checkValue;
        this.query = dbValue;
    }

    @Override
    protected void doWork() throws Exception {
        System.out.println("        проверка соответствия значения " + checkValue + " со значение из БД по запросу " +
                query);
        String dbValue = (String) dataBaseService().getQueryFirstResult(query);
        if (!checkValue.equals(dbValue)) {
            throw new Exception("Значение " + checkValue + " не совпадает с значением из БД " + dbValue);
        }
    }
}
