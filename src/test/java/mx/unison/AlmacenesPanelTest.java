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
 * Pruebas unitarias exhaustivas para la clase AlmacenesPanel.
 * Cubre: instanciación, tipo de componente, layout, tabla (modelo, columnas, no editable),
 * botones (Regresar, Agregar, Modificar, Eliminar), callback onGoBack,
 * carga de datos, reflexión de campos internos.
 */
public class AlmacenesPanelTest {

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
        assertDoesNotThrow(() -> new AlmacenesPanel(db, () -> {}));
    }

    @Test
    @DisplayName("Es una instancia de JPanel")
    public void testEsJPanel() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        assertInstanceOf(JPanel.class, panel);
    }

    // ============================================================
    // 2. LAYOUT
    // ============================================================

    @Test
    @DisplayName("El layout principal es BorderLayout")
    public void testLayoutEsBorderLayout() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        assertInstanceOf(BorderLayout.class, panel.getLayout());
    }

    @Test
    @DisplayName("El panel tiene componentes hijos (al menos barra superior y tabla)")
    public void testTieneComponentes() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        assertTrue(panel.getComponentCount() >= 2,
            "Debe tener al menos la barra de botones (NORTH) y la tabla (CENTER)");
    }

    // ============================================================
    // 3. BOTONES
    // ============================================================

    @Test
    @DisplayName("Existe un botón 'Regresar'")
    public void testExisteBotonRegresar() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        JButton btn = encontrarJButton(panel, "Regresar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Agregar'")
    public void testExisteBotonAgregar() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        JButton btn = encontrarJButton(panel, "Agregar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Modificar'")
    public void testExisteBotonModificar() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        JButton btn = encontrarJButton(panel, "Modificar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("Existe un botón 'Eliminar'")
    public void testExisteBotonEliminar() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        JButton btn = encontrarJButton(panel, "Eliminar");
        assertNotNull(btn);
    }

    @Test
    @DisplayName("El botón Regresar ejecuta el callback onGoBack")
    public void testBotonRegresarCallback() {
        java.util.concurrent.atomic.AtomicBoolean ejecutado = new java.util.concurrent.atomic.AtomicBoolean(false);
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> ejecutado.set(true));
        JButton btn = encontrarJButton(panel, "Regresar");
        assertNotNull(btn);
        btn.doClick();
        assertTrue(ejecutado.get(), "El callback onGoBack debe ejecutarse al pulsar 'Regresar'");
    }

    // ============================================================
    // 4. TABLA
    // ============================================================

    @Test
    @DisplayName("El campo table existe y es JTable")
    public void testCampoTableExiste() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("table");
        f.setAccessible(true);
        Object tabla = f.get(panel);
        assertNotNull(tabla);
        assertInstanceOf(JTable.class, tabla);
    }

    @Test
    @DisplayName("El modelo de la tabla tiene 6 columnas")
    public void testModeloTiene6Columnas() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);
        assertEquals(6, model.getColumnCount(), "La tabla debe tener 6 columnas");
    }

    @Test
    @DisplayName("Las columnas de la tabla son: ID, Nombre, Ubicación, Creado, Últ.Mod, Últ.Usuario")
    public void testNombresColumnas() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        assertEquals("ID", model.getColumnName(0));
        assertEquals("Nombre", model.getColumnName(1));
        assertEquals("Ubicación", model.getColumnName(2));
        assertEquals("Creado", model.getColumnName(3));
        assertEquals("Últ.Mod", model.getColumnName(4));
        assertEquals("Últ.Usuario", model.getColumnName(5));
    }

    @Test
    @DisplayName("Las celdas de la tabla no son editables")
    public void testCeldasNoEditables() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        // Insertar una fila de prueba para que isCellEditable tenga sentido
        model.addRow(new Object[]{1, "Test", "Loc", "2026-01-01", null, "ADMIN"});
        assertFalse(model.isCellEditable(0, 0), "Las celdas no deben ser editables");
        assertFalse(model.isCellEditable(0, 1), "Las celdas no deben ser editables");
        assertFalse(model.isCellEditable(0, 2), "Las celdas no deben ser editables");
    }

    // ============================================================
    // 5. CARGA DE DATOS
    // ============================================================

    @Test
    @DisplayName("La tabla se carga con datos de la base de datos al iniciar")
    public void testCargaDatos() throws Exception {
        // Insertar un almacén para asegurar que hay datos
        int id = db.insertAlmacen("AlmPanelTest", "LocTest", "ADMIN");

        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        assertTrue(model.getRowCount() > 0, "La tabla debe tener al menos una fila después de la carga");

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("La tabla refleja los datos del almacén insertado")
    public void testTablaReflajaDatos() throws Exception {
        int id = db.insertAlmacen("AlmReflejo", "LocReflejo", "ADMIN");

        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("model");
        f.setAccessible(true);
        DefaultTableModel model = (DefaultTableModel) f.get(panel);

        boolean encontrado = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == id) {
                assertEquals("AlmReflejo", model.getValueAt(i, 1));
                assertEquals("LocReflejo", model.getValueAt(i, 2));
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "El almacén insertado debe aparecer en la tabla");

        db.deleteAlmacen(id);
    }

    // ============================================================
    // 6. CAMPOS INTERNOS
    // ============================================================

    @Test
    @DisplayName("El campo db es la misma instancia pasada al constructor")
    public void testCampoDB() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("db");
        f.setAccessible(true);
        assertSame(db, f.get(panel), "El campo db debe ser la misma referencia pasada al constructor");
    }

    @Test
    @DisplayName("El campo onGoBack no es null")
    public void testCampoOnGoBackNoNull() throws Exception {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
        Field f = AlmacenesPanel.class.getDeclaredField("onGoBack");
        f.setAccessible(true);
        assertNotNull(f.get(panel));
    }

    // ============================================================
    // 7. ESTRUCTURA DE SCROLL
    // ============================================================

    @Test
    @DisplayName("La tabla está dentro de un JScrollPane")
    public void testTablaEnScrollPane() {
        AlmacenesPanel panel = new AlmacenesPanel(db, () -> {});
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
