package sokoban.modelo.dificultad;

import sokoban.modelo.puntaje.EstrategiaPuntaje;
import sokoban.modelo.puntaje.PuntajeEstandar;

public class DificultadFacil extends DificultadBase {
    private static final long serialVersionUID = 1L;

    @Override public String obtenerNombre() { return "Facil"; }
    @Override public int obtenerCantidadCajasFragiles() { return 2; }

    @Override
    public EstrategiaPuntaje crearEstrategiaPuntaje() {
        return new PuntajeEstandar(1000, 2, 1, 25);
    }

    @Override
    public int calcularRecompensa(int puntaje) {
        return puntaje;
    }
}
