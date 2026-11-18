package sokoban.modelo.dificultad;

public abstract class DificultadBase implements EstrategiaDificultad {
    private static final long serialVersionUID = 1L;

    public static final int CANTIDAD_NIVELES = 6;

    @Override
    public String obtenerRutaNivel(int n) {
        return "/niveles/nivel" + n + ".txt";
    }

    @Override
    public int obtenerCantidadNiveles() {
        return CANTIDAD_NIVELES;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass() == getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return obtenerNombre();
    }
}
