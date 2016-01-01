/**
 * Created by Aleksandar Djokic on 12/30/2015.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

public class ReadFromForecast {
    int brojac = 0;
    int rowCount = 0;
    boolean firstRow = true;
    List<Product> listOfProducts = new ArrayList<Product>();

    public static Map<String, Map<String, Double>> mapaVelika = new HashMap<>();

    public ReadFromForecast(String path, String[] headersFromActual, List<String> upcs) {
        String[] headers = new String[52];
        try {
            FileInputStream fileInput = new FileInputStream(path);
            Workbook wb = null;
            wb = WorkbookFactory.create(fileInput);
            Sheet s = wb.getSheet("FORECAST");
            rowCount = s.getLastRowNum();
            Iterator<Row> rows = s.rowIterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                if (firstRow) {
                    for (int i = 0; i < headers.length; i++) {
//                        System.out.println(row.getCell(i + 11).toString() + " " + (i));
                        String toHeader = row.getCell(i + 11).getNumericCellValue() + "";
                        headers[i] = toHeader.substring(0, toHeader.length() - 2);
                    }
                }
                if (!firstRow && row.getCell(10).toString().length() > 1) {
                    Map<String, Double> mapa = new HashMap<>();

                    String upc = (long) row.getCell(9).getNumericCellValue() + "";
                    for(int b = 0; b<upcs.size();b++){
                        if(upcs.get(b).equals(upc)) {
                            Product p = new Product();
                            for (int i = 0; i < headers.length; i++) {
                                for (int k = 0; k < headersFromActual.length; k++) {
                                    if(headers[i].equals(headersFromActual[k])) {
                                        if (row.getCell(i + 11).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                            Double value = row.getCell(i + 11).getNumericCellValue();
                                            mapa.put(headers[i], value);
                                        } else {
                                            Double value = 0.0;
                                            mapa.put(headers[i], value);
                                        }
                                    }
                                }

                            }
                            mapaVelika.put(upc, mapa);
                            p.setMapaVelika(mapaVelika);
                            listOfProducts.add(p);
                        }
                    }


                }
                firstRow = false;
//                if (row.getCell(0).getCellType() > 2)
//                    break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
//        for (Product p : listOfProducts) {
//            System.out.println(p.toString());
//            break;
//        }
//
//        System.out.println("Week's numbers: " + Arrays.toString(headers));
    }
}
/*
   try{
//                        switch (row.getCell(i).getCellType()) {
//                            case Cell.CELL_TYPE_STRING:
//                                System.out.println();
//
//                                break;
//                            case Cell.CELL_TYPE_NUMERIC:
//                                System.out.println(headers[i] = row.getCell(i + 11).getNumericCellValue() + "");
//                                break;
//                        }}catch (IllegalStateException e) {
//                            e.printStackTrace();
* while (cells.hasNext()) {
                    counter1++;
                    Cell cell = cells.next();

                    switch (cell.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            listOfProducts.forEach(product -> {
                            });
                            System.out.println(counter + "  " + counter1 + "  " + cell.getNumericCellValue());
                            break;

                        case Cell.CELL_TYPE_STRING:

                            if(counter1==1 && !cell.getStringCellValue().equals("UPC")) {
                                Product p = new Product();
                                p.setUpc(cell.getStringCellValue());
                                listOfProducts.add(p);
                            }
                            System.out.println(counter + "  " + counter1 + "  " + cell.getStringCellValue());
                            break;

                        default:
                            break;
                    }
                }
                */