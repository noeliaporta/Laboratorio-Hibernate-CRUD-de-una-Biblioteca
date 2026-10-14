import java.util.List;
import java.util.Scanner;
import dao.LibroDAO;
import dao.SocioDAO;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Instancion de los DAOs que van a  hablar con Hibernate
    private static final LibroDAO libroDAO = new LibroDAO();

    public static void main(String[] args) {
        System.out.println("Iniciando sistema de Gestión de Biblioteca con Hibernate...");

        // Hibernate se encarga de inicializar la SessionFactory al arrancar, no como en JDBC que debiamos probar la conexion a manopla

        menuPrincipal();

        System.out.println("\nGracias por utilizar el sistema. ¡Hasta pronto!");
        HibernateUtil.shutdown(); // Cierra Hibernate al salir
    }

    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n====================================");
            System.out.println("    SISTEMA DE BIBLIOTECA (HIBERNATE)");
            System.out.println("====================================");
            System.out.println("1. Gestionar libros");
            System.out.println("2. Gestionar socios");
            System.out.println("3. Gestionar ejemplares");
            System.out.println("4. Gestionar préstamos");
            System.out.println("0. Salir");
            System.out.println();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: menuLibros();     break;
                case 2: menuSocios();     break;
                case 3: menuEjemplares(); break;
                case 4: menuPrestamos();  break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // =================================================================
    //  MENÚ DE LIBROS
    // =================================================================
    private static void menuLibros() {
        int opcion;
        do {
            System.out.println("\n------- GESTIÓN DE LIBROS -------");
            System.out.println("1. Agregar libro");
            System.out.println("2. Listar libros");
            System.out.println("3. Buscar libro por ISBN");
            System.out.println("4. Modificar libro");
            System.out.println("5. Eliminar libro");
            System.out.println("0. Volver al menú principal");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: agregarLibro();  break;
                case 2: listarLibros();  break;
                case 3: buscarLibro();   break;
                case 4: modificarLibro();break;
                case 5: eliminarLibro(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void agregarLibro() {
        System.out.println("\n-- Agregar libro --");
        String isbn      = leerTextoObligatorio("ISBN: ");
        String titulo    = leerTextoObligatorio("Título: ");
        String autor     = leerTextoObligatorio("Autor: ");
        String editorial = leerTextoOpcional("Editorial (opcional): ");
        int anio         = leerEntero("Año de publicación: ");

        // Cre0 el objeto Libro plano
        Libro nuevoLibro = new Libro(isbn, titulo, autor, editorial, anio);

        // pasamos a Hibernate a través del DAO sin escribir SQL
        libroDAO.guardar(nuevoLibro);
    }

    private static void listarLibros() {
        System.out.println();
        List<Libro> libros = libroDAO.listarTodos();

        System.out.printf("%-18s %-30s %-30s%n", "ISBN", "TÍTULO", "AUTOR");
        System.out.println("--------------------------------------------------------------------------------");

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro l : libros) {
                System.out.printf("%-18s %-30s %-30s%n",
                        l.getIsbn(), l.getTitulo(), l.getAutor());
            }
        }
    }

    private static void buscarLibro() {
        System.out.println("\n-- Buscar libro --");
        String isbn = leerTextoObligatorio("Ingrese el ISBN a buscar: ");

        Libro l = libroDAO.buscarPorIsbn(isbn);
        if (l != null) {
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

        Libro l = libroDAO.buscarPorIsbn(isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN");
            return;
        }

        String titulo    = leerTextoObligatorio("Nuevo título: ");
        String autor     = leerTextoObligatorio("Nuevo autor: ");
        String editorial = leerTextoOpcional("Nueva editorial (opcional): ");
        int anio         = leerEntero("Nuevo año de publicación: ");

        l.setTitulo(titulo);
        l.setAutor(autor);
        l.setEditorial(editorial);
        l.setAnioPublicacion(anio);

        libroDAO.actualizar(l);
    }

    private static void eliminarLibro() {
        System.out.println("\n-- Eliminar libro --");
        String isbn = leerTextoObligatorio("Ingrese el ISBN del libro a eliminar: ");
        libroDAO.eliminar(isbn);
    }

    // Métodos de lectura auxiliares (idénticos a los que ya tenías)
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

    private static final SocioDAO socioDAO = new SocioDAO();

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
                case 1: agregarSocio();  break;
                case 2: listarSocios();  break;
                case 3: buscarSocio();   break;
                case 4: eliminarSocio(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void agregarSocio() {
        System.out.println("\n-- Agregar socio --");
        String nombre    = leerTextoObligatorio("Nombre: ");
        String dni       = leerTextoObligatorio("DNI: ");
        String telefono  = leerTextoOpcional("Teléfono (opcional): ");
        String fechaAlta = leerTextoObligatorio("Fecha de alta (aaaa-mm-dd): ");

        Socio nuevoSocio = new Socio(nombre, dni, telefono, fechaAlta);
        socioDAO.guardar(nuevoSocio);
    }

    private static void listarSocios() {
        System.out.println();
        List<Socio> socios = socioDAO.listarTodos();

        System.out.printf("%-5s %-20s %-11s %-15s%n", "ID", "NOMBRE", "DNI", "TELÉFONO");
        System.out.println("---------------------------------------------------------");

        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
        } else {
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
    }

    private static void menuEjemplares() {
        System.out.println("\n[Próximamente: Menú de Ejemplares con Hibernate]");
    }

    private static void menuPrestamos() {
        System.out.println("\n[Próximamente: Menú de Préstamos con Hibernate]");
    }
}