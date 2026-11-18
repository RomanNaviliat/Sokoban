package sokoban.modelo.creadores;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.cajas.CajaTimba;
import sokoban.modelo.timba.CajaTimbaService;

public class CajaTimbaCreator extends CajaCreator {
    public static final int APUESTA_MINIMA = 100;
    public static final int APUESTA_MAXIMA = 1000;

    private final int apuestaMinima;
    private final int apuestaMaxima;
    private final CajaTimbaService servicio;

    public CajaTimbaCreator(CajaTimbaService servicio) {
        this(APUESTA_MINIMA, APUESTA_MAXIMA, servicio);
    }

    public CajaTimbaCreator(int apuestaMinima, int apuestaMaxima, CajaTimbaService servicio) {
        this.apuestaMinima = apuestaMinima;
        this.apuestaMaxima = apuestaMaxima;
        this.servicio = servicio;
    }

    @Override
    public Caja crearCaja() {
        return new CajaTimba(apuestaMinima, apuestaMaxima, servicio);
    }
}
