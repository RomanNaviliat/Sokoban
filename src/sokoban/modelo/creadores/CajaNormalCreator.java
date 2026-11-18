package sokoban.modelo.creadores;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.cajas.CajaNormal;

public class CajaNormalCreator extends CajaCreator {
    @Override
    public Caja crearCaja() {
        return new CajaNormal();
    }
}
