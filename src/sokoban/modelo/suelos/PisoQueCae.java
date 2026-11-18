package sokoban.modelo.suelos;

import sokoban.modelo.cajas.Caja;

public class PisoQueCae implements Suelo, ObservadorBoton, ConEstadoInterno {
    private static final long serialVersionUID = 1L;

    private final int pasadasMaximas;
    private int pasadasRestantes;

    public PisoQueCae(int pasadasMaximas) {
        this.pasadasMaximas = pasadasMaximas;
        this.pasadasRestantes = pasadasMaximas;
    }

    public boolean estaCaido() { return pasadasRestantes <= 0; }

    @Override public boolean esTransitable() { return !estaCaido(); }
    @Override public void alEntrarJugador() { }

    @Override
    public void alSalirJugador() {
        if (pasadasRestantes > 0) pasadasRestantes--;
    }

    @Override public void alRecibirCaja(Caja c) { }
    @Override public void alRetirarCaja(Caja c) { }

    public void regenerar() {
        pasadasRestantes = pasadasMaximas;
    }

    @Override
    public void actualizar(boolean presionado) {
        if (presionado) regenerar();
    }

    public int obtenerPasadasRestantes() { return pasadasRestantes; }

    @Override public int obtenerEstadoInterno() { return pasadasRestantes; }
    @Override public void restaurarEstadoInterno(int estado) { pasadasRestantes = estado; }

    @Override
    public String obtenerClaveImagen() {
        if (estaCaido()) return "piso_caido";
        return pasadasRestantes == 1 ? "piso_que_cae_agrietado" : "piso_que_cae";
    }
}
