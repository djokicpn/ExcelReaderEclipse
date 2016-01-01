/**
 * Created by Aleksandar Djokic on 12/30/2015.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

public class ReadFromActual {

    int brojac = 0;
    int rowCount = 0;
    boolean firstRow = true;
    List<Product> listOfProducts = new ArrayList<Product>();
    String[] headers = new String[13];
    List<String> upcs = new ArrayList<>();
    public static Map<String, Map<String, Double>> mapaVelika = new HashMap<>();

    public ReadFromActual(String path) {
        try {
            FileInputStream fileInput = new FileInputStream(path);
            Workbook wb = null;
            wb = WorkbookFactory.create(fileInput);
            Sheet s = wb.getSheet("ACTUAL");
            rowCount = s.getLastRowNum();
            Iterator<Row> rows = s.rowIterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                if (firstRow) {
                    for (int i = 0; i < headers.length; i++) {
                        headers[i] = row.getCell(i + 1).getStringCellValue().substring(5, row.getCell(i + 1).getStringCellValue().length());
                    }
                }
                if (!firstRow && row.getCell(0).toString().length() > 1) {
                    Map<String, Double> mapa = new HashMap<>();
                    String upc = row.getCell(0).toString() + "";
                    upcs.add(upc);
                    Product p = new Product();
                    for (int i = 0; i < headers.length; i++) {
                        Double value = row.getCell(i + 1).getNumericCellValue();
                        mapa.put(headers[i], value);
                    }
                    mapaVelika.put(upc, mapa);
                    p.setMapaVelika(mapaVelika);
                    listOfProducts.add(p);
                }
                firstRow = false;
                if (row.getCell(0).getCellType() > 2)
                    break;
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

//        System.out.println("Week's numbers: " + Arrays.toString(headers));

    }

    public Map<String, Map<String, Double>> ReadFromFile() {
        return mapaVelika;
    }

    public int getBrojac() {
        return brojac;
    }

    public void setBrojac(int brojac) {
        this.brojac = brojac;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isFirstRow() {
        return firstRow;
    }

    public void setFirstRow(boolean firstRow) {
        this.firstRow = firstRow;
    }

    public List<String> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<String> upcs) {
        this.upcs = upcs;
    }

    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public static Map<String, Map<String, Double>> getMapaVelika() {
        return mapaVelika;
    }

    public static void setMapaVelika(Map<String, Map<String, Double>> mapaVelika) {
        ReadFromActual.mapaVelika = mapaVelika;
    }
}


/*
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