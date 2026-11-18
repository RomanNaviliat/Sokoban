package sokoban.modelo;

import java.io.Serializable;

public final class Posicion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int posicionX;
    private final int posicionY;

    public Posicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    public int obtenerX() { return posicionX; }
    public int obtenerY() { return posicionY; }

    public Posicion desplazar(Direccion d) {
        return new Posicion(posicionX + d.obtenerDx(), posicionY + d.obtenerDy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicion)) return false;
        Posicion p = (Posicion) o;
        return posicionX == p.posicionX && posicionY == p.posicionY;
    }

    @Override
    public int hashCode() {
        return 31 * posicionX + posicionY;
    }

    @Override
    public String toString() {
        return "(" + posicionX + "," + posicionY + ")";
    }
}
