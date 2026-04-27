package mx.unison;

/**
 * Clase Launcher necesaria para poder ejecutar JavaFX desde el IDE (IntelliJ, Eclipse, VSCode)
 * sin tener que configurar el module-path manualmente, ya que esta clase no extiende de Application.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
