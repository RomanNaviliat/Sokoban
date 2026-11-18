package sokoban.vista;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ProveedorImagenes {
    public static final int TAM = 64;

    private static final ProveedorImagenes INSTANCIA = new ProveedorImagenes();
    private final Map<String, BufferedImage> cache = new HashMap<>();

    private ProveedorImagenes() { }

    public static ProveedorImagenes obtenerInstancia() {
        return INSTANCIA;
    }

    public BufferedImage obtener(String clave) {
        BufferedImage img = cache.get(clave);
        if (img == null) {
            img = crear(clave);
            cache.put(clave, img);
        }
        return img;
    }

    private BufferedImage crear(String clave) {
        String[] partes = clave.split("\\+");
        BufferedImage img = new BufferedImage(TAM, TAM, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (String parte : partes) {
            BufferedImage externa = cargarExterna(parte);
            if (externa != null) {
                g.drawImage(externa, 0, 0, TAM, TAM, null);
            } else {
                dibujar(g, parte);
            }
        }
        g.dispose();
        return img;
    }

    private BufferedImage cargarExterna(String clave) {
        try (InputStream in = ProveedorImagenes.class.getResourceAsStream("/imagenes/" + clave + ".png")) {
            return in == null ? null : ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }

    private void dibujar(Graphics2D g, String clave) {
        switch (clave) {
            case "suelo_normal": suelo(g); break;
            case "suelo_resbaladizo": hielo(g); break;
            case "casillero_destino": suelo(g); destino(g); break;
            case "suelo_boton": suelo(g); boton(g, false); break;
            case "suelo_boton_presionado": suelo(g); boton(g, true); break;
            case "suelo_cerrojo": suelo(g); cerrojo(g, false); break;
            case "suelo_cerrojo_activo": suelo(g); cerrojo(g, true); break;
            case "piso_que_cae": tablones(g, false); break;
            case "piso_que_cae_agrietado": tablones(g, true); break;
            case "piso_caido": pozo(g); break;
            case "pared": ladrillos(g); break;
            case "puerta_cerrada": puerta(g, false); break;
            case "puerta_abierta": puerta(g, true); break;
            case "caja_normal": caja(g, new Color(176, 112, 52), new Color(120, 72, 30)); break;
            case "caja_fragil": cajaFragil(g); break;
            case "caja_llave": caja(g, new Color(222, 178, 40), new Color(150, 110, 10)); llave(g); break;
            case "caja_timba": timba(g); break;
            case "jugador": jugador(g); break;
            case "robot": robot(g); break;
            case "manosuave": guantes(g); break;
            case "ludopata": visera(g); break;
            default:
                g.setColor(Color.MAGENTA);
                g.fillRect(0, 0, TAM, TAM);
        }
    }

    private void suelo(Graphics2D g) {
        g.setColor(new Color(214, 200, 176));
        g.fillRect(0, 0, TAM, TAM);
        g.setColor(new Color(196, 181, 156));
        g.drawRect(0, 0, TAM - 1, TAM - 1);
        g.drawLine(TAM / 2, 0, TAM / 2, TAM);
        g.drawLine(0, TAM / 2, TAM, TAM / 2);
    }

    private void hielo(Graphics2D g) {
        g.setPaint(new GradientPaint(0, 0, new Color(196, 236, 255), TAM, TAM, new Color(140, 200, 240)));
        g.fillRect(0, 0, TAM, TAM);
        g.setColor(new Color(255, 255, 255, 190));
        g.setStroke(new BasicStroke(3));
        g.drawLine(10, 22, 26, 10);
        g.drawLine(30, 50, 52, 30);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(40, 14, 46, 9);
    }

    private void destino(Graphics2D g) {
        Path2D rombo = new Path2D.Double();
        rombo.moveTo(TAM / 2.0, 14);
        rombo.lineTo(TAM - 14, TAM / 2.0);
        rombo.lineTo(TAM / 2.0, TAM - 14);
        rombo.lineTo(14, TAM / 2.0);
        rombo.closePath();
        g.setColor(new Color(232, 74, 60, 90));
        g.fill(rombo);
        g.setColor(new Color(200, 40, 30));
        g.setStroke(new BasicStroke(3));
        g.draw(rombo);
    }

    private void boton(Graphics2D g, boolean presionado) {
        g.setColor(new Color(90, 90, 90));
        g.fill(new Ellipse2D.Double(12, 14, 40, 40));
        g.setColor(presionado ? new Color(120, 30, 30) : new Color(220, 50, 50));
        int alto = presionado ? 0 : 5;
        g.fill(new Ellipse2D.Double(16, 16 - alto + 2, 32, 32));
        g.setColor(new Color(255, 255, 255, presionado ? 40 : 120));
        g.fill(new Ellipse2D.Double(22, 20 - alto + 2, 12, 8));
    }

    private void cerrojo(Graphics2D g, boolean activo) {
        g.setColor(activo ? new Color(80, 190, 90) : new Color(150, 120, 40));
        g.fill(new RoundRectangle2D.Double(10, 10, 44, 44, 10, 10));
        g.setColor(activo ? new Color(40, 120, 50) : new Color(90, 70, 20));
        g.setStroke(new BasicStroke(3));
        g.draw(new RoundRectangle2D.Double(10, 10, 44, 44, 10, 10));
        g.setColor(new Color(30, 30, 30));
        g.fill(new Ellipse2D.Double(26, 20, 12, 12));
        g.fillRect(29, 28, 6, 16);
    }

    private void tablones(Graphics2D g, boolean agrietado) {
        g.setColor(new Color(40, 30, 25));
        g.fillRect(0, 0, TAM, TAM);
        for (int i = 0; i < 4; i++) {
            g.setColor(i % 2 == 0 ? new Color(160, 110, 65) : new Color(145, 98, 56));
            g.fillRect(2, 2 + i * 15, TAM - 4, 13);
        }
        if (agrietado) {
            g.setColor(new Color(30, 20, 15));
            g.setStroke(new BasicStroke(2.5f));
            Path2D grieta = new Path2D.Double();
            grieta.moveTo(10, 6);
            grieta.lineTo(24, 24);
            grieta.lineTo(18, 36);
            grieta.lineTo(34, 50);
            grieta.lineTo(30, 60);
            grieta.moveTo(24, 24);
            grieta.lineTo(46, 20);
            grieta.lineTo(56, 30);
            g.draw(grieta);
        }
    }

    private void pozo(Graphics2D g) {
        g.setColor(new Color(15, 12, 20));
        g.fillRect(0, 0, TAM, TAM);
        g.setPaint(new GradientPaint(0, 0, new Color(70, 60, 60), 0, 18, new Color(15, 12, 20)));
        g.fillRect(0, 0, TAM, 18);
    }

    private void ladrillos(Graphics2D g) {
        g.setColor(new Color(110, 60, 50));
        g.fillRect(0, 0, TAM, TAM);
        g.setColor(new Color(178, 84, 62));
        int alto = TAM / 4;
        for (int fila = 0; fila < 4; fila++) {
            int desfase = fila % 2 == 0 ? 0 : TAM / 4;
            for (int x = -TAM / 4; x < TAM; x += TAM / 2) {
                g.fillRect(x + desfase + 2, fila * alto + 2, TAM / 2 - 4, alto - 4);
            }
        }
    }

    private void puerta(Graphics2D g, boolean abierta) {
        if (abierta) {
            g.setColor(new Color(90, 90, 100, 160));
            g.setStroke(new BasicStroke(4));
            g.drawRect(3, 3, TAM - 7, TAM - 7);
            g.fillRect(3, 3, 8, TAM - 6);
            g.fillRect(TAM - 11, 3, 8, TAM - 6);
            return;
        }
        g.setColor(new Color(70, 75, 90));
        g.fillRect(0, 0, TAM, TAM);
        g.setColor(new Color(150, 156, 175));
        for (int x = 6; x < TAM; x += 12) g.fillRect(x, 0, 6, TAM);
        g.setColor(new Color(110, 115, 130));
        g.fillRect(0, 10, TAM, 6);
        g.fillRect(0, TAM - 16, TAM, 6);
        g.setColor(new Color(220, 180, 40));
        g.fill(new Ellipse2D.Double(TAM / 2.0 - 7, TAM / 2.0 - 7, 14, 14));
    }

    private void caja(Graphics2D g, Color relleno, Color borde) {
        g.setColor(relleno);
        g.fillRect(6, 6, TAM - 12, TAM - 12);
        g.setColor(borde);
        g.setStroke(new BasicStroke(4));
        g.drawRect(6, 6, TAM - 12, TAM - 12);
        g.drawRect(13, 13, TAM - 26, TAM - 26);
        g.setStroke(new BasicStroke(3));
        g.drawLine(13, 13, TAM - 13, TAM - 13);
        g.drawLine(TAM - 13, 13, 13, TAM - 13);
    }

    private void cajaFragil(Graphics2D g) {
        g.setColor(new Color(200, 230, 245, 230));
        g.fillRect(6, 6, TAM - 12, TAM - 12);
        g.setColor(new Color(70, 130, 170));
        g.setStroke(new BasicStroke(3));
        g.drawRect(6, 6, TAM - 12, TAM - 12);
        Path2D copa = new Path2D.Double();
        copa.moveTo(22, 16);
        copa.lineTo(42, 16);
        copa.curveTo(42, 34, 22, 34, 22, 16);
        g.setColor(new Color(200, 60, 60));
        g.fill(copa);
        g.setStroke(new BasicStroke(2.5f));
        g.drawLine(32, 30, 32, 44);
        g.drawLine(25, 45, 39, 45);
    }

    private void llave(Graphics2D g) {
        g.setColor(new Color(90, 60, 0));
        g.setStroke(new BasicStroke(4));
        g.draw(new Ellipse2D.Double(16, 24, 14, 14));
        g.drawLine(30, 31, 48, 31);
        g.drawLine(42, 31, 42, 38);
        g.drawLine(47, 31, 47, 37);
    }

    private void timba(Graphics2D g) {
        g.setColor(new Color(20, 110, 60));
        g.fill(new RoundRectangle2D.Double(3, 3, TAM - 6, TAM - 6, 12, 12));
        g.setColor(new Color(212, 175, 55));
        g.setStroke(new BasicStroke(3));
        g.draw(new RoundRectangle2D.Double(3, 3, TAM - 6, TAM - 6, 12, 12));
        int sectores = 10;
        for (int i = 0; i < sectores; i++) {
            Color c = i == 0 || i == 5 ? new Color(20, 160, 70) : (i % 2 == 0 ? new Color(200, 30, 30) : new Color(25, 25, 25));
            g.setColor(c);
            g.fillArc(10, 10, TAM - 20, TAM - 20, i * 360 / sectores, 360 / sectores + 1);
        }
        g.setColor(new Color(212, 175, 55));
        g.fill(new Ellipse2D.Double(TAM / 2.0 - 7, TAM / 2.0 - 7, 14, 14));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(TAM / 2.0 + 10, 16, 6, 6));
    }

    private void jugador(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 60));
        g.fill(new Ellipse2D.Double(14, TAM - 12, 36, 8));
        g.setColor(new Color(40, 80, 170));
        g.fill(new RoundRectangle2D.Double(18, 28, 28, 26, 10, 10));
        g.setColor(new Color(30, 30, 50));
        g.fillRect(21, 50, 8, 9);
        g.fillRect(35, 50, 8, 9);
        g.setColor(new Color(245, 205, 160));
        g.fill(new Ellipse2D.Double(20, 8, 24, 24));
        g.setColor(new Color(240, 200, 40));
        g.fill(new RoundRectangle2D.Double(18, 6, 28, 10, 8, 8));
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(26, 19, 4, 4));
        g.fill(new Ellipse2D.Double(35, 19, 4, 4));
        g.setColor(new Color(245, 205, 160));
        g.fill(new Ellipse2D.Double(12, 34, 9, 9));
        g.fill(new Ellipse2D.Double(43, 34, 9, 9));
    }

    private void robot(Graphics2D g) {
        g.setColor(new Color(170, 175, 185));
        g.fill(new RoundRectangle2D.Double(19, 9, 26, 22, 6, 6));
        g.setColor(new Color(40, 220, 255));
        g.fillRect(23, 16, 18, 6);
        g.setColor(new Color(120, 125, 135));
        g.setStroke(new BasicStroke(2));
        g.drawLine(32, 9, 32, 2);
        g.setColor(Color.RED);
        g.fill(new Ellipse2D.Double(29, 0, 6, 6));
    }

    private void guantes(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(10, 32, 13, 13));
        g.fill(new Ellipse2D.Double(41, 32, 13, 13));
        g.setColor(new Color(180, 180, 200));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new Ellipse2D.Double(10, 32, 13, 13));
        g.draw(new Ellipse2D.Double(41, 32, 13, 13));
    }

    private void visera(Graphics2D g) {
        g.setColor(new Color(20, 160, 70, 220));
        Path2D v = new Path2D.Double();
        v.moveTo(16, 14);
        v.lineTo(48, 14);
        v.lineTo(52, 20);
        v.lineTo(12, 20);
        v.closePath();
        g.fill(v);
        g.setColor(Color.WHITE);
        g.fillRect(40, 38, 10, 10);
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(42, 40, 2.5, 2.5));
        g.fill(new Ellipse2D.Double(46, 44, 2.5, 2.5));
    }
}
