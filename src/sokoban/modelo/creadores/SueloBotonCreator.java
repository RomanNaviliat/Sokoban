package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.Suelo;
import sokoban.modelo.suelos.SueloBoton;

public class SueloBotonCreator extends SueloCreator {
    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        SueloBoton boton = new SueloBoton();
        mecanismos.agregarBoton(boton);
        mecanismos.agregarConEstado(p, boton);
        return boton;
    }
}
