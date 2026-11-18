package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.PisoQueCae;
import sokoban.modelo.suelos.Suelo;

public class PisoQueCaeCreator extends SueloCreator {
    public static final int PASADAS_POR_DEFECTO = 2;

    private final int pasadasMaximas;

    public PisoQueCaeCreator() {
        this(PASADAS_POR_DEFECTO);
    }

    public PisoQueCaeCreator(int pasadasMaximas) {
        this.pasadasMaximas = pasadasMaximas;
    }

    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        PisoQueCae piso = new PisoQueCae(pasadasMaximas);
        mecanismos.agregarPisoQueCae(piso);
        mecanismos.agregarConEstado(p, piso);
        return piso;
    }
}
