package sokoban.modelo.puntaje;

import java.io.Serializable;

public interface EstrategiaPuntaje extends Serializable {
    int calcular(int movimientos, int empujes, int retrocesos);

    String obtenerDescripcion();
}
