package dao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class SocioDAO {

    // 1. Guardar Socio
    public void guardar(Socio socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(socio);
            tx.commit();
            System.out.println("Socio agregado correctamente con Hibernate.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error al guardar el socio: " + e.getMessage());
        }
    }

    // 2. Listar todos los socios
    public List<Socio> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Socio", Socio.class).getResultList();
        }
    }

    // 3. Buscar socio por DNI (Usando HQL con parámetros)
    public Socio buscarPorDni(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Socio> query = session.createQuery("FROM Socio WHERE dni = :dni", Socio.class);
            query.setParameter("dni", dni);
            return query.uniqueResult(); // Devuelve el socio o null si no existe
        }
    }

    // 4. Eliminar socio por ID
    public void eliminar(int idSocio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Socio socio = session.get(Socio.class, idSocio);
            if (socio != null) {
                session.remove(socio);
                tx.commit();
                System.out.println("Socio eliminado correctamente.");
            } else {
                System.out.println("No se encontró un socio con ese ID.");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error al eliminar el socio: " + e.getMessage());
        }
    }
}