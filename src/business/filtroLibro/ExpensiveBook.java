package business.filtroLibro;

import business.entity.Libro;

public class ExpensiveBook implements BookFilter{
    @Override
    public boolean test(Libro libro) {
        return !(libro.isUnexpensive()) ;
    }
}
