package dao;

import Entidades.Libro;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LibroDAO {

    // 1. Agregar Entidades.Libro
    public void guardar(Libro libro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(libro); // Equivale al INSERT
            tx.commit();
            System.out.println("Entidades.Libro guardado correctamente con Hibernate.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error al guardar el libro: " + e.getMessage());
        }
    }

    // 2. Buscar Entidades.Libro por ISBN (Equivalente al SELECT por ID)
    public Libro buscarPorIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Libro.class, isbn); // ¡Hibernate busca solo por la Primary Key!
        }
    }

    // 3. Listar todos los libros (Usando HQL - Hibernate Query Language)
    public List<Libro> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Libro", Libro.class).getResultList();
        }
    }

    // 4. Modificar Entidades.Libro
    public void actualizar(Libro libro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(libro); // Equivale al UPDATE
            tx.commit();
            System.out.println("Entidades.Libro actualizado correctamente.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    // 5. Eliminar Entidades.Libro
    public void eliminar(String isbn) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Libro libro = session.get(Libro.class, isbn);
            if (libro != null) {
                session.remove(libro); // Equivale al DELETE
                tx.commit();
                System.out.println("Entidades.Libro eliminado correctamente.");
            } else {
                System.out.println("No se encontró el libro con ese ISBN.");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}