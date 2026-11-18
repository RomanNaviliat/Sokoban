package sokoban.modelo.skins;

public enum TipoSkin {
    MANO_SUAVE("Manos Suaves", 2500, "Las cajas fragiles soportan el doble de empujes") {
        @Override public SkinInterface decorar(SkinInterface s) { return new SkinManoSuave(s); }
    },
    LUDOPATA("Ludopata", 5000, "+10% de probabilidad de ganar en la caja timba") {
        @Override public SkinInterface decorar(SkinInterface s) { return new SkinLudopata(s); }
    },
    ROBOT("Robot", 8000, "Puede empujar dos cajas a la vez") {
        @Override public SkinInterface decorar(SkinInterface s) { return new SkinRobot(s); }
    };

    private final String nombre;
    private final int precio;
    private final String descripcion;

    TipoSkin(String nombre, int precio, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public abstract SkinInterface decorar(SkinInterface skin);

    public String obtenerNombre() { return nombre; }
    public int obtenerPrecio() { return precio; }
    public String obtenerDescripcion() { return descripcion; }
}
