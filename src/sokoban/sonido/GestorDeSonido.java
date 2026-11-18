package sokoban.sonido;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GestorDeSonido {
    public static final String PASO = "paso";
    public static final String EMPUJE = "empuje";
    public static final String BLOQUEO = "bloqueo";
    public static final String ROMPER = "romper";
    public static final String PUERTA = "puerta";
    public static final String CAE = "cae";
    public static final String REGENERAR = "regenerar";
    public static final String DESHACER = "deshacer";
    public static final String REINICIAR = "reiniciar";
    public static final String VICTORIA = "victoria";
    public static final String TIMBA = "timba";
    public static final String GANAR = "ganar";
    public static final String PERDER = "perder";
    public static final String DESBLOQUEO = "desbloqueo";

    private static final float FRECUENCIA_MUESTREO = 22050f;
    private static GestorDeSonido instancia;

    private final Map<String, byte[]> sonidos = new HashMap<>();
    private final AudioFormat formato = new AudioFormat(FRECUENCIA_MUESTREO, 16, 1, true, false);
    private final ExecutorService ejecutor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "sonido");
        t.setDaemon(true);
        return t;
    });
    private volatile double volumen = 0.6;
    private volatile boolean silenciado;

    private GestorDeSonido() {
        generarSonidos();
    }

    public static synchronized GestorDeSonido obtenerInstancia() {
        if (instancia == null) instancia = new GestorDeSonido();
        return instancia;
    }

    public void reproducirSonido(String nombre) {
        if (silenciado) return;
        byte[] datos = sonidos.get(nombre);
        if (datos == null) return;
        final byte[] escalado = aplicarVolumen(datos, volumen);
        ejecutor.submit(() -> reproducir(escalado));
    }

    public void cambiarVolumen(double v) {
        volumen = Math.max(0, Math.min(1, v));
    }

    public double obtenerVolumen() { return volumen; }

    public void alternarSilencio() { silenciado = !silenciado; }

    public boolean estaSilenciado() { return silenciado; }

    private void reproducir(byte[] datos) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(formato, datos, 0, datos.length);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) clip.close();
            });
            clip.start();
        } catch (Exception | LinkageError e) {
        }
    }

    private void generarSonidos() {
        registrar(PASO, barrido(260, 220, 35, 0.25, false));
        registrar(EMPUJE, barrido(160, 90, 90, 0.5, false));
        registrar(BLOQUEO, barrido(95, 80, 70, 0.4, false));
        registrar(ROMPER, ruido(280, 0.6));
        registrar(PUERTA, concatenar(barrido(300, 450, 120, 0.4, true), barrido(450, 700, 140, 0.4, true)));
        registrar(CAE, barrido(500, 60, 380, 0.5, false));
        registrar(REGENERAR, barrido(300, 1000, 220, 0.4, true));
        registrar(DESHACER, barrido(700, 300, 140, 0.4, true));
        registrar(REINICIAR, concatenar(barrido(500, 250, 100, 0.4, true), barrido(250, 500, 100, 0.4, true)));
        registrar(VICTORIA, concatenar(nota(523, 120), nota(659, 120), nota(784, 120), nota(1046, 320)));
        registrar(TIMBA, concatenar(nota(880, 50), silencio(30), nota(988, 50), silencio(30), nota(1175, 80)));
        registrar(GANAR, concatenar(nota(784, 100), nota(988, 100), nota(1318, 300)));
        registrar(PERDER, concatenar(barrido(400, 300, 200, 0.4, true), barrido(300, 150, 400, 0.4, true)));
        registrar(DESBLOQUEO, concatenar(nota(880, 90), nota(1175, 90), nota(1760, 220)));
    }

    private void registrar(String nombre, byte[] sintetizado) {
        byte[] externo = cargarRecurso("/sonidos/" + nombre + ".wav");
        sonidos.put(nombre, externo != null ? externo : sintetizado);
    }

    private byte[] cargarRecurso(String ruta) {
        InputStream in = GestorDeSonido.class.getResourceAsStream(ruta);
        if (in == null) return null;
        try (AudioInputStream original = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
             AudioInputStream convertido = AudioSystem.getAudioInputStream(formato, original)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = convertido.read(buf)) > 0) out.write(buf, 0, n);
            return out.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] nota(double frecuencia, int ms) {
        return barrido(frecuencia, frecuencia, ms, 0.35, true);
    }

    private byte[] barrido(double f0, double f1, int ms, double amplitud, boolean senoidal) {
        int n = (int) (FRECUENCIA_MUESTREO * ms / 1000);
        short[] m = new short[n];
        double fase = 0;
        for (int i = 0; i < n; i++) {
            double t = (double) i / n;
            double f = f0 + (f1 - f0) * t;
            fase += 2 * Math.PI * f / FRECUENCIA_MUESTREO;
            double onda = senoidal ? Math.sin(fase) : Math.signum(Math.sin(fase)) * 0.6;
            double envolvente = Math.min(1, i / (FRECUENCIA_MUESTREO * 0.004)) * (1 - t);
            m[i] = (short) (onda * envolvente * amplitud * Short.MAX_VALUE);
        }
        return aBytes(m);
    }

    private byte[] ruido(int ms, double amplitud) {
        int n = (int) (FRECUENCIA_MUESTREO * ms / 1000);
        short[] m = new short[n];
        Random r = new Random(42);
        for (int i = 0; i < n; i++) {
            double t = (double) i / n;
            m[i] = (short) ((r.nextDouble() * 2 - 1) * Math.pow(1 - t, 2) * amplitud * Short.MAX_VALUE);
        }
        return aBytes(m);
    }

    private byte[] silencio(int ms) {
        return new byte[2 * (int) (FRECUENCIA_MUESTREO * ms / 1000)];
    }

    private static byte[] concatenar(byte[]... partes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] p : partes) out.write(p, 0, p.length);
        return out.toByteArray();
    }

    private static byte[] aBytes(short[] muestras) {
        byte[] b = new byte[muestras.length * 2];
        for (int i = 0; i < muestras.length; i++) {
            b[2 * i] = (byte) (muestras[i] & 0xff);
            b[2 * i + 1] = (byte) ((muestras[i] >> 8) & 0xff);
        }
        return b;
    }

    private static byte[] aplicarVolumen(byte[] datos, double v) {
        byte[] r = new byte[datos.length];
        for (int i = 0; i + 1 < datos.length; i += 2) {
            short s = (short) ((datos[i] & 0xff) | (datos[i + 1] << 8));
            s = (short) (s * v);
            r[i] = (byte) (s & 0xff);
            r[i + 1] = (byte) ((s >> 8) & 0xff);
        }
        return r;
    }
}
