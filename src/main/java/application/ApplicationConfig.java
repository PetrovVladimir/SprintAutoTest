package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ApplicationConfig {

    private static String geckodriverPath;
    private static String webPackageStorage;
    private static String testCaseStorage;
    private static String jsStorage;
    private static String reportStorage;
    private static String screenShotStorage;
    private static String db_url;
    private static String db_sid;
    private static String db_user;
    private static String db_user_pass;

    private static int lowTimeOutsForWebDriverWait;
    private static int defaultTimeOutsForWebDriverWait;

    //хотел поместить в модель, но т.к. доступ нужен всегда, дабы не плодить классы со статикой
    private static HashMap<String, String> jsFunctions;

    public static String getGeckodriverPath() {
        return geckodriverPath;
    }

    static void setGeckodriverPath(String geckodriverPath) {
        ApplicationConfig.geckodriverPath = geckodriverPath;
    }

    static String getWebPackageStorage() {
        return webPackageStorage;
    }

    static void setWebPackageStorage(String webPackageStorage) {
        ApplicationConfig.webPackageStorage = webPackageStorage;
    }

    static String getTestCaseStorage() {
        return testCaseStorage;
    }

    static void setTestCaseStorage(String testCaseStorage) {
        ApplicationConfig.testCaseStorage = testCaseStorage;
    }

    static String getJsStorage() {
        return jsStorage;
    }

    static void setJsStorage(String jsStorage) {
        ApplicationConfig.jsStorage = jsStorage;
    }

    static String getReportStorage() {
        return reportStorage;
    }

    static void setReportStorage(String reportStorage) {
        ApplicationConfig.reportStorage = reportStorage;
    }

    public static String getScreenShotStorage() {
        return screenShotStorage;
    }

    static void setScreenShotStorage(String screenShotStorage) {
        ApplicationConfig.screenShotStorage = screenShotStorage;
    }

    public static String getDb_url() {
        return db_url;
    }

    static void setDb_url(String db_url) {
        ApplicationConfig.db_url = db_url;
    }

    public static String getDb_sid() {
        return db_sid;
    }

    static void setDb_sid(String db_sid) {
        ApplicationConfig.db_sid = db_sid;
    }

    public static String getDb_user() {
        return db_user;
    }

    static void setDb_user(String db_user) {
        ApplicationConfig.db_user = db_user;
    }

    public static String getDb_user_pass() {
        return db_user_pass;
    }

    static void setDb_user_pass(String db_user_pass) {
        ApplicationConfig.db_user_pass = db_user_pass;
    }

    public static String getJsFunctionByName(String name) {
        return jsFunctions.get(name);
    }

    static void setJsFunctions(File dir) {
        HashMap<String, String> jsFunctions = new HashMap<>();

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String function;
                StringBuilder stringBuilder = new StringBuilder();
                while (bufferedReader.ready()) {
                    stringBuilder.append(bufferedReader.readLine());
                }
                function = stringBuilder.toString();
                jsFunctions.put(file.getName().replaceAll("\\.txt", ""), function);

            } catch (IOException e) {
                System.out.println("Произошла ошибка при считывании файлов JavaScript!");
            }
        }

        ApplicationConfig.jsFunctions = jsFunctions;
    }

    public static int getLowTimeOutsForWebDriverWait() {
        return lowTimeOutsForWebDriverWait;
    }

    static void setLowTimeOutsForWebDriverWait(int lowTimeOutsForWebDriverWait) {
        ApplicationConfig.lowTimeOutsForWebDriverWait = lowTimeOutsForWebDriverWait;
    }

    public static int getDefaultTimeOutsForWebDriverWait() {
        return defaultTimeOutsForWebDriverWait;
    }

    static void setDefaultTimeOutsForWebDriverWait(int defaultTimeOutsForWebDriverWait) {
        ApplicationConfig.defaultTimeOutsForWebDriverWait = defaultTimeOutsForWebDriverWait;
    }
}
