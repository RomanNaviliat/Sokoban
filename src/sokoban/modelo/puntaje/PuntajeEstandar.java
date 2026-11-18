package sokoban.modelo.puntaje;

public class PuntajeEstandar implements EstrategiaPuntaje {
    private static final long serialVersionUID = 1L;

    private final int puntajeInicial;
    private final int costoPorMovimiento;
    private final int costoPorEmpuje;
    private final int penalizacionPorRetroceso;

    public PuntajeEstandar(int puntajeInicial, int costoPorMovimiento, int costoPorEmpuje, int penalizacionPorRetroceso) {
        this.puntajeInicial = puntajeInicial;
        this.costoPorMovimiento = costoPorMovimiento;
        this.costoPorEmpuje = costoPorEmpuje;
        this.penalizacionPorRetroceso = penalizacionPorRetroceso;
    }

    @Override
    public int calcular(int movimientos, int empujes, int retrocesos) {
        int p = puntajeInicial
                - costoPorMovimiento * movimientos
                - costoPorEmpuje * empujes
                - penalizacionPorRetroceso * retrocesos;
        return Math.max(0, p);
    }

    @Override
    public String obtenerDescripcion() {
        return puntajeInicial + " - " + costoPorMovimiento + " x mov - " + costoPorEmpuje
                + " x empuje - " + penalizacionPorRetroceso + " x undo";
    }
}
