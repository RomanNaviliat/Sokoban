package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.Suelo;
import sokoban.modelo.suelos.SueloCerrojo;

public class SueloCerrojoCreator extends SueloCreator {
    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        SueloCerrojo cerrojo = new SueloCerrojo();
        mecanismos.agregarCerrojo(cerrojo);
        mecanismos.agregarConEstado(p, cerrojo);
        return cerrojo;
    }
}
