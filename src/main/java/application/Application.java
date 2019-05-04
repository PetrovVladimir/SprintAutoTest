package application;


import models.ModelData;

class Application {
    private ModelData modelData;

    Application(ModelData modelData) {
        this.modelData = modelData;
    }

    TestCaseService getTestCaseService() {
        return new TestCaseService(modelData);
    }
}
