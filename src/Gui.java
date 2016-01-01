import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Aleksandar Djokic on 12/31/2015.
 */
public class Gui {

    private String filePath = "";
    private String savedFilePath = "";

    public Gui() {
        Stage stage = new Stage();

        GridPane gp = new GridPane();
        FlowPane fp = new FlowPane();

        TextField label = new TextField("");
        Button btnOk = new Button("Add file");
        Button btnCalculate = new Button("Calculate");

        fp.getChildren().add(btnOk);
        fp.getChildren().add(btnCalculate);
        fp.setAlignment(Pos.CENTER);

        btnCalculate.setDisable(true);
        label.setDisable(true);

        gp.add(label, 0, 1);
        gp.add(fp, 0, 2);
        gp.setAlignment(Pos.CENTER);

        btnOk.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel File", "*.xlsx")
            );
            File file = fc.showOpenDialog(new Stage());
            filePath = file.getAbsolutePath();
            label.setText(filePath);
            btnCalculate.setDisable(false);

        });
        btnCalculate.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("*.xls");
            File file = fc.showSaveDialog(new Stage());
            if (file != null) {
                ReadFromActual readFromActual = new ReadFromActual(filePath);
                ReadFromForecast readFromForecast = new ReadFromForecast(filePath, readFromActual.getHeaders(), readFromActual.getUpcs());
                Compare.compare(readFromActual, readFromForecast, file.getAbsolutePath());
                savedFilePath = file.getAbsolutePath();
                showInfoAlert();
                System.exit(0);
            }


        });

        stage.setScene(new Scene(gp, 200, 50));
        stage.setResizable(false);
        stage.setTitle("CTG");
        stage.setIconified(false);
        stage.show();

    }


    private void showInfoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Done !");
        alert.setContentText("Comparing values done ! File saved on: " + savedFilePath);

        alert.showAndWait();
    }
}
