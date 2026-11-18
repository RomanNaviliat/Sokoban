package sokoban.modelo.timba;

public enum ColorApuesta {
    ROJO("Rojo", 0.40, 2),
    NEGRO("Negro", 0.40, 2),
    VERDE("Verde", 0.20, 4);

    private final String nombre;
    private final double probabilidadBase;
    private final int multiplicadorPremio;

    ColorApuesta(String nombre, double probabilidadBase, int multiplicadorPremio) {
        this.nombre = nombre;
        this.probabilidadBase = probabilidadBase;
        this.multiplicadorPremio = multiplicadorPremio;
    }

    public String obtenerNombre() { return nombre; }
    public double obtenerProbabilidadBase() { return probabilidadBase; }
    public int obtenerMultiplicadorPremio() { return multiplicadorPremio; }
}
