package sokoban.modelo.timba;

import java.io.Serializable;

public final class Apuesta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int monto;
    private final ColorApuesta color;

    public Apuesta(int monto, ColorApuesta color) {
        this.monto = monto;
        this.color = color;
    }

    public int obtenerMonto() { return monto; }
    public ColorApuesta obtenerColor() { return color; }
}
