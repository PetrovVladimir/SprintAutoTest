package models;

import models.testcase.Action;
import models.testcase.TestCaseModel;
import models.webpackage.Form;
import models.webpackage.WebPackage;

import java.util.Collections;
import java.util.List;

public class ModelData implements ModelService{
    private TestCaseModel testCaseModel;
    private WebPackage webPackage;

    public ModelData(TestCaseModel testCaseModel, WebPackage webPackage) {
        this.testCaseModel = testCaseModel;
        this.webPackage = webPackage;
    }

    public Form getFormByName(String name) {
        int pos = Collections.binarySearch(webPackage.getForms(), new Form(name));
        return pos >= 0 ? webPackage.getForms().get(pos) : null;
    }

    public Action getActionByOrderNumber(int orderNumber) {
        int pos = Collections.binarySearch(testCaseModel.getActions(), new Action(orderNumber));
        return pos >= 0 ? testCaseModel.getActions().get(pos) : null;
    }

    public List<Action> getTestCaseActions() {
        return testCaseModel.getActions();
    }

}
