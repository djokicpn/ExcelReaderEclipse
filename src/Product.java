import java.util.*;

/**
 * Created by Aleksandar Djokic on 12/30/2015.
 */
public class Product {

    Map<String, Map<String, Double>> mapaVelika = new HashMap<>();

    public Product() {
    }

    public String getValuesFromMap() {
        String toRet = "";
        for (Map.Entry<String, Map<String, Double>> entry : mapaVelika.entrySet()) {
            String upc = entry.getKey();
            Map<String, Double> productValueMap = entry.getValue();
            toRet += "\n \nUPC: " + upc;
            for (Map.Entry<String, Double> entry1 : productValueMap.entrySet()) {
                String week = entry1.getKey();
                double value = entry1.getValue();
                toRet += "\nWEEK : " + week + " VALUE: " + value + ",";
            }
//            toRet = key + Arrays.toString(values);
        }
        return toRet;
    }

    public Map<String, Map<String, Double>> getMapaVelika() {
        return mapaVelika;
    }

    public void setMapaVelika(Map<String, Map<String, Double>> mapaVelika) {
        this.mapaVelika = mapaVelika;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Values=" + getValuesFromMap() +
                '}';
    }
}
