package sokoban.modelo;

import sokoban.modelo.cajas.Caja;
import sokoban.modelo.creadores.CajaCreator;
import sokoban.modelo.creadores.CajaFragilCreator;
import sokoban.modelo.creadores.CajaLlaveCreator;
import sokoban.modelo.creadores.CajaNormalCreator;
import sokoban.modelo.creadores.CajaTimbaCreator;
import sokoban.modelo.creadores.CasilleroDestinoCreator;
import sokoban.modelo.creadores.PisoQueCaeCreator;
import sokoban.modelo.creadores.SueloBotonCreator;
import sokoban.modelo.creadores.SueloCerrojoCreator;
import sokoban.modelo.creadores.SueloCreator;
import sokoban.modelo.creadores.SueloNormalCreator;
import sokoban.modelo.creadores.SueloResbaladizoCreator;
import sokoban.modelo.paredes.ParedNormal;
import sokoban.modelo.paredes.ParedPuerta;
import sokoban.modelo.skins.SkinInterface;
import sokoban.modelo.timba.CajaTimbaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapaNivel {
    private static final char CLAVE_DESTINO = '.';
    private static final char CLAVE_NORMAL = ' ';

    private final int numero;
    private final String rutaArchivo;
    private final Map<Character, SueloCreator> creadoresDeSuelo = new HashMap<>();
    private final Map<Character, CajaCreator> creadoresDeCaja = new HashMap<>();
    private final CajaCreator creadorFragil;
    private final int cantidadFragiles;

    public MapaNivel(int numero, String rutaArchivo, int cantidadFragiles, int resistenciaFragil,
                     CajaTimbaService servicioTimba) {
        this.numero = numero;
        this.rutaArchivo = rutaArchivo;
        this.cantidadFragiles = cantidadFragiles;
        this.creadorFragil = new CajaFragilCreator(resistenciaFragil);

        creadoresDeSuelo.put(CLAVE_NORMAL, new SueloNormalCreator());
        creadoresDeSuelo.put(CLAVE_DESTINO, new CasilleroDestinoCreator());
        creadoresDeSuelo.put('~', new SueloResbaladizoCreator());
        creadoresDeSuelo.put('B', new SueloBotonCreator());
        creadoresDeSuelo.put('L', new SueloCerrojoCreator());
        creadoresDeSuelo.put('F', new PisoQueCaeCreator());

        creadoresDeCaja.put('$', new CajaNormalCreator());
        creadoresDeCaja.put('K', new CajaLlaveCreator());
        creadoresDeCaja.put('T', new CajaTimbaCreator(servicioTimba));
    }

    public int obtenerNumero() { return numero; }

    public Tablero construirTablero(SkinInterface jugador) {
        List<String> filas = leerFilas();
        int alto = filas.size();
        int ancho = 0;
        Posicion inicio = null;
        for (int y = 0; y < alto; y++) {
            ancho = Math.max(ancho, filas.get(y).length());
            int x = filas.get(y).indexOf('@');
            if (x < 0) x = filas.get(y).indexOf('+');
            if (x >= 0) {
                if (inicio != null) throw new NivelInvalidoException("Hay mas de un jugador en " + rutaArchivo);
                inicio = new Posicion(x, y);
            }
        }
        if (inicio == null) throw new NivelInvalidoException("El nivel no tiene jugador: " + rutaArchivo);

        Set<Posicion> interior = calcularInterior(filas, inicio);
        MecanismosDelNivel mecanismos = new MecanismosDelNivel();
        Tablero tablero = new Tablero(ancho, alto, mecanismos);

        for (int y = 0; y < alto; y++) {
            String fila = filas.get(y);
            for (int x = 0; x < fila.length(); x++) {
                char ch = fila.charAt(x);
                Posicion p = new Posicion(x, y);
                if (ch == '#') {
                    tablero.colocarPared(p, new ParedNormal());
                } else if (interior.contains(p)) {
                    tablero.colocarSuelo(p, creadorDeSueloPara(ch).crearSuelo(p, mecanismos));
                    if (ch == 'D') {
                        ParedPuerta puerta = new ParedPuerta();
                        mecanismos.agregarPuerta(puerta);
                        tablero.colocarPared(p, puerta);
                    }
                }
            }
        }
        mecanismos.conectar();

        int fragilesAsignadas = 0;
        for (int y = 0; y < alto; y++) {
            String fila = filas.get(y);
            for (int x = 0; x < fila.length(); x++) {
                char ch = fila.charAt(x);
                Posicion p = new Posicion(x, y);
                if (!interior.contains(p)) continue;
                CajaCreator creador = creadorDeCajaPara(ch);
                if (creador == null) continue;
                if (ch == '$' && fragilesAsignadas < cantidadFragiles) {
                    creador = creadorFragil;
                    fragilesAsignadas++;
                }
                Caja caja = creador.crearCaja();
                tablero.colocarCaja(p, caja);
            }
        }
        tablero.colocarJugador(inicio, jugador);
        return tablero;
    }

    private SueloCreator creadorDeSueloPara(char ch) {
        switch (ch) {
            case '.': case '*': case '+':
                return creadoresDeSuelo.get(CLAVE_DESTINO);
            default:
                SueloCreator c = creadoresDeSuelo.get(ch);
                return c != null ? c : creadoresDeSuelo.get(CLAVE_NORMAL);
        }
    }

    private CajaCreator creadorDeCajaPara(char ch) {
        if (ch == '*') return creadoresDeCaja.get('$');
        return creadoresDeCaja.get(ch);
    }

    private Set<Posicion> calcularInterior(List<String> filas, Posicion inicio) {
        Set<Posicion> visitadas = new HashSet<>();
        Deque<Posicion> pendientes = new ArrayDeque<>();
        pendientes.push(inicio);
        while (!pendientes.isEmpty()) {
            Posicion p = pendientes.pop();
            if (visitadas.contains(p)) continue;
            int x = p.obtenerX();
            int y = p.obtenerY();
            if (y < 0 || y >= filas.size() || x < 0 || x >= filas.get(y).length()) continue;
            if (filas.get(y).charAt(x) == '#') continue;
            visitadas.add(p);
            for (Direccion d : Direccion.values()) pendientes.push(p.desplazar(d));
        }
        return visitadas;
    }

    private List<String> leerFilas() {
        List<String> filas = new ArrayList<>();
        try (BufferedReader br = abrir()) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith(";")) continue;
                filas.add(linea.replace("\t", "    "));
            }
        } catch (IOException e) {
            throw new NivelInvalidoException("No se pudo leer el nivel " + rutaArchivo, e);
        }
        while (!filas.isEmpty() && filas.get(filas.size() - 1).trim().isEmpty()) {
            filas.remove(filas.size() - 1);
        }
        if (filas.isEmpty()) throw new NivelInvalidoException("Nivel vacio: " + rutaArchivo);
        return filas;
    }

    private BufferedReader abrir() throws IOException {
        InputStream in = MapaNivel.class.getResourceAsStream(rutaArchivo);
        if (in != null) return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        Path p = Paths.get("src" + rutaArchivo);
        if (!Files.exists(p)) throw new NivelInvalidoException("No existe el nivel " + rutaArchivo);
        return Files.newBufferedReader(p, StandardCharsets.UTF_8);
    }
}
