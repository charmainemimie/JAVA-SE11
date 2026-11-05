package business.entity;
import java.util.Objects;
/**
 * La clase Libro mantiene los datos de un Libro
 *
 * @author Alejandro Bremermann
 * @version 0.4
 *
 */
public class Libro {
    private final double EXPENSIVE_BOOK = 20.00;
    private final String isbn;
    private final String titulo;
    private final String autor;
    private final double precio;
    private final int stock;

    /**
     * El constructor base de Libro que a partir de los datos de cada uno de sus parámetros genera un objeto de tipo Libro.
     *
     * @param isbn - identificador universal único de cada libro
     * @param titulo -título del libro
     * @param autor - autor o autores del libro
     * @param precio - precio del libro
     * @param stock - stock actual del libro
     */


    /*public Libro(String isbn, String titulo, String autor, double precio, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.stock = stock;
    }*/
    public Libro(String isbn, String titulo, String autor, double precio, int stock) {
        // requireNonNull throws NullPointerException with your message if the value is null
        this.isbn   = Objects.requireNonNull(isbn, "ISBN can not be null");
        this.titulo = Objects.requireNonNull(titulo, "título can not be null");
        this.autor  = Objects.requireNonNull(autor, "autor can not be null");

        //reject impossible values
        if (isbn.isBlank())  throw new IllegalArgumentException("ISBN can not be empty");
        if (precio < 0)      throw new IllegalArgumentException("precio can not be negative");
        if (stock < 0)       throw new IllegalArgumentException("stock can not be negative");

        this.precio = precio;
        this.stock  = stock;
    }

    /**
     * Constructor que recibe un parámetro para cada atributo, excepto para el stock, que lo inicializa con un valor predeterminado
     *
     * @param isbn - identificador universal único de cada libro
     * @param titulo -título del libro
     * @param autor - autor o autores del libro
     * @param parametroPrecio - precio del libro
     */
    /*public Libro(String isbn, String titulo, String autor, double parametroPrecio) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        precio = parametroPrecio;
        stock = 99;
    }*/

    /**
     * Valida que un texto no sea nulo ni esté vacío y lo devuelve sin espacios sobrantes.
     * @throws NullPointerException si el valor es null
     * @throws IllegalArgumentException si el valor está vacío
     */

    private static String validarTexto(String valor, String campo) {
        Objects.requireNonNull(valor, "El " + campo + " no puede ser nulo");
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " no puede estar vacío");
        }
        return limpio;
    }

    /**
     * Método estático que se invoca con una línea de un fichero csv que cotinene los datos de un Libro y lo transforma en un objeto Libro
     * @param line - datos del libro en formato String, en el mismo orden en que tenemos definidos los atributos: isbn, titulo, autor, precio, stock
     * @return Libro - invoca al constructor base y devuelve un objeto de tipo Libro
     */
    public static Libro fromLine(String line) {
        String[] parts = line.split(",");

        return new Libro(
                parts[0],
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4])
        );
    }

    /**
     * Busca si una aguja/dato-ingresado-por-el-usuario es igual a algún título o es parte de un autor
     * @param aguja - dato ingresado por el usuario para realizar una búsqueda
     * @return boolean - devuelve true si esa aguja se corresponde con un título o es parte de un autor
     */
    public boolean matches(String aguja){
        if (titulo.equalsIgnoreCase(aguja)) return true;
        if (autor.toUpperCase().contains(aguja.toUpperCase())) return true;
        return false;
    }

    /**
     * Indica si el Libro tiene el isbn recibido como parámetro
     *
     * @param isbn - isbn a buscar
     * @return boolean - true indica si el libro tiene el isbn recibido o false en caso de que no se corresponda con el del libro
     */
    public boolean hasISBN(String isbn){
        return this.isbn.equalsIgnoreCase(isbn);
    }

    /**
     * Devuelve el valor del isbn
     *
     * @return String - el valor del isbn del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Devuelve el título del libro
     *
     * @return String - el valor del título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve el Autor del libro
     *
     * @return String - el valor del autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Devuelve el precio del libro
     *
     * @return double - el valor del precio del libro
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Devuelve el stock del Libro
     *
     * @return int - el valor del stock del libro
     */
    public int getStock() {
        return stock;
    }

    /**
     * Devuelve un String con los datos del libro para ser presentado por pantalla
     *
     * @return String - el valor del los datos del libro formateados para imprimir por pantalla
     */
    public String toString(){
        return String.format("Libro - ISBN: %s, Título: %s, Autor: %s, Precio: %.2f€, Stock:%d.",isbn,titulo, autor, precio, stock);
    }

    /**
     * Devuelve los datos del libro en formato CSV para ser persistidos
     *
     * @return String - línea para ser añadida a un fichero CSV con los datos del libro
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%f,%d", isbn, titulo, autor, precio, stock);
    }

    /**
     * Indica si un libro está disponible (stock > 0)
     *
     * @return boolean - true si hay stock, false si no lo hay
     */
    public boolean isAvailable() {
        return stock > 0;
    }

    /**
     * Indica si el libro no es caro
     *
     * @return boolean - true si el precio es inferior a EXPENSIVE_BOOK, false en caso de que sea igual o mayor
     */
    public boolean isUnexpensive() {
        return precio < EXPENSIVE_BOOK;
    }

    public static String validateISBN(String inputISBN){
        String cleanedISBN;
        cleanedISBN = inputISBN.replace("/", "");


        return cleanedISBN;
    }
}
