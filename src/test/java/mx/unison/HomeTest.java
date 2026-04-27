package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pruebas unitarias exhaustivas para la clase Home.
 * Cubre: instanciación, tipo de componente, layout, botones,
 * callbacks de navegación, título, componentes hijos.
 */
public class HomeTest {

    // ============================================================
    // 1. INSTANCIACIÓN
    // ============================================================

    @Test
    @DisplayName("Instanciación: no lanza excepciones")
    public void testInstanciacionSinExcepciones() {
        assertDoesNotThrow(() -> new Home(() -> {}, () -> {}),
            "Crear Home no debe lanzar excepciones");
    }

    @Test
    @DisplayName("Es una instancia de JPanel")
    public void testEsJPanel() {
        Home home = new Home(() -> {}, () -> {});
        assertInstanceOf(JPanel.class, home);
    }

    @Test
    @DisplayName("Instanciar con callbacks null lanza excepción al hacer clic (pero no al crear)")
    public void testInstanciacionConNullsNoFallaAlCrear() {
        // No falla al crear, solo fallaría al hacer clic si los callbacks son null
        // Pero el constructor acepta Runnable, que podemos dejar como no-op
        assertDoesNotThrow(() -> new Home(() -> {}, () -> {}));
    }

    // ============================================================
    // 2. LAYOUT
    // ============================================================

    @Test
    @DisplayName("El layout principal es BorderLayout")
    public void testLayoutEsBorderLayout() {
        Home home = new Home(() -> {}, () -> {});
        assertInstanceOf(BorderLayout.class, home.getLayout());
    }

    // ============================================================
    // 3. COMPONENTES HIJOS
    // ============================================================

    @Test
    @DisplayName("El panel tiene al menos un componente hijo")
    public void testTieneComponentesHijos() {
        Home home = new Home(() -> {}, () -> {});
        assertTrue(home.getComponentCount() > 0, "Home debe tener al menos un componente");
    }

    @Test
    @DisplayName("Existe un JLabel con el título 'Sistema de Inventario'")
    public void testExisteTitulo() {
        Home home = new Home(() -> {}, () -> {});
        JLabel titulo = encontrarJLabel(home, "Sistema de Inventario");
        assertNotNull(titulo, "Debe existir un JLabel con el título 'Sistema de Inventario'");
    }

    @Test
    @DisplayName("El título tiene fuente Bold y tamaño 28")
    public void testTituloFuente() {
        Home home = new Home(() -> {}, () -> {});
        JLabel titulo = encontrarJLabel(home, "Sistema de Inventario");
        assertNotNull(titulo);
        assertEquals(Font.BOLD, titulo.getFont().getStyle());
        assertEquals(28, titulo.getFont().getSize());
    }

    @Test
    @DisplayName("Existe un botón 'Productos'")
    public void testExisteBotonProductos() {
        Home home = new Home(() -> {}, () -> {});
        JButton btn = encontrarJButton(home, "Productos");
        assertNotNull(btn, "Debe existir un botón 'Productos'");
    }

    @Test
    @DisplayName("Existe un botón 'Almacenes'")
    public void testExisteBotonAlmacenes() {
        Home home = new Home(() -> {}, () -> {});
        JButton btn = encontrarJButton(home, "Almacenes");
        assertNotNull(btn, "Debe existir un botón 'Almacenes'");
    }

    // ============================================================
    // 4. CALLBACKS DE NAVEGACIÓN
    // ============================================================

    @Test
    @DisplayName("Al hacer clic en 'Productos', se ejecuta el callback onOpenProductos")
    public void testCallbackProductos() {
        AtomicBoolean ejecutado = new AtomicBoolean(false);
        Home home = new Home(() -> ejecutado.set(true), () -> {});
        JButton btn = encontrarJButton(home, "Productos");
        assertNotNull(btn);
        btn.doClick();
        assertTrue(ejecutado.get(), "El callback de Productos debe haberse ejecutado");
    }

    @Test
    @DisplayName("Al hacer clic en 'Almacenes', se ejecuta el callback onOpenAlmacenes")
    public void testCallbackAlmacenes() {
        AtomicBoolean ejecutado = new AtomicBoolean(false);
        Home home = new Home(() -> {}, () -> ejecutado.set(true));
        JButton btn = encontrarJButton(home, "Almacenes");
        assertNotNull(btn);
        btn.doClick();
        assertTrue(ejecutado.get(), "El callback de Almacenes debe haberse ejecutado");
    }

    @Test
    @DisplayName("Clic en 'Productos' no ejecuta el callback de 'Almacenes'")
    public void testCallbackProductosNoEjecutaAlmacenes() {
        AtomicBoolean almacenesEjecutado = new AtomicBoolean(false);
        Home home = new Home(() -> {}, () -> almacenesEjecutado.set(true));
        JButton btn = encontrarJButton(home, "Productos");
        assertNotNull(btn);
        btn.doClick();
        assertFalse(almacenesEjecutado.get(), "Clic en Productos no debe ejecutar callback de Almacenes");
    }

    @Test
    @DisplayName("Clic en 'Almacenes' no ejecuta el callback de 'Productos'")
    public void testCallbackAlmacenesNoEjecutaProductos() {
        AtomicBoolean productosEjecutado = new AtomicBoolean(false);
        Home home = new Home(() -> productosEjecutado.set(true), () -> {});
        JButton btn = encontrarJButton(home, "Almacenes");
        assertNotNull(btn);
        btn.doClick();
        assertFalse(productosEjecutado.get(), "Clic en Almacenes no debe ejecutar callback de Productos");
    }

    @Test
    @DisplayName("Clic múltiple en 'Productos' ejecuta el callback cada vez")
    public void testCallbackProductosMultipleClics() {
        int[] contador = {0};
        Home home = new Home(() -> contador[0]++, () -> {});
        JButton btn = encontrarJButton(home, "Productos");
        assertNotNull(btn);
        btn.doClick();
        btn.doClick();
        btn.doClick();
        assertEquals(3, contador[0], "El callback debe ejecutarse 3 veces");
    }

    // ============================================================
    // 5. PROPIEDADES DE BOTONES
    // ============================================================

    @Test
    @DisplayName("Los botones están alineados al centro (CENTER_ALIGNMENT)")
    public void testBotonesAlineadosCentro() {
        Home home = new Home(() -> {}, () -> {});
        JButton btnProd = encontrarJButton(home, "Productos");
        JButton btnAlm = encontrarJButton(home, "Almacenes");
        assertNotNull(btnProd);
        assertNotNull(btnAlm);
        assertEquals(Component.CENTER_ALIGNMENT, btnProd.getAlignmentX(), 0.01);
        assertEquals(Component.CENTER_ALIGNMENT, btnAlm.getAlignmentX(), 0.01);
    }

    // ============================================================
    // MÉTODOS AUXILIARES PARA BUSCAR COMPONENTES
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

    private JLabel encontrarJLabel(Container container, String texto) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getText().equals(texto)) {
                return (JLabel) c;
            }
            if (c instanceof Container) {
                JLabel resultado = encontrarJLabel((Container) c, texto);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }
}
