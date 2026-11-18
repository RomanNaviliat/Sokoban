package sokoban.vista;

import sokoban.controlador.SokobanController;
import sokoban.modelo.ResumenNivel;
import sokoban.modelo.timba.ColorApuesta;
import sokoban.modelo.timba.ResultadoApuesta;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.function.BiFunction;
import java.util.function.Function;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String MENU = "menu";
    private static final String JUEGO = "juego";

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);
    private final PanelMenu panelMenu;
    private final PanelJuego panelJuego;
    private final transient SokobanController controlador;

    public VentanaPrincipal(SokobanController controlador) {
        super("Sokoban - UADE");
        this.controlador = controlador;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panelMenu = new PanelMenu(controlador);
        panelJuego = new PanelJuego(controlador);
        contenedor.add(panelMenu, MENU);
        contenedor.add(panelJuego, JUEGO);
        setContentPane(contenedor);
        setMinimumSize(new Dimension(900, 640));
        setSize(1060, 760);
        setLocationRelativeTo(null);
    }

    public void mostrarMenu() {
        panelMenu.refrescar();
        cartas.show(contenedor, MENU);
    }

    public void mostrarJuego() {
        panelJuego.refrescar();
        cartas.show(contenedor, JUEGO);
        panelJuego.requestFocusInWindow();
    }

    public void refrescar() {
        panelJuego.refrescar();
        panelMenu.refrescar();
    }

    public void refrescarJuego() {
        panelJuego.refrescar();
    }

    public void mostrarApuesta(int saldo, int minimo, int maximo, Function<ColorApuesta, Double> probabilidad,
                               BiFunction<Integer, ColorApuesta, ResultadoApuesta> apostar) {
        new DialogoApuesta(this, saldo, minimo, maximo, probabilidad, apostar).setVisible(true);
    }

    public boolean mostrarResumen(ResumenNivel r) {
        return new DialogoResumen(this, r).mostrar();
    }

    public void mostrarSkins() {
        new DialogoSkins(this, controlador.obtenerJuego()).setVisible(true);
    }

    public void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }
}
