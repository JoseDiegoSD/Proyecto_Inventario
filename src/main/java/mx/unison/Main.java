package mx.unison;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.unison.dao.DatabaseHelper;

import java.net.URL;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        // Inicializar base de datos
        DatabaseHelper.init();
        
        // Cargar vista de Login inicial
        URL loginUrl = getClass().getResource("/mx/unison/views/Login.fxml");
        if (loginUrl == null) {
            System.err.println("No se encontró Login.fxml. Asegúrate de que esté en src/main/resources/mx/unison/views/");
            System.exit(1);
        }
        Parent root = FXMLLoader.load(loginUrl);
        Scene scene = new Scene(root, 1000, 720);
        
        // Cargar CSS
        URL cssUrl = getClass().getResource("/mx/unison/views/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Sistema de Inventario - Cliente");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        DatabaseHelper.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
