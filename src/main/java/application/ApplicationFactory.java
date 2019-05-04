package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.ModelData;
import models.testcase.TestCaseModel;
import models.webpackage.WebPackage;

import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.util.*;

class ApplicationFactory {

    static Application createFromConfig(String testCaseName) {
        TestCaseModel testCaseModel;
        WebPackage webPackage;

        try {
            testCaseModel = setTestCaseConfig(testCaseName);
            webPackage = setWebPackage(testCaseModel.getWebPackageName());

            return new Application(new ModelData(testCaseModel, webPackage));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NullPointerException("Не удалось построить модель!");
        }
    }

    private static TestCaseModel setTestCaseConfig(String testCaseName) {
        TestCaseModel testCaseModel;

        for (File file : Objects.requireNonNull(Main.listFilesFolders(new File(ApplicationConfig.getTestCaseStorage())),
                "Не найдено ни одного TestCaseModel, соответствующего указанному имени")) {
            testCaseModel = createModel(TestCaseModel.class, file);

            if (Objects.nonNull(testCaseModel) && testCaseModel.getTestCaseName().equals(testCaseName)) {
                Collections.sort(testCaseModel.getActions());
                testCaseModel.getActions().parallelStream()
                        .forEach(action -> {
                            Collections.sort(action.getRunConfigurations().getEvents());
                            if (Objects.nonNull(action.getRunConfigurations().getExceptionBlock()))
                                Collections.sort(action.getRunConfigurations().getExceptionBlock());
                        });
                return testCaseModel;
            }
        }

        throw new NullPointerException("Не найдено ни одного TestCaseModel, соответствующего указанному имени");
    }

    private static WebPackage setWebPackage(String webPackageName) throws NullPointerException {
        WebPackage webPackage;

        for (File file : Objects.requireNonNull(Main.listFilesFolders(new File(ApplicationConfig.getWebPackageStorage())),
                "Не найдено ни одного корретктного WegPackage, соответствующего указанному имени в TestCaseModel: " + webPackageName)) {
            webPackage = createModel(WebPackage.class, file);

            if (Objects.nonNull(webPackage) && webPackage.getWebPackageName().equals(webPackageName)) {
                Collections.sort(webPackage.getForms());
                webPackage.getForms().parallelStream()
                        .forEach(form -> Collections.sort(form.getElements()));
                return webPackage;
            }
        }

        throw new NullPointerException("Не найдено ни одного корретктного WegPackage, соответствующего указанному имени в TestCaseModel: " + webPackageName);
    }

    private static<T> T createModel(Class<T> type, File file) {
        T model = null;
        ObjectMapper objectMapper = new XmlMapper(XMLInputFactory.newInstance()); //XMLInputFactory нужна чтобы парсились Entitites

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            model = objectMapper.readValue(bufferedReader, type);
        } catch (FileNotFoundException e) {
            System.out.println("Произошла какая-то ерунда с файлом " + file.getAbsolutePath() + ". Типа он не найден");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Произошла ошибка при считывание файла " + file.getAbsolutePath());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
