package persistence;

import business.entity.Libro;

import java.util.List;

public interface DAOLibro {
    void add(Libro libro);
    List<Libro> getAllBooks();

    Libro findByISBN(String isbn);

    void disconnect();
    //public List<Libro> recuperarTodosLosLibros();
}
