package logic.testcase.events;

import models.EventParams;

public class WaitEvent extends EventWrapper {
    private long millis;

    public WaitEvent(EventParams params, long millis) {
        super(params);
        this.millis = millis;
    }

    @Override
    protected void doWork() throws Exception {
        System.out.println("        ожидание " + millis + " милисекунд");
        Thread.sleep(millis);
    }
}
