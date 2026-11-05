package persistence;

import business.entity.Libro;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 *
 */
public class DAOLibroCSV implements DAOLibro {
    private String FILE_NAME = "files/libros.csv";
    // Usamos la Clase FileWriter para añadir líneas al fichero csv.
    // En el método público add se añade un Libro y para ello usamos FileWriter
    private FileWriter fileWriter;

    /**
     *
     */
    public DAOLibroCSV() {
        try {
            // Generamos un objeto de tipo FileWriter indicándole la ruta al fichero y que se abre en modo escritura.
            fileWriter = new FileWriter(FILE_NAME, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param libro
     */
    @Override
    public void add(Libro libro) {
        try {
            // Añadimos un final de línea, el método lineSeparator de System lo genera para el sistema operativo que corresponda
            // Esta rutina la tenemos que mejorar porque si el fichero estuviese vación dejaría la primer línea en blanco
            // De modo que tenemos que verificar si el fichero está vacío, si lo está no añadimos lineSeparator y sino sí lo añadimos
            fileWriter.write(System.lineSeparator());
            fileWriter.write(libro.toCSV());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<Libro> getAllBooks() {
        List<Libro> libros = new ArrayList<>();

        try {
            // Usamos la clase Files y en particular su método estático readAllLines que dado un camino a un fichero lee todas sus líneas y las devuelve en una colección de líneas/String.
            // También usamos la clase Path y en particular su método estático of al que le pasamos el camino relativo al fichero y genera el camino absoluto
            List<String> lines = Files.readAllLines(Path.of(FILE_NAME));

            for (String line : lines) {
                libros.add(Libro.fromLine(line));
            }
        } catch (IOException e) {
            System.err.println("No puedo abrir el fichero libros.csv");
            exit(0);
        }
        return libros;
    }

    /**
     *
     * @param isbn
     * @return
     */
    @Override
    public Libro findByISBN(String isbn) {
        return null;
    }

    /**
     *
     */
    @Override
    public void disconnect() {
        try{
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
