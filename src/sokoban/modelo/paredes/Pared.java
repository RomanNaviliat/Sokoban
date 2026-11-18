package sokoban.modelo.paredes;

import java.io.Serializable;

public abstract class Pared implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract boolean esTransitable();

    public abstract String obtenerClaveImagen();
}
