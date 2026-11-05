package presentation;

import business.Biblioteca;
import business.entity.Libro;
import business.filtroLibro.AvailableUnexpensiveBook;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Controller {
    //Biblioteca biblioteca = new Biblioteca();
    private Biblioteca biblioteca;
    private UserInterface userInterface;

    public Controller() {
        this.biblioteca = new Biblioteca();
        this.userInterface = new UserInterface();
    }

    public void run() {
        int MENU_MAX_OPTION = 14;
        int option = -1;
        do {
            userInterface.showMenu();
            option = userInterface.askForInteger("Ingresa una opción: ", 1, MENU_MAX_OPTION);
            runOption(option);
        } while (option != MENU_MAX_OPTION);
    }

    private void runOption(int option) {
        System.out.println();
        switch (option) {
            case 1 -> agregarLibro();
            case 2 -> listarLibros();
            case 3 -> buscarLibro();
            case 4 -> buscarLibrosEnStock();
            case 5 -> obtenerLibrosConStockMayorA();
            case 6 -> obtenerLibrosOrdenadosPorPrecio();
            case 7 -> obtenerTitulosSinRepetir();
            case 8 -> mapearLibrosPorISBN();
            case 9 -> agruparLibrosPorAutor();
            case 10 -> particionarLibrosPorStock();
            case 11 -> aumentarPreciosResilientes();
            case 12 -> removeLibros();
            case 13 -> obtenerLibrosEnOrdenInverso();
            case 14 -> removeNoStockLibrosUsingIterator();
            case 15 -> salirDelPrograma(biblioteca);
        }
        System.out.println();
    }




    private void agregarLibro() {
        int MAXIMO_NUMERO_DE_LIBROS_EN_STOCK = 100;

        String isbn;
        String titulo;
        String autor;
        double precio;
        int stock;

        isbn = userInterface.askForString("Ingresa el ISBN: ");
        titulo = userInterface.askForString("Ingresa el título: ");
        autor = userInterface.askForString("Ingresa el autor: ");
        precio = userInterface.askForDouble("Ingresa el precio: ");
        stock = userInterface.askForInteger("Ingresa el stock:", 0, MAXIMO_NUMERO_DE_LIBROS_EN_STOCK);

        Libro libro = new Libro(isbn, titulo, autor, precio, stock);
        biblioteca.agregarLibro(libro);
    }

    private void listarLibros() {
        List<Libro> librosAListar = biblioteca.obtenerLibros();
        userInterface.showBookList(librosAListar, "La biblioteca está vacía");
    }

    private void buscarLibro() {
        String tituloOAutorInput = userInterface.askForString("Ingresa el título o autor a buscar: ");
        List<Libro> librosEncontrados = biblioteca.buscarLibros(tituloOAutorInput);
        userInterface.showBookList(librosEncontrados, "No se ha encontrado ningún libro con el dato ingresado.");
    }

    private void buscarLibrosEnStock() {
        //List<Libro> librosFiltrados = biblioteca.buscarLibros(new AvailableBooks());
        List<Libro> librosFiltrados = biblioteca.buscarLibros(new AvailableUnexpensiveBook());

        userInterface.showBookList(librosFiltrados, "No hay libros con esta condición");
    }

    private void mapearLibrosPorISBN() {
        Map<String, Libro> mapaDeISBNLibro = biblioteca.mapearLibrosPorISBN();
        userInterface.showBookMap(mapaDeISBNLibro, "No hay libros en la biblioteca");
    }

    private void obtenerTitulosSinRepetir() {
        Set<String> titulos = biblioteca.obtenerTitulosSinRepetirSet();
        userInterface.showUniqueTitles(titulos, "No hay libros en la biblioteca");
    }

    private void obtenerLibrosOrdenadosPorPrecio() {
        List<Libro> libros = biblioteca.obtenerLibrosOrdenadosPorPrecio();
        userInterface.showBookList(libros, "No hay libros en la biblioteca");
    }

    private void obtenerLibrosConStockMayorA() {
        int stockMinimo = userInterface.askForInteger("Ingresa el stock mínimo: ", 0, 100);
        List<Libro> libros = biblioteca.obtenerLibrosConStockMayorA(stockMinimo);
        userInterface.showBookList(libros, "No hay libros con stock mayor a: " + stockMinimo);
    }

    private void salirDelPrograma(Biblioteca biblioteca) {
        //mostrarInformacionForkJoinPool();
        biblioteca.disconnect();
        System.out.println("Adiós");
    }

    private void agruparLibrosPorAutor() {
        Map<String, List<Libro>> librosPorAutor = biblioteca.agruparLibrosPorAutor();
        userInterface.showBookListMap(librosPorAutor, "No hay libros registrados para agrupar por autor.");
    }

    private void particionarLibrosPorStock() {
        int stockLimite = userInterface.askForInteger("Ingresa el stock límite: ", 0, 100);
        Map<Boolean, List<Libro>> resultadoParticionado = biblioteca.particionarLibrosPorStock(stockLimite);
        userInterface.showPartitionedListMap(resultadoParticionado, "No hay resultados");
    }

    private void mostrarInformacionForkJoinPool() {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println("--- Información del ForkJoinPool ---");
        System.out.println("Hilos paralelos disponibles (Parallelism): " + commonPool.getParallelism());
        System.out.println("Hilos activos actuales: " + commonPool.getActiveThreadCount());
        System.out.println("Tareas en cola: " + commonPool.getQueuedSubmissionCount());
    }

    private void aumentarPreciosResilientes() {
        List<Double> preciosAumentados = biblioteca.procesarPreciosConResiliencia();
        userInterface.showProcessedPrices(preciosAumentados);
    }

    private void removeLibros(){
        biblioteca.removeLibros();
        //UserInterface.showMessage();
    }


    private void obtenerLibrosEnOrdenInverso() {
        List<Libro> inversedList = biblioteca.obtenerLibrosEnOrdenInverso();
        userInterface.showInvertedBookList(inversedList);
    }

    private void removeNoStockLibrosUsingIterator() {
        int countRemoved =biblioteca.removeNoStockLibrosUsingIterator();
        userInterface.showMessage();
        System.out.println(countRemoved);
    }

}
