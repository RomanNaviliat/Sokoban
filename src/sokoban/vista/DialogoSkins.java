package sokoban.vista;

import sokoban.modelo.Juego;
import sokoban.modelo.Perfil;
import sokoban.modelo.Tienda;
import sokoban.modelo.skins.ResultadoDesbloqueo;
import sokoban.modelo.skins.TipoSkin;
import sokoban.sonido.GestorDeSonido;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

public class DialogoSkins extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JFrame padre;
    private final Juego juego;

    public DialogoSkins(JFrame padre, Juego juego) {
        super(padre, "Skins", true);
        this.padre = padre;
        this.juego = juego;
        construir();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void construir() {
        Perfil perfil = juego.obtenerPerfil();
        Tienda tienda = juego.obtenerTienda();

        JPanel raiz = new JPanel(new BorderLayout(0, 12));
        raiz.setBackground(Estilo.PANEL);
        raiz.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = Estilo.etiqueta("Skins - puntaje acumulado: " + perfil.obtenerPuntajeAcumulado() + " pts", 18, true);
        titulo.setForeground(Estilo.ACENTO);

        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 10));
        lista.setOpaque(false);
        for (TipoSkin s : tienda.obtenerCatalogo()) {
            lista.add(filaSkin(juego, perfil, tienda, s));
        }

        JLabel nota = Estilo.etiqueta("<html>Comprar una skin descuenta su precio del puntaje acumulado.<br>Las skins se aplican al iniciar o reiniciar un nivel.</html>", 12, false);
        nota.setForeground(Estilo.TEXTO_SUAVE);

        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setOpaque(false);
        JButton cerrar = Estilo.boton("Cerrar");
        cerrar.addActionListener(e -> dispose());
        abajo.add(nota, BorderLayout.CENTER);
        abajo.add(cerrar, BorderLayout.EAST);

        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(lista, BorderLayout.CENTER);
        raiz.add(abajo, BorderLayout.SOUTH);
        setContentPane(raiz);
        pack();
    }

    private JPanel filaSkin(Juego juego, Perfil perfil, Tienda tienda, TipoSkin s) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(new Color(58, 62, 78));
        fila.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        String clave = "jugador+" + claveAccesorio(s);
        Image img = ProveedorImagenes.obtenerInstancia().obtener(clave).getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        fila.add(new JLabel(new ImageIcon(img)), BorderLayout.WEST);

        JPanel textos = new JPanel(new GridLayout(0, 1));
        textos.setOpaque(false);
        textos.add(Estilo.etiqueta(s.obtenerNombre(), 15, true));
        JLabel desc = Estilo.etiqueta(s.obtenerDescripcion(), 12, false);
        desc.setForeground(Estilo.TEXTO_SUAVE);
        textos.add(desc);
        JProgressBar barra = new JProgressBar(0, s.obtenerPrecio());
        barra.setValue(perfil.tieneSkin(s) ? s.obtenerPrecio()
                : Math.min(perfil.obtenerPuntajeAcumulado(), s.obtenerPrecio()));
        barra.setStringPainted(true);
        if (perfil.tieneSkin(s)) barra.setString("Comprada");
        else if (tienda.puedeComprar(perfil, s)) barra.setString("Precio: " + s.obtenerPrecio() + " pts");
        else barra.setString("Precio: " + s.obtenerPrecio() + " pts (faltan " + tienda.puntosFaltantes(perfil, s) + ")");
        textos.add(barra);
        fila.add(textos, BorderLayout.CENTER);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        der.setOpaque(false);
        if (perfil.tieneSkin(s)) {
            JCheckBox equipar = new JCheckBox("Equipada", perfil.estaEquipada(s));
            equipar.setOpaque(false);
            equipar.setForeground(Estilo.TEXTO);
            equipar.addActionListener(e -> juego.equiparSkin(s, equipar.isSelected()));
            der.add(equipar);
        } else {
            JButton comprar = Estilo.boton("Comprar", new Color(60, 140, 90));
            comprar.setEnabled(tienda.puedeComprar(perfil, s));
            comprar.addActionListener(e -> {
                if (juego.comprarSkin(s) == ResultadoDesbloqueo.DESBLOQUEADA) {
                    GestorDeSonido.obtenerInstancia().reproducirSonido(GestorDeSonido.DESBLOQUEO);
                    construir();
                    setLocationRelativeTo(padre);
                    if (padre != null) padre.repaint();
                }
            });
            der.add(comprar);
        }
        fila.add(der, BorderLayout.EAST);
        return fila;
    }

    private static String claveAccesorio(TipoSkin s) {
        switch (s) {
            case ROBOT: return "robot";
            case LUDOPATA: return "ludopata";
            default: return "manosuave";
        }
    }
}
