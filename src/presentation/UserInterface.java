package presentation;

import business.entity.Libro;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;

public class UserInterface {
    private Scanner scanner;


    public UserInterface() {
        scanner = new Scanner(System.in);
    }



    public void showMenu() {
        System.out.println("-- Biblioteca ---");
        System.out.println("1. Agregar Libro");
        System.out.println("2. Listar Libros");
        System.out.println("3. Buscar Libro");
        System.out.println("4. Buscar Libros en Stock");
        System.out.println("5. Libros con stock mayor a...");
        System.out.println("6. Libros ordenados por precio");
        System.out.println("7. Títulos sin repetir");
        System.out.println("8. Mapear libros por ISBN");
        System.out.println("9. Agrupar libros por Autor");
        System.out.println("10. Particionar libros x stock");
        System.out.println("11. Aumentar precios un 10% en forma resiliente");
        System.out.println("12. Remove libro using foreach to see ConcurrentModificationException");
        System.out.println("13. Obtener Libros En Orden Inverso");
        System.out.println("14. Remove No Stock Libros Using Iterator");
        System.out.println("15. Salir");
    }

    public int askForInteger(String message, int minValue, int maxValue) {
        int resultado = minValue - 1;

        do {
            System.out.print(message);

            try {
                resultado = scanner.nextInt();
                if (resultado < minValue || resultado > maxValue) {
                    System.out.printf("Por favor ingresa un número entre: %d y %d.", minValue, maxValue);
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingresa un número entero");
            } finally {
                scanner.nextLine();
            }
        } while (resultado < minValue || resultado > maxValue);

        return resultado;
    }

    public double askForDouble(String message) {
        do {
            System.out.print(message);
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingresa un número");
            } finally {
                scanner.nextLine();
            }
        } while (true);
    }

    public String askForString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void showBookList(List<Libro> libros, String mensajeError) {
        if (libros.isEmpty()) {
            System.out.println(mensajeError);
            return;
        }
        for (Libro libro : libros) {
            System.out.println(libro.toString());
        }
    }

    public void showUniqueTitles(Set<String> titulos, String mensajeError) {
        if (titulos.isEmpty()) {
            System.out.println(mensajeError);
            return;
        }
        for (String titulo : titulos) {
            System.out.println(titulo);
        }
    }

    public void showBookMap(Map<String, Libro> mapa, String mensajeError) {
        if (mapa.isEmpty()) {
            System.out.println(mensajeError);
            return;
        }
        mapa.forEach((key,libro) ->
                System.out.println(key + " -> " + libro.toString()));
    }

    public void showBookListMap(Map<String, List<Libro>> mapa, String mensajeError) {
        if (mapa.isEmpty()) {
            System.out.println(mensajeError);
            return;
        }
        mapa.forEach((key,libros) -> {
            System.out.println(key);
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        });
    }

    public void showPartitionedListMap(Map<Boolean, List<Libro>> resultadoParticionado, String mensajeError) {
        if (resultadoParticionado.isEmpty()) {
            System.out.println(mensajeError);
            return;
        }
        resultadoParticionado.forEach((key,libros) -> {
            System.out.println("");
            System.out.println(key
                    ? "Libros que cumplen la condición: "
                    : "Libros que no cumplen la condición"
                    );
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        });
    }

    public void showProcessedPrices(List<Double> preciosProcesados) {
        if (preciosProcesados.isEmpty()){
            System.out.println("No hay precios procesados o todos fallaron");
            return;
        }
        System.out.println("Precios procesados exitosamente de forma resiliente");
        for (Double precio : preciosProcesados){
            System.out.printf(" - %6.2f€\n", precio);
        }
    }

    public void showInvertedBookList(List<Libro> librosInversos) {
        if (librosInversos.isEmpty()) {
            System.out.println("There are no registered books to display in reverse order.");
            return;
        }
        System.out.println("--- Book List in Reverse Order ---");
        for (Libro libro : librosInversos) {
            System.out.println(libro.toString());
        }
    }

    public void showMessage() {
        System.out.print("\nDeleted!\n");
    }
}
