package mx.unison;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Pruebas unitarias exhaustivas para la clase FormProducto.
 * Cubre: instanciación en modo creación/edición, estado de saved,
 * precarga de datos, fillModel(), validación de campos,
 * ComboBox de almacenes, caracteres especiales, reflexión.
 */
public class FormProductoTest {

    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
    }

    // ============================================================
    // 1. INSTANCIACIÓN
    // ============================================================

    @Test
    @DisplayName("Instanciación modo creación (Producto nuevo): no lanza excepciones")
    public void testInstanciacionModoCreacion() {
        Producto p = new Producto();
        assertDoesNotThrow(() -> {
            FormProducto form = new FormProducto(null, p, db);
            form.dispose();
        });
    }

    @Test
    @DisplayName("Es una instancia de JDialog")
    public void testEsJDialog() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertInstanceOf(JDialog.class, form);
        form.dispose();
    }

    @Test
    @DisplayName("El título del diálogo es correcto")
    public void testTituloDialogo() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertEquals("Formulario de Producto", form.getTitle());
        form.dispose();
    }

    @Test
    @DisplayName("El diálogo es modal")
    public void testDialogoEsModal() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertEquals(Dialog.ModalityType.APPLICATION_MODAL, form.getModalityType());
        form.dispose();
    }

    @Test
    @DisplayName("Instanciación con owner JFrame")
    public void testInstanciacionConOwner() {
        JFrame frame = new JFrame();
        Producto p = new Producto();
        assertDoesNotThrow(() -> {
            FormProducto form = new FormProducto(frame, p, db);
            form.dispose();
        });
        frame.dispose();
    }

    // ============================================================
    // 2. ESTADO INICIAL DE saved
    // ============================================================

    @Test
    @DisplayName("isSaved() devuelve false por defecto")
    public void testIsSavedFalsePorDefecto() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertFalse(form.isSaved());
        form.dispose();
    }

    @Test
    @DisplayName("isSaved() devuelve false en modo edición antes de guardar")
    public void testIsSavedFalseEdicion() {
        Producto p = new Producto();
        p.nombre = "Laptop";
        p.cantidad = 5;
        p.precio = 100.0;
        FormProducto form = new FormProducto(null, p, db);
        assertFalse(form.isSaved());
        form.dispose();
    }

    // ============================================================
    // 3. PRECARGA DE DATOS EN MODO EDICIÓN
    // ============================================================

    @Test
    @DisplayName("Precarga: nombre del producto aparece en el campo de texto")
    public void testPrecargaNombre() throws Exception {
        Producto p = new Producto();
        p.nombre = "Impresora HP";
        p.cantidad = 3;
        p.precio = 2500.0;
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("Impresora HP", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: descripción del producto aparece en el campo de texto")
    public void testPrecargaDescripcion() throws Exception {
        Producto p = new Producto();
        p.nombre = "Test";
        p.descripcion = "Descripción detallada";
        p.cantidad = 1;
        p.precio = 10.0;
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtDescripcion");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("Descripción detallada", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: cantidad aparece como texto numérico")
    public void testPrecargaCantidad() throws Exception {
        Producto p = new Producto();
        p.nombre = "Test";
        p.cantidad = 42;
        p.precio = 10.0;
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtCantidad");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("42", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: precio aparece como texto numérico")
    public void testPrecargaPrecio() throws Exception {
        Producto p = new Producto();
        p.nombre = "Test";
        p.cantidad = 1;
        p.precio = 999.99;
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtPrecio");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("999.99", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: producto nuevo sin nombre muestra campo vacío")
    public void testPrecargaProductoNuevoNombreVacio() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: producto nuevo sin descripción muestra campo vacío")
    public void testPrecargaProductoNuevoDescVacia() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtDescripcion");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: producto nuevo tiene cantidad '0'")
    public void testPrecargaProductoNuevoCantidadCero() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtCantidad");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("0", txt.getText());
        form.dispose();
    }

    @Test
    @DisplayName("Precarga: producto nuevo tiene precio '0.0'")
    public void testPrecargaProductoNuevoPrecioCero() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);

        Field f = FormProducto.class.getDeclaredField("txtPrecio");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        assertEquals("0.0", txt.getText());
        form.dispose();
    }

    // ============================================================
    // 4. fillModel() — LLENADO DEL MODELO
    // ============================================================

    @Test
    @DisplayName("fillModel() asigna nombre correctamente")
    public void testFillModelNombre() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtNombre", "Monitor LED");
        setTextField(form, "txtCantidad", "5");
        setTextField(form, "txtPrecio", "3500.0");
        form.fillModel(p);
        assertEquals("Monitor LED", p.nombre);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() asigna descripción correctamente")
    public void testFillModelDescripcion() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtDescripcion", "Monitor 27 pulgadas");
        setTextField(form, "txtCantidad", "5");
        setTextField(form, "txtPrecio", "3500.0");
        form.fillModel(p);
        assertEquals("Monitor 27 pulgadas", p.descripcion);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() asigna cantidad correctamente")
    public void testFillModelCantidad() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "25");
        setTextField(form, "txtPrecio", "100.0");
        form.fillModel(p);
        assertEquals(25, p.cantidad);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() asigna precio correctamente")
    public void testFillModelPrecio() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "1299.99");
        form.fillModel(p);
        assertEquals(1299.99, p.precio, 0.01);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() asigna almacenId del ComboBox (primer item = 0)")
    public void testFillModelAlmacenIdSinAlmacen() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "10.0");
        // Por defecto el primer item es "Sin Almacén (Nulo)" con id=0
        form.fillModel(p);
        assertEquals(0, p.almacenId);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() aplica trim al nombre")
    public void testFillModelTrimNombre() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtNombre", "  Laptop  ");
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "10.0");
        form.fillModel(p);
        assertEquals("Laptop", p.nombre);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() aplica trim a la descripción")
    public void testFillModelTrimDescripcion() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtDescripcion", "  Desc con espacios  ");
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "10.0");
        form.fillModel(p);
        assertEquals("Desc con espacios", p.descripcion);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con cantidad cero")
    public void testFillModelCantidadCero() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "0");
        setTextField(form, "txtPrecio", "10.0");
        form.fillModel(p);
        assertEquals(0, p.cantidad);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con precio cero")
    public void testFillModelPrecioCero() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "0.0");
        form.fillModel(p);
        assertEquals(0.0, p.precio, 0.001);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con precio con muchos decimales")
    public void testFillModelPrecioDecimales() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "123.456");
        form.fillModel(p);
        assertEquals(123.456, p.precio, 0.001);
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con caracteres especiales en nombre")
    public void testFillModelCaracteresEspecialesNombre() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtNombre", "Lámina pequeña ñ");
        setTextField(form, "txtCantidad", "1");
        setTextField(form, "txtPrecio", "10.0");
        form.fillModel(p);
        assertEquals("Lámina pequeña ñ", p.nombre);
        form.dispose();
    }

    // ============================================================
    // 5. COMBOBOX DE ALMACENES
    // ============================================================

    @Test
    @DisplayName("ComboBox tiene al menos un item ('Sin Almacén')")
    public void testComboBoxTieneItemsPorDefecto() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("cbAlmacen");
        f.setAccessible(true);
        JComboBox<?> cb = (JComboBox<?>) f.get(form);
        assertTrue(cb.getItemCount() >= 1, "Debe tener al menos el item 'Sin Almacén'");
        form.dispose();
    }

    @Test
    @DisplayName("El primer item del ComboBox es 'Sin Almacén (Nulo)'")
    public void testComboBoxPrimerItem() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("cbAlmacen");
        f.setAccessible(true);
        JComboBox<?> cb = (JComboBox<?>) f.get(form);
        assertEquals("Sin Almacén (Nulo)", cb.getItemAt(0).toString());
        form.dispose();
    }

    @Test
    @DisplayName("ComboBox carga los almacenes de la base de datos")
    public void testComboBoxCargaAlmacenes() throws Exception {
        // Insertar un almacén temporal
        int idAlm = db.insertAlmacen("AlmComboTest", "Loc", "ADMIN");

        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("cbAlmacen");
        f.setAccessible(true);
        JComboBox<?> cb = (JComboBox<?>) f.get(form);

        // Debe tener al menos 2 items: "Sin Almacén" + el almacén insertado
        assertTrue(cb.getItemCount() >= 2);

        // Buscar el almacén en los items
        boolean encontrado = false;
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equals("AlmComboTest")) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "El almacén insertado debe aparecer en el ComboBox");

        form.dispose();
        db.deleteAlmacen(idAlm);
    }

    // ============================================================
    // 6. CAMPOS INTERNOS VÍA REFLEXIÓN
    // ============================================================

    @Test
    @DisplayName("El campo txtNombre existe y es JTextField")
    public void testCampoTxtNombreExiste() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        assertInstanceOf(JTextField.class, f.get(form));
        form.dispose();
    }

    @Test
    @DisplayName("El campo txtDescripcion existe y es JTextField")
    public void testCampoTxtDescripcionExiste() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("txtDescripcion");
        f.setAccessible(true);
        assertInstanceOf(JTextField.class, f.get(form));
        form.dispose();
    }

    @Test
    @DisplayName("El campo txtCantidad existe y es JTextField")
    public void testCampoTxtCantidadExiste() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("txtCantidad");
        f.setAccessible(true);
        assertInstanceOf(JTextField.class, f.get(form));
        form.dispose();
    }

    @Test
    @DisplayName("El campo txtPrecio existe y es JTextField")
    public void testCampoTxtPrecioExiste() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("txtPrecio");
        f.setAccessible(true);
        assertInstanceOf(JTextField.class, f.get(form));
        form.dispose();
    }

    @Test
    @DisplayName("El campo saved existe y es false por defecto")
    public void testCampoSavedExiste() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("saved");
        f.setAccessible(true);
        assertFalse((boolean) f.get(form));
        form.dispose();
    }

    // ============================================================
    // 7. FORZAR saved VÍA REFLEXIÓN
    // ============================================================

    @Test
    @DisplayName("Forzar saved=true: isSaved() devuelve true")
    public void testForzarSavedTrue() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        Field f = FormProducto.class.getDeclaredField("saved");
        f.setAccessible(true);
        f.set(form, true);
        assertTrue(form.isSaved());
        form.dispose();
    }

    // ============================================================
    // 8. LAYOUT Y COMPONENTES
    // ============================================================

    @Test
    @DisplayName("El layout principal es BorderLayout")
    public void testLayoutPrincipal() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertInstanceOf(BorderLayout.class, form.getContentPane().getLayout());
        form.dispose();
    }

    @Test
    @DisplayName("El diálogo tiene componentes hijos")
    public void testTieneComponentes() {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        assertTrue(form.getContentPane().getComponentCount() > 0);
        form.dispose();
    }

    // ============================================================
    // 9. fillModel() CON ERRORES DE PARSEO
    // ============================================================

    @Test
    @DisplayName("fillModel() con cantidad no numérica lanza NumberFormatException")
    public void testFillModelCantidadNoNumerica() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "abc");
        setTextField(form, "txtPrecio", "10.0");
        assertThrows(NumberFormatException.class, () -> form.fillModel(p),
            "fillModel con cantidad no numérica debe lanzar NumberFormatException");
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con precio no numérico lanza NumberFormatException")
    public void testFillModelPrecioNoNumerico() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "5");
        setTextField(form, "txtPrecio", "abc");
        assertThrows(NumberFormatException.class, () -> form.fillModel(p),
            "fillModel con precio no numérico debe lanzar NumberFormatException");
        form.dispose();
    }

    @Test
    @DisplayName("fillModel() con campos vacíos lanza NumberFormatException")
    public void testFillModelCamposVacios() throws Exception {
        Producto p = new Producto();
        FormProducto form = new FormProducto(null, p, db);
        setTextField(form, "txtCantidad", "");
        setTextField(form, "txtPrecio", "");
        assertThrows(NumberFormatException.class, () -> form.fillModel(p));
        form.dispose();
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private void setTextField(FormProducto form, String fieldName, String value) throws Exception {
        Field f = FormProducto.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        txt.setText(value);
    }
}
