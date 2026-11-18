package sokoban.modelo.cajas;

import sokoban.modelo.ResultadoMovimiento;

import java.io.Serializable;

public interface Caja extends Serializable {
    void empujar();

    boolean estaRota();

    boolean activaCerrojo();

    boolean esEmpujable();

    String obtenerInfo();

    String obtenerClaveImagen();

    default int obtenerEstadoInterno() { return 0; }

    default void restaurarEstadoInterno(int estado) { }

    void interactuar(ResultadoMovimiento resultado);

    String obtenerEtiqueta();
}
