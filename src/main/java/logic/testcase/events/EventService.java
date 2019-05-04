package logic.testcase.events;

import logic.database.DataBaseService;
import logic.selenium.SeleniumService;

public interface EventService {
    SeleniumService seleniumServise();
    DataBaseService dataBaseService();
    boolean runEvent();

    String getEventOrderNumberAndType();
    String getStackTrace();
    boolean hasExceptionBlock();
}
