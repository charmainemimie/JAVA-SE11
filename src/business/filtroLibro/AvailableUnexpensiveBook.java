package business.filtroLibro;

import business.entity.Libro;

public class AvailableUnexpensiveBook implements BookFilter {
    @Override
    public boolean test(Libro libro) {
        return (libro.isAvailable() && libro.isUnexpensive());
    }
}
