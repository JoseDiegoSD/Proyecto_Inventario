package mx.unison.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.unison.Main;
import mx.unison.dao.DatabaseHelper;
import mx.unison.models.Usuario;
import mx.unison.util.CryptoUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = txtUsuario.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor ingrese usuario y contraseña");
            return;
        }

        try {
            String encryptedPass = CryptoUtils.md5(password);
            Usuario user = DatabaseHelper.getUsuarioDao().queryBuilder()
                    .where().eq("nombre", username).and().eq("password", encryptedPass)
                    .queryForFirst();

            if (user != null) {
                // Actualizar último inicio de sesión
                user.setFechaHoraUltimoInicio(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                DatabaseHelper.getUsuarioDao().update(user);
                
                System.out.println("Login exitoso para: " + user.getNombre());
                // Navegar a MainLayout
                goToMainLayout(user);
            } else {
                showError("Credenciales incorrectas");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error al conectar con la base de datos");
        }
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    private void goToMainLayout(Usuario user) {
        try {
            URL url = getClass().getResource("/mx/unison/views/MainLayout.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            MainLayoutController controller = loader.getController();
            controller.setUsuario(user);

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
}
