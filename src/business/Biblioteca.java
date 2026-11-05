package business;

import business.entity.Libro;
import business.filtroLibro.BookFilter;
import business.filtroLibro.ExpensiveBook;
import persistence.DAOLibro;
import persistence.DAOLibroCSV;
import persistence.DAOLibroJDBC;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Biblioteca es como llamamos al Manager de Libro - como su nombre lo indica gestiona una "colección de libros"
 *
 * @author Alejandro Bremermann
 * @version 0.4
 */
public class Biblioteca {
    private final DAOLibro daoLibro;

    /**
     * Constructor de Biblioteca, asigna una implementación de DAOLibro para gestionar la biblioteca
     * -- se podría hacer preguntando por menú/sub-menú al usuario qué fuente de datos quiere usar y
     * -- recibiéndo la opción como parámetro para luego asignar en forma dinámica.
     */
    public Biblioteca() {
        //daoLibro = new DAOLibroList();
        //daoLibro = new DAOLibroCSVaJSON();
        daoLibro = new DAOLibroCSV();
        //daoLibro = new DAOLibroJDBC();
    }

    /**
     * Añade un Libro a la biblioteca
     *
     * @param libro - el libro a ser añadido
     */
    public void agregarLibro(Libro libro) {
        Objects.requireNonNull(libro, "No se puede añadir un libro null");
        daoLibro.add(libro);
    }


    /**
     * Obtiene una copia de la Lista con todos los libros de la Biblioteca
     *
     * @return List<Libro> la colección de Libros
     */
    public List<Libro> obtenerLibros() {
        return new ArrayList<>(daoLibro.getAllBooks());
    }

    public Libro buscarLibroPorISBN(String isbn) {
        return daoLibro.findByISBN(isbn);
    }

    public List<Libro> buscarLibros(String tituloOAutorABuscar) {
        List<Libro> libros = daoLibro.getAllBooks();
        List<Libro> librosEncontrados = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.matches(tituloOAutorABuscar)) {
                librosEncontrados.add(libro);
            }
        }
        return librosEncontrados;
    }


    public List<Libro> obtenerLibrosConStockMayorA(int stockMinimo) {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .filter(libro -> libro.getStock() > stockMinimo)
                .filter(libro -> libro.getStock() > stockMinimo)
                .collect(Collectors.toList());
    }

    public List<Libro> obtenerLibrosOrdenadosPorPrecio() {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .sorted((libro1, libro2) -> Double.compare(libro1.getPrecio(), libro2.getPrecio()))
                .collect(Collectors.toList());
    }

    public List<String> obtenerTitulosSinRepetir() {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .map(Libro::getTitulo)
                .distinct()
                .collect(Collectors.toList());
    }

    public Set<String> obtenerTitulosSinRepetirSet() {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.toSet());
    }

    public Map<String, Libro> mapearLibrosPorISBN() {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .collect(Collectors.toMap(
                        Libro::getIsbn,
                        libro -> libro
                ));
    }

    public Map<String, List<Libro>> agruparLibrosPorAutor() {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .collect(Collectors.groupingBy(Libro::getAutor));
    }

    public Map<Boolean, List<Libro>> particionarLibrosPorStock(int stockLimite) {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .collect(Collectors.partitioningBy(libro -> libro.getStock() >= stockLimite));
    }

    // Búsquedas aplicando API BookFilter secuencial
    public List<Libro> buscarLibros(BookFilter filtro) {
        List<Libro> libros = daoLibro.getAllBooks();
        List<Libro> librosEncontrados = new ArrayList<>();
        for (Libro libro : libros) {
            if (filtro.test(libro)) {
                librosEncontrados.add(libro);
            }
        }
        return librosEncontrados;
    }

    public List<Libro> buscarLibrosCostosos() {
        return buscarLibros(new ExpensiveBook());
    }


    // Búsquedas aplicando API BookFilter en Paralelo
    public List<Libro> buscarLibrosEnParalelo(BookFilter filtro) {
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.parallelStream()
                .filter(filtro::test)
                .collect(Collectors.toList());
    }
    public List<Libro> buscarLibrosCostososEnParaleloExplicito() {
        BookFilter filtro = new ExpensiveBook();
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .parallel()
                .filter(filtro::test)
                .collect(Collectors.toList());
    }

    public List<Libro> buscarLibrosCostososEnParalelo() {
        return buscarLibrosEnParalelo(new ExpensiveBook());
    }

    // Búsquedas aplicando API BookFilter en Paralelo Seguro
    public List<Double> procesarPreciosConResiliencia() {
        double DEFAULT_VALUE = 99.99;
        List<Libro> libros = daoLibro.getAllBooks();
        return libros.stream()
                .map(libro -> envoltorioSeguro(this::procesarPrecioPeligroso, libro, DEFAULT_VALUE))
                .filter(precio -> precio != DEFAULT_VALUE)
                .collect(Collectors.toList());
    }

    private Double procesarPrecioPeligroso(Libro libro) throws Exception {
        double ZERO = 0.00;
        if (libro.getPrecio() < ZERO) {
            throw new Exception("Error crítico: el libro: " + libro.getTitulo() + "tiene un precio inferior a 0");
        }
        return libro.getPrecio() * 1.10; // Aplicar incremento de precio
    }

    /**
     * @param funcion      - es la función que implementa FuncionValidadora
     * @param <T>          - tipo/clase del elemento/objeto sobre el que se ejecuta la operación aplicar
     * @param target       - es el elemento/objeto sobre el que se aplicará la operación aplicar
     * @param <R>          - el tipo del resultado que se devuelve frente a una excepción
     * @param defaultValue - es el resutado que se devuelve cuando ocurre una excepción
     */
    private <T, R> R envoltorioSeguro(FuncionValidadora<T, R> funcion, T target, R defaultValue) {
        try {
            return funcion.aplicar(target);
        } catch (Exception e) {
            System.err.println("[ADVERTENCIA RESILIENTE CAPTURADA]: " + e.getMessage());
            return defaultValue;
        }
    }
    //REMOVE LIBRO USING FOREACH TO GET THE ConcurrentModificationException
    //option 12
    public void removeLibros(){
        List<Libro> libros = daoLibro.getAllBooks();
        for(Libro libro : libros){
            if(libro.getStock()==0){
                libros.remove(libro);
            }
        }

    }
    //option 13
    public List<Libro> obtenerLibrosEnOrdenInverso() {
        List<Libro> librosInversos = new ArrayList<>();
        List<Libro> libros = daoLibro.getAllBooks();
        ListIterator<Libro> lit = libros.listIterator(libros.size());

        while (lit.hasPrevious()) {
            librosInversos.add(lit.previous());
        }
        return librosInversos;
    }

    //REMOVE LIBRO USING ITERATOR ---- NO ConcurrentModificationException
    //option 14
    public  int removeNoStockLibrosUsingIterator() {
        Iterator<Libro> iterator = daoLibro.getAllBooks().iterator();
        int countRemoved = 0;
        while (iterator.hasNext()) {
            Libro libro = iterator.next();
            if (libro.getStock() == 0) {
                iterator.remove();  //safe removal
                countRemoved++;
            }
        }
        return countRemoved;
    }


    // Cierre de la conexión a la fuente de persistencia de datos
    public void disconnect() {
        daoLibro.disconnect();
    }

}
