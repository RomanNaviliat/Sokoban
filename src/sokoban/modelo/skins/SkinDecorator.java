package sokoban.modelo.skins;

public abstract class SkinDecorator implements SkinInterface {
    private static final long serialVersionUID = 1L;

    protected final SkinInterface skin;

    protected SkinDecorator(SkinInterface skin) {
        this.skin = skin;
    }

    @Override public String obtenerClaveImagen() { return skin.obtenerClaveImagen(); }
    @Override public String obtenerNombre() { return skin.obtenerNombre(); }
    @Override public double ajustarProbabilidadTimba(double p) { return skin.ajustarProbabilidadTimba(p); }
    @Override public int ajustarResistenciaFragil(int r) { return skin.ajustarResistenciaFragil(r); }
    @Override public int obtenerMaxCajasEmpujables() { return skin.obtenerMaxCajasEmpujables(); }
}
