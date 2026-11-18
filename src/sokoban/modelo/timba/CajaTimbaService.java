package sokoban.modelo.timba;

import sokoban.modelo.cajas.CajaTimba;

import java.io.Serializable;

public interface CajaTimbaService extends Serializable {
    boolean validarApuesta(CajaTimba caja, Apuesta a);

    ResultadoApuesta procesarApuesta(CajaTimba caja, Apuesta a, double probabilidadVictoria);

    int calcularPremio(CajaTimba caja, Apuesta a);
}
