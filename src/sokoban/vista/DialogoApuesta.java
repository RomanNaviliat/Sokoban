package sokoban.vista;

import sokoban.modelo.timba.ColorApuesta;
import sokoban.modelo.timba.ResultadoApuesta;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DialogoApuesta extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JLabel lblRuleta = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblMensaje = Estilo.etiqueta(" ", 15, true);
    private final JPanel panelColores = new JPanel(new GridLayout(1, 3, 10, 0));
    private final JButton btnCerrar = Estilo.boton("No apostar", new Color(110, 110, 120));
    private final JSpinner spMonto;

    public DialogoApuesta(JFrame padre, int saldo, int minimo, int maximo,
                          Function<ColorApuesta, Double> probabilidad,
                          BiFunction<Integer, ColorApuesta, ResultadoApuesta> apostar) {
        super(padre, "Caja timba", true);
        int tope = Math.min(maximo, saldo);
        spMonto = new JSpinner(new SpinnerNumberModel(minimo, minimo, tope, 50));
        spMonto.setPreferredSize(new Dimension(110, 32));
        spMonto.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        JPanel raiz = new JPanel(new BorderLayout(0, 12));
        raiz.setBackground(new Color(18, 70, 40));
        raiz.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel arriba = new JPanel(new GridLayout(0, 1, 0, 4));
        arriba.setOpaque(false);
        JLabel titulo = Estilo.etiqueta("¡Encontraste una caja timba!", 20, true);
        titulo.setForeground(Estilo.ACENTO);
        arriba.add(titulo);
        arriba.add(Estilo.etiqueta("Puntaje acumulado: " + saldo + " pts. Apuesta entre " + minimo + " y " + maximo + ".", 14, false));
        JPanel filaMonto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaMonto.setOpaque(false);
        filaMonto.add(Estilo.etiqueta("Monto:", 15, true));
        filaMonto.add(spMonto);
        arriba.add(filaMonto);

        panelColores.setOpaque(false);
        for (ColorApuesta c : ColorApuesta.values()) {
            String texto = String.format("<html><center>%s<br><small>%.0f%% - paga x%d</small></center></html>",
                    c.obtenerNombre(), probabilidad.apply(c) * 100, c.obtenerMultiplicadorPremio());
            JButton b = Estilo.boton(texto, colorDe(c));
            b.setPreferredSize(new Dimension(130, 60));
            b.addActionListener(e -> jugar(c, apostar));
            panelColores.add(b);
        }

        lblRuleta.setOpaque(true);
        lblRuleta.setBackground(new Color(10, 40, 22));
        lblRuleta.setForeground(Color.WHITE);
        lblRuleta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        lblRuleta.setPreferredSize(new Dimension(300, 56));

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.add(panelColores, BorderLayout.NORTH);
        centro.add(lblRuleta, BorderLayout.CENTER);
        centro.add(lblMensaje, BorderLayout.SOUTH);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        abajo.setOpaque(false);
        abajo.add(btnCerrar);
        btnCerrar.addActionListener(e -> dispose());

        raiz.add(arriba, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(abajo, BorderLayout.SOUTH);
        setContentPane(raiz);
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void jugar(ColorApuesta elegido, BiFunction<Integer, ColorApuesta, ResultadoApuesta> apostar) {
        int monto = (Integer) spMonto.getValue();
        final ResultadoApuesta r;
        try {
            r = apostar.apply(monto, elegido);
        } catch (IllegalArgumentException ex) {
            lblMensaje.setText(ex.getMessage());
            return;
        }
        for (java.awt.Component c : panelColores.getComponents()) c.setEnabled(false);
        spMonto.setEnabled(false);
        btnCerrar.setEnabled(false);
        lblMensaje.setText("Girando...");

        Random random = new Random();
        ColorApuesta[] colores = ColorApuesta.values();
        final int[] vueltas = {0};
        Timer t = new Timer(70, null);
        t.addActionListener(e -> {
            vueltas[0]++;
            ColorApuesta c = vueltas[0] >= 22 ? r.obtenerColorSalido() : colores[random.nextInt(colores.length)];
            lblRuleta.setBackground(colorDe(c));
            lblRuleta.setText(c.obtenerNombre().toUpperCase());
            if (vueltas[0] >= 22) {
                t.stop();
                lblMensaje.setForeground(r.gano() ? new Color(120, 255, 140) : new Color(255, 140, 140));
                lblMensaje.setText(r.gano()
                        ? "¡Ganaste " + r.obtenerPremio() + " pts! (neto +" + r.obtenerNeto() + ")"
                        : "Perdiste " + monto + " pts");
                btnCerrar.setText("Continuar");
                btnCerrar.setEnabled(true);
            }
        });
        t.start();
    }

    private static Color colorDe(ColorApuesta c) {
        switch (c) {
            case ROJO: return Estilo.ROJO;
            case NEGRO: return Estilo.NEGRO;
            default: return Estilo.VERDE;
        }
    }
}
