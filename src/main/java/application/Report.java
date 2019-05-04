package application;

import models.ActionWithEvents;
import models.TestCaseResultModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Report {

    void generateReport(List<ActionWithEvents> actionsWithEvents, List<TestCaseResultModel> results, String testCaseName) {
        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Result");

        int i = 0;

        //стиль для шапки
        HSSFCellStyle cellStyleHat = book.createCellStyle();
        HSSFFont hatFont = book.createFont();
        hatFont.setBold(true);
        cellStyleHat.setFont(hatFont);
        //стили для контента
        HSSFCellStyle backGroungGrey = book.createCellStyle();
        backGroungGrey.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        backGroungGrey.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());

        HSSFCellStyle backGroungBlue = book.createCellStyle();
        backGroungBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        backGroungBlue.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

        HSSFCellStyle backGroungGreen = book.createCellStyle();
        backGroungGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        backGroungGreen.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());

        HSSFCellStyle backGroungRed = book.createCellStyle();
        backGroungRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        backGroungRed.setFillForegroundColor(IndexedColors.ROSE.getIndex());

        //шапка файла
        Row row_hat = sheet.createRow(i);

        Cell nameNumber = row_hat.createCell(0);
        nameNumber.setCellStyle(cellStyleHat);
        nameNumber.setCellValue("№ пп");
        Cell nameAction = row_hat.createCell(1);
        nameAction.setCellStyle(cellStyleHat);
        nameAction.setCellValue("Проверки");
        Cell nameEvent = row_hat.createCell(2);
        nameEvent.setCellStyle(cellStyleHat);
        nameEvent.setCellValue("Действия");
        Cell nameSuccess = row_hat.createCell(3);
        nameSuccess.setCellStyle(cellStyleHat);
        nameSuccess.setCellValue("Результат");
        Cell executionTime = row_hat.createCell(4);
        executionTime.setCellStyle(cellStyleHat);
        executionTime.setCellValue("Время выполнения, мс");
        Cell stackTrace = row_hat.createCell(5);
        stackTrace.setCellStyle(cellStyleHat);
        stackTrace.setCellValue("Ошибка");

        boolean changeStyle = true;
        HSSFCellStyle backGroungStyle = backGroungGrey;
        for (TestCaseResultModel result : results) {

            i++;
            Row row = sheet.createRow(i);

            nameAction = row.createCell(1);
            nameAction.setCellStyle(backGroungStyle);
            if (i == 1 || !result.getActionName().equals(results.get(i - 2).getActionName())) { //чтобы не дублировать actions
                changeStyle = !changeStyle;
                if (changeStyle) {
                    backGroungStyle = backGroungBlue;
                } else {
                    backGroungStyle = backGroungGrey;
                }
                nameAction.setCellStyle(backGroungStyle);
                nameAction.setCellValue(result.getActionName());
            }

            nameNumber = row.createCell(0);
            nameNumber.setCellStyle(backGroungStyle);
            nameNumber.setCellValue(i);

            nameEvent = row.createCell(2);
            nameEvent.setCellStyle(backGroungStyle);
            nameEvent.setCellValue(result.getEventOrderNumberAndType());

            nameSuccess = row.createCell(3);
            nameSuccess.setCellStyle(backGroungGreen);
            if (!result.isSuccess()) {
                nameSuccess.setCellStyle(backGroungRed);
                nameSuccess.setCellValue(results.size() > i ? "ожидаемая ошибка" : "ошибка");
            } else {
                nameSuccess.setCellValue("успешно");
            }


            executionTime = row.createCell(4);
            executionTime.setCellStyle(backGroungStyle);
            if (i != 1) {
                executionTime.setCellValue(TimeUnit.NANOSECONDS.toMillis(result.getExecutionTime() - results.get(i - 2).getExecutionTime()));
            } else {
                executionTime.setCellValue(TimeUnit.NANOSECONDS.toMillis(result.getExecutionTime() - Main.startTime));
            }

            stackTrace = row.createCell(5);
            stackTrace.setCellStyle(backGroungStyle);
            if (!result.isSuccess() && result.getStackTrace() != null) {
                stackTrace.setCellStyle(backGroungRed);
                stackTrace.setCellValue(result.getStackTrace());
            }
        }

        //когда прошлись по всем выполненным действиям, надо отразить которые не выполнялись
        if (!results.get(results.size() - 1).isSuccess()) {
            TestCaseResultModel lastResult = results.get(results.size() - 1);
            boolean continuedManagement = false;
            for (ActionWithEvents entry : actionsWithEvents) {
                if (continuedManagement) {
                    for (String event : entry.getEventList()) {
                        if (event.contains("/exception")) break; //не логируем блок исключений
                        i++;
                        Row row = sheet.createRow(i);

                        nameAction = row.createCell(1);
                        nameAction.setCellStyle(backGroungStyle);
                        if (entry.getEventList().indexOf(event) == 0) {
                            changeStyle = !changeStyle;
                            if (changeStyle) {
                                backGroungStyle = backGroungBlue;
                            } else {
                                backGroungStyle = backGroungGrey;
                            }
                            nameAction.setCellStyle(backGroungStyle);
                            nameAction.setCellValue(entry.getAction());

                        }

                        nameNumber = row.createCell(0);
                        nameNumber.setCellStyle(backGroungStyle);
                        nameNumber.setCellValue(i);

                        nameEvent = row.createCell(2);
                        nameEvent.setCellStyle(backGroungStyle);
                        nameEvent.setCellValue(event);

                        nameSuccess = row.createCell(3);
                        nameSuccess.setCellStyle(backGroungStyle);
                        nameSuccess.setCellValue("Не выполнялся");

                        executionTime = row.createCell(4);
                        executionTime.setCellStyle(backGroungStyle);

                        stackTrace = row.createCell(5);
                        stackTrace.setCellStyle(backGroungStyle);
                    }
                    continue; //чутка ускорим алгоритм
                }

                if (lastResult.getActionName().equals(entry.getAction())) {
                    continuedManagement = true;
                    for (int j = entry.getEventList().indexOf(lastResult.getEventOrderNumberAndType()) + 1; j < entry.getEventList().size(); j++) {
                        if (entry.getEventList().get(j).contains("/exception")) break; //не логируем блок исключений
                        i++;
                        Row row = sheet.createRow(i);
                        nameNumber = row.createCell(0);
                        nameNumber.setCellStyle(backGroungStyle);
                        nameNumber.setCellValue(i);

                        nameAction = row.createCell(1);
                        nameAction.setCellStyle(backGroungStyle);

                        nameEvent = row.createCell(2);
                        nameEvent.setCellStyle(backGroungStyle);
                        nameEvent.setCellValue(entry.getEventList().get(j));

                        nameSuccess = row.createCell(3);
                        nameSuccess.setCellStyle(backGroungStyle);
                        nameSuccess.setCellValue("Не выполнялся");

                        executionTime = row.createCell(4);
                        executionTime.setCellStyle(backGroungStyle);

                        stackTrace = row.createCell(5);
                        stackTrace.setCellStyle(backGroungStyle);
                    }
                }
            }
        }

        for (int j = 0; j < 6; j++) {
            sheet.autoSizeColumn(j);
        }

        try {
            book.write(new FileOutputStream(ApplicationConfig.getReportStorage() + "\\" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + " " + testCaseName + ".xls"));
            book.close();
            System.out.println("Отчет сформирован!");
        } catch (IOException e) {
            System.out.println("Произошла ошибка при сохранении отчета!");
            e.printStackTrace();
        }
    }
}
