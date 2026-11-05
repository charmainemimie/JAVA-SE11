package persistence;

import business.entity.Libro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DAOLibroList implements DAOLibro {
    private final List<Libro> libros;

    public DAOLibroList() {
        this.libros = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void add(Libro libro) {
        synchronized (libros){
            libros.add(libro);
        }
    }

    @Override
    public List<Libro> getAllBooks() {
        synchronized (libros){
            return new ArrayList<>(libros);
        }
    }

    @Override
    public Libro findByISBN(String isbn) {
        return libros.stream()
                .filter(libro -> libro.hasISBN(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void disconnect() {
    }
}

