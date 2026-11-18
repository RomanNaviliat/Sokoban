package sokoban.modelo;

import sokoban.modelo.paredes.ParedPuerta;
import sokoban.modelo.suelos.ConEstadoInterno;
import sokoban.modelo.suelos.Objetivo;
import sokoban.modelo.suelos.PisoQueCae;
import sokoban.modelo.suelos.SueloBoton;
import sokoban.modelo.suelos.SueloCerrojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MecanismosDelNivel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Objetivo> objetivos = new ArrayList<>();
    private final Set<Posicion> deslizantes = new HashSet<>();
    private final Map<Posicion, ConEstadoInterno> suelosConEstado = new HashMap<>();
    private final List<SueloBoton> botones = new ArrayList<>();
    private final List<PisoQueCae> pisosQueCaen = new ArrayList<>();
    private final List<SueloCerrojo> cerrojos = new ArrayList<>();
    private final List<ParedPuerta> puertas = new ArrayList<>();

    public void agregarObjetivo(Objetivo o) { objetivos.add(o); }
    public void agregarDeslizante(Posicion p) { deslizantes.add(p); }
    public void agregarConEstado(Posicion p, ConEstadoInterno s) { suelosConEstado.put(p, s); }
    public void agregarBoton(SueloBoton b) { botones.add(b); }
    public void agregarPisoQueCae(PisoQueCae piso) { pisosQueCaen.add(piso); }
    public void agregarCerrojo(SueloCerrojo c) { cerrojos.add(c); }
    public void agregarPuerta(ParedPuerta puerta) { puertas.add(puerta); }

    public void conectar() {
        for (SueloBoton b : botones) {
            for (PisoQueCae piso : pisosQueCaen) b.suscribir(piso);
        }
        for (SueloCerrojo c : cerrojos) {
            for (ParedPuerta puerta : puertas) c.suscribirPuerta(puerta);
        }
    }

    public boolean esDeslizante(Posicion p) { return deslizantes.contains(p); }

    public boolean todosLosObjetivosCumplidos() {
        if (objetivos.isEmpty()) return false;
        for (Objetivo o : objetivos) {
            if (!o.cumpleObjetivo()) return false;
        }
        return true;
    }

    public int contarObjetivosCumplidos() {
        int n = 0;
        for (Objetivo o : objetivos) if (o.cumpleObjetivo()) n++;
        return n;
    }

    public int contarObjetivos() { return objetivos.size(); }

    public int contarPuertasAbiertas() {
        int n = 0;
        for (ParedPuerta p : puertas) if (p.estaAbierta()) n++;
        return n;
    }

    public int contarPisosCaidos() {
        int n = 0;
        for (PisoQueCae p : pisosQueCaen) if (p.estaCaido()) n++;
        return n;
    }

    public Map<Posicion, Integer> guardarEstados() {
        Map<Posicion, Integer> estados = new HashMap<>();
        for (Map.Entry<Posicion, ConEstadoInterno> e : suelosConEstado.entrySet()) {
            estados.put(e.getKey(), e.getValue().obtenerEstadoInterno());
        }
        return estados;
    }

    public void restaurarEstados(Map<Posicion, Integer> estados) {
        for (Map.Entry<Posicion, Integer> s : estados.entrySet()) {
            ConEstadoInterno suelo = suelosConEstado.get(s.getKey());
            if (suelo != null) suelo.restaurarEstadoInterno(s.getValue());
        }
    }
}
