package sokoban.modelo.skins;

import java.io.Serializable;

public interface SkinInterface extends Serializable {
    String obtenerClaveImagen();

    String obtenerNombre();

    double ajustarProbabilidadTimba(double probabilidadBase);

    int ajustarResistenciaFragil(int resistenciaBase);

    int obtenerMaxCajasEmpujables();
}
