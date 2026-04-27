package mx.unison.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mx.unison.Main;
import mx.unison.models.Usuario;

import java.io.IOException;
import java.net.URL;

public class MainLayoutController {

    @FXML private BorderPane mainPane;
    @FXML private Label lblBienvenida;
    @FXML private VBox contentArea;
    
    private Usuario usuarioActual;

    public void setUsuario(Usuario user) {
        this.usuarioActual = user;
        lblBienvenida.setText("Bienvenido, " + user.getNombre() + " (" + user.getRol() + ")");
    }

    @FXML
    public void showProductos(ActionEvent event) {
        loadView("/mx/unison/views/Productos.fxml");
    }

    @FXML
    public void showAlmacenes(ActionEvent event) {
        loadView("/mx/unison/views/Almacenes.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            URL url = getClass().getResource("/mx/unison/views/Login.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 1000, 720);
            URL cssUrl = getClass().getResource("/mx/unison/views/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            Main.primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("Vista no encontrada: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent view = loader.load();
            
            // Pasar usuario si el controlador lo soporta
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUsuario(usuarioActual);
            }
            
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
