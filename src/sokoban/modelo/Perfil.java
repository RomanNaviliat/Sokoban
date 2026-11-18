package sokoban.modelo;

import sokoban.modelo.skins.TipoSkin;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;

    private int puntajeAcumulado;
    private final Set<TipoSkin> skinsDesbloqueadas = EnumSet.noneOf(TipoSkin.class);
    private final Set<TipoSkin> skinsEquipadas = EnumSet.noneOf(TipoSkin.class);
    private final Map<String, Integer> mejoresPuntajes = new HashMap<>();

    public int obtenerPuntajeAcumulado() { return puntajeAcumulado; }

    public void agregarPuntaje(int n) {
        if (n > 0) puntajeAcumulado += n;
    }

    public boolean gastarPuntaje(int n) {
        if (n <= 0 || n > puntajeAcumulado) return false;
        puntajeAcumulado -= n;
        return true;
    }

    public boolean tieneSkin(TipoSkin s) { return skinsDesbloqueadas.contains(s); }

    void desbloquearSkin(TipoSkin s) { skinsDesbloqueadas.add(s); }

    public boolean equiparSkin(TipoSkin s, boolean equipar) {
        if (!tieneSkin(s)) return false;
        if (equipar) skinsEquipadas.add(s); else skinsEquipadas.remove(s);
        return true;
    }

    public boolean estaEquipada(TipoSkin s) { return skinsEquipadas.contains(s); }

    public Set<TipoSkin> obtenerSkinsEquipadas() {
        return Collections.unmodifiableSet(skinsEquipadas);
    }

    public void registrarNivelCompletado(String dificultad, int nivel, int puntaje, int recompensa) {
        agregarPuntaje(recompensa);
        String clave = clave(dificultad, nivel);
        Integer anterior = mejoresPuntajes.get(clave);
        if (anterior == null || puntaje > anterior) {
            mejoresPuntajes.put(clave, puntaje);
        }
    }

    public boolean completoNivel(String dificultad, int nivel) {
        return mejoresPuntajes.containsKey(clave(dificultad, nivel));
    }

    public Integer obtenerMejorPuntaje(String dificultad, int nivel) {
        return mejoresPuntajes.get(clave(dificultad, nivel));
    }

    private static String clave(String dificultad, int nivel) {
        return dificultad + ":" + nivel;
    }
}
