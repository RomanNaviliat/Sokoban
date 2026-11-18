package sokoban.vista;

import sokoban.modelo.ResumenNivel;
import sokoban.modelo.skins.TipoSkin;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class DialogoResumen extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean continuar;

    public DialogoResumen(JFrame padre, ResumenNivel r) {
        super(padre, "Nivel completado", true);
        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(Estilo.PANEL);
        raiz.setBorder(BorderFactory.createEmptyBorder(18, 26, 16, 26));

        JLabel titulo = Estilo.etiqueta("¡Nivel " + r.obtenerNivel() + " completado! (" + r.obtenerDificultad() + ")", 20, true);
        titulo.setForeground(Estilo.ACENTO);

        JPanel tabla = new JPanel(new GridLayout(0, 2, 24, 6));
        tabla.setOpaque(false);
        fila(tabla, "Movimientos", String.valueOf(r.obtenerMovimientos()));
        fila(tabla, "Empujes", String.valueOf(r.obtenerEmpujes()));
        fila(tabla, "Usos de undo", String.valueOf(r.obtenerUsosUndo()));
        fila(tabla, "Caja timba (neto)", (r.obtenerResultadoApuestas() > 0 ? "+" : "") + r.obtenerResultadoApuestas());
        fila(tabla, "Criterio", r.obtenerCriterioPuntaje());
        JLabel lblPuntaje = fila(tabla, "PUNTAJE FINAL", String.valueOf(r.obtenerPuntaje()));
        lblPuntaje.setForeground(Estilo.ACENTO);
        fila(tabla, "Suma al puntaje acumulado", "+" + r.obtenerRecompensa() + " pts");

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.add(tabla, BorderLayout.CENTER);
        if (!r.obtenerSkinsComprables().isEmpty()) {
            StringBuilder sb = new StringBuilder("<html>Ya te alcanza para comprar: ");
            for (TipoSkin s : r.obtenerSkinsComprables()) sb.append("<b>").append(s.obtenerNombre()).append("</b> ");
            sb.append("<br>Compralas desde el menu Skins.</html>");
            JLabel l = Estilo.etiqueta(sb.toString(), 14, false);
            l.setForeground(new Color(150, 230, 160));
            centro.add(l, BorderLayout.SOUTH);
        }

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setOpaque(false);
        JButton btnMenu = Estilo.boton("Menu");
        btnMenu.addActionListener(e -> dispose());
        botones.add(btnMenu);
        if (r.haySiguienteNivel()) {
            JButton btnSiguiente = Estilo.boton("Siguiente nivel", new Color(60, 140, 90));
            btnSiguiente.addActionListener(e -> { continuar = true; dispose(); });
            botones.add(btnSiguiente);
        } else {
            JLabel fin = Estilo.etiqueta("¡Completaste todos los niveles de esta dificultad!  ", 13, true);
            botones.add(fin, 0);
        }

        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(botones, BorderLayout.SOUTH);
        setContentPane(raiz);
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private JLabel fila(JPanel p, String nombre, String valor) {
        JLabel n = Estilo.etiqueta(nombre, 15, false);
        n.setForeground(Estilo.TEXTO_SUAVE);
        JLabel v = Estilo.etiqueta(valor, 15, true);
        p.add(n);
        p.add(v);
        return v;
    }

    public boolean mostrar() {
        setVisible(true);
        return continuar;
    }
}
