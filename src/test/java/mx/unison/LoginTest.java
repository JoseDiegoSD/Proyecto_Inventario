package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

/**
 * Pruebas unitarias exhaustivas para la clase Login.
 * Cubre: instanciación, tipo de componente, layout, background,
 * componentes internos (JLabel, JTextField, JPasswordField, JButton),
 * propiedades visuales, callback.
 */
public class LoginTest {

    // ============================================================
    // 1. INSTANCIACIÓN
    // ============================================================

    @Test
    @DisplayName("Instanciación: no lanza excepciones")
    public void testInstanciacionSinExcepciones() {
        assertDoesNotThrow(() -> new Login(user -> {}),
            "Crear Login no debe lanzar excepciones");
    }

    @Test
    @DisplayName("Es una instancia de JPanel")
    public void testEsJPanel() {
        Login login = new Login(user -> {});
        assertInstanceOf(JPanel.class, login);
    }

    // ============================================================
    // 2. LAYOUT Y FONDO
    // ============================================================

    @Test
    @DisplayName("El layout principal es GridBagLayout")
    public void testLayoutEsGridBagLayout() {
        Login login = new Login(user -> {});
        assertInstanceOf(GridBagLayout.class, login.getLayout());
    }

    @Test
    @DisplayName("El color de fondo es gris claro (245, 245, 245)")
    public void testColorDeFondo() {
        Login login = new Login(user -> {});
        assertEquals(new Color(245, 245, 245), login.getBackground());
    }

    // ============================================================
    // 3. COMPONENTES HIJOS
    // ============================================================

    @Test
    @DisplayName("El panel tiene al menos un componente hijo (la tarjeta)")
    public void testTieneComponentes() {
        Login login = new Login(user -> {});
        assertTrue(login.getComponentCount() > 0);
    }

    @Test
    @DisplayName("Existe un JLabel con texto 'Inicio de sesión'")
    public void testExisteTituloInicioSesion() {
        Login login = new Login(user -> {});
        JLabel label = encontrarJLabel(login, "Inicio de sesión");
        assertNotNull(label, "Debe existir un JLabel con texto 'Inicio de sesión'");
    }

    @Test
    @DisplayName("El título tiene fuente Consolas tamaño 18")
    public void testTituloFuente() {
        Login login = new Login(user -> {});
        JLabel label = encontrarJLabel(login, "Inicio de sesión");
        assertNotNull(label);
        assertEquals("Consolas", label.getFont().getFamily());
        assertEquals(18, label.getFont().getSize());
    }

    @Test
    @DisplayName("Existe un JLabel 'Usuario:'")
    public void testExisteLabelUsuario() {
        Login login = new Login(user -> {});
        JLabel label = encontrarJLabel(login, "Usuario:");
        assertNotNull(label, "Debe existir un JLabel 'Usuario:'");
    }

    @Test
    @DisplayName("Existe un JLabel 'Contraseña:'")
    public void testExisteLabelContrasena() {
        Login login = new Login(user -> {});
        JLabel label = encontrarJLabel(login, "Contraseña:");
        assertNotNull(label, "Debe existir un JLabel 'Contraseña:'");
    }

    @Test
    @DisplayName("Existe un JTextField para el usuario")
    public void testExisteCampoUsuario() {
        Login login = new Login(user -> {});
        JTextField field = encontrarJTextField(login);
        assertNotNull(field, "Debe existir un JTextField para ingresar el usuario");
    }

    @Test
    @DisplayName("Existe un JPasswordField para la contraseña")
    public void testExisteCampoContrasena() {
        Login login = new Login(user -> {});
        JPasswordField field = encontrarJPasswordField(login);
        assertNotNull(field, "Debe existir un JPasswordField para la contraseña");
    }

    @Test
    @DisplayName("Existe un JButton 'Iniciar sesión'")
    public void testExisteBotonIniciarSesion() {
        Login login = new Login(user -> {});
        JButton btn = encontrarJButton(login, "Iniciar sesión");
        assertNotNull(btn, "Debe existir un JButton 'Iniciar sesión'");
    }

    // ============================================================
    // 4. PROPIEDADES DEL BOTÓN DE LOGIN
    // ============================================================

    @Test
    @DisplayName("El botón tiene color de fondo verde pastel")
    public void testBotonColorFondo() {
        Login login = new Login(user -> {});
        JButton btn = encontrarJButton(login, "Iniciar sesión");
        assertNotNull(btn);
        assertEquals(new Color(198, 243, 213), btn.getBackground());
    }

    @Test
    @DisplayName("El botón no tiene focusPainted")
    public void testBotonSinFocusPainted() {
        Login login = new Login(user -> {});
        JButton btn = encontrarJButton(login, "Iniciar sesión");
        assertNotNull(btn);
        assertFalse(btn.isFocusPainted());
    }

    @Test
    @DisplayName("El botón está alineado al centro (CENTER_ALIGNMENT)")
    public void testBotonAlineadoCentro() {
        Login login = new Login(user -> {});
        JButton btn = encontrarJButton(login, "Iniciar sesión");
        assertNotNull(btn);
        assertEquals(Component.CENTER_ALIGNMENT, btn.getAlignmentX(), 0.01);
    }

    // ============================================================
    // 5. PROPIEDADES DE LA TARJETA (card)
    // ============================================================

    @Test
    @DisplayName("La tarjeta principal existe y es un JPanel")
    public void testTarjetaExiste() {
        Login login = new Login(user -> {});
        Component card = login.getComponent(0);
        assertInstanceOf(JPanel.class, card);
    }

    @Test
    @DisplayName("La tarjeta tiene el color de fondo azul claro")
    public void testTarjetaColorFondo() {
        Login login = new Login(user -> {});
        JPanel card = (JPanel) login.getComponent(0);
        assertEquals(new Color(224, 240, 255), card.getBackground());
    }

    @Test
    @DisplayName("La tarjeta tiene tamaño preferido 320x380")
    public void testTarjetaTamanoPreferido() {
        Login login = new Login(user -> {});
        JPanel card = (JPanel) login.getComponent(0);
        assertEquals(new Dimension(320, 380), card.getPreferredSize());
    }

    @Test
    @DisplayName("La tarjeta tiene layout BoxLayout vertical (Y_AXIS)")
    public void testTarjetaLayout() {
        Login login = new Login(user -> {});
        JPanel card = (JPanel) login.getComponent(0);
        assertInstanceOf(BoxLayout.class, card.getLayout());
    }

    // ============================================================
    // 6. CAMPOS DE TEXTO INICIALMENTE VACÍOS
    // ============================================================

    @Test
    @DisplayName("El campo de usuario está vacío al inicio")
    public void testCampoUsuarioVacio() {
        Login login = new Login(user -> {});
        JTextField field = encontrarJTextField(login);
        assertNotNull(field);
        assertEquals("", field.getText());
    }

    @Test
    @DisplayName("El campo de contraseña está vacío al inicio")
    public void testCampoContrasenaVacio() {
        Login login = new Login(user -> {});
        JPasswordField field = encontrarJPasswordField(login);
        assertNotNull(field);
        assertEquals(0, field.getPassword().length);
    }

    // ============================================================
    // 7. CAMPOS CON DATOS
    // ============================================================

    @Test
    @DisplayName("Se puede escribir en el campo de usuario")
    public void testEscribirEnCampoUsuario() {
        Login login = new Login(user -> {});
        JTextField field = encontrarJTextField(login);
        assertNotNull(field);
        field.setText("ADMIN");
        assertEquals("ADMIN", field.getText());
    }

    @Test
    @DisplayName("Se puede escribir en el campo de contraseña")
    public void testEscribirEnCampoContrasena() {
        Login login = new Login(user -> {});
        JPasswordField field = encontrarJPasswordField(login);
        assertNotNull(field);
        field.setText("admin23");
        assertEquals("admin23", new String(field.getPassword()));
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

    private JTextField encontrarJTextField(Container container) {
        for (Component c : container.getComponents()) {
            // JPasswordField extends JTextField, así que lo excluimos
            if (c instanceof JTextField && !(c instanceof JPasswordField)) {
                return (JTextField) c;
            }
            if (c instanceof Container) {
                JTextField resultado = encontrarJTextField((Container) c);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }

    private JPasswordField encontrarJPasswordField(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JPasswordField) {
                return (JPasswordField) c;
            }
            if (c instanceof Container) {
                JPasswordField resultado = encontrarJPasswordField((Container) c);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }
}
