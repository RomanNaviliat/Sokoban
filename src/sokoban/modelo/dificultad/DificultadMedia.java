package sokoban.modelo.dificultad;

import sokoban.modelo.puntaje.EstrategiaPuntaje;
import sokoban.modelo.puntaje.PuntajeEstandar;

public class DificultadMedia extends DificultadBase {
    private static final long serialVersionUID = 1L;

    @Override public String obtenerNombre() { return "Media"; }
    @Override public int obtenerCantidadCajasFragiles() { return 4; }

    @Override
    public EstrategiaPuntaje crearEstrategiaPuntaje() {
        return new PuntajeEstandar(1200, 3, 2, 50);
    }

    @Override
    public int calcularRecompensa(int puntaje) {
        return (int) Math.round(puntaje * 1.5);
    }
}
