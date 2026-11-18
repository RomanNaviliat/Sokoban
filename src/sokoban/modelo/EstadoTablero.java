package sokoban.modelo;

import sokoban.modelo.cajas.Caja;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class EstadoTablero implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Posicion posicionJugador;
    private final Map<Posicion, Caja> posicionesCajas;
    private final Map<Caja, Integer> estadosCajas;
    private final Map<Posicion, Integer> estadosSuelos;

    EstadoTablero(Posicion posicionJugador, Map<Posicion, Caja> cajas,
                  Map<Caja, Integer> estadosCajas, Map<Posicion, Integer> estadosSuelos) {
        this.posicionJugador = posicionJugador;
        this.posicionesCajas = Collections.unmodifiableMap(new HashMap<>(cajas));
        this.estadosCajas = Collections.unmodifiableMap(new HashMap<>(estadosCajas));
        this.estadosSuelos = Collections.unmodifiableMap(new HashMap<>(estadosSuelos));
    }

    public Posicion obtenerPosicionJugador() { return posicionJugador; }
    public Map<Posicion, Caja> obtenerPosicionesCajas() { return posicionesCajas; }
    public Map<Caja, Integer> obtenerEstadosCajas() { return estadosCajas; }
    public Map<Posicion, Integer> obtenerEstadosSuelos() { return estadosSuelos; }
}
