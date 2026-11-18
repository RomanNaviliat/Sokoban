package sokoban.modelo.suelos;

import java.io.Serializable;

public interface ObservadorBoton extends Serializable {
    void actualizar(boolean presionado);
}
