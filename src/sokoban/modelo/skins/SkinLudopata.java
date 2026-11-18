package sokoban.modelo.skins;

public class SkinLudopata extends SkinDecorator {
    private static final long serialVersionUID = 1L;

    public static final double BONUS_PROBABILIDAD = 0.10;
    private static final double PROBABILIDAD_MAXIMA = 0.95;

    public SkinLudopata(SkinInterface skin) {
        super(skin);
    }

    @Override
    public double ajustarProbabilidadTimba(double p) {
        return Math.min(PROBABILIDAD_MAXIMA, super.ajustarProbabilidadTimba(p) + BONUS_PROBABILIDAD);
    }

    @Override public String obtenerClaveImagen() { return super.obtenerClaveImagen() + "+ludopata"; }
    @Override public String obtenerNombre() { return super.obtenerNombre() + " Ludopata"; }
}
