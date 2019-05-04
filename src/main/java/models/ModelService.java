package models;

import models.testcase.Action;
import models.webpackage.Form;

import java.util.List;

public interface ModelService {

    Form getFormByName(String name);
    Action getActionByOrderNumber(int orderNumber);
    List<Action> getTestCaseActions();
}
