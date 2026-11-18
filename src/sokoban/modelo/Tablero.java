package sokoban.modelo;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.paredes.Pared;
import sokoban.modelo.skins.SkinInterface;
import sokoban.modelo.suelos.Suelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int ancho;
    private final int alto;

    private final Map<Posicion, Suelo> suelos = new LinkedHashMap<>();
    private final Map<Posicion, Pared> paredes = new LinkedHashMap<>();
    private final Map<Posicion, Caja> cajas = new LinkedHashMap<>();
    private SkinInterface jugador;
    private Posicion posicionJugador;
    private final MecanismosDelNivel mecanismos;

    public Tablero(int ancho, int alto, MecanismosDelNivel mecanismos) {
        this.ancho = ancho;
        this.alto = alto;
        this.mecanismos = mecanismos;
    }

    public void colocarSuelo(Posicion p, Suelo s) {
        suelos.put(p, s);
    }

    public void colocarPared(Posicion p, Pared pa) {
        paredes.put(p, pa);
    }

    public void colocarCaja(Posicion p, Caja c) {
        cajas.put(p, c);
        Suelo s = suelos.get(p);
        if (s != null) s.alRecibirCaja(c);
    }

    public void colocarJugador(Posicion p, SkinInterface j) {
        this.posicionJugador = p;
        this.jugador = j;
        Suelo s = suelos.get(p);
        if (s != null) s.alEntrarJugador();
    }

    private boolean esTransitable(Posicion p) {
        Suelo s = suelos.get(p);
        if (s == null || !s.esTransitable()) return false;
        Pared pa = paredes.get(p);
        return pa == null || pa.esTransitable();
    }

    private boolean estaLibreParaCaja(Posicion p) {
        return esTransitable(p) && !cajas.containsKey(p) && !p.equals(posicionJugador);
    }

    public ResultadoMovimiento moverJugador(Direccion d) {
        ResultadoMovimiento r = new ResultadoMovimiento();
        Posicion destino = posicionJugador.desplazar(d);
        if (!esTransitable(destino)) return r;

        int puertasAntes = mecanismos.contarPuertasAbiertas();
        int caidosAntes = mecanismos.contarPisosCaidos();

        List<Posicion> cadena = new ArrayList<>();
        Posicion actual = destino;
        while (cajas.containsKey(actual)) {
            Caja c = cajas.get(actual);
            if (!c.esEmpujable()) {
                if (cadena.isEmpty()) c.interactuar(r);
                return r;
            }
            cadena.add(actual);
            if (cadena.size() > jugador.obtenerMaxCajasEmpujables()) return r;
            actual = actual.desplazar(d);
        }
        if (!cadena.isEmpty() && !esTransitable(actual)) return r;

        for (int i = cadena.size() - 1; i >= 0; i--) {
            moverCaja(cadena.get(i), d, r);
        }

        suelos.get(posicionJugador).alSalirJugador();
        posicionJugador = destino;
        suelos.get(posicionJugador).alEntrarJugador();
        r.marcarMovido(cadena.size());
        deslizarJugador(d);

        if (mecanismos.contarPuertasAbiertas() != puertasAntes) r.marcarPuertaCambio();
        int caidosDespues = mecanismos.contarPisosCaidos();
        if (caidosDespues > caidosAntes) r.marcarPisoCayo();
        if (caidosDespues < caidosAntes) r.marcarPisoRegenerado();
        return r;
    }

    private void deslizarJugador(Direccion d) {
        while (mecanismos.esDeslizante(posicionJugador)) {
            Posicion siguiente = posicionJugador.desplazar(d);
            if (!esTransitable(siguiente) || cajas.containsKey(siguiente)) return;
            suelos.get(posicionJugador).alSalirJugador();
            posicionJugador = siguiente;
            suelos.get(posicionJugador).alEntrarJugador();
        }
    }

    private void moverCaja(Posicion origen, Direccion d, ResultadoMovimiento r) {
        Caja caja = cajas.get(origen);
        caja.empujar();
        Posicion pos = trasladar(caja, origen, origen.desplazar(d));
        while (mecanismos.esDeslizante(pos) && estaLibreParaCaja(pos.desplazar(d))) {
            pos = trasladar(caja, pos, pos.desplazar(d));
        }
        if (caja.estaRota()) {
            cajas.remove(pos);
            suelos.get(pos).alRetirarCaja(caja);
            r.marcarCajaRota();
        }
    }

    private Posicion trasladar(Caja caja, Posicion desde, Posicion hasta) {
        cajas.remove(desde);
        suelos.get(desde).alRetirarCaja(caja);
        cajas.put(hasta, caja);
        suelos.get(hasta).alRecibirCaja(caja);
        return hasta;
    }

    public void retirarCaja(Caja caja) {
        Iterator<Map.Entry<Posicion, Caja>> it = cajas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Posicion, Caja> e = it.next();
            if (e.getValue() == caja) {
                it.remove();
                suelos.get(e.getKey()).alRetirarCaja(caja);
                return;
            }
        }
    }

    public boolean todosLosObjetivosCumplidos() { return mecanismos.todosLosObjetivosCumplidos(); }
    public int contarObjetivosCumplidos() { return mecanismos.contarObjetivosCumplidos(); }
    public int contarObjetivos() { return mecanismos.contarObjetivos(); }

    public EstadoTablero guardarEstado() {
        Map<Posicion, Caja> posiciones = new HashMap<>();
        Map<Caja, Integer> estadosCajas = new HashMap<>();
        for (Map.Entry<Posicion, Caja> e : cajas.entrySet()) {
            Caja c = e.getValue();
            if (c.esEmpujable()) {
                posiciones.put(e.getKey(), c);
                estadosCajas.put(c, c.obtenerEstadoInterno());
            }
        }
        return new EstadoTablero(posicionJugador, posiciones, estadosCajas, mecanismos.guardarEstados());
    }

    public void restaurarEstado(EstadoTablero e) {
        cajas.values().removeIf(Caja::esEmpujable);
        cajas.putAll(e.obtenerPosicionesCajas());
        for (Map.Entry<Caja, Integer> c : e.obtenerEstadosCajas().entrySet()) {
            c.getKey().restaurarEstadoInterno(c.getValue());
        }
        mecanismos.restaurarEstados(e.obtenerEstadosSuelos());
        posicionJugador = e.obtenerPosicionJugador();
    }

    public int obtenerAncho() { return ancho; }
    public int obtenerAlto() { return alto; }
    public Suelo obtenerSuelo(Posicion p) { return suelos.get(p); }
    public Pared obtenerPared(Posicion p) { return paredes.get(p); }
    public Caja obtenerCaja(Posicion p) { return cajas.get(p); }
    public Posicion obtenerPosicionJugador() { return posicionJugador; }
    public SkinInterface obtenerJugador() { return jugador; }
}
