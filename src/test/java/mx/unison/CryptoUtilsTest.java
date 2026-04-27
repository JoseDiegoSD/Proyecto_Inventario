package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias exhaustivas para la clase CryptoUtils.
 * Cubre: generación de hash valida, determinismo, sensibilidad a cambios,
 * cadena vacía, caracteres especiales, Unicode, longitud de hash,
 * formato hexadecimal, hashes conocidos, cadenas largas.
 */
public class CryptoUtilsTest {

    // ============================================================
    // 1. PROPIEDADES BÁSICAS DEL HASH MD5
    // ============================================================

    @Test
    @DisplayName("El hash generado no debe ser null")
    public void testHashNoEsNull() {
        String hash = CryptoUtils.md5("test");
        assertNotNull(hash, "El hash no debería ser nulo");
    }

    @Test
    @DisplayName("El hash MD5 debe tener exactamente 32 caracteres")
    public void testHashTiene32Caracteres() {
        String hash = CryptoUtils.md5("cualquierTexto");
        assertEquals(32, hash.length(), "Un hash MD5 siempre tiene 32 caracteres hexadecimales");
    }

    @Test
    @DisplayName("El hash MD5 debe ser una cadena hexadecimal válida (solo 0-9 y a-f)")
    public void testHashEsHexadecimalValido() {
        String hash = CryptoUtils.md5("admin23");
        assertTrue(hash.matches("^[a-f0-9]{32}$"), "El hash debe contener solo caracteres hexadecimales en minúsculas");
    }

    @Test
    @DisplayName("El hash generado debe estar en minúsculas")
    public void testHashEnMinusculas() {
        String hash = CryptoUtils.md5("HELLO");
        assertEquals(hash, hash.toLowerCase(), "El hash debe estar completamente en minúsculas");
    }

    // ============================================================
    // 2. DETERMINISMO (misma entrada = mismo resultado)
    // ============================================================

    @Test
    @DisplayName("La misma entrada produce siempre el mismo hash")
    public void testDeterminismoMismaEntrada() {
        String input = "holaMundo123";
        String hash1 = CryptoUtils.md5(input);
        String hash2 = CryptoUtils.md5(input);
        assertEquals(hash1, hash2, "El hash de la misma cadena debe ser idéntico siempre");
    }

    @Test
    @DisplayName("Determinismo con múltiples invocaciones consecutivas")
    public void testDeterminismoMultiplesInvocaciones() {
        String input = "test_determinism";
        String esperado = CryptoUtils.md5(input);
        for (int i = 0; i < 100; i++) {
            assertEquals(esperado, CryptoUtils.md5(input),
                "Iteración " + i + ": El hash debe ser idéntico en cada invocación");
        }
    }

    // ============================================================
    // 3. SENSIBILIDAD A CAMBIOS (efecto avalancha)
    // ============================================================

    @Test
    @DisplayName("Entradas diferentes producen hashes diferentes")
    public void testEntradasDiferentesHashesDiferentes() {
        assertNotEquals(CryptoUtils.md5("password"), CryptoUtils.md5("Password"),
            "Cambiar mayúscula produce hash diferente");
    }

    @Test
    @DisplayName("Un solo carácter de diferencia cambia el hash")
    public void testUnCaracterDeDiferencia() {
        String hash1 = CryptoUtils.md5("abc");
        String hash2 = CryptoUtils.md5("abd");
        assertNotEquals(hash1, hash2, "Cambiar un solo carácter debe alterar el hash");
    }

    @Test
    @DisplayName("Cadena con espacio adicional produce hash diferente")
    public void testEspacioAdicional() {
        String hash1 = CryptoUtils.md5("test");
        String hash2 = CryptoUtils.md5("test ");
        assertNotEquals(hash1, hash2, "Un espacio al final cambia el hash");
    }

    @Test
    @DisplayName("Cadena con espacio al inicio produce hash diferente")
    public void testEspacioAlInicio() {
        String hash1 = CryptoUtils.md5("test");
        String hash2 = CryptoUtils.md5(" test");
        assertNotEquals(hash1, hash2, "Un espacio al inicio cambia el hash");
    }

    @Test
    @DisplayName("Orden de caracteres afecta el hash")
    public void testOrdenCaracteres() {
        String hash1 = CryptoUtils.md5("ab");
        String hash2 = CryptoUtils.md5("ba");
        assertNotEquals(hash1, hash2, "Invertir caracteres debe cambiar el hash");
    }

    // ============================================================
    // 4. CADENA VACÍA
    // ============================================================

    @Test
    @DisplayName("Hash de cadena vacía es válido y conocido")
    public void testHashCadenaVacia() {
        String hash = CryptoUtils.md5("");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // El MD5 de "" es d41d8cd98f00b204e9800998ecf8427e
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", hash,
            "El MD5 de una cadena vacía es un valor conocido y estandarizado");
    }

    // ============================================================
    // 5. HASHES CONOCIDOS (valores de referencia estándar)
    // ============================================================

    @Test
    @DisplayName("Hash MD5 de 'admin' es conocido")
    public void testHashConocidoAdmin() {
        // MD5("admin") = 21232f297a57a5a743894a0e4a801fc3
        assertEquals("21232f297a57a5a743894a0e4a801fc3", CryptoUtils.md5("admin"));
    }

    @Test
    @DisplayName("Hash MD5 de 'hello' es conocido")
    public void testHashConocidoHello() {
        // MD5("hello") = 5d41402abc4b2a76b9719d911017c592
        assertEquals("5d41402abc4b2a76b9719d911017c592", CryptoUtils.md5("hello"));
    }

    @Test
    @DisplayName("Hash MD5 de '123456' es conocido")
    public void testHashConocido123456() {
        // MD5("123456") = e10adc3949ba59abbe56e057f20f883e
        assertEquals("e10adc3949ba59abbe56e057f20f883e", CryptoUtils.md5("123456"));
    }

    // ============================================================
    // 6. CARACTERES ESPECIALES
    // ============================================================

    @Test
    @DisplayName("Hash de cadena con caracteres especiales")
    public void testCaracteresEspeciales() {
        String hash = CryptoUtils.md5("!@#$%^&*()");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertTrue(hash.matches("^[a-f0-9]{32}$"));
    }

    @Test
    @DisplayName("Hash de cadena con saltos de línea")
    public void testSaltosDeLinea() {
        String hash = CryptoUtils.md5("linea1\nlinea2\nlinea3");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Hash de cadena con tabulaciones")
    public void testTabulaciones() {
        String hash = CryptoUtils.md5("\t\t\t");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Hash de cadena con comillas dobles")
    public void testComillasDobles() {
        String hash = CryptoUtils.md5("\"comillas\"");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    // ============================================================
    // 7. UNICODE Y ACENTOS
    // ============================================================

    @Test
    @DisplayName("Hash de cadena con acentos es válido")
    public void testAcentos() {
        String hash = CryptoUtils.md5("contraseña");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertTrue(hash.matches("^[a-f0-9]{32}$"));
    }

    @Test
    @DisplayName("Hash con ñ es diferente al hash sin ñ")
    public void testNConYSinEne() {
        String hashConEne = CryptoUtils.md5("año");
        String hashSinEne = CryptoUtils.md5("ano");
        assertNotEquals(hashConEne, hashSinEne);
    }

    @Test
    @DisplayName("Hash de caracteres japoneses es válido")
    public void testCaracteresJaponeses() {
        String hash = CryptoUtils.md5("東京");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Hash de emojis es válido")
    public void testEmojis() {
        String hash = CryptoUtils.md5("🔑🔐");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    // ============================================================
    // 8. CADENAS LARGAS
    // ============================================================

    @Test
    @DisplayName("Hash de cadena muy larga (10000 caracteres)")
    public void testCadenaMuyLarga() {
        String largo = "A".repeat(10000);
        String hash = CryptoUtils.md5(largo);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Hash de cadenas largas distintas son diferentes")
    public void testCadenasLargasDistintas() {
        String largo1 = "A".repeat(5000);
        String largo2 = "A".repeat(5000) + "B";
        assertNotEquals(CryptoUtils.md5(largo1), CryptoUtils.md5(largo2));
    }

    // ============================================================
    // 9. CONTRASEÑAS DEL SISTEMA (pruebas de integración ligeras)
    // ============================================================

    @Test
    @DisplayName("Hash de las contraseñas base del sistema son consistentes")
    public void testContrasenasSistema() {
        String hashAdmin = CryptoUtils.md5("admin23");
        String hashProductos = CryptoUtils.md5("productos19");
        String hashAlmacenes = CryptoUtils.md5("almacenes11");

        // Verificar que todas son válidas
        assertAll("Hashes de contraseñas del sistema",
            () -> assertEquals(32, hashAdmin.length()),
            () -> assertEquals(32, hashProductos.length()),
            () -> assertEquals(32, hashAlmacenes.length()),
            () -> assertNotEquals(hashAdmin, hashProductos),
            () -> assertNotEquals(hashAdmin, hashAlmacenes),
            () -> assertNotEquals(hashProductos, hashAlmacenes)
        );
    }

    @Test
    @DisplayName("Hash de contraseñas incorrectas no coincide con las correctas")
    public void testContrasenaIncorrectaNoCoincide() {
        String hashCorrecto = CryptoUtils.md5("admin23");
        String hashIncorrecto = CryptoUtils.md5("admin24");
        assertNotEquals(hashCorrecto, hashIncorrecto);
    }

    // ============================================================
    // 10. SOLO NÚMEROS
    // ============================================================

    @Test
    @DisplayName("Hash de cadena con solo números")
    public void testSoloNumeros() {
        String hash = CryptoUtils.md5("0123456789");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Hash de cadena con solo espacios")
    public void testSoloEspacios() {
        String hash = CryptoUtils.md5("     ");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertNotEquals(CryptoUtils.md5(""), hash, "Solo espacios NO es igual a cadena vacía");
    }
}
