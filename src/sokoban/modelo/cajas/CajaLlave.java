package sokoban.modelo.cajas;

import sokoban.modelo.ResultadoMovimiento;

public class CajaLlave implements Caja {
    private static final long serialVersionUID = 1L;

    @Override public void empujar() { }
    @Override public boolean estaRota() { return false; }
    @Override public boolean activaCerrojo() { return true; }
    @Override public boolean esEmpujable() { return true; }
    @Override public String obtenerInfo() { return "Caja llave"; }
    @Override public String obtenerClaveImagen() { return "caja_llave"; }

    @Override public void interactuar(ResultadoMovimiento resultado) { }
    @Override public String obtenerEtiqueta() { return ""; }
}
