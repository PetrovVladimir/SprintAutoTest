package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import logic.testcase.TestCaseActionResultCollector;
import logic.testcase.TestCaseResult;
import models.ActionWithEvents;
import models.testcase.TestCaseName;
import utils.DBConnector;
import utils.SeleniumDriver;

import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    static long startTime;
    private static String testCaseName;
    private static BufferedReader reader;

    public static void main(String[] args){
        setSystemProperties();
        setApplicationProperties();
        createDirs();
        printHead();
        setTestCaseName(args);
        runApplication();
    }

    private static void setTestCaseName(String[] args) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        if (args.length == 0) {
            List<String> testCaseNames = getTestCaseNames();

            if (testCaseNames.isEmpty()) {
                System.out.println("Не обнаружено валидных TestCase");
                System.exit(1);
            }

            System.out.println("Доступные TestCases:");
            testCaseNames.forEach(System.out::println);

            System.out.println("\nВведите TestCase, который желаете выполнить");
            try {
                reader = new BufferedReader(new InputStreamReader(System.in));
                while (!testCaseNames.contains(testCaseName = reader.readLine())) {
                    System.out.println("Ошибка воода имени Тест Кейса!");
                    System.out.println("Посмотрите внимательнее список доступных тест кейсов выше и введите еще раз!");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            testCaseName = args[0];
        }
    }

    private static List<String> getTestCaseNames() {
        ArrayList<String> testCaseNames = new ArrayList<>();
        ObjectMapper objectMapper = new XmlMapper(XMLInputFactory.newInstance());

        Arrays.stream(Objects.requireNonNull(new File(System.getProperty("user.dir") + "\\TestSuite\\TestCase").listFiles()))
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".xml"))
                .sorted()
                .forEach(file -> {
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
                        testCaseNames.add(Objects.requireNonNull(objectMapper.readValue(bufferedReader, TestCaseName.class).getTestCaseName()));
                    } catch (FileNotFoundException e) {
                        System.out.println("Произошла какая-то ерунда с файлом " + file.getAbsolutePath() + ". Типа он не найден");
                        System.out.println(e.getMessage());
                    } catch (IOException | NullPointerException e) {
                        System.out.println("Произошла ошибка при считывание файла " + file.getAbsolutePath());
                        System.out.println(e.getMessage());
                    }
                });

        return testCaseNames;
    }

    private static void runApplication() {
        startTime = System.nanoTime();

        Application application = ApplicationFactory.createFromConfig(testCaseName);
        TestCaseService testCaseService = application.getTestCaseService();
        System.out.println("Время формирования моделей: " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) + " сек");
        startTime = System.nanoTime();

        TestCaseResult result = null;
        try {
            result = (TestCaseResult) testCaseService.executeTestCase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        finish(result, testCaseService.getActionsWithEventsList());
    }

    private static void finish(TestCaseActionResultCollector result, List<ActionWithEvents> actionsWithEvents) {
        try {
            DBConnector.connector().connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            SeleniumDriver.webDriver().driver.quit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Время выполнения теста: " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) + " сек");
            new Report().generateReport(actionsWithEvents, result.getResult(), testCaseName);
        } catch (Exception e) {
            System.out.println("Отчет не сформирован!");
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            System.out.println("Работа приложения завершена!");
            System.out.println("Для выхода нажмите ENTER...");
            try {

                reader.readLine();
                reader.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void printHead() {
        System.out.println("********************************************************************************");
        System.out.println("*                                   AutoTest                                   *");
        System.out.println("********************************************************************************");
        System.out.println();
    }

    private static void setSystemProperties() {
        //убираем варнинги
        System.err.close();
        System.setErr(System.out);

        //устанавливаем кодировку - нужно для отчета
        System.setProperty("file.encoding","UTF-8");
        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (charset != null) {
            charset.setAccessible(true);
        }
        try {
            if (charset != null) {
                charset.set(null,null);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**Инициирование значений переменных ApplicationConfig*/
    private static void setApplicationProperties() {
        Properties properties = new Properties();
        String baseDir = System.getProperty("user.dir");

        try(FileInputStream fileInputStream = new FileInputStream(baseDir + "\\config.properties")) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            System.out.println("Файл конфига не найден! \nОн должен быть расположен в дирректории запуска программы с названием \"config.properties\"");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Произошла ошибка при считывании файла конфига!");
            System.out.println(e.getMessage());
        }

        ApplicationConfig.setGeckodriverPath(baseDir + "\\BuildKit\\geckodriver.exe");
        ApplicationConfig.setWebPackageStorage(baseDir + "\\TestSuite\\WebPackage");
        ApplicationConfig.setTestCaseStorage(baseDir + "\\TestSuite\\TestCase");
        ApplicationConfig.setJsStorage(baseDir + "\\TestSuite\\JS");
        ApplicationConfig.setReportStorage(baseDir + "\\Reports");
        ApplicationConfig.setScreenShotStorage(baseDir + "\\Reports\\ErrorScreens");
        ApplicationConfig.setDb_url(properties.getProperty("db_url"));
        ApplicationConfig.setDb_sid(properties.getProperty("db_sid"));
        ApplicationConfig.setDb_user(properties.getProperty("db_user"));
        ApplicationConfig.setDb_user_pass(properties.getProperty("db_user_pass"));
        ApplicationConfig.setDefaultTimeOutsForWebDriverWait(Integer.parseInt(properties.getProperty("defaultTimeOutsForWebDriverWait")));
        ApplicationConfig.setLowTimeOutsForWebDriverWait(Integer.parseInt(properties.getProperty("lowTimeOutsForWebDriverWait")));
        ApplicationConfig.setJsFunctions(new File(ApplicationConfig.getJsStorage()));
    }

    /**
     * Создание директорий для отчётов
     */
    private static void createDirs() {
        Path reportStoragePath = Paths.get(ApplicationConfig.getReportStorage());
        Path screenShotStoragePath = Paths.get(ApplicationConfig.getScreenShotStorage());
        try {
            Files.createDirectories(reportStoragePath);
            Files.createDirectories(screenShotStoragePath);
        } catch (IOException e) {
            System.out.println("Не удалось создать директории для отчётов");
            e.printStackTrace();
        }
    }

    static List<File> listFilesFolders(File dir) {
        return Arrays.stream(Objects.requireNonNull(dir.listFiles(), "Произошла ошибка при получении файлов из " + dir.getPath() + "! Проверьте настройки config.properties!"))
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".xml"))
                .sorted()
                .collect(Collectors.toList());
    }
}