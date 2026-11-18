package sokoban.vista;

import sokoban.controlador.SokobanController;
import sokoban.modelo.Juego;
import sokoban.modelo.Partida;
import sokoban.modelo.puntaje.Puntaje;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

public class PanelHud extends JPanel {
    private static final long serialVersionUID = 1L;

    private final transient SokobanController controlador;
    private final JLabel lblNivel = Estilo.etiqueta("", 15, true);
    private final JLabel lblMovimientos = Estilo.etiqueta("", 15, false);
    private final JLabel lblEmpujes = Estilo.etiqueta("", 15, false);
    private final JLabel lblObjetivos = Estilo.etiqueta("", 15, false);
    private final JLabel lblPuntaje = Estilo.etiqueta("", 15, false);
    private final JLabel lblSkin = Estilo.etiqueta("", 12, false);
    private final JButton btnDeshacer = Estilo.boton("Deshacer", new Color(60, 110, 180));
    private final JButton btnReiniciar = Estilo.boton("Reiniciar", new Color(180, 100, 40));
    private final JButton btnGuardar = Estilo.boton("Guardar");
    private final JButton btnSonido = Estilo.boton("Sonido: si");
    private final JButton btnMenu = Estilo.boton("Menu");

    public PanelHud(SokobanController controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout());
        setBackground(Estilo.PANEL);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 2));
        datos.setOpaque(false);
        datos.add(lblNivel);
        datos.add(lblMovimientos);
        datos.add(lblEmpujes);
        datos.add(lblObjetivos);
        datos.add(lblPuntaje);
        lblPuntaje.setForeground(Estilo.ACENTO);
        lblSkin.setForeground(Estilo.TEXTO_SUAVE);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        botones.setOpaque(false);
        botones.add(btnDeshacer);
        botones.add(btnReiniciar);
        botones.add(btnGuardar);
        botones.add(btnSonido);
        botones.add(btnMenu);

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setOpaque(false);
        izquierda.add(datos, BorderLayout.CENTER);
        JPanel filaSkin = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        filaSkin.setOpaque(false);
        filaSkin.add(lblSkin);
        izquierda.add(filaSkin, BorderLayout.SOUTH);

        add(izquierda, BorderLayout.CENTER);
        add(botones, BorderLayout.EAST);

        btnDeshacer.addActionListener(e -> controlador.deshacer());
        btnReiniciar.addActionListener(e -> controlador.reiniciarNivel());
        btnGuardar.addActionListener(e -> controlador.guardarPartida());
        btnSonido.addActionListener(e -> controlador.alternarSonido());
        btnMenu.addActionListener(e -> controlador.volverAlMenu());
    }

    public void refrescar() {
        Juego juego = controlador.obtenerJuego();
        Partida partida = juego.obtenerPartida();
        if (partida == null) return;
        Puntaje p = partida.obtenerPuntaje();
        lblNivel.setText("Nivel " + partida.obtenerNivelActual() + " / " + juego.obtenerDificultad().obtenerCantidadNiveles()
                + "  (" + juego.obtenerDificultad().obtenerNombre() + ")");
        lblMovimientos.setText("Movimientos: " + p.obtenerMovimientos());
        lblEmpujes.setText("Empujes: " + p.obtenerEmpujes());
        lblObjetivos.setText("Objetivos: " + partida.obtenerTablero().contarObjetivosCumplidos()
                + "/" + partida.obtenerTablero().contarObjetivos());
        lblPuntaje.setText("Puntaje acumulado: " + juego.obtenerPerfil().obtenerPuntajeAcumulado() + " pts");
        lblSkin.setText("Jugador: " + partida.obtenerSkin().obtenerNombre()
                + "   |   Flechas: mover   Z/U: deshacer   R: reiniciar   Esc: menu");
        btnDeshacer.setText("Deshacer (" + partida.obtenerUndosRestantes() + ")");
        btnDeshacer.setEnabled(partida.puedeDeshacer());
        btnSonido.setText(controlador.sonidoActivo() ? "Sonido: si" : "Sonido: no");
    }
}
