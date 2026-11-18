package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

import java.util.ArrayList;
import java.util.List;

public class SueloBoton implements Suelo, ConEstadoInterno {
    private static final long serialVersionUID = 1L;

    private boolean estaPresionado;
    private final List<ObservadorBoton> observadores = new ArrayList<>();

    public void suscribir(ObservadorBoton o) {
        if (!observadores.contains(o)) observadores.add(o);
    }

    public void desuscribir(ObservadorBoton o) {
        observadores.remove(o);
    }

    private void cambiarEstado(boolean presionado) {
        if (estaPresionado != presionado) {
            estaPresionado = presionado;
            notificar();
        }
    }

    private void notificar() {
        for (ObservadorBoton o : new ArrayList<>(observadores)) {
            o.actualizar(estaPresionado);
        }
    }

    @Override public boolean esTransitable() { return true; }
    @Override public void alEntrarJugador() { cambiarEstado(true); }
    @Override public void alSalirJugador() { cambiarEstado(false); }
    @Override public void alRecibirCaja(Caja c) { cambiarEstado(true); }
    @Override public void alRetirarCaja(Caja c) { cambiarEstado(false); }

    public boolean estaPresionado() { return estaPresionado; }

    @Override public int obtenerEstadoInterno() { return estaPresionado ? 1 : 0; }
    @Override public void restaurarEstadoInterno(int estado) { estaPresionado = estado == 1; }

    @Override
    public String obtenerClaveImagen() {
        return estaPresionado ? "suelo_boton_presionado" : "suelo_boton";
    }
}
