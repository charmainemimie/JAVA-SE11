package business.filtroLibro;

import business.entity.Libro;

public class AvailableBook implements BookFilter {
    @Override
    public boolean test(Libro libro) {
        return libro.isAvailable();
    }
}
