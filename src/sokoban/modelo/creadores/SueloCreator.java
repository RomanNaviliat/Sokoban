package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.Suelo;

public abstract class SueloCreator {
    public abstract Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos);
}
