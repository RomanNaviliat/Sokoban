package sokoban.vista;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public final class Estilo {
    public static final Color FONDO = new Color(34, 36, 46);
    public static final Color PANEL = new Color(46, 49, 62);
    public static final Color TEXTO = new Color(235, 235, 240);
    public static final Color TEXTO_SUAVE = new Color(170, 175, 190);
    public static final Color ACENTO = new Color(240, 190, 60);
    public static final Color ROJO = new Color(200, 40, 40);
    public static final Color NEGRO = new Color(25, 25, 25);
    public static final Color VERDE = new Color(20, 150, 70);

    private Estilo() { }

    public static JLabel etiqueta(String texto, int tamano, boolean negrita) {
        JLabel l = new JLabel(texto);
        l.setForeground(TEXTO);
        l.setFont(new Font(Font.SANS_SERIF, negrita ? Font.BOLD : Font.PLAIN, tamano));
        return l;
    }

    public static JButton boton(String texto) {
        JButton b = new JButton(texto);
        aplicar(b, new Color(70, 75, 95));
        return b;
    }

    public static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        aplicar(b, fondo);
        return b;
    }

    public static void aplicar(AbstractButton b, Color fondo) {
        b.setFocusable(false);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker(), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
