package sokoban.modelo.puntaje;

public class PuntajeEstricto implements EstrategiaPuntaje {
    private static final long serialVersionUID = 1L;

    private final int puntajeInicial;
    private final int costoPorMovimiento;
    private final int penalizacionPorRetroceso;
    private final int incrementoPorRetroceso;

    public PuntajeEstricto(int puntajeInicial, int costoPorMovimiento, int penalizacionPorRetroceso, int incrementoPorRetroceso) {
        this.puntajeInicial = puntajeInicial;
        this.costoPorMovimiento = costoPorMovimiento;
        this.penalizacionPorRetroceso = penalizacionPorRetroceso;
        this.incrementoPorRetroceso = incrementoPorRetroceso;
    }

    @Override
    public int calcular(int movimientos, int empujes, int retrocesos) {
        int penalizacion = 0;
        for (int i = 0; i < retrocesos; i++) {
            penalizacion += penalizacionPorRetroceso + i * incrementoPorRetroceso;
        }
        return Math.max(0, puntajeInicial - costoPorMovimiento * movimientos - penalizacion);
    }

    @Override
    public String obtenerDescripcion() {
        return puntajeInicial + " - " + costoPorMovimiento + " x mov - undo creciente ("
                + penalizacionPorRetroceso + ", +" + incrementoPorRetroceso + " cada uno)";
    }
}
