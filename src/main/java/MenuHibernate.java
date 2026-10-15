import java.util.List;
import java.util.Scanner;

import Entidades.Libro;
import Entidades.Socio;
import dao.LibroDAO;
import dao.SocioDAO;

public class MenuHibernate {
    private static final Scanner sc = new Scanner(System.in);

    // Instancia el DAO para gestionar las operaciones de los libros/socios mediante Hibernate
    private static final LibroDAO libroDAO = new LibroDAO();
    private static final SocioDAO socioDAO = new SocioDAO();

    public static void mostrarMenuPrincipal() {
        int opcion; // almacena la opcion elegida

        do { // mantiene el menu abierto hasta que el usuario ingrese 0
            System.out.println("    SISTEMA DE BIBLIOTECA (HIBERNETE)");
            System.out.println("====================================");
            System.out.println("1 Gestionar libros");
            System.out.println("2 Gestionar socios");
            System.out.println("3 Gestionar ejemplares");
            System.out.println("4 Gestionar préstamos");
            System.out.println("0 Salir");
            System.out.println();

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    menuLibros();
                    break;
                case 2:
                    menuSocios();
                    break;
                case 3:
                    menuEjemplares();
                    break;
                case 4:
                    menuPrestamos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    //  MENÚ DE LIBROS
    private static void menuLibros() {
        int opcion;
        do {
            System.out.println("\n------- GESTIÓN DE LIBROS -------");
            System.out.println("1 Agregar libro");
            System.out.println("2 Listar libros");
            System.out.println("3 Buscar libro por ISBN");
            System.out.println("4 Modificar libro");
            System.out.println("5 Eliminar libro");
            System.out.println("0 Volver al menú principal");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    agregarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    buscarLibro();
                    break;
                case 4:
                    modificarLibro();
                    break;
                case 5:
                    eliminarLibro();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private static void agregarLibro() {
        System.out.println("\n-- Agregar libro --");

        String isbn = leerTextoObligatorio("ISBN: ");
        String titulo = leerTextoObligatorio("Título: ");
        String autor = leerTextoObligatorio("Autor: ");
        String editorial = leerTextoOpcional("Editorial (opcional): ");
        int anio = leerEntero("Año de publicación: ");

        // crea el objeto Libro en memoria empaquetando los datos ingresados por consola
        Libro nuevoLibro = new Libro(isbn, titulo, autor, editorial, anio);

        // Envía el objeto al DAO para que Hibernate ejecute el guardado en la base de datos
        libroDAO.guardar(nuevoLibro);
        System.out.println("Entidades.Libro guardado correctamente con Hibernate.");
    }

    private static void listarLibros() {
        System.out.println();
        List<Libro> libros = libroDAO.listarTodos(); // Consulta todos los registros y los mapea automáticamente a una lista de objetos Libro

        // cabecera de la tabla formateada
        System.out.printf("%-18s %-30s %-30s%n", "ISBN", "TÍTULO", "AUTOR");
        System.out.println("--------------------------------------------------------------------------------");

        //válida si la lista está vacia o contiene registros
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            // Recorre y muestra los datos de la lista de libros
            for (Libro l : libros) {
                System.out.printf("%-18s %-30s %-30s%n",
                        l.getIsbn(), l.getTitulo(), l.getAutor());
            }
        }
    }

    private static void buscarLibro() {
        System.out.println("\n-- Buscar libro --");
        String isbn = leerTextoObligatorio("Ingrese el ISBN a buscar: ");

        Libro l = libroDAO.buscarPorIsbn(isbn); // llama al DAO para buscar el objeto Libro por su clave primaria (ISBN)
        if (l != null) { // Verifica si el libro fue encontrado (si el objeto no es nulo)
            System.out.println("\nISBN     : " + l.getIsbn());
            System.out.println("Título   : " + l.getTitulo());
            System.out.println("Autor    : " + l.getAutor());
            System.out.println("Editorial: " + l.getEditorial());
            System.out.println("Año      : " + l.getAnioPublicacion());
        } else {
            System.out.println("No se encontró un libro con ese ISBN");
        }
    }

    private static void modificarLibro() {
        System.out.println("\n-- Modificar libro --");
        String isbn = leerTextoObligatorio("Ingrese el ISBN del libro a modificar: ");

        Libro l = libroDAO.buscarPorIsbn(isbn); // Busca primero si el libro existe en la base de datos
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN");
            return;
        }
        // Solicita los nuevos valores por consola
        String titulo    = leerTextoObligatorio("Nuevo título: ");
        String autor     = leerTextoObligatorio("Nuevo autor: ");
        String editorial = leerTextoOpcional("Nueva editorial (opcional): ");
        int anio         = leerEntero("Nuevo año de publicación: ");

        //actualizo los atributos del objeto recuperado
        l.setTitulo(titulo);
        l.setAutor(autor);
        l.setEditorial(editorial);
        l.setAnioPublicacion(anio);

        libroDAO.actualizar(l); //Sincronizo los cambios con la base de datos usando el DAO
        System.out.println("Entidades.Libro actualizado correctamente.");
    }

    private static void eliminarLibro() {
        System.out.println("\n-- Eliminar libro --");
        String isbn = leerTextoObligatorio("Ingrese el ISBN del libro a eliminar: ");

        //verifico primero si existe
        Libro l = libroDAO.buscarPorIsbn(isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
            return;
        }
        // proceod a eliminarlo mediante hibernete
        libroDAO.eliminar(isbn);
        System.out.println("Entidades.Libro eliminado correctamente.");
    }

    // =================================================================
    //  MENÚ DE SOCIOS
    // =================================================================
    private static void menuSocios() {
        int opcion;
        do {
            System.out.println("\n------- GESTIÓN DE SOCIOS (HIBERNATE) -------");
            System.out.println("1. Agregar socio");
            System.out.println("2. Listar socios");
            System.out.println("3. Buscar socio por DNI");
            System.out.println("4. Eliminar socio");
            System.out.println("0. Volver al menú principal");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    agregarSocio();
                    break;
                case 2:
                    listarSocios();
                    break;
                case 3:
                    buscarSocio();
                    break;
                case 4:
                    eliminarSocio();
                    break;
                case 0:
                    break;
                default: System.out.println("Opcion inválda");
            }
        } while (opcion != 0);
    }

    private static void agregarSocio() {
        System.out.println("\nAgregar socio");

        String nombre = leerTextoObligatorio("Nombre: ");
        String dni = leerTextoObligatorio("DNI: ");
        String telefono = leerTextoOpcional("Teléfono: ");
        String fechaAlta = leerTextoObligatorio("Fecha de alta (aaaa-mm-dd): ");

        Socio nuevoSocio = new Socio(nombre, dni, telefono, fechaAlta);  // Instancia el objeto Socio con los datos provistos
        socioDAO.guardar(nuevoSocio);
        System.out.println("Entidades.Socio guardado correctamente con Hibernate.");
    }

    private static void listarSocios() {
        System.out.println();
        List<Socio> socios = socioDAO.listarTodos(); // Obtiene todos los objetos Socio en una lista

        System.out.printf("%-5s %-20s %-11s %-15s%n", "ID", "NOMBRE", "DNI", "TELÉFONO");
        System.out.println("---------------------------------------------------------");

        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
        } else {
            // Itera sobre la lista e imprime la información de cada socio
            for (Socio s : socios) {
                System.out.printf("%-5d %-20s %-11s %-15s%n",
                        s.getIdSocio(), s.getNombre(), s.getDni(), s.getTelefono());
            }
        }
    }

    private static void buscarSocio() {
        System.out.println("\n-- Buscar socio por DNI --");
        String dni = leerTextoObligatorio("Ingrese el DNI a buscar: ");

        Socio s = socioDAO.buscarPorDni(dni);
        if (s != null) {
            System.out.println("\nID        : " + s.getIdSocio());
            System.out.println("Nombre    : " + s.getNombre());
            System.out.println("DNI       : " + s.getDni());
            System.out.println("Teléfono  : " + s.getTelefono());
            System.out.println("Fecha alta: " + s.getFechaAlta());
        } else {
            System.out.println("No se encontró un socio con ese DNI.");
        }
    }

    private static void eliminarSocio() {
        System.out.println("\n-- Eliminar socio --");
        int id = leerEntero("Ingrese el ID del socio a eliminar: ");
        socioDAO.eliminar(id);
        System.out.println("Entidades.Socio eliminado correctamente.");
    }

    private static void menuEjemplares() {

    }

    private static void menuPrestamos() {

    }

    // Métodos auxiliares

    // exigiendo que no se deje vacío el campo
    private static String leerTextoObligatorio(String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            valor = sc.nextLine().trim();
            if (valor.isEmpty()) System.out.println("Este dato es obligatorio.");
        } while (valor.isEmpty());
        return valor;
    }

    private static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }
}