package sokoban.modelo.guardado;

import sokoban.modelo.Partida;
import sokoban.modelo.Perfil;
import sokoban.modelo.dificultad.EstrategiaDificultad;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class PartidaGuardada implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Perfil perfil;
    private final EstrategiaDificultad dificultad;
    private final Partida partida;
    private final LocalDateTime fecha;

    public PartidaGuardada(Perfil perfil, EstrategiaDificultad dificultad, Partida partida, LocalDateTime fecha) {
        this.perfil = perfil;
        this.dificultad = dificultad;
        this.partida = partida;
        this.fecha = fecha;
    }

    public Perfil obtenerPerfil() { return perfil; }
    public EstrategiaDificultad obtenerDificultad() { return dificultad; }
    public Partida obtenerPartida() { return partida; }
    public LocalDateTime obtenerFecha() { return fecha; }

    public String obtenerDescripcion() {
        String f = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        if (partida == null) return "Perfil guardado el " + f;
        return dificultad.obtenerNombre() + " - nivel " + partida.obtenerNivelActual() + " (" + f + ")";
    }
}
