package sokoban.modelo.cajas;

import sokoban.modelo.ResultadoMovimiento;
import sokoban.modelo.timba.Apuesta;
import sokoban.modelo.timba.CajaTimbaService;
import sokoban.modelo.timba.ResultadoApuesta;

public class CajaTimba implements Caja {
    private static final long serialVersionUID = 1L;

    private final int apuestaMinima;
    private final int apuestaMaxima;
    private final CajaTimbaService servicio;
    private boolean activa = true;

    public CajaTimba(int apuestaMinima, int apuestaMaxima, CajaTimbaService servicio) {
        this.apuestaMinima = apuestaMinima;
        this.apuestaMaxima = apuestaMaxima;
        this.servicio = servicio;
    }

    @Override public void empujar() { }
    @Override public boolean estaRota() { return false; }
    @Override public boolean activaCerrojo() { return false; }
    @Override public boolean esEmpujable() { return false; }

    @Override
    public String obtenerInfo() {
        return "Caja timba (apuesta entre " + apuestaMinima + " y " + apuestaMaxima + ")";
    }

    @Override public String obtenerClaveImagen() { return "caja_timba"; }

    public boolean validarApuesta(Apuesta a) {
        return activa && servicio.validarApuesta(this, a);
    }

    public ResultadoApuesta realizarApuesta(Apuesta a, double probabilidadVictoria) {
        if (!validarApuesta(a)) {
            throw new IllegalArgumentException("Apuesta inválida");
        }
        ResultadoApuesta r = servicio.procesarApuesta(this, a, probabilidadVictoria);
        activa = false;
        return r;
    }

    public boolean estaActiva() { return activa; }
    public int obtenerApuestaMinima() { return apuestaMinima; }
    public int obtenerApuestaMaxima() { return apuestaMaxima; }

    @Override
    public void interactuar(ResultadoMovimiento resultado) {
        if (activa) resultado.marcarTimba(this);
    }

    @Override public String obtenerEtiqueta() { return activa ? "$" : ""; }
}
