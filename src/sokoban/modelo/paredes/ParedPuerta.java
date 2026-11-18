package sokoban.modelo.paredes;

import sokoban.modelo.suelos.ObservadorCerrojo;

public class ParedPuerta extends Pared implements ObservadorCerrojo {
    private static final long serialVersionUID = 1L;

    private int cerrojosRequeridos;
    private int cerrojosActivos;

    public boolean estaAbierta() {
        return cerrojosRequeridos > 0 && cerrojosActivos >= cerrojosRequeridos;
    }

    @Override public boolean esTransitable() { return estaAbierta(); }

    @Override
    public void registrarCerrojo() {
        cerrojosRequeridos++;
    }

    @Override
    public void actualizar(boolean tieneLlave) {
        cerrojosActivos += tieneLlave ? 1 : -1;
        if (cerrojosActivos < 0) cerrojosActivos = 0;
    }

    @Override
    public String obtenerClaveImagen() {
        return estaAbierta() ? "puerta_abierta" : "puerta_cerrada";
    }
}
