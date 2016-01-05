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
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Aleksandar Djokic on 12/31/2015.
 */
public class Compare {
	// public static List<Product> lista = new ArrayList<>();

	// public Compare() {
	////
	//// lista.forEach(p->{
	//// System.out.println(p.toString());
	//// });
	// }

	@SuppressWarnings("unchecked")
	public Compare(ReadFromActual actual, ReadFromForecast forecast, String path) {

		Set<String> stringsForCbox = new HashSet<>();
		Stage stage = new Stage();
		stage.setTitle("Bar Chart");

		BorderPane bp = new BorderPane();
		ComboBox<String> cBox = new ComboBox<>();
		cBox.setValue("Select UPC...	");
		
		Label l = new Label("You have to select article to view Bar Chart");
		bp.setAlignment(l, Pos.CENTER);
		bp.setCenter(l);
		
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

		Map<String, Map<String, Double>> toRet = new TreeMap<>();

		for (Map.Entry<String, Map<String, Double>> entryForecast : ReadFromForecast.mapaVelika.entrySet()) {
			Row newRoww = newSheet.createRow(rowCounter++);
			newRoww.createCell(0).setCellValue(entryForecast.getKey());

			Map<String, Double> productValueMapFromForecast = entryForecast.getValue();
			forecastUpc = entryForecast.getKey();
			for (Map.Entry<String, Map<String, Double>> entryActual : ReadFromActual.mapaVelika.entrySet()) {
				String actualUpc = entryActual.getKey();
				stringsForCbox.add(actualUpc);

				if (forecastUpc.equals(actualUpc)) {
					Map<String, Double> productValueMapFromActual = entryActual.getValue();
					for (Map.Entry<String, Double> entryForecast1 : productValueMapFromForecast.entrySet()) {
						for (Map.Entry<String, Double> entryActual1 : productValueMapFromActual.entrySet()) {
							if (entryForecast.getKey().equals(entryActual.getKey())
									&& entryForecast1.getKey().equals(entryActual1.getKey())) {

								week = entryForecast1.getKey();
								difference = entryForecast1.getValue() - entryActual1.getValue();

								// Product p = new Product();
								// p.setMapaVelika(toRet);
								// lista.add(p);

								System.out.println(
										" UPC : " + forecastUpc + "  " + week + "  " + entryForecast1.getValue() + " - "
												+ "  " + entryActual1.getValue() + " = " + difference);

								heading.createCell(0).setCellValue("UPC");
								newSheet.autoSizeColumn(0);

								CellStyle style1 = workbook.createCellStyle();
								style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);

								// if (actualUpc.equals("6005000817478")) {
								// actualChart.getData().add(
								// new XYChart.Data(actualUpc + week,
								// Math.floor(entryActual1.getValue())));
								// forecastChart.getData().add(
								// new XYChart.Data(actualUpc + week,
								// Math.floor(entryForecast1.getValue())));
								// }
								newRoww.createCell(valuesCounter++).setCellValue(Math.floor(entryActual1.getValue()));
								newRoww.getCell(valuesCounter - 1).setCellStyle(style1);
								newRoww.createCell(valuesCounter++)
										.setCellValue((Math.floor(entryForecast1.getValue())));

								if (difference < 0) {
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
								CellStyle style = workbook.createCellStyle();
								style.setBorderLeft(CellStyle.BORDER_THIN);
								style.setAlignment(CellStyle.ALIGN_CENTER);
								style.setVerticalAlignment(CellStyle.ALIGN_CENTER);

								weekHeading.createCell(counterForHeading).setCellValue("WEEK " + week);
								weekHeading.getCell(counterForHeading).setCellStyle(style);

								newSheet.addMergedRegion(
										new CellRangeAddress(0, 0, counterForHeading, counterForHeading + 2));

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
		cBox.getItems().addAll(stringsForCbox);
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

		cBox.setOnAction(e -> {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);

			bc.setTitle("Actual vs Forecast difference chart");
			xAxis.setLabel("Week");
			yAxis.setLabel("Value");

			XYChart.Series actualChart = new XYChart.Series();
			actualChart.setName("Actual");

			XYChart.Series forecastChart = new XYChart.Series();
			forecastChart.setName("Forecast");
			String forecastUpc1 = "";
			String week1 = "";
			Map<String, Map<String, Double>> toRet1 = new TreeMap<>();

			for (Map.Entry<String, Map<String, Double>> entryForecast : ReadFromForecast.mapaVelika.entrySet()) {
				Map<String, Double> productValueMapFromForecast = entryForecast.getValue();
				forecastUpc1 = entryForecast.getKey();
				for (Map.Entry<String, Map<String, Double>> entryActual : ReadFromActual.mapaVelika.entrySet()) {
					String actualUpc = entryActual.getKey();
					stringsForCbox.add(actualUpc);
					if (forecastUpc1.equals(actualUpc)) {
						Map<String, Double> productValueMapFromActual = entryActual.getValue();
						for (Map.Entry<String, Double> entryForecast1 : productValueMapFromForecast.entrySet()) {
							for (Map.Entry<String, Double> entryActual1 : productValueMapFromActual.entrySet()) {
								if (entryForecast.getKey().equals(entryActual.getKey())
										&& entryForecast1.getKey().equals(entryActual1.getKey())) {

									week1 = entryForecast1.getKey();
									if (actualUpc.equals(cBox.getSelectionModel().getSelectedItem().toString())) {
										actualChart.getData()
												.add(new XYChart.Data(week1, Math.floor(entryActual1.getValue())));
										forecastChart.getData()
												.add(new XYChart.Data(week1, Math.floor(entryForecast1.getValue())));
									}
								}
							}
						}
					}
				}
			}
			cBox.getItems().addAll(stringsForCbox);
			bc.getData().addAll(actualChart, forecastChart);
			bp.setCenter(bc);
		});

		bp.setAlignment(cBox, Pos.CENTER);
		bp.setTop(cBox);

		Scene scene = new Scene(bp, 800, 600);

		stage.setScene(scene);
		stage.show();

	}
}
