package sokoban.modelo;

import sokoban.modelo.cajas.CajaTimba;
import sokoban.modelo.dificultad.DificultadFacil;
import sokoban.modelo.dificultad.EstrategiaDificultad;
import sokoban.modelo.guardado.PartidaGuardada;
import sokoban.modelo.puntaje.Puntaje;
import sokoban.modelo.skins.ResultadoDesbloqueo;
import sokoban.modelo.skins.SkinInterface;
import sokoban.modelo.skins.TipoSkin;
import sokoban.modelo.timba.Apuesta;
import sokoban.modelo.timba.CajaTimbaService;
import sokoban.modelo.timba.CajaTimbaServiceImpl;
import sokoban.modelo.timba.ColorApuesta;
import sokoban.modelo.timba.ResultadoApuesta;

import java.time.LocalDateTime;
import java.util.List;

public class Juego {
    private Perfil perfil;
    private EstrategiaDificultad dificultad;
    private Partida partida;
    private final Tienda tienda;
    private final CajaTimbaService servicioTimba;
    private boolean recompensaEntregada;
    private ResumenNivel ultimoResumen;

    public Juego() {
        this(new Perfil(), new Tienda(), new CajaTimbaServiceImpl());
    }

    public Juego(Perfil perfil, Tienda tienda, CajaTimbaService servicioTimba) {
        this.perfil = perfil;
        this.tienda = tienda;
        this.servicioTimba = servicioTimba;
        this.dificultad = new DificultadFacil();
    }

    public void seleccionarDificultad(EstrategiaDificultad d) {
        this.dificultad = d;
    }

    public boolean estaDesbloqueado(EstrategiaDificultad d, int nivel) {
        return nivel == 1 || perfil.completoNivel(d.obtenerNombre(), nivel - 1);
    }

    public void iniciarNivel(int n) {
        partida = new Partida(dificultad, servicioTimba);
        partida.cargarNivel(n, obtenerSkinJugador());
        recompensaEntregada = false;
        ultimoResumen = null;
    }

    public boolean haySiguienteNivel() {
        return partida != null && partida.obtenerNivelActual() < dificultad.obtenerCantidadNiveles();
    }

    public void siguienteNivel() {
        if (haySiguienteNivel()) iniciarNivel(partida.obtenerNivelActual() + 1);
    }

    public ResultadoMovimiento mover(Direccion d) {
        ResultadoMovimiento r = partida.mover(d);
        if (partida.estaCompleta() && !recompensaEntregada) {
            finalizarNivel();
        }
        return r;
    }

    private void finalizarNivel() {
        Puntaje p = partida.obtenerPuntaje();
        int puntos = p.calcularPuntaje();
        int recompensa = dificultad.calcularRecompensa(puntos);
        perfil.registrarNivelCompletado(dificultad.obtenerNombre(), partida.obtenerNivelActual(), puntos, recompensa);
        List<TipoSkin> comprables = tienda.obtenerComprables(perfil);
        recompensaEntregada = true;
        ultimoResumen = new ResumenNivel(
                dificultad.obtenerNombre(), partida.obtenerNivelActual(),
                p.obtenerMovimientos(), p.obtenerEmpujes(), p.obtenerRetrocesos(),
                p.obtenerResultadoApuestas(), puntos, recompensa,
                p.obtenerEstrategia().obtenerDescripcion(), comprables, haySiguienteNivel());
    }

    public boolean nivelCompletado() {
        return partida != null && partida.estaCompleta();
    }

    public ResumenNivel obtenerResumen() {
        return ultimoResumen;
    }

    public boolean deshacer() {
        return partida.deshacer();
    }

    public void reiniciarNivel() {
        partida.cargarNivel(partida.obtenerNivelActual(), obtenerSkinJugador());
        recompensaEntregada = false;
        ultimoResumen = null;
    }

    public boolean hayApuestaPendiente() {
        return partida != null && partida.hayApuestaPendiente();
    }

    public CajaTimba obtenerTimbaPendiente() {
        return partida.obtenerApuestaPendiente();
    }

    public boolean puedeApostar() {
        return hayApuestaPendiente()
                && perfil.obtenerPuntajeAcumulado() >= partida.obtenerApuestaPendiente().obtenerApuestaMinima();
    }

    public double obtenerProbabilidad(ColorApuesta color) {
        return partida.obtenerSkin().ajustarProbabilidadTimba(color.obtenerProbabilidadBase());
    }

    public ResultadoApuesta apostar(int monto, ColorApuesta color) {
        if (!hayApuestaPendiente()) throw new IllegalStateException("No hay apuesta pendiente");
        Apuesta apuesta = new Apuesta(monto, color);
        if (!partida.obtenerApuestaPendiente().validarApuesta(apuesta)) {
            throw new IllegalArgumentException("La apuesta debe estar entre "
                    + partida.obtenerApuestaPendiente().obtenerApuestaMinima() + " y "
                    + partida.obtenerApuestaPendiente().obtenerApuestaMaxima());
        }
        if (!perfil.gastarPuntaje(monto)) {
            throw new IllegalArgumentException("Puntaje acumulado insuficiente");
        }
        ResultadoApuesta r = partida.apostar(apuesta, obtenerProbabilidad(color));
        if (r.gano()) perfil.agregarPuntaje(r.obtenerPremio());
        return r;
    }

    public void rechazarApuesta() {
        if (partida != null) partida.rechazarApuesta();
    }

    public SkinInterface obtenerSkinJugador() {
        SkinInterface skin = new Jugador();
        for (TipoSkin t : perfil.obtenerSkinsEquipadas()) {
            skin = t.decorar(skin);
        }
        return skin;
    }

    public ResultadoDesbloqueo comprarSkin(TipoSkin s) {
        return tienda.comprar(perfil, s);
    }

    public boolean equiparSkin(TipoSkin s, boolean equipada) {
        return perfil.equiparSkin(s, equipada);
    }

    public PartidaGuardada crearGuardado() {
        return new PartidaGuardada(perfil, dificultad, partida, LocalDateTime.now());
    }

    public void restaurar(PartidaGuardada g) {
        this.perfil = g.obtenerPerfil();
        this.dificultad = g.obtenerDificultad();
        this.partida = g.obtenerPartida();
        this.recompensaEntregada = partida != null && partida.estaCompleta();
        this.ultimoResumen = null;
    }

    public Perfil obtenerPerfil() { return perfil; }
    public EstrategiaDificultad obtenerDificultad() { return dificultad; }
    public Partida obtenerPartida() { return partida; }
    public Tienda obtenerTienda() { return tienda; }
    public boolean hayPartida() { return partida != null; }
}
