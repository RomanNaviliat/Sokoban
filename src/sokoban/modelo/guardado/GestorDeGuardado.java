package sokoban.modelo.guardado;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorDeGuardado {
    private final Path archivo;

    public GestorDeGuardado() {
        this(Paths.get(System.getProperty("user.home"), ".sokoban-uade", "partida.sav"));
    }

    public GestorDeGuardado(Path archivo) {
        this.archivo = archivo;
    }

    public void guardar(PartidaGuardada g) throws IOException {
        if (archivo.getParent() != null) Files.createDirectories(archivo.getParent());
        try (OutputStream os = Files.newOutputStream(archivo);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(g);
        }
    }

    public PartidaGuardada cargar() throws IOException {
        try (InputStream is = Files.newInputStream(archivo);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return (PartidaGuardada) ois.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("El archivo de guardado no es valido", e);
        }
    }

    public boolean existeGuardado() {
        return Files.exists(archivo);
    }
}
