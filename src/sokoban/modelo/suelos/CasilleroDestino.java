package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

public class CasilleroDestino implements Suelo, Objetivo, ConEstadoInterno {
    private static final long serialVersionUID = 1L;

    private boolean ocupado;

    @Override public boolean esTransitable() { return true; }
    @Override public void alEntrarJugador() { }
    @Override public void alSalirJugador() { }

    @Override
    public void alRecibirCaja(Caja c) {
        ocupado = true;
    }

    @Override
    public void alRetirarCaja(Caja c) {
        ocupado = false;
    }

    @Override public boolean cumpleObjetivo() { return ocupado; }

    @Override public int obtenerEstadoInterno() { return ocupado ? 1 : 0; }
    @Override public void restaurarEstadoInterno(int estado) { ocupado = estado == 1; }

    @Override public String obtenerClaveImagen() { return "casillero_destino"; }
}
