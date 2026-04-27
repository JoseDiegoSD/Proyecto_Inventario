package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Pruebas unitarias exhaustivas para la clase FormAlmacen.
 * Cubre: instanciación en modo creación y edición, estado inicial de saved,
 * getters de nombre y ubicación, precarga de datos, campos vacíos,
 * caracteres especiales, valores nulos, componentes de la UI.
 */
public class FormAlmacenTest {

    // ============================================================
    // 1. INSTANCIACIÓN — MODO CREACIÓN (Almacen null)
    // ============================================================

    @Test
    @DisplayName("Instanciación modo creación: no lanza excepciones")
    public void testInstanciacionModoCreacion() {
        assertDoesNotThrow(() -> new FormAlmacen(null, null),
            "Crear FormAlmacen con Almacen null (modo creación) no debe lanzar excepción");
    }

    @Test
    @DisplayName("Instanciación modo creación: es una instancia de JDialog")
    public void testEsJDialog() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertInstanceOf(JDialog.class, form);
        form.dispose();
    }

    @Test
    @DisplayName("Instanciación modo creación: el título del diálogo es correcto")
    public void testTituloDialogo() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertEquals("Formulario de Almacén", form.getTitle());
        form.dispose();
    }

    // ============================================================
    // 2. ESTADO INICIAL DE saved
    // ============================================================

    @Test
    @DisplayName("isSaved() devuelve false por defecto (modo creación)")
    public void testIsSavedFalsePorDefectoCreacion() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertFalse(form.isSaved(), "isSaved debe ser false al crear el diálogo");
        form.dispose();
    }

    @Test
    @DisplayName("isSaved() devuelve false por defecto (modo edición)")
    public void testIsSavedFalsePorDefectoEdicion() {
        Almacen a = new Almacen();
        a.nombre = "Test";
        a.ubicacion = "Loc";
        FormAlmacen form = new FormAlmacen(null, a);
        assertFalse(form.isSaved(), "isSaved debe ser false antes de guardar");
        form.dispose();
    }

    // ============================================================
    // 3. GETTERS DE NOMBRE Y UBICACIÓN — MODO CREACIÓN
    // ============================================================

    @Test
    @DisplayName("getNombre() devuelve cadena vacía en modo creación")
    public void testGetNombreVacioModoCreacion() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertEquals("", form.getNombre(), "En modo creación, el nombre debe estar vacío");
        form.dispose();
    }

    @Test
    @DisplayName("getUbicacion() devuelve cadena vacía en modo creación")
    public void testGetUbicacionVacioModoCreacion() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertEquals("", form.getUbicacion(), "En modo creación, la ubicación debe estar vacía");
        form.dispose();
    }

    // ============================================================
    // 4. GETTERS — MODO EDICIÓN (precarga de datos)
    // ============================================================

    @Test
    @DisplayName("getNombre() devuelve el nombre precargado en modo edición")
    public void testGetNombrePrecargado() {
        Almacen a = new Almacen();
        a.nombre = "Bodega Central";
        a.ubicacion = "Hermosillo";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("Bodega Central", form.getNombre());
        form.dispose();
    }

    @Test
    @DisplayName("getUbicacion() devuelve la ubicación precargada en modo edición")
    public void testGetUbicacionPrecargada() {
        Almacen a = new Almacen();
        a.nombre = "Bodega Central";
        a.ubicacion = "Hermosillo, Sonora";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("Hermosillo, Sonora", form.getUbicacion());
        form.dispose();
    }

    // ============================================================
    // 5. PRECARGA CON VALORES ESPECIALES
    // ============================================================

    @Test
    @DisplayName("Modo edición con nombre que tiene espacios: trim los elimina")
    public void testNombreConEspaciosTrim() {
        Almacen a = new Almacen();
        a.nombre = "  Bodega  ";
        a.ubicacion = "Loc";
        FormAlmacen form = new FormAlmacen(null, a);
        // getNombre() hace trim
        assertEquals("Bodega", form.getNombre());
        form.dispose();
    }

    @Test
    @DisplayName("Modo edición con ubicación que tiene espacios: trim los elimina")
    public void testUbicacionConEspaciosTrim() {
        Almacen a = new Almacen();
        a.nombre = "Alm";
        a.ubicacion = "  Sonora  ";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("Sonora", form.getUbicacion());
        form.dispose();
    }

    @Test
    @DisplayName("Modo edición con caracteres especiales en nombre")
    public void testNombreCaracteresEspeciales() {
        Almacen a = new Almacen();
        a.nombre = "Almacén #1 ñ áéíóú";
        a.ubicacion = "Loc";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("Almacén #1 ñ áéíóú", form.getNombre());
        form.dispose();
    }

    @Test
    @DisplayName("Modo edición con caracteres Unicode en ubicación")
    public void testUbicacionUnicode() {
        Almacen a = new Almacen();
        a.nombre = "Alm";
        a.ubicacion = "東京 Tokyo";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("東京 Tokyo", form.getUbicacion());
        form.dispose();
    }

    @Test
    @DisplayName("Modo edición con nombre vacío")
    public void testNombreVacioEdicion() {
        Almacen a = new Almacen();
        a.nombre = "";
        a.ubicacion = "Loc";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("", form.getNombre());
        form.dispose();
    }

    @Test
    @DisplayName("Modo edición con ubicación vacía")
    public void testUbicacionVaciaEdicion() {
        Almacen a = new Almacen();
        a.nombre = "Alm";
        a.ubicacion = "";
        FormAlmacen form = new FormAlmacen(null, a);
        assertEquals("", form.getUbicacion());
        form.dispose();
    }

    // ============================================================
    // 6. CAMPOS INTERNOS VÍA REFLEXIÓN
    // ============================================================

    @Test
    @DisplayName("El campo txtNombre existe y es JTextField")
    public void testCampoTxtNombreExiste() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        Object campo = f.get(form);
        assertNotNull(campo);
        assertInstanceOf(JTextField.class, campo);
        form.dispose();
    }

    @Test
    @DisplayName("El campo txtUbicacion existe y es JTextField")
    public void testCampoTxtUbicacionExiste() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("txtUbicacion");
        f.setAccessible(true);
        Object campo = f.get(form);
        assertNotNull(campo);
        assertInstanceOf(JTextField.class, campo);
        form.dispose();
    }

    @Test
    @DisplayName("El campo saved existe y es boolean false por defecto")
    public void testCampoSavedExiste() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("saved");
        f.setAccessible(true);
        boolean valor = (boolean) f.get(form);
        assertFalse(valor);
        form.dispose();
    }

    // ============================================================
    // 7. MANIPULACIÓN DE CAMPOS INTERNOS
    // ============================================================

    @Test
    @DisplayName("Escribir en txtNombre se refleja en getNombre()")
    public void testEscribirEnTxtNombre() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        txt.setText("Nuevo Almacén");
        assertEquals("Nuevo Almacén", form.getNombre());
        form.dispose();
    }

    @Test
    @DisplayName("Escribir en txtUbicacion se refleja en getUbicacion()")
    public void testEscribirEnTxtUbicacion() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("txtUbicacion");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        txt.setText("Nogales, Sonora");
        assertEquals("Nogales, Sonora", form.getUbicacion());
        form.dispose();
    }

    @Test
    @DisplayName("El getter aplica trim: texto con espacios se recorta")
    public void testGetterAplicaTrim() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("txtNombre");
        f.setAccessible(true);
        JTextField txt = (JTextField) f.get(form);
        txt.setText("   Espacios   ");
        assertEquals("Espacios", form.getNombre());
        form.dispose();
    }

    // ============================================================
    // 8. FORZAR saved=true VÍA REFLEXIÓN Y VERIFICAR
    // ============================================================

    @Test
    @DisplayName("Forzar saved=true: isSaved() devuelve true")
    public void testForzarSavedTrue() throws Exception {
        FormAlmacen form = new FormAlmacen(null, null);
        Field f = FormAlmacen.class.getDeclaredField("saved");
        f.setAccessible(true);
        f.set(form, true);
        assertTrue(form.isSaved());
        form.dispose();
    }

    // ============================================================
    // 9. MODALIDAD DEL DIÁLOGO
    // ============================================================

    @Test
    @DisplayName("El diálogo es modal (APPLICATION_MODAL)")
    public void testDialogoEsModal() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertEquals(Dialog.ModalityType.APPLICATION_MODAL, form.getModalityType());
        form.dispose();
    }

    // ============================================================
    // 10. LAYOUT Y COMPONENTES
    // ============================================================

    @Test
    @DisplayName("El layout principal es BorderLayout")
    public void testLayoutPrincipal() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertInstanceOf(BorderLayout.class, form.getContentPane().getLayout());
        form.dispose();
    }

    @Test
    @DisplayName("El diálogo tiene componentes hijos")
    public void testTieneComponentes() {
        FormAlmacen form = new FormAlmacen(null, null);
        assertTrue(form.getContentPane().getComponentCount() > 0,
            "El diálogo debe tener al menos un componente");
        form.dispose();
    }

    // ============================================================
    // 11. INSTANCIACIÓN CON OWNER
    // ============================================================

    @Test
    @DisplayName("Instanciar con owner JFrame no lanza excepciones")
    public void testInstanciarConOwner() {
        JFrame frame = new JFrame();
        assertDoesNotThrow(() -> {
            FormAlmacen form = new FormAlmacen(frame, null);
            form.dispose();
        });
        frame.dispose();
    }

    @Test
    @DisplayName("Instanciar con owner y almacén existente no lanza excepciones")
    public void testInstanciarConOwnerYAlmacen() {
        JFrame frame = new JFrame();
        Almacen a = new Almacen();
        a.nombre = "Test";
        a.ubicacion = "Loc";
        assertDoesNotThrow(() -> {
            FormAlmacen form = new FormAlmacen(frame, a);
            form.dispose();
        });
        frame.dispose();
    }
}
