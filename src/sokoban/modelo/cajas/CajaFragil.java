package sokoban.modelo.cajas;

import sokoban.modelo.ResultadoMovimiento;

public class CajaFragil implements Caja {
    private static final long serialVersionUID = 1L;

    private final int movimientosMaximos;
    private int movimientosRestantes;

    public CajaFragil(int movimientosMaximos) {
        this.movimientosMaximos = movimientosMaximos;
        this.movimientosRestantes = movimientosMaximos;
    }

    @Override
    public void empujar() {
        if (movimientosRestantes > 0) {
            movimientosRestantes--;
        }
    }

    @Override public boolean estaRota() { return movimientosRestantes <= 0; }
    @Override public boolean activaCerrojo() { return false; }
    @Override public boolean esEmpujable() { return true; }

    @Override
    public String obtenerInfo() {
        return "Caja frágil (" + movimientosRestantes + "/" + movimientosMaximos + ")";
    }

    @Override public String obtenerClaveImagen() { return "caja_fragil"; }

    public int obtenerMovimientosRestantes() { return movimientosRestantes; }
    public int obtenerMovimientosMaximos() { return movimientosMaximos; }

    @Override public int obtenerEstadoInterno() { return movimientosRestantes; }
    @Override public void restaurarEstadoInterno(int estado) { movimientosRestantes = estado; }

    @Override public void interactuar(ResultadoMovimiento resultado) { }
    @Override public String obtenerEtiqueta() { return String.valueOf(movimientosRestantes); }
}
