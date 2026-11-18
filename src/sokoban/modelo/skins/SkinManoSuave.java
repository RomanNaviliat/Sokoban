package sokoban.modelo.skins;

public class SkinManoSuave extends SkinDecorator {
    private static final long serialVersionUID = 1L;

    public SkinManoSuave(SkinInterface skin) {
        super(skin);
    }

    @Override
    public int ajustarResistenciaFragil(int r) {
        return super.ajustarResistenciaFragil(r) * 2;
    }

    @Override public String obtenerClaveImagen() { return super.obtenerClaveImagen() + "+manosuave"; }
    @Override public String obtenerNombre() { return super.obtenerNombre() + " Manos Suaves"; }
}
