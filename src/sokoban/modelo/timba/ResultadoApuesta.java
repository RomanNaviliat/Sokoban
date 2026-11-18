package sokoban.modelo.timba;

public final class ResultadoApuesta {
    private final Apuesta apuesta;
    private final boolean gano;
    private final ColorApuesta colorSalido;
    private final int premio;

    public ResultadoApuesta(Apuesta apuesta, boolean gano, ColorApuesta colorSalido, int premio) {
        this.apuesta = apuesta;
        this.gano = gano;
        this.colorSalido = colorSalido;
        this.premio = premio;
    }

    public Apuesta obtenerApuesta() { return apuesta; }
    public boolean gano() { return gano; }
    public ColorApuesta obtenerColorSalido() { return colorSalido; }
    public int obtenerPremio() { return premio; }
    public int obtenerNeto() { return premio - apuesta.obtenerMonto(); }
}
