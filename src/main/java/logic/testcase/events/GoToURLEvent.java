package logic.testcase.events;

import models.EventParams;

public class GoToURLEvent extends EventWrapper {
    private String url;

    public GoToURLEvent(EventParams params, String url) {
        super(params);
        this.url = url;
    }

    @Override
    protected void doWork() {
        System.out.println("        переход по ссылке " + url);
        seleniumServise().nacigateTo(url);
    }
}
