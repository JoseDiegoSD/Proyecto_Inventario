package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Pruebas unitarias exhaustivas para la clase Vistas.
 * Cubre: instanciación, tipo de componente, título, tamaño,
 * CardLayout, contenedor con paneles (LOGIN, INICIO, PRODUCTOS, ALMACENES),
 * panel inicial visible, campos internos.
 */
public class VistasTest {

    private Vistas vistas;

    @AfterEach
    public void tearDown() {
        if (vistas != null) {
            vistas.dispose();
        }
    }

    // ============================================================
    // 1. INSTANCIACIÓN
    // ============================================================

    @Test
    @DisplayName("Instanciación: no lanza excepciones")
    public void testInstanciacionSinExcepciones() {
        assertDoesNotThrow(() -> {
            vistas = new Vistas();
        });
    }

    @Test
    @DisplayName("Es una instancia de JFrame")
    public void testEsJFrame() {
        vistas = new Vistas();
        assertInstanceOf(JFrame.class, vistas);
    }

    // ============================================================
    // 2. PROPIEDADES DEL FRAME
    // ============================================================

    @Test
    @DisplayName("El título es 'Sistema de Inventario - Cliente'")
    public void testTitulo() {
        vistas = new Vistas();
        assertEquals("Sistema de Inventario - Cliente", vistas.getTitle());
    }

    @Test
    @DisplayName("El tamaño es 1000x720")
    public void testTamano() {
        vistas = new Vistas();
        assertEquals(1000, vistas.getWidth());
        assertEquals(720, vistas.getHeight());
    }

    @Test
    @DisplayName("La operación de cierre es EXIT_ON_CLOSE")
    public void testOperacionCierre() {
        vistas = new Vistas();
        assertEquals(JFrame.EXIT_ON_CLOSE, vistas.getDefaultCloseOperation());
    }

    // ============================================================
    // 3. CARDLAYOUT Y CONTENEDOR
    // ============================================================

    @Test
    @DisplayName("El campo cardLayout existe y es CardLayout")
    public void testCampoCardLayout() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("cardLayout");
        f.setAccessible(true);
        Object cl = f.get(vistas);
        assertNotNull(cl);
        assertInstanceOf(CardLayout.class, cl);
    }

    @Test
    @DisplayName("El campo container existe y es JPanel")
    public void testCampoContainer() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        Object c = f.get(vistas);
        assertNotNull(c);
        assertInstanceOf(JPanel.class, c);
    }

    @Test
    @DisplayName("El container tiene exactamente 4 paneles (LOGIN, INICIO, PRODUCTOS, ALMACENES)")
    public void testContainerTiene4Paneles() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertEquals(4, container.getComponentCount(),
            "El container debe tener 4 paneles: LOGIN, INICIO, PRODUCTOS, ALMACENES");
    }

    @Test
    @DisplayName("El container usa CardLayout")
    public void testContainerUsaCardLayout() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertInstanceOf(CardLayout.class, container.getLayout());
    }

    // ============================================================
    // 4. PANELES INTERNOS
    // ============================================================

    @Test
    @DisplayName("El primer panel del container es Login")
    public void testPrimerPanelEsLogin() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertInstanceOf(Login.class, container.getComponent(0));
    }

    @Test
    @DisplayName("El segundo panel del container es Home")
    public void testSegundoPanelEsHome() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertInstanceOf(Home.class, container.getComponent(1));
    }

    @Test
    @DisplayName("El tercer panel del container es PanelProductos")
    public void testTercerPanelEsPanelProductos() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertInstanceOf(PanelProductos.class, container.getComponent(2));
    }

    @Test
    @DisplayName("El cuarto panel del container es AlmacenesPanel")
    public void testCuartoPanelEsAlmacenesPanel() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);
        assertInstanceOf(AlmacenesPanel.class, container.getComponent(3));
    }

    // ============================================================
    // 5. PANEL INICIAL VISIBLE
    // ============================================================

    @Test
    @DisplayName("El panel Login es visible al inicio")
    public void testPanelLoginVisibleAlInicio() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);

        // En CardLayout, el panel visible es el que tiene isVisible=true
        Component login = container.getComponent(0);
        assertTrue(login.isVisible(), "El panel de Login debe ser visible al inicio");
    }

    // ============================================================
    // 6. CAMPO DB
    // ============================================================

    @Test
    @DisplayName("El campo db existe y no es null")
    public void testCampoDB() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("db");
        f.setAccessible(true);
        Object dbObj = f.get(vistas);
        assertNotNull(dbObj, "La instancia de Database no debe ser null");
        assertInstanceOf(Database.class, dbObj);
    }

    // ============================================================
    // 7. MÉTODOS PRIVADOS VÍA REFLEXIÓN
    // ============================================================

    @Test
    @DisplayName("El método showHome existe")
    public void testMetodoShowHomeExiste() {
        vistas = new Vistas();
        assertDoesNotThrow(() -> {
            var m = Vistas.class.getDeclaredMethod("showHome", String.class);
            assertNotNull(m);
        });
    }

    @Test
    @DisplayName("El método showPanel existe")
    public void testMetodoShowPanelExiste() {
        vistas = new Vistas();
        assertDoesNotThrow(() -> {
            var m = Vistas.class.getDeclaredMethod("showPanel", String.class);
            assertNotNull(m);
        });
    }

    @Test
    @DisplayName("showPanel('INICIO') no lanza excepciones")
    public void testShowPanelInicio() throws Exception {
        vistas = new Vistas();
        var m = Vistas.class.getDeclaredMethod("showPanel", String.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(vistas, "INICIO"));
    }

    @Test
    @DisplayName("showPanel('PRODUCTOS') no lanza excepciones")
    public void testShowPanelProductos() throws Exception {
        vistas = new Vistas();
        var m = Vistas.class.getDeclaredMethod("showPanel", String.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(vistas, "PRODUCTOS"));
    }

    @Test
    @DisplayName("showPanel('ALMACENES') no lanza excepciones")
    public void testShowPanelAlmacenes() throws Exception {
        vistas = new Vistas();
        var m = Vistas.class.getDeclaredMethod("showPanel", String.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(vistas, "ALMACENES"));
    }

    @Test
    @DisplayName("showPanel('LOGIN') no lanza excepciones")
    public void testShowPanelLogin() throws Exception {
        vistas = new Vistas();
        var m = Vistas.class.getDeclaredMethod("showPanel", String.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(vistas, "LOGIN"));
    }

    @Test
    @DisplayName("showHome con cualquier nombre no lanza excepciones")
    public void testShowHomeNoLanzaExcepciones() throws Exception {
        vistas = new Vistas();
        var m = Vistas.class.getDeclaredMethod("showHome", String.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(vistas, "ADMIN"));
    }

    // ============================================================
    // 8. CONTENIDO DEL FRAME
    // ============================================================

    @Test
    @DisplayName("El contentPane del frame contiene el container")
    public void testContentPaneContieneContainer() throws Exception {
        vistas = new Vistas();
        Field f = Vistas.class.getDeclaredField("container");
        f.setAccessible(true);
        JPanel container = (JPanel) f.get(vistas);

        boolean encontrado = false;
        for (Component c : vistas.getContentPane().getComponents()) {
            if (c == container) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "El contentPane debe contener el container");
    }
}
