package sokoban.modelo;

import sokoban.modelo.skins.ResultadoDesbloqueo;
import sokoban.modelo.skins.TipoSkin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tienda implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<TipoSkin> obtenerCatalogo() {
        return Arrays.asList(TipoSkin.values());
    }

    public int puntosFaltantes(Perfil p, TipoSkin s) {
        return Math.max(0, s.obtenerPrecio() - p.obtenerPuntajeAcumulado());
    }

    public boolean puedeComprar(Perfil p, TipoSkin s) {
        return !p.tieneSkin(s) && puntosFaltantes(p, s) == 0;
    }

    public ResultadoDesbloqueo comprar(Perfil p, TipoSkin s) {
        if (p.tieneSkin(s)) return ResultadoDesbloqueo.YA_DESBLOQUEADA;
        if (!p.gastarPuntaje(s.obtenerPrecio())) return ResultadoDesbloqueo.PUNTAJE_INSUFICIENTE;
        p.desbloquearSkin(s);
        return ResultadoDesbloqueo.DESBLOQUEADA;
    }

    public List<TipoSkin> obtenerComprables(Perfil p) {
        List<TipoSkin> lista = new ArrayList<>();
        for (TipoSkin s : obtenerCatalogo()) {
            if (puedeComprar(p, s)) lista.add(s);
        }
        return lista;
    }
}
