package mx.unison.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void testMd5Encryption() {
        String input = "admin23";
        String hash = CryptoUtils.md5(input);
        
        assertNotNull(hash);
        assertEquals(32, hash.length()); // MD5 siempre tiene 32 caracteres hexadecimales
        
        // El hash del mismo texto debe ser igual
        String hash2 = CryptoUtils.md5(input);
        assertEquals(hash, hash2);
        
        // Diferentes textos dan diferentes hashes
        assertNotEquals(hash, CryptoUtils.md5("admin24"));
    }
}
