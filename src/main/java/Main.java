import dao.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando sistema de Gestión de Biblioteca con Hibernate...");

        MenuHibernate.mostrarMenuPrincipal();

        System.out.println("\nGracias por utilizar el sistema. ¡Hasta pronto!");

        // Cierra la SessionFactory de Hibernate
        HibernateUtil.shutdown();
    }
}