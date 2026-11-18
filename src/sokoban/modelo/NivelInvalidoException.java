package sokoban.modelo;

public class NivelInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NivelInvalidoException(String mensaje) {
        super(mensaje);
    }

    public NivelInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
