package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.Suelo;
import sokoban.modelo.suelos.SueloNormal;

public class SueloNormalCreator extends SueloCreator {
    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        return new SueloNormal();
    }
}
