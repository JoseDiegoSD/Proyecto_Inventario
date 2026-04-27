package mx.unison;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Pruebas unitarias exhaustivas para la clase PanelProductos.
 * Cubre: instanciación, tipo de componente, layout, tabla (modelo, columnas, no editable),
 * botones (Regresar, Agregar, Modificar, Eliminar), callback onGoBack,
 * carga de datos, reflexión de campos internos.
 */
public class PanelProductosTest {

    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
    }

    // ============================================================
    // 1. INSTANCIACIÓN
    // ============================================================

    @Test
    @DisplayName("Instanciación: no lanza excepciones")
    public void testInstanciacionSinExcepciones() {
        assertDoesNotThrow(() -> new PanelProductos(db, () -> {}));
    }

    @Test
    @DisplayName("Es una instancia de JPanel")
    public void testEsJPanel() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        assertInstanceOf(JPanel.class, panel);
    }

    // ============================================================
    // 2. LAYOUT
    // ============================================================

    @Test
    @DisplayName("El layout principal es BorderLayout")
    public void testLayoutEsBorderLayout() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        assertInstanceOf(BorderLayout.class, panel.getLayout());
    }

    @Test
    @DisplayName("El panel tiene componentes hijos (al menos barra superior y tabla)")
    public void testTieneComponentes() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        assertTrue(panel.getComponentCount() >= 2);
    }

    // ============================================================
    // 3. BOTONES
    // ============================================================

    @Test
    @DisplayName("Existe un botón 'Regresar'")
    public void testExisteBotonRegresar() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        JButton btn = encontrarJButton(panel, "Regresar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Agregar'")
    public void testExisteBotonAgregar() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        JButton btn = encontrarJButton(panel, "Agregar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Modificar'")
    public void testExisteBotonModificar() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        JButton btn = encontrarJButton(panel, "Modificar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Eliminar'")
    public void testExisteBotonEliminar() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        JButton btn = encontrarJButton(panel, "Eliminar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("El botón Regresar ejecuta el callback onGoBack")
    public void testBotonRegresarCallback() {
        java.util.concurrent.atomic.AtomicBoolean ejecutado = new java.util.concurrent.atomic.AtomicBoolean(false);
        PanelProductos panel = new PanelProductos(db, () -> ejecutado.set(true));
        JButton btn = encontrarJButton(panel, "Regresar");
        assertNotNull(btn);
        btn.doClick();
        assertTrue(ejecutado.get());
    }

    @Test
    @DisplayName("El botón Regresar se puede pulsar varias veces")
    public void testBotonRegresarMultipleClics() {
        int[] contador = {0};
        PanelProductos panel = new PanelProductos(db, () -> contador[0]++);
        JButton btn = encontrarJButton(panel, "Regresar");
        assertNotNull(btn);
        btn.doClick();
        btn.doClick();
        btn.doClick();
        assertEquals(3, contador[0]);
    }

    // ============================================================
    // 4. TABLA
    // ============================================================

    @Test
    @DisplayName("El campo table existe y es JTable")
    public void testCampoTableExiste() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("table");
        f.setAccessible(true);
        assertInstanceOf(JTable.class, f.get(panel));
    }

    @Test
    @DisplayName("El modelo de la tabla tiene 9 columnas")
    public void testModeloTiene9Columnas() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);
        assertEquals(9, model.getColumnCount());
    }

    @Test
    @DisplayName("Las columnas son: ID, Nombre, Descripción, Cantidad, Precio, Almacén, Creado, Últ.Mod, Últ.Usuario")
    public void testNombresColumnas() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        assertEquals("ID", model.getColumnName(0));
        assertEquals("Nombre", model.getColumnName(1));
        assertEquals("Descripción", model.getColumnName(2));
        assertEquals("Cantidad", model.getColumnName(3));
        assertEquals("Precio", model.getColumnName(4));
        assertEquals("Almacén", model.getColumnName(5));
        assertEquals("Creado", model.getColumnName(6));
        assertEquals("Últ.Mod", model.getColumnName(7));
        assertEquals("Últ.Usuario", model.getColumnName(8));
    }

    @Test
    @DisplayName("Las celdas de la tabla no son editables")
    public void testCeldasNoEditables() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        model.addRow(new Object[]{1, "Test", "Desc", 5, 100.0, "Alm", "2026-01-01", null, "ADMIN"});
        for (int col = 0; col < model.getColumnCount(); col++) {
            assertFalse(model.isCellEditable(0, col),
                "La columna " + col + " no debe ser editable");
        }
    }

    // ============================================================
    // 5. CARGA DE DATOS
    // ============================================================

    @Test
    @DisplayName("La tabla carga datos de la base de datos al iniciar")
    public void testCargaDatos() throws Exception {
        Producto p = new Producto();
        p.nombre = "ProdPanelTest";
        p.descripcion = "Desc";
        p.cantidad = 1;
        p.precio = 10.0;
        p.almacenId = 0;
        int id = db.insertProducto(p, "ADMIN");

        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        assertTrue(model.getRowCount() > 0);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("La tabla refleja los datos del producto insertado")
    public void testTablaReflejaDatos() throws Exception {
        Producto p = new Producto();
        p.nombre = "ProdReflejo";
        p.descripcion = "DescReflejo";
        p.cantidad = 7;
        p.precio = 250.0;
        p.almacenId = 0;
        int id = db.insertProducto(p, "ADMIN");

        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        boolean encontrado = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == id) {
                assertEquals("ProdReflejo", model.getValueAt(i, 1));
                assertEquals("DescReflejo", model.getValueAt(i, 2));
                assertEquals(7, model.getValueAt(i, 3));
                assertEquals(250.0, model.getValueAt(i, 4));
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "El producto insertado debe reflejarse en la tabla");

        db.deleteProducto(id);
    }

    // ============================================================
    // 6. CAMPOS INTERNOS
    // ============================================================

    @Test
    @DisplayName("El campo db es la misma instancia pasada al constructor")
    public void testCampoDB() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("db");
        f.setAccessible(true);
        assertSame(db, f.get(panel));
    }

    @Test
    @DisplayName("El campo onGoBack no es null")
    public void testCampoOnGoBackNoNull() throws Exception {
        PanelProductos panel = new PanelProductos(db, () -> {});
        Field f = PanelProductos.class.getDeclaredField("onGoBack");
        f.setAccessible(true);
        assertNotNull(f.get(panel));
    }

    // ============================================================
    // 7. SCROLL Y ESTRUCTURA
    // ============================================================

    @Test
    @DisplayName("La tabla está dentro de un JScrollPane")
    public void testTablaEnScrollPane() {
        PanelProductos panel = new PanelProductos(db, () -> {});
        boolean tieneScroll = false;
        for (Component c : panel.getComponents()) {
            if (c instanceof JScrollPane) {
                tieneScroll = true;
                break;
            }
        }
        assertTrue(tieneScroll, "La tabla debe estar dentro de un JScrollPane");
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private JButton encontrarJButton(Container container, String texto) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equals(texto)) {
                return (JButton) c;
            }
            if (c instanceof Container) {
                JButton resultado = encontrarJButton((Container) c, texto);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }
}
