package persistence;

import business.entity.Libro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class DAOLibroCSVaJSON implements DAOLibro{

    private String CSV_FILE_NAME = "files/libros.csv";
    private static final String JSON_FILE_NAME = "files/libro.json";
    private final Gson gson;
    private List<Libro> libros;

    public DAOLibroCSVaJSON() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.libros = importarInventario();
    }

    private List<Libro> importarInventario(){
        return getAllBooks();
    }

    /**
     * Rutina interna de exportación que guarda los cambios usando java.io (BufferedWriter).
     */
    private void guardarInventario() {
        String jsonOutput = gson.toJson(libros);

        // Asegura el cierre correcto del flujo utilizando try-with-resources (Ejercicio 4)
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(JSON_FILE_NAME))) {
            writer.write(jsonOutput);
        } catch (IOException e) {
            System.err.println("Error al exportar/guardar el inventario: " + e.getMessage());
        }
    }

    @Override
    public void add(Libro libro) {
        if (libro != null) {
            this.libros.add(libro);
            guardarInventario(); // Persiste automáticamente tras agregar
        }
    }

    @Override
    public List<Libro> getAllBooks() {
        List<Libro> libros = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Path.of(CSV_FILE_NAME));

            for (String line : lines) {
                libros.add(Libro.fromLine(line));
            }
        } catch (IOException e) {
            System.err.println("No puedo abrir el fichero libros.csv");
            exit(0);
        }
        return libros;
    }

    @Override
    public Libro findByISBN(String isbn) {
        return null;
    }

    @Override
    public void disconnect() {
        // Asegura el guardado final antes de cerrar la conexión o la aplicación
        guardarInventario();
        System.out.println("Desconexión completada. Datos guardados en " + JSON_FILE_NAME);
    }
}
