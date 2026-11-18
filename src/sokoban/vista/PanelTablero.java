package sokoban.vista;

import sokoban.modelo.Posicion;
import sokoban.modelo.Tablero;
import sokoban.modelo.cajas.Caja;
import sokoban.modelo.paredes.Pared;
import sokoban.modelo.suelos.Suelo;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Supplier;

public class PanelTablero extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color FONDO = new Color(34, 36, 46);

    private final transient Supplier<Tablero> fuente;
    private final transient ProveedorImagenes imagenes = ProveedorImagenes.obtenerInstancia();

    public PanelTablero(Supplier<Tablero> fuente) {
        this.fuente = fuente;
        setBackground(FONDO);
        setPreferredSize(new Dimension(800, 560));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Tablero t = fuente.get();
        if (t == null) return;
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int tam = Math.max(16, Math.min(ProveedorImagenes.TAM,
                Math.min((getWidth() - 20) / t.obtenerAncho(), (getHeight() - 20) / t.obtenerAlto())));
        int ox = (getWidth() - tam * t.obtenerAncho()) / 2;
        int oy = (getHeight() - tam * t.obtenerAlto()) / 2;

        for (int y = 0; y < t.obtenerAlto(); y++) {
            for (int x = 0; x < t.obtenerAncho(); x++) {
                Posicion p = new Posicion(x, y);
                int px = ox + x * tam;
                int py = oy + y * tam;
                Suelo s = t.obtenerSuelo(p);
                if (s != null) g.drawImage(imagenes.obtener(s.obtenerClaveImagen()), px, py, tam, tam, null);
                Pared pa = t.obtenerPared(p);
                if (pa != null) g.drawImage(imagenes.obtener(pa.obtenerClaveImagen()), px, py, tam, tam, null);
                Caja c = t.obtenerCaja(p);
                if (c != null) {
                    g.drawImage(imagenes.obtener(c.obtenerClaveImagen()), px, py, tam, tam, null);
                    if (s != null && "casillero_destino".equals(s.obtenerClaveImagen())) {
                        g.setColor(new Color(60, 220, 90, 200));
                        g.setStroke(new BasicStroke(Math.max(2, tam / 14f)));
                        g.drawRect(px + 3, py + 3, tam - 6, tam - 6);
                    }
                    dibujarEtiqueta(g, c.obtenerEtiqueta(), px, py, tam);
                }
            }
        }
        Posicion pj = t.obtenerPosicionJugador();
        g.drawImage(imagenes.obtener(t.obtenerJugador().obtenerClaveImagen()),
                ox + pj.obtenerX() * tam, oy + pj.obtenerY() * tam, tam, tam, null);
        g.dispose();
    }

    private void dibujarEtiqueta(Graphics2D g, String etiqueta, int px, int py, int tam) {
        if (etiqueta == null || etiqueta.isEmpty()) return;
        int d = Math.max(14, tam / 3);
        int cx = px + tam - d - 2;
        int cy = py + tam - d - 2;
        g.setColor(new Color(20, 20, 20, 210));
        g.fillOval(cx, cy, d, d);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(10, d * 2 / 3)));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(etiqueta, cx + (d - fm.stringWidth(etiqueta)) / 2, cy + (d + fm.getAscent() - fm.getDescent()) / 2);
    }
}
