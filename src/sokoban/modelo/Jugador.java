package sokoban.modelo;

import sokoban.modelo.skins.SkinInterface;

public class Jugador implements SkinInterface {
    private static final long serialVersionUID = 1L;

    private final String nombre;

    public Jugador() {
        this("Sokoban");
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    @Override public String obtenerClaveImagen() { return "jugador"; }
    @Override public String obtenerNombre() { return nombre; }
    @Override public double ajustarProbabilidadTimba(double probabilidadBase) { return probabilidadBase; }
    @Override public int ajustarResistenciaFragil(int resistenciaBase) { return resistenciaBase; }
    @Override public int obtenerMaxCajasEmpujables() { return 1; }
}
