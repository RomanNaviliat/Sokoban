# Sokoban 



## Requisitos

- JDK 8 o superior (probado compilando con JDK 25 y `--release 8`).

## Cómo ejecutar

### Desde un IDE (IntelliJ / Eclipse / VS Code)
1. Abrir la carpeta del proyecto.
2. Marcar `src` como *Sources Root* (los niveles `src/niveles/*.txt` se copian al classpath automáticamente).
3. Ejecutar la clase `sokoban.Main`.

### Desde la consola
Windows:
```bat
run.bat
```
Linux / macOS:
```bash
./run.sh
```
O a mano:
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp "out:src" sokoban.Main        # en Windows: -cp "out;src"
```

## Controles

| Tecla / botón | Acción |
|---|---|
| Flechas | Mover al sokoban |
| `Z` / `U` o botón **Deshacer** | Retrocede 5 movimientos (máx. 3 veces seguidas, historial de 15) |
| `R` o botón **Reiniciar** | Reinicia el nivel |
| `Esc` o botón **Menú** | Vuelve al menú |
| **Guardar** / **Cargar partida** | Guarda perfil + partida en `~/.sokoban-uade/partida.sav` |

## Elementos del juego

| Carácter | Elemento | Regla |
|---|---|---|
| `#` | Pared | Bloquea el paso |
| ` ` | Suelo | — |
| `.` | Casillero destino | Todas deben quedar ocupadas para ganar |
| `$` | Caja | Normal; las primeras *N* se vuelven **frágiles** según la dificultad |
| `*` | Caja sobre destino | |
| `@` / `+` | Jugador / jugador sobre destino | |
| `K` | Caja llave | Sobre un cerrojo abre los muros `D` |
| `L` | Casillero cerrojo | |
| `D` | Muro (puerta) | Se abre cuando todos los cerrojos tienen llave |
| `~` | Suelo resbaladizo | La caja y el sokoban se deslizan hasta chocar o salir del hielo (el sokoban no empuja cajas mientras se desliza) |
| `B` | Botón | Al presionarlo (jugador o caja) regenera los pisos que caen |
| `F` | Piso que cae | Tras 2 pasadas del jugador se cae y no se puede pisar |
| `T` | Caja timba | No se mueve; al chocarla ofrece una apuesta |



## Estructura

```
src/
├── niveles/                 nivel1.txt … nivel6.txt
└── sokoban/
    ├── Main.java
    ├── controlador/         SokobanController
    ├── vista/               VentanaPrincipal, PanelMenu, PanelJuego, PanelHud, PanelTablero, diálogos
    ├── sonido/              GestorDeSonido
    └── modelo/
        ├── Juego, Partida, Tablero, MapaNivel, EstadoTablero, Perfil, Tienda, Jugador, ...
        ├── cajas/  suelos/  paredes/  creadores/
        ├── dificultad/  puntaje/  skins/  timba/  guardado/
```
