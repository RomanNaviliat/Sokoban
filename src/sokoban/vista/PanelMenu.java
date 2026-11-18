package sokoban.vista;

import sokoban.controlador.SokobanController;
import sokoban.modelo.Juego;
import sokoban.modelo.Perfil;
import sokoban.modelo.dificultad.EstrategiaDificultad;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PanelMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    private final transient SokobanController controlador;
    private final JLabel lblPerfil = Estilo.etiqueta("", 15, false);
    private final JLabel lblDescripcion = Estilo.etiqueta("", 13, false);
    private final JPanel panelDificultades = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
    private final JPanel panelNiveles = new JPanel(new GridLayout(2, 3, 10, 10));
    private final JButton btnCargar = Estilo.boton("Cargar partida");

    public PanelMenu(SokobanController controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titulo = Estilo.etiqueta("SOKOBAN", 54, true);
        titulo.setForeground(Estilo.ACENTO);
        JLabel subtitulo = Estilo.etiqueta("Trabajo Integrador - Proceso de Desarrollo de Software", 14, false);
        subtitulo.setForeground(Estilo.TEXTO_SUAVE);
        lblPerfil.setForeground(Estilo.ACENTO);
        lblDescripcion.setForeground(Estilo.TEXTO_SUAVE);

        panelDificultades.setOpaque(false);
        panelDificultades.setMaximumSize(new java.awt.Dimension(600, 44));
        panelNiveles.setOpaque(false);
        panelNiveles.setMaximumSize(new java.awt.Dimension(520, 160));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        acciones.setOpaque(false);
        acciones.setMaximumSize(new java.awt.Dimension(700, 44));
        JButton btnSkins = Estilo.boton("Skins", new Color(120, 70, 160));
        JButton btnGuardar = Estilo.boton("Guardar perfil");
        JButton btnSalir = Estilo.boton("Salir", new Color(150, 50, 50));
        acciones.add(btnSkins);
        acciones.add(btnCargar);
        acciones.add(btnGuardar);
        acciones.add(btnSalir);
        btnSkins.addActionListener(e -> controlador.abrirSkins());
        btnCargar.addActionListener(e -> controlador.cargarPartida());
        btnGuardar.addActionListener(e -> controlador.guardarPartida());
        btnSalir.addActionListener(e -> System.exit(0));

        agregarCentrado(centro, titulo, 4);
        agregarCentrado(centro, subtitulo, 20);
        agregarCentrado(centro, lblPerfil, 24);
        agregarCentrado(centro, Estilo.etiqueta("Dificultad", 17, true), 8);
        agregarCentrado(centro, panelDificultades, 6);
        agregarCentrado(centro, lblDescripcion, 22);
        agregarCentrado(centro, Estilo.etiqueta("Niveles", 17, true), 8);
        agregarCentrado(centro, panelNiveles, 26);
        agregarCentrado(centro, acciones, 0);

        add(centro, BorderLayout.CENTER);
    }

    private void agregarCentrado(JPanel contenedor, Component c, int espacio) {
        if (c instanceof javax.swing.JComponent) ((javax.swing.JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
        if (c instanceof JLabel) ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
        contenedor.add(c);
        contenedor.add(Box.createVerticalStrut(espacio));
    }

    public void refrescar() {
        Juego juego = controlador.obtenerJuego();
        Perfil perfil = juego.obtenerPerfil();
        EstrategiaDificultad actual = juego.obtenerDificultad();
        lblPerfil.setText("Puntaje acumulado: " + perfil.obtenerPuntajeAcumulado() + " pts");
        lblDescripcion.setText(actual.obtenerCantidadCajasFragiles() + " cajas fragiles por nivel  -  puntaje: "
                + actual.crearEstrategiaPuntaje().obtenerDescripcion());

        panelDificultades.removeAll();
        ButtonGroup grupo = new ButtonGroup();
        for (EstrategiaDificultad d : controlador.obtenerDificultades()) {
            JToggleButton b = new JToggleButton(d.obtenerNombre(), d.equals(actual));
            Estilo.aplicar(b, d.equals(actual) ? new Color(60, 140, 90) : new Color(70, 75, 95));
            b.addActionListener(e -> controlador.seleccionarDificultad(d));
            grupo.add(b);
            panelDificultades.add(b);
        }

        panelNiveles.removeAll();
        for (int n = 1; n <= actual.obtenerCantidadNiveles(); n++) {
            final int nivel = n;
            boolean desbloqueado = juego.estaDesbloqueado(actual, n);
            Integer mejor = perfil.obtenerMejorPuntaje(actual.obtenerNombre(), n);
            String texto = "<html><center>Nivel " + n + "<br><small>"
                    + (!desbloqueado ? "bloqueado" : mejor != null ? "mejor: " + mejor : "nuevo")
                    + "</small></center></html>";
            JButton b = Estilo.boton(texto, desbloqueado ? new Color(60, 110, 180) : new Color(60, 60, 70));
            b.setEnabled(desbloqueado);
            b.addActionListener(e -> controlador.iniciarPartida(nivel));
            panelNiveles.add(b);
        }
        btnCargar.setEnabled(controlador.hayGuardado());
        revalidate();
        repaint();
    }
}
