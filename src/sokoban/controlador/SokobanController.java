package sokoban.controlador;

import sokoban.modelo.Direccion;
import sokoban.modelo.Juego;
import sokoban.modelo.NivelInvalidoException;
import sokoban.modelo.ResultadoMovimiento;
import sokoban.modelo.ResumenNivel;
import sokoban.modelo.cajas.CajaTimba;
import sokoban.modelo.dificultad.DificultadDificil;
import sokoban.modelo.dificultad.DificultadFacil;
import sokoban.modelo.dificultad.DificultadMedia;
import sokoban.modelo.dificultad.EstrategiaDificultad;
import sokoban.modelo.guardado.GestorDeGuardado;
import sokoban.modelo.guardado.PartidaGuardada;
import sokoban.modelo.timba.ColorApuesta;
import sokoban.modelo.timba.ResultadoApuesta;
import sokoban.sonido.GestorDeSonido;
import sokoban.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SokobanController {
    private final Juego juego;
    private final GestorDeSonido sonido;
    private final GestorDeGuardado guardado;
    private final List<EstrategiaDificultad> dificultades = Collections.unmodifiableList(Arrays.asList(
            new DificultadFacil(), new DificultadMedia(), new DificultadDificil()));
    private VentanaPrincipal vista;

    public SokobanController(Juego juego, GestorDeSonido sonido, GestorDeGuardado guardado) {
        this.juego = juego;
        this.sonido = sonido;
        this.guardado = guardado;
    }

    public void iniciar() {
        vista = new VentanaPrincipal(this);
        vista.mostrarMenu();
        vista.setVisible(true);
    }

    public Juego obtenerJuego() { return juego; }

    public List<EstrategiaDificultad> obtenerDificultades() { return dificultades; }

    public void seleccionarDificultad(EstrategiaDificultad d) {
        juego.seleccionarDificultad(d);
        vista.mostrarMenu();
    }

    public void iniciarPartida(int nivel) {
        try {
            juego.iniciarNivel(nivel);
            vista.mostrarJuego();
        } catch (NivelInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void volverAlMenu() {
        vista.mostrarMenu();
    }

    public void abrirSkins() {
        vista.mostrarSkins();
        vista.mostrarMenu();
    }

    public void mover(Direccion d) {
        if (!juego.hayPartida() || juego.nivelCompletado() || juego.hayApuestaPendiente()) return;
        ResultadoMovimiento r = juego.mover(d);
        reproducirSonidos(r);
        vista.refrescarJuego();

        if (r.encontroTimba()) {
            SwingUtilities.invokeLater(this::abrirApuesta);
        } else if (juego.nivelCompletado()) {
            sonido.reproducirSonido(GestorDeSonido.VICTORIA);
            SwingUtilities.invokeLater(this::mostrarResumen);
        }
    }

    private void reproducirSonidos(ResultadoMovimiento r) {
        if (r.encontroTimba()) {
            sonido.reproducirSonido(GestorDeSonido.TIMBA);
            return;
        }
        if (!r.seMovio()) {
            sonido.reproducirSonido(GestorDeSonido.BLOQUEO);
            return;
        }
        sonido.reproducirSonido(r.huboEmpuje() ? GestorDeSonido.EMPUJE : GestorDeSonido.PASO);
        if (r.seRompioCaja()) sonido.reproducirSonido(GestorDeSonido.ROMPER);
        if (r.cambioPuerta()) sonido.reproducirSonido(GestorDeSonido.PUERTA);
        if (r.cayoPiso()) sonido.reproducirSonido(GestorDeSonido.CAE);
        if (r.seRegeneroPiso()) sonido.reproducirSonido(GestorDeSonido.REGENERAR);
    }

    public void deshacer() {
        if (!juego.hayPartida() || juego.hayApuestaPendiente()) return;
        if (juego.deshacer()) {
            sonido.reproducirSonido(GestorDeSonido.DESHACER);
        } else {
            sonido.reproducirSonido(GestorDeSonido.BLOQUEO);
        }
        vista.refrescarJuego();
    }

    public void reiniciarNivel() {
        if (!juego.hayPartida()) return;
        juego.reiniciarNivel();
        sonido.reproducirSonido(GestorDeSonido.REINICIAR);
        vista.refrescarJuego();
    }

    private void mostrarResumen() {
        ResumenNivel r = juego.obtenerResumen();
        if (r == null) return;
        if (!r.obtenerSkinsComprables().isEmpty()) sonido.reproducirSonido(GestorDeSonido.DESBLOQUEO);
        boolean siguiente = vista.mostrarResumen(r);
        if (siguiente && juego.haySiguienteNivel()) {
            juego.siguienteNivel();
            vista.mostrarJuego();
        } else {
            vista.mostrarMenu();
        }
    }

    private void abrirApuesta() {
        if (!juego.hayApuestaPendiente()) return;
        CajaTimba timba = juego.obtenerTimbaPendiente();
        if (!juego.puedeApostar()) {
            vista.mostrarMensaje("Caja timba", "Necesitas al menos " + timba.obtenerApuestaMinima()
                    + " pts para apostar. Tu puntaje acumulado es " + juego.obtenerPerfil().obtenerPuntajeAcumulado() + ".");
            juego.rechazarApuesta();
            return;
        }
        vista.mostrarApuesta(juego.obtenerPerfil().obtenerPuntajeAcumulado(),
                timba.obtenerApuestaMinima(), timba.obtenerApuestaMaxima(),
                juego::obtenerProbabilidad,
                this::apostar);
        if (juego.hayApuestaPendiente()) juego.rechazarApuesta();
        vista.refrescarJuego();
    }

    private ResultadoApuesta apostar(int monto, ColorApuesta color) {
        ResultadoApuesta r = juego.apostar(monto, color);
        javax.swing.Timer t = new javax.swing.Timer(1550, e ->
                sonido.reproducirSonido(r.gano() ? GestorDeSonido.GANAR : GestorDeSonido.PERDER));
        t.setRepeats(false);
        t.start();
        sonido.reproducirSonido(GestorDeSonido.TIMBA);
        vista.refrescarJuego();
        return r;
    }

    public boolean hayGuardado() {
        return guardado.existeGuardado();
    }

    public void guardarPartida() {
        try {
            PartidaGuardada g = juego.crearGuardado();
            guardado.guardar(g);
            vista.mostrarMensaje("Guardado", "Se guardo: " + g.obtenerDescripcion());
            vista.refrescar();
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar: " + e.getMessage());
        }
    }

    public void cargarPartida() {
        try {
            PartidaGuardada g = guardado.cargar();
            if (!vista.confirmar("¿Cargar " + g.obtenerDescripcion() + "?\nSe perdera el progreso no guardado.")) return;
            juego.restaurar(g);
            if (juego.hayPartida() && !juego.nivelCompletado()) {
                vista.mostrarJuego();
                if (juego.hayApuestaPendiente()) SwingUtilities.invokeLater(this::abrirApuesta);
            } else {
                vista.mostrarMenu();
            }
        } catch (IOException e) {
            vista.mostrarError("No se pudo cargar: " + e.getMessage());
        }
    }

    public void alternarSonido() {
        sonido.alternarSilencio();
        vista.refrescarJuego();
    }

    public boolean sonidoActivo() {
        return !sonido.estaSilenciado();
    }
}
