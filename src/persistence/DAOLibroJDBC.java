package persistence;

import business.entity.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de DAOLibro utilizando JDBC y una base de datos en memoria H2.
 */
public class DAOLibroJDBC implements DAOLibro {

    // Configuración de la URL de conexión para H2 en memoria
    private static final String JDBC_URL = "jdbc:h2:mem:bibliotecaDB;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public DAOLibroJDBC() {
        inicializarBaseDatos();
    }

    /**
     * Método de inicialización que establece la conexión,
     * verifica su estado operativo y crea la estructura inicial de tablas.
     */
    private void inicializarBaseDatos() {
        try {
            // 1. Establecimiento de la conexión JDBC
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            // 2. Verificación del estado operativo de la conexión
            if (connection != null && connection.isValid(2)) {
                System.out.println("¡Conexión JDBC establecida y operativa con H2 (En memoria)!");
            } else {
                System.err.println("Advertencia: La conexión a la base de datos no es válida.");
            }

            // 3. Crear la tabla de libros si no existe
            crearTablaSiNoExiste();

        } catch (SQLException e) {
            // 3. Gestión adecuada de excepciones de tipo SQLException durante el arranque
            System.err.println("Error crítico de SQL durante la inicialización de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS libro (" +
                "isbn VARCHAR(255) PRIMARY KEY, " +
                "titulo VARCHAR(255), " +
                "autor VARCHAR(255), " +
                "precio DOUBLE, " +
                "stock SMALLINT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'libros' verificada/creada correctamente en H2.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla en H2: " + e.getMessage());
        }
    }

    @Override
    public void add(Libro libro) {
        String sql = "INSERT INTO libro (isbn, titulo, autor, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setDouble(4, libro.getPrecio());
            pstmt.setInt(5, libro.getStock());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar el libro en la base de datos: " + e.getMessage());
        }
    }


    @Override
    public List<Libro> getAllBooks() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT isbn, titulo, autor, precio, stock FROM libro";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                Libro libro = new Libro(isbn, titulo, autor, precio, stock);
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los libros: " + e.getMessage());
        }
        return libros;
    }

    @Override
    public Libro findByISBN(String isbnBuscado) {
        String sql = "SELECT isbn, titulo, autor, precio, stock FROM libro WHERE isbn = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, isbnBuscado);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Libro(
                            rs.getString("isbn"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el libro por ISBN: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión JDBC cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión JDBC: " + e.getMessage());
        }
    }
}