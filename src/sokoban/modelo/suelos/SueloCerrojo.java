package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

import java.util.ArrayList;
import java.util.List;

public class SueloCerrojo implements Suelo, ConEstadoInterno {
    private static final long serialVersionUID = 1L;

    private boolean tieneLlave;
    private final List<ObservadorCerrojo> observadores = new ArrayList<>();

    public void suscribirPuerta(ObservadorCerrojo o) {
        if (!observadores.contains(o)) {
            observadores.add(o);
            o.registrarCerrojo();
            if (tieneLlave) o.actualizar(true);
        }
    }

    public void desuscribirPuerta(ObservadorCerrojo o) {
        observadores.remove(o);
    }

    private void cambiarEstado(boolean llave) {
        if (tieneLlave != llave) {
            tieneLlave = llave;
            notificarPuertas();
        }
    }

    private void notificarPuertas() {
        for (ObservadorCerrojo o : new ArrayList<>(observadores)) {
            o.actualizar(tieneLlave);
        }
    }

    @Override public boolean esTransitable() { return true; }
    @Override public void alEntrarJugador() { }
    @Override public void alSalirJugador() { }

    @Override
    public void alRecibirCaja(Caja c) {
        if (c.activaCerrojo()) cambiarEstado(true);
    }

    @Override
    public void alRetirarCaja(Caja c) {
        if (c.activaCerrojo()) cambiarEstado(false);
    }

    public boolean tieneLlave() { return tieneLlave; }

    @Override public int obtenerEstadoInterno() { return tieneLlave ? 1 : 0; }
    @Override public void restaurarEstadoInterno(int estado) { cambiarEstado(estado == 1); }

    @Override
    public String obtenerClaveImagen() {
        return tieneLlave ? "suelo_cerrojo_activo" : "suelo_cerrojo";
    }
}
