package persistence;

import business.entity.Libro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de DAOLibro basada en java.io y Gson para la persistencia
 * del inventario en un archivo de texto plano (libro.json).
 */
public class DAOLibroJson implements DAOLibro {
    private static final String JSON_FILE_NAME = "files/libro.json";
    private final Gson gson;
    private List<Libro> libros;

    public DAOLibroJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.libros = importarInventario();
    }

    /**
     * Rutina interna de importación que lee el archivo de texto usando java.io (BufferedReader).
     */
    private List<Libro> importarInventario() {
        Path path = Path.of(JSON_FILE_NAME);
        if (!Files.exists(path)) {
            System.out.println("El archivo " + JSON_FILE_NAME +  " no existe. Se iniciará con un inventario vacío.");
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Libro>>() {}.getType();

        // Asegura el cierre correcto del flujo utilizando try-with-resources (Ejercicio 4)
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<Libro> librosRestaurados = gson.fromJson(reader, listType);
            if (librosRestaurados != null) {
                System.out.println("Inventario importado exitosamente desde " + JSON_FILE_NAME);
                return librosRestaurados;
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de inventario: " + e.getMessage());
        }

        return new ArrayList<>();
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
        return new ArrayList<>(libros); // Devuelve una copia defensiva
    }

    @Override
    public Libro findByISBN(String isbn) {
        if (isbn == null) return null;
        for (Libro libro : libros) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    @Override
    public void disconnect() {
        // Asegura el guardado final antes de cerrar la conexión o la aplicación
        guardarInventario();
        System.out.println("Desconexión completada. Datos guardados en " + JSON_FILE_NAME);
    }
}
