package sokoban.modelo.dificultad;

import sokoban.modelo.puntaje.EstrategiaPuntaje;
import sokoban.modelo.puntaje.PuntajeEstricto;

public class DificultadDificil extends DificultadBase {
    private static final long serialVersionUID = 1L;

    @Override public String obtenerNombre() { return "Dificil"; }
    @Override public int obtenerCantidadCajasFragiles() { return 6; }

    @Override
    public EstrategiaPuntaje crearEstrategiaPuntaje() {
        return new PuntajeEstricto(1500, 4, 50, 50);
    }

    @Override
    public int calcularRecompensa(int puntaje) {
        return puntaje * 2;
    }
}
