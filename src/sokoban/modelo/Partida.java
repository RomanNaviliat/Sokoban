package sokoban.modelo;

import sokoban.modelo.cajas.CajaTimba;
import sokoban.modelo.creadores.CajaFragilCreator;
import sokoban.modelo.dificultad.EstrategiaDificultad;
import sokoban.modelo.puntaje.Puntaje;
import sokoban.modelo.skins.SkinInterface;
import sokoban.modelo.timba.Apuesta;
import sokoban.modelo.timba.CajaTimbaService;
import sokoban.modelo.timba.ResultadoApuesta;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int MAX_HISTORIAL = 15;
    public static final int MOVIMIENTOS_POR_UNDO = 5;
    public static final int MAX_UNDOS_CONSECUTIVOS = 3;

    private final EstrategiaDificultad dificultad;
    private final CajaTimbaService servicioTimba;
    private Tablero tablero;
    private Puntaje puntaje;
    private final Deque<EstadoTablero> historial = new ArrayDeque<>();
    private int nivelActual;
    private SkinInterface skin;
    private CajaTimba apuestaPendiente;
    private int undosConsecutivos;

    public Partida(EstrategiaDificultad dificultad, CajaTimbaService servicioTimba) {
        this.dificultad = dificultad;
        this.servicioTimba = servicioTimba;
    }

    public void cargarNivel(int numero, SkinInterface skin) {
        this.nivelActual = numero;
        this.skin = skin;
        MapaNivel mapa = new MapaNivel(
                numero,
                dificultad.obtenerRutaNivel(numero),
                dificultad.obtenerCantidadCajasFragiles(),
                skin.ajustarResistenciaFragil(CajaFragilCreator.RESISTENCIA_BASE),
                servicioTimba);
        this.tablero = mapa.construirTablero(skin);
        this.puntaje = new Puntaje(dificultad.crearEstrategiaPuntaje());
        this.historial.clear();
        this.apuestaPendiente = null;
        this.undosConsecutivos = 0;
    }

    public ResultadoMovimiento mover(Direccion d) {
        if (estaCompleta() || hayApuestaPendiente()) return ResultadoMovimiento.bloqueado();
        EstadoTablero antes = tablero.guardarEstado();
        ResultadoMovimiento r = tablero.moverJugador(d);
        if (r.seMovio()) {
            historial.addFirst(antes);
            while (historial.size() > MAX_HISTORIAL) historial.removeLast();
            puntaje.registrarMovimiento();
            puntaje.registrarEmpuje(r.obtenerCajasEmpujadas());
            undosConsecutivos = 0;
        }
        if (r.encontroTimba()) apuestaPendiente = r.obtenerTimba();
        return r;
    }

    public boolean puedeDeshacer() {
        return !estaCompleta() && !historial.isEmpty() && undosConsecutivos < MAX_UNDOS_CONSECUTIVOS;
    }

    public boolean deshacer() {
        if (!puedeDeshacer()) return false;
        EstadoTablero objetivo = null;
        for (int i = 0; i < MOVIMIENTOS_POR_UNDO && !historial.isEmpty(); i++) {
            objetivo = historial.pollFirst();
        }
        tablero.restaurarEstado(objetivo);
        undosConsecutivos++;
        puntaje.registrarRetroceso();
        return true;
    }

    public int obtenerUndosRestantes() {
        return historial.isEmpty() ? 0 : MAX_UNDOS_CONSECUTIVOS - undosConsecutivos;
    }

    public void reiniciar() {
        cargarNivel(nivelActual, skin);
    }

    public boolean estaCompleta() {
        return tablero.todosLosObjetivosCumplidos();
    }

    public boolean hayApuestaPendiente() {
        return apuestaPendiente != null;
    }

    public CajaTimba obtenerApuestaPendiente() {
        return apuestaPendiente;
    }

    public ResultadoApuesta apostar(Apuesta a, double probabilidadVictoria) {
        if (apuestaPendiente == null) throw new IllegalStateException("No hay apuesta pendiente");
        ResultadoApuesta r = apuestaPendiente.realizarApuesta(a, probabilidadVictoria);
        tablero.retirarCaja(apuestaPendiente);
        puntaje.registrarApuesta(r.obtenerNeto());
        apuestaPendiente = null;
        return r;
    }

    public void rechazarApuesta() {
        apuestaPendiente = null;
    }

    public Tablero obtenerTablero() { return tablero; }
    public Puntaje obtenerPuntaje() { return puntaje; }
    public int obtenerNivelActual() { return nivelActual; }
    public EstrategiaDificultad obtenerDificultad() { return dificultad; }
    public SkinInterface obtenerSkin() { return skin; }
}
