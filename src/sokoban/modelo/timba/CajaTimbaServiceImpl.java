package sokoban.modelo.timba;

import sokoban.modelo.cajas.CajaTimba;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CajaTimbaServiceImpl implements CajaTimbaService {
    private static final long serialVersionUID = 1L;

    private final Random random;

    public CajaTimbaServiceImpl() {
        this(new Random());
    }

    public CajaTimbaServiceImpl(Random random) {
        this.random = random;
    }

    @Override
    public boolean validarApuesta(CajaTimba caja, Apuesta a) {
        return a != null && a.obtenerColor() != null
                && a.obtenerMonto() >= caja.obtenerApuestaMinima()
                && a.obtenerMonto() <= caja.obtenerApuestaMaxima();
    }

    @Override
    public ResultadoApuesta procesarApuesta(CajaTimba caja, Apuesta a, double probabilidadVictoria) {
        boolean gano = random.nextDouble() < probabilidadVictoria;
        ColorApuesta salido = gano ? a.obtenerColor() : otroColor(a.obtenerColor());
        int premio = gano ? calcularPremio(caja, a) : 0;
        return new ResultadoApuesta(a, gano, salido, premio);
    }

    @Override
    public int calcularPremio(CajaTimba caja, Apuesta a) {
        return a.obtenerMonto() * a.obtenerColor().obtenerMultiplicadorPremio();
    }

    private ColorApuesta otroColor(ColorApuesta elegido) {
        List<ColorApuesta> otros = new ArrayList<>();
        for (ColorApuesta c : ColorApuesta.values()) {
            if (c != elegido) otros.add(c);
        }
        return otros.get(random.nextInt(otros.size()));
    }
}
