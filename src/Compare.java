import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksandar Djokic on 12/31/2015.
 */
public class Compare {
    public static List<Product> lista = new ArrayList<>();

    public Compare() {

//        compare(readFromActual, readFromForecast);
//        for (Product p : lista) {
//            System.out.println(p.toString());
//            break;
//        }
    }

    public static Map<String, Map<String, Double>> compare(ReadFromActual actual, ReadFromForecast forecast,String path) {
        int counterForHeading = 1;
        int rowCounter = 2;
        int valuesCounter = 1;
        double difference = 0.0;
        String forecastUpc = "";
        String week = "";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet newSheet = workbook.createSheet("DIFFERENCE");

        Row weekHeading = newSheet.createRow(0);
        Row heading = newSheet.createRow(1);

        Map<String, Map<String, Double>> toRet = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entryForecast : forecast.mapaVelika.entrySet()) {
            Row newRoww = newSheet.createRow(rowCounter++);
            newRoww.createCell(0).setCellValue(entryForecast.getKey());

            Map<String, Double> productValueMapFromForecast = entryForecast.getValue();
            forecastUpc = entryForecast.getKey();
            for (Map.Entry<String, Map<String, Double>> entryActual : actual.mapaVelika.entrySet()) {

                String actualUpc = entryActual.getKey();

                if (forecastUpc.equals(actualUpc)) {
                    Map<String, Double> productValueMapFromActual = entryActual.getValue();
                    Map<String, Double> mapToBeChanged = new HashMap<>();
                    for (Map.Entry<String, Double> entryForecast1 : productValueMapFromForecast.entrySet()) {
                        for (Map.Entry<String, Double> entryActual1 : productValueMapFromActual.entrySet()) {
                            if (entryForecast.getKey().equals(entryActual.getKey()) && entryForecast1.getKey().equals(entryActual1.getKey())) {

                                week = entryForecast1.getKey();
                                Product p = new Product();
//                                System.out.println(" UPC : " + forecastUpc + "  " + week + "  " + difference + " = " + entryForecast1.getValue() + " - " + "  " + entryActual1.getValue());
                                difference = entryForecast1.getValue() - entryActual1.getValue();
                                mapToBeChanged.put(week, Math.floor(difference));
                                toRet.put(forecastUpc, mapToBeChanged);
                                p.setMapaVelika(toRet);
                                lista.add(p);

                                heading.createCell(0).setCellValue("UPC");
                                newSheet.autoSizeColumn(0);

                                CellStyle style1 = workbook.createCellStyle();
                                style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                newRoww.createCell(valuesCounter++).setCellValue(Math.floor(entryActual1.getValue()));
                                newRoww.getCell(valuesCounter - 1).setCellStyle(style1);
                                newRoww.createCell(valuesCounter++).setCellValue((Math.floor(entryForecast1.getValue())));

                                if (difference < 0.0) {
                                    CellStyle style = workbook.createCellStyle();
                                    Font font = workbook.createFont();
                                    font.setColor(HSSFColor.RED.index);
                                    font.setBold(true);
                                    style.setFont(font);
                                    newRoww.createCell(valuesCounter++).setCellValue(Math.floor(difference));
                                    newRoww.getCell(valuesCounter - 1).setCellStyle(style);

                                } else {
                                    CellStyle style = workbook.createCellStyle();
                                    Font font = workbook.createFont();
                                    font.setBold(true);
                                    font.setColor(HSSFColor.GREEN.index);
                                    style.setFont(font);
                                    newRoww.createCell(valuesCounter++).setCellValue(Math.floor(difference));
                                    newRoww.getCell(valuesCounter - 1).setCellStyle(style);

                                }
                                weekHeading.createCell(counterForHeading).setCellValue("WEEK " + week);
                                newSheet.addMergedRegion(new CellRangeAddress(0, 0, counterForHeading, counterForHeading + 2));

                                CellStyle style = workbook.createCellStyle();
                                style.setBorderLeft(CellStyle.BORDER_THIN);
                                style.setAlignment(CellStyle.ALIGN_CENTER);
                                style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
                                weekHeading.getCell(counterForHeading).setCellStyle(style);

                                heading.createCell(counterForHeading++).setCellValue("ACTUAL");
                                newSheet.autoSizeColumn(counterForHeading - 1);
                                heading.getCell(counterForHeading - 1).setCellStyle(style);
                                heading.createCell(counterForHeading++).setCellValue("FORECAST");
                                newSheet.autoSizeColumn(counterForHeading - 1);
                                heading.createCell(counterForHeading++).setCellValue("DIFFERENCE");
                                newSheet.autoSizeColumn(counterForHeading - 1);

                                difference = 0.0;

                            }
                        }
                    }
                    counterForHeading = 1;
                    valuesCounter = 1;
                }


            }
        }
        rowCounter = 0;
        try {
            FileOutputStream out = new FileOutputStream(new File(path));
            try {
                workbook.write(out);
                out.close();
                workbook.close();
                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return toRet;
    }
}
