package sokoban.vista;

import sokoban.controlador.SokobanController;
import sokoban.modelo.Direccion;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PanelJuego extends JPanel {
    private static final long serialVersionUID = 1L;

    private final PanelHud hud;
    private final PanelTablero tablero;

    public PanelJuego(SokobanController controlador) {
        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);
        hud = new PanelHud(controlador);
        tablero = new PanelTablero(() -> controlador.obtenerJuego().hayPartida()
                ? controlador.obtenerJuego().obtenerPartida().obtenerTablero() : null);
        add(hud, BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);

        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        vincular(im, am, KeyEvent.VK_UP, "arriba", () -> controlador.mover(Direccion.ARRIBA));
        vincular(im, am, KeyEvent.VK_DOWN, "abajo", () -> controlador.mover(Direccion.ABAJO));
        vincular(im, am, KeyEvent.VK_LEFT, "izquierda", () -> controlador.mover(Direccion.IZQUIERDA));
        vincular(im, am, KeyEvent.VK_RIGHT, "derecha", () -> controlador.mover(Direccion.DERECHA));
        vincular(im, am, KeyEvent.VK_Z, "deshacer", controlador::deshacer);
        vincular(im, am, KeyEvent.VK_U, "deshacer2", controlador::deshacer);
        vincular(im, am, KeyEvent.VK_R, "reiniciar", controlador::reiniciarNivel);
        vincular(im, am, KeyEvent.VK_ESCAPE, "menu", controlador::volverAlMenu);
    }

    private void vincular(InputMap im, ActionMap am, int tecla, String nombre, Runnable accion) {
        im.put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        am.put(nombre, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                accion.run();
            }
        });
    }

    public void refrescar() {
        hud.refrescar();
        tablero.repaint();
    }
}
