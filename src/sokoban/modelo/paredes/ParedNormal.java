package sokoban.modelo.paredes;

public class ParedNormal extends Pared {
    private static final long serialVersionUID = 1L;

    @Override public boolean esTransitable() { return false; }
    @Override public String obtenerClaveImagen() { return "pared"; }
}
