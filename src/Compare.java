import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.scenicview.ScenicView;

import com.sun.javafx.charts.Legend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.Global;

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
	BufferedImage bufferedImage = new BufferedImage(550, 400, BufferedImage.TYPE_INT_ARGB);
	Label copyLabel = new Label("");

	@SuppressWarnings("unchecked")
	public Compare(ReadFromActual actual, ReadFromForecast forecast, String path) {
		Set<String> stringsForCbox = new HashSet<>();
		Stage stage = new Stage();
		stage.setTitle("Bar Chart");

		BorderPane bp = new BorderPane();
		ComboBox<String> cBox = new ComboBox<>();
		cBox.setValue("Select UPC...	");

		GridPane gridForTop = new GridPane();
		gridForTop.setAlignment(Pos.CENTER);
		gridForTop.add(cBox, 0, 1);
		gridForTop.add(copyLabel, 0, 2);

		Label l = new Label("You have to select article to view Bar Chart");
		bp.setAlignment(l, Pos.CENTER);
		bp.setCenter(l);

		int counterForHeading = 2;
		int rowCounter = 2;
		int valuesCounter = 2;
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
			newRoww.createCell(1)
					.setCellValue(GlobalVariables.mapOfProducts.get(Long.parseLong(entryForecast.getKey())));
			newSheet.autoSizeColumn(1);

			Map<String, Double> productValueMapFromForecast = entryForecast.getValue();
			forecastUpc = entryForecast.getKey();
			for (Map.Entry<String, Map<String, Double>> entryActual : ReadFromActual.mapaVelika.entrySet()) {
				String actualUpc = entryActual.getKey();
				stringsForCbox.add(GlobalVariables.mapOfProducts.get(Long.parseLong(actualUpc)));
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

								// System.out.println(
								// " UPC : " + forecastUpc + " " + week + " " +
								// entryForecast1.getValue() + " - "
								// + " " + entryActual1.getValue() + " = " +
								// difference);

								heading.createCell(0).setCellValue("UPC");
								heading.createCell(1).setCellValue("PRODUCT NAME");

								newSheet.autoSizeColumn(0);

								CellStyle style1 = workbook.createCellStyle();
								style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);

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
					counterForHeading = 2;
					valuesCounter = 2;
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
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR!");
			alert.setHeaderText("Problem with writing file!");
			alert.setContentText("Your file might be oppened by another source!");
			alert.showAndWait();
		}

		cBox.setOnAction(e -> {

			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			BarChart bc = new BarChart(xAxis, yAxis);

			bc.setTitle("");
			xAxis.setLabel("Week");
			yAxis.setLabel("Sell Units");

			XYChart.Series actualChart = new XYChart.Series();
			actualChart.setName("Actual");

			XYChart.Series forecastChart = new XYChart.Series();
			forecastChart.setName("Forecast");
			String forecastUpc1 = "";
			String week1 = "";

			for (Map.Entry<String, Map<String, Double>> entryForecast : ReadFromForecast.mapaVelika.entrySet()) {
				Map<String, Double> productValueMapFromForecast = entryForecast.getValue();
				forecastUpc1 = entryForecast.getKey();
				for (Map.Entry<String, Map<String, Double>> entryActual : ReadFromActual.mapaVelika.entrySet()) {
					String actualUpc = entryActual.getKey();

					if (forecastUpc1.equals(actualUpc)) {
						Map<String, Double> productValueMapFromActual = entryActual.getValue();
						for (Map.Entry<String, Double> entryForecast1 : productValueMapFromForecast.entrySet()) {
							for (Map.Entry<String, Double> entryActual1 : productValueMapFromActual.entrySet()) {

								if (entryForecast.getKey().equals(entryActual.getKey())
										&& entryForecast1.getKey().equals(entryActual1.getKey())) {

									week1 = entryForecast1.getKey();
									if (GlobalVariables.mapOfProducts.get(Long.parseLong(actualUpc))
											.equals(cBox.getSelectionModel().getSelectedItem().toString())) {

										bc.setTitle(cBox.getSelectionModel().getSelectedItem().toString());

										Data<String, Double> data = new XYChart.Data<String, Double>(week1,
												Math.floor(entryActual1.getValue()));
										Data<String, Double> data1 = new XYChart.Data<String, Double>(week1,
												Math.floor(entryForecast1.getValue()));

										actualChart.getData().add(data);
										forecastChart.getData().add(data1);
									}
								}
							}
						}
					}
				}
			}
			bc.getData().addAll(actualChart, forecastChart);
			bp.setCenter(bc);
			bp.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					WritableImage snapshot = bp.getCenter().snapshot(new SnapshotParameters(), null);
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putImage(snapshot);
					clipboard.setContent(content);
					copyLabel.setText(
							(cBox.getSelectionModel().getSelectedItem().toString()) + " chart copied to clipboard!");

				}
			});

		});

		BorderPane.setAlignment(gridForTop, Pos.CENTER);
		bp.setTop(gridForTop);

		Scene scene = new Scene(bp, 800, 600);
		stage.setScene(scene);
		stage.getScene().getStylesheets().add("style.css");
		stage.show();

	}
}
