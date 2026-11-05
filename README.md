# GestorBiblioteca — Library Manager

A console-based library management application built throughout the **Java Programming II** course (La Salle). Each module of the course adds new concepts to this same project: managing books (`Libro`) — adding, listing, searching, filtering, grouping and persisting them.

## Tech stack

- Java 25, Maven
- [Gson](https://github.com/google/gson) for JSON
- [H2](https://www.h2database.com/) in-memory database via JDBC

## Project structure

```
src/
├── Main.java                  # Entry point
├── presentation/              # Controller (menu) + UserInterface (console I/O)
├── business/                  # Biblioteca (core logic), FuncionValidadora
│   ├── entity/                # Libro, ISBN
│   └── filtroLibro/           # BookFilter + implementations (ExpensiveBook, AvailableBook, ...)
└── persistence/               # DAOLibro interface + List, CSV, JSON, CSV→JSON and JDBC implementations
files/                         # libros.csv, libro.json (data files)
javadoc/                       # Generated documentation
```

The data source is selected in the `Biblioteca` constructor (e.g. `DAOLibroCSV`, `DAOLibroJson`, `DAOLibroJDBC`).

## Running

Open the project in IntelliJ (or any Maven IDE) and run `Main`. A menu lets you add, list and search books, filter by stock, sort by price, group by author, partition by stock, raise prices, iterate in reverse order and remove out-of-stock books.

---

## Course modules covered

### Module 1 — Functional Programming
- Imperative vs. functional style, pure functions and immutability
- Interfaces and anonymous classes (the `BookFilter` filters)
- Lambdas: syntax and extending our interfaces with them
- Built-in functional interfaces (`Predicate`, `Function`, `Consumer`, `Supplier`) and combining predicates
- `Comparator` and list methods (`forEach`, `removeIf`, `sort`, ...)
- Introduction to streams: structure (source → intermediate → terminal)
- Method references and `Optional`

### Module 2 — Modular Programming & Services
- Problems with the classpath ("JAR hell") and packages vs. modules
- Java Platform Module System (JPMS) and `module-info.java`
- Structure of a modular project
- `requires`, `exports`, strong encapsulation, `requires transitive` / `requires static`
- Qualified exports and `opens`
- Compiling, packaging (modular JARs) and running modules; useful tools
- Services: defining a service, `provides`, `uses` and `ServiceLoader`
- Migrating to modules: classpath + module path, automatic modules, migration strategies, `jdeps`, common problems

### Module 3 — Stream Management & Parallel Streams
- Streams definition; stateless (`filter`, `map`) vs. stateful (`sorted`, `distinct`, `limit`) operations
- Traditional concurrency: threads, concurrency vs. parallelism, thread life cycle and their limitations
- Collectors: basic (`toList`, `toSet`, `toMap`), `groupingBy`, `partitioningBy`, `reducing`
- Fork/Join framework and creating parallel streams
- Performance and when to use parallel streams; ordering/stability; side effects and mutability
- Debugging pipelines with `peek`
- Handling exceptions inside streams
- Best practices for streams and concurrency

### Module 4 — Java APIs & Coding Concepts
- Designing a robust API: Javadoc (`@param`, `@return`, `@throws`), encapsulation, defensive copies, contract validation
- DAO / Repository pattern in the persistence layer
- I/O with `java.io` and NIO.2 (`java.nio.file`, `Files` utility class)
- Serialization/deserialization of structured data (CSV, JSON with Gson)
- Relational databases and JDBC: `Statement`, `PreparedStatement`, `ResultSet` mapping, transactions
- Secure coding: preventing SQL injection, safe handling of memory, resources and credentials
- Unit and integration testing for I/O and database layers
- Internationalization and localization (I18N / L10N): dates, currencies and numbers by region

### Module 5 — Collections & Generics, Exceptions & Assertions
- Collections API hierarchy (`List`, `Set`, `Queue`) and typed generic collections
- Iterators and the Iterator pattern; `ListIterator` (e.g. reverse order, safe removal)
- Type-wrapper classes, autoboxing and unboxing
- Generics fundamentals; wildcards and bounds (`?`, `extends`, `super`)
- Identifying and classifying exceptions (checked vs. unchecked)
- `try` / `catch` / `finally`, multi-catch and try-with-resources
- Assertions (`assert`) and when to use them vs. exceptions
- Best practices for exceptions and generics
