package logic.testcase.events;

import application.ApplicationConfig;
import logic.database.DataBaseService;
import logic.selenium.SeleniumService;
import models.EventParams;
import utils.DBConnector;
import utils.SeleniumDriver;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public abstract class EventWrapper implements EventService {
    private EventParams params;
    private String stackTrace;

    EventWrapper(EventParams params) {
        this.params = params;
    }

    @Override
    public SeleniumService seleniumServise() {
        return SeleniumDriver.webDriver().seleniumService;
    }

    @Override
    public DataBaseService dataBaseService() {
        return DBConnector.connector().dataBaseService;
    }

    @Override
    public final boolean runEvent() {
        boolean success = true;
        try {
            seleniumServise().initialization(params.isHasExceptionBlock()); //добавлен для контрольных действий над исключениями сделаем таймаут короче, а то не камильфо
            doWork();
        } catch (Exception e) {
            success = false;
            exceptionActions(e);
        } finally {
            success = success != params.isInvertResult();
            if (params.isInvertResult() && !success) exceptionActions(new Exception("Действие должно было закончиться неудачно"));
        }
        return success;
    }

    @Override
    public String getEventOrderNumberAndType() {
        return params.getOrderNumberAndType();
    }

    @Override
    public String getStackTrace() {
        return stackTrace;
    }

    @Override
    public boolean hasExceptionBlock() {
        return params.isHasExceptionBlock();
    }

    private void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    private void exceptionActions(Exception e) {
        String errorMessage = Objects.nonNull(e.getMessage()) ? e.getMessage() : Objects.nonNull(e.getCause().getMessage()) ? e.getCause().getMessage() : Arrays.toString(e.getStackTrace());
        System.out.println(errorMessage);
        setStackTrace(errorMessage);
        seleniumServise().getScreenShot(ApplicationConfig.getScreenShotStorage() + "\\" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss-SS").format(new Date()) + ".png");
    }

    protected abstract void doWork() throws Exception;
}
