package sokoban.modelo.creadores;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.cajas.CajaLlave;

public class CajaLlaveCreator extends CajaCreator {
    @Override
    public Caja crearCaja() {
        return new CajaLlave();
    }
}
