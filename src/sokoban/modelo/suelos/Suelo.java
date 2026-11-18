package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

import java.io.Serializable;

public interface Suelo extends Serializable {
    boolean esTransitable();

    void alEntrarJugador();

    void alSalirJugador();

    void alRecibirCaja(Caja c);

    void alRetirarCaja(Caja c);

    String obtenerClaveImagen();
}
