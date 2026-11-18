package sokoban.modelo;

import sokoban.modelo.cajas.CajaTimba;

public class ResultadoMovimiento {
    private boolean seMovio;
    private int cajasEmpujadas;
    private boolean cajaRota;
    private boolean puertaCambio;
    private boolean pisoCayo;
    private boolean pisoRegenerado;
    private CajaTimba timbaEncontrada;

    public static ResultadoMovimiento bloqueado() {
        return new ResultadoMovimiento();
    }

    public boolean seMovio() { return seMovio; }
    public int obtenerCajasEmpujadas() { return cajasEmpujadas; }
    public boolean huboEmpuje() { return cajasEmpujadas > 0; }
    public boolean seRompioCaja() { return cajaRota; }
    public boolean cambioPuerta() { return puertaCambio; }
    public boolean cayoPiso() { return pisoCayo; }
    public boolean seRegeneroPiso() { return pisoRegenerado; }
    public CajaTimba obtenerTimba() { return timbaEncontrada; }
    public boolean encontroTimba() { return timbaEncontrada != null; }

    void marcarMovido(int cajas) { this.seMovio = true; this.cajasEmpujadas = cajas; }
    void marcarCajaRota() { this.cajaRota = true; }
    void marcarPuertaCambio() { this.puertaCambio = true; }
    void marcarPisoCayo() { this.pisoCayo = true; }
    void marcarPisoRegenerado() { this.pisoRegenerado = true; }
    public void marcarTimba(CajaTimba t) { this.timbaEncontrada = t; }
}
