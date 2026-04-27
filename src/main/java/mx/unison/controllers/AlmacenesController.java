package mx.unison.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.unison.dao.DatabaseHelper;
import mx.unison.models.Almacen;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlmacenesController extends BaseController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtUbicacion;
    @FXML private Label lblError;
    
    @FXML private TableView<Almacen> tablaAlmacenes;
    @FXML private TableColumn<Almacen, Integer> colId;
    @FXML private TableColumn<Almacen, String> colNombre;
    @FXML private TableColumn<Almacen, String> colUbicacion;
    @FXML private TableColumn<Almacen, String> colUltimaMod;

    private ObservableList<Almacen> almacenesList = FXCollections.observableArrayList();
    private Almacen almacenSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colUltimaMod.setCellValueFactory(new PropertyValueFactory<>("fechaHoraUltimaModificacion"));

        tablaAlmacenes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                seleccionarAlmacen(newSelection);
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<Almacen> almacenes = DatabaseHelper.getAlmacenDao().queryForAll();
            almacenesList.setAll(almacenes);
            tablaAlmacenes.setItems(almacenesList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error al cargar almacenes");
        }
    }

    private void seleccionarAlmacen(Almacen almacen) {
        this.almacenSeleccionado = almacen;
        txtNombre.setText(almacen.getNombre());
        txtUbicacion.setText(almacen.getUbicacion());
        lblError.setVisible(false);
    }

    @FXML
    public void guardarAlmacen(ActionEvent event) {
        String nombre = txtNombre.getText();
        String ubicacion = txtUbicacion.getText();

        if (nombre.isEmpty()) {
            showError("El nombre es obligatorio");
            return;
        }

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String usuarioMod = (usuarioActual != null) ? usuarioActual.getNombre() : "SISTEMA";

            if (almacenSeleccionado == null) {
                // Nuevo
                Almacen nuevo = new Almacen();
                nuevo.setNombre(nombre);
                nuevo.setUbicacion(ubicacion);
                nuevo.setFechaHoraCreacion(timestamp);
                nuevo.setFechaHoraUltimaModificacion(timestamp);
                nuevo.setUltimoUsuarioEnModificar(usuarioMod);
                DatabaseHelper.getAlmacenDao().create(nuevo);
            } else {
                // Actualizar
                almacenSeleccionado.setNombre(nombre);
                almacenSeleccionado.setUbicacion(ubicacion);
                almacenSeleccionado.setFechaHoraUltimaModificacion(timestamp);
                almacenSeleccionado.setUltimoUsuarioEnModificar(usuarioMod);
                DatabaseHelper.getAlmacenDao().update(almacenSeleccionado);
            }
            limpiarFormulario(null);
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error al guardar en base de datos");
        }
    }

    @FXML
    public void eliminarAlmacen(ActionEvent event) {
        if (almacenSeleccionado != null) {
            try {
                DatabaseHelper.getAlmacenDao().delete(almacenSeleccionado);
                limpiarFormulario(null);
                cargarDatos();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error al eliminar almacén. Puede que tenga productos asociados.");
            }
        }
    }

    @FXML
    public void limpiarFormulario(ActionEvent event) {
        almacenSeleccionado = null;
        txtNombre.clear();
        txtUbicacion.clear();
        lblError.setVisible(false);
        tablaAlmacenes.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }
}
