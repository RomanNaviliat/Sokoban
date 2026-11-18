package sokoban;

import sokoban.controlador.SokobanController;
import sokoban.modelo.Juego;
import sokoban.modelo.guardado.GestorDeGuardado;
import sokoban.sonido.GestorDeSonido;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            SokobanController controlador = new SokobanController(
                    new Juego(), GestorDeSonido.obtenerInstancia(), new GestorDeGuardado());
            controlador.iniciar();
        });
    }
}
