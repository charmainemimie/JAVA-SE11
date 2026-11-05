package business.filtroLibro;

import business.entity.Libro;

public interface BookFilter {
    boolean test(Libro libro);
}
