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

    boolean firstRow = true;
    
    List<Product> listOfProducts = new ArrayList<Product>();
    String[] headers = new String[13];
    
    //Lista UPC-a koja je potrebna zbog provere sa Forecast tabom, kako bi se izbeglo uporedjivanje n elementa.
    List<String> upcs = new ArrayList<>();
    
    //kljuc ove mape je zapravo Universal Product Code (UPC) 
    //vrednost je nova mapa koja za kljc ima redni broj nedelje a za value ima vrednost product-a za datu nedelju
    public static Map<String, Map<String, Double>> mapaVelika = new HashMap<>();

    public ReadFromActual(String path) {
        try {
            FileInputStream fileInput = new FileInputStream(path);
            Workbook wb = null;
            wb = WorkbookFactory.create(fileInput);
            Sheet s = wb.getSheet("ACTUAL");

            Iterator<Row> rows = s.rowIterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                
                //Proveravam da li je prvi red, ako jeste onda to koristim kao headere, tj kao redne brojeve nedelja.
                if (firstRow) {
                    for (int i = 0; i < headers.length; i++) {
                    	//nedelje pocinju od cetvrte kolone
                        headers[i] = row.getCell(i + 3).getStringCellValue().substring(5, row.getCell(i + 3).getStringCellValue().length());
                    }
                }
                if (!firstRow && row.getCell(0).toString().length() > 1) {
                    Map<String, Double> mapa = new HashMap<>();
                    String upc = (long) row.getCell(0).getNumericCellValue() + "";
                    upcs.add(upc);
                    Product p = new Product();
                    for (int i = 0; i < headers.length; i++) {
                        Double value = row.getCell(i + 3).getNumericCellValue();
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