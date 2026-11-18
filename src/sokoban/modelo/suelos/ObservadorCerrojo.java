package sokoban.modelo.suelos;

import java.io.Serializable;

public interface ObservadorCerrojo extends Serializable {
    void registrarCerrojo();

    void actualizar(boolean tieneLlave);
}
