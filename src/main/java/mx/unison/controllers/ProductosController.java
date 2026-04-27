package mx.unison.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.unison.dao.DatabaseHelper;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProductosController extends BaseController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<Almacen> cmbAlmacen;
    @FXML private Label lblError;
    
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colAlmacen;
    @FXML private TableColumn<Producto, String> colUltimaMod;

    private ObservableList<Producto> productosList = FXCollections.observableArrayList();
    private Producto productoSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colUltimaMod.setCellValueFactory(new PropertyValueFactory<>("fechaHoraUltimaModificacion"));
        
        // Custom value factory for Almacen relation
        colAlmacen.setCellValueFactory(cellData -> {
            Almacen a = cellData.getValue().getAlmacen();
            return new SimpleStringProperty(a != null ? a.getNombre() : "Sin Almacén");
        });

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                seleccionarProducto(newSelection);
            }
        });

        cargarAlmacenes();
        cargarDatos();
    }

    private void cargarAlmacenes() {
        try {
            List<Almacen> almacenes = DatabaseHelper.getAlmacenDao().queryForAll();
            cmbAlmacen.setItems(FXCollections.observableArrayList(almacenes));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try {
            List<Producto> productos = DatabaseHelper.getProductoDao().queryForAll();
            productosList.setAll(productos);
            tablaProductos.setItems(productosList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error al cargar productos");
        }
    }

    private void seleccionarProducto(Producto producto) {
        this.productoSeleccionado = producto;
        txtNombre.setText(producto.getNombre());
        txtDescripcion.setText(producto.getDescripcion());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        
        // Seleccionar almacen en el combo si existe
        if (producto.getAlmacen() != null) {
            for (Almacen a : cmbAlmacen.getItems()) {
                if (a.getId() == producto.getAlmacen().getId()) {
                    cmbAlmacen.getSelectionModel().select(a);
                    break;
                }
            }
        } else {
            cmbAlmacen.getSelectionModel().clearSelection();
        }
        
        lblError.setVisible(false);
    }

    @FXML
    public void guardarProducto(ActionEvent event) {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String cantidadStr = txtCantidad.getText();
        String precioStr = txtPrecio.getText();
        Almacen almacen = cmbAlmacen.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            showError("Nombre, cantidad y precio son obligatorios");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String usuarioMod = (usuarioActual != null) ? usuarioActual.getNombre() : "SISTEMA";

            if (productoSeleccionado == null) {
                // Nuevo
                Producto nuevo = new Producto();
                nuevo.setNombre(nombre);
                nuevo.setDescripcion(descripcion);
                nuevo.setCantidad(cantidad);
                nuevo.setPrecio(precio);
                nuevo.setAlmacen(almacen);
                nuevo.setFechaHoraCreacion(timestamp);
                nuevo.setFechaHoraUltimaModificacion(timestamp);
                nuevo.setUltimoUsuarioEnModificar(usuarioMod);
                DatabaseHelper.getProductoDao().create(nuevo);
            } else {
                // Actualizar
                productoSeleccionado.setNombre(nombre);
                productoSeleccionado.setDescripcion(descripcion);
                productoSeleccionado.setCantidad(cantidad);
                productoSeleccionado.setPrecio(precio);
                productoSeleccionado.setAlmacen(almacen);
                productoSeleccionado.setFechaHoraUltimaModificacion(timestamp);
                productoSeleccionado.setUltimoUsuarioEnModificar(usuarioMod);
                DatabaseHelper.getProductoDao().update(productoSeleccionado);
            }
            limpiarFormulario(null);
            cargarDatos();
        } catch (NumberFormatException e) {
            showError("Cantidad debe ser entero y Precio decimal");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error al guardar en base de datos");
        }
    }

    @FXML
    public void eliminarProducto(ActionEvent event) {
        if (productoSeleccionado != null) {
            try {
                DatabaseHelper.getProductoDao().delete(productoSeleccionado);
                limpiarFormulario(null);
                cargarDatos();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error al eliminar producto.");
            }
        }
    }

    @FXML
    public void limpiarFormulario(ActionEvent event) {
        productoSeleccionado = null;
        txtNombre.clear();
        txtDescripcion.clear();
        txtCantidad.clear();
        txtPrecio.clear();
        cmbAlmacen.getSelectionModel().clearSelection();
        lblError.setVisible(false);
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }
}
