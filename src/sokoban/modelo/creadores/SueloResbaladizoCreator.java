package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.Suelo;
import sokoban.modelo.suelos.SueloResbaladizo;

public class SueloResbaladizoCreator extends SueloCreator {
    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        mecanismos.agregarDeslizante(p);
        return new SueloResbaladizo();
    }
}
