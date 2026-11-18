package sokoban.modelo.dificultad;

import sokoban.modelo.puntaje.EstrategiaPuntaje;

import java.io.Serializable;

public interface EstrategiaDificultad extends Serializable {
    String obtenerNombre();

    String obtenerRutaNivel(int n);

    int obtenerCantidadNiveles();

    int obtenerCantidadCajasFragiles();

    EstrategiaPuntaje crearEstrategiaPuntaje();

    int calcularRecompensa(int puntaje);
}
