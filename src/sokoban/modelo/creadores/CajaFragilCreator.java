package sokoban.modelo.creadores;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.cajas.CajaFragil;

public class CajaFragilCreator extends CajaCreator {
    public static final int RESISTENCIA_BASE = 10;

    private final int movimientosMaximos;

    public CajaFragilCreator(int movimientosMaximos) {
        this.movimientosMaximos = movimientosMaximos;
    }

    @Override
    public Caja crearCaja() {
        return new CajaFragil(movimientosMaximos);
    }
}
