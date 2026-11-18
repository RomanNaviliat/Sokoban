package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

public class SueloNormal implements Suelo {
    private static final long serialVersionUID = 1L;

    @Override public boolean esTransitable() { return true; }
    @Override public void alEntrarJugador() { }
    @Override public void alSalirJugador() { }
    @Override public void alRecibirCaja(Caja c) { }
    @Override public void alRetirarCaja(Caja c) { }
    @Override public String obtenerClaveImagen() { return "suelo_normal"; }
}
