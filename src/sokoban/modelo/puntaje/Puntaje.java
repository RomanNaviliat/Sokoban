package sokoban.modelo.puntaje;

import java.io.Serializable;

public class Puntaje implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cantidadMovimientos;
    private int cantidadEmpujes;
    private int cantidadRetrocesos;
    private int resultadoApuestas;
    private final EstrategiaPuntaje estrategia;

    public Puntaje(EstrategiaPuntaje estrategia) {
        this.estrategia = estrategia;
    }

    public void registrarMovimiento() { cantidadMovimientos++; }
    public void registrarEmpuje(int cajas) { cantidadEmpujes += cajas; }
    public void registrarRetroceso() { cantidadRetrocesos++; }

    public void registrarApuesta(int neto) { resultadoApuestas += neto; }

    public int calcularPuntaje() {
        return estrategia.calcular(cantidadMovimientos, cantidadEmpujes, cantidadRetrocesos);
    }

    public int obtenerMovimientos() { return cantidadMovimientos; }
    public int obtenerEmpujes() { return cantidadEmpujes; }
    public int obtenerRetrocesos() { return cantidadRetrocesos; }
    public int obtenerResultadoApuestas() { return resultadoApuestas; }
    public EstrategiaPuntaje obtenerEstrategia() { return estrategia; }
}
