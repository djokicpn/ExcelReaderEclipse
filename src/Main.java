import java.io.IOException;

import com.sun.media.sound.InvalidFormatException;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * Created by Aleksandar Djokic on 12/30/2015.
 */
public class Main extends Application {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Gui();
    }
}
