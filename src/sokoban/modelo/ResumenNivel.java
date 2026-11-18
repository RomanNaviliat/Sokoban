package sokoban.modelo;

import sokoban.modelo.skins.TipoSkin;

import java.util.Collections;
import java.util.List;

public final class ResumenNivel {
    private final String dificultad;
    private final int nivel;
    private final int movimientos;
    private final int empujes;
    private final int usosUndo;
    private final int resultadoApuestas;
    private final int puntaje;
    private final int recompensa;
    private final String criterioPuntaje;
    private final List<TipoSkin> skinsComprables;
    private final boolean haySiguienteNivel;

    public ResumenNivel(String dificultad, int nivel, int movimientos, int empujes, int usosUndo,
                        int resultadoApuestas, int puntaje, int recompensa, String criterioPuntaje,
                        List<TipoSkin> skinsComprables, boolean haySiguienteNivel) {
        this.dificultad = dificultad;
        this.nivel = nivel;
        this.movimientos = movimientos;
        this.empujes = empujes;
        this.usosUndo = usosUndo;
        this.resultadoApuestas = resultadoApuestas;
        this.puntaje = puntaje;
        this.recompensa = recompensa;
        this.criterioPuntaje = criterioPuntaje;
        this.skinsComprables = Collections.unmodifiableList(skinsComprables);
        this.haySiguienteNivel = haySiguienteNivel;
    }

    public String obtenerDificultad() { return dificultad; }
    public int obtenerNivel() { return nivel; }
    public int obtenerMovimientos() { return movimientos; }
    public int obtenerEmpujes() { return empujes; }
    public int obtenerUsosUndo() { return usosUndo; }
    public int obtenerResultadoApuestas() { return resultadoApuestas; }
    public int obtenerPuntaje() { return puntaje; }
    public int obtenerRecompensa() { return recompensa; }
    public String obtenerCriterioPuntaje() { return criterioPuntaje; }
    public List<TipoSkin> obtenerSkinsComprables() { return skinsComprables; }
    public boolean haySiguienteNivel() { return haySiguienteNivel; }
}
