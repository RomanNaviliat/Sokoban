package sokoban.modelo.creadores;

import sokoban.modelo.MecanismosDelNivel;
import sokoban.modelo.Posicion;
import sokoban.modelo.suelos.CasilleroDestino;
import sokoban.modelo.suelos.Suelo;

public class CasilleroDestinoCreator extends SueloCreator {
    @Override
    public Suelo crearSuelo(Posicion p, MecanismosDelNivel mecanismos) {
        CasilleroDestino destino = new CasilleroDestino();
        mecanismos.agregarObjetivo(destino);
        mecanismos.agregarConEstado(p, destino);
        return destino;
    }
}
