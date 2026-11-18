package sokoban.modelo.skins;

public class SkinRobot extends SkinDecorator {
    private static final long serialVersionUID = 1L;

    public SkinRobot(SkinInterface skin) {
        super(skin);
    }

    @Override
    public int obtenerMaxCajasEmpujables() {
        return Math.max(2, super.obtenerMaxCajasEmpujables());
    }

    @Override public String obtenerClaveImagen() { return super.obtenerClaveImagen() + "+robot"; }
    @Override public String obtenerNombre() { return super.obtenerNombre() + " Robot"; }
}
