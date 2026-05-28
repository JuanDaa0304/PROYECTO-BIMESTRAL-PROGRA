
package Controladores;

import Clases.Historialestado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Paquete;
import Clases.Usuario;
import Controladores.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author A S U S
 */
public class HistorialestadoJpaController implements Serializable {

    public HistorialestadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historialestado historialestado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paqueteidPaquete = historialestado.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete = em.getReference(paqueteidPaquete.getClass(), paqueteidPaquete.getIdPaquete());
                historialestado.setPaqueteidPaquete(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = historialestado.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                historialestado.setUsuarioidUsuario(usuarioidUsuario);
            }
            em.persist(historialestado);
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getHistorialestadoCollection().add(historialestado);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getHistorialestadoCollection().add(historialestado);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historialestado historialestado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historialestado persistentHistorialestado = em.find(Historialestado.class, historialestado.getIdHistorial());
            Paquete paqueteidPaqueteOld = persistentHistorialestado.getPaqueteidPaquete();
            Paquete paqueteidPaqueteNew = historialestado.getPaqueteidPaquete();
            Usuario usuarioidUsuarioOld = persistentHistorialestado.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = historialestado.getUsuarioidUsuario();
            if (paqueteidPaqueteNew != null) {
                paqueteidPaqueteNew = em.getReference(paqueteidPaqueteNew.getClass(), paqueteidPaqueteNew.getIdPaquete());
                historialestado.setPaqueteidPaquete(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                historialestado.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            historialestado = em.merge(historialestado);
            if (paqueteidPaqueteOld != null && !paqueteidPaqueteOld.equals(paqueteidPaqueteNew)) {
                paqueteidPaqueteOld.getHistorialestadoCollection().remove(historialestado);
                paqueteidPaqueteOld = em.merge(paqueteidPaqueteOld);
            }
            if (paqueteidPaqueteNew != null && !paqueteidPaqueteNew.equals(paqueteidPaqueteOld)) {
                paqueteidPaqueteNew.getHistorialestadoCollection().add(historialestado);
                paqueteidPaqueteNew = em.merge(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getHistorialestadoCollection().remove(historialestado);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getHistorialestadoCollection().add(historialestado);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialestado.getIdHistorial();
                if (findHistorialestado(id) == null) {
                    throw new NonexistentEntityException("The historialestado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historialestado historialestado;
            try {
                historialestado = em.getReference(Historialestado.class, id);
                historialestado.getIdHistorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialestado with id " + id + " no longer exists.", enfe);
            }
            Paquete paqueteidPaquete = historialestado.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getHistorialestadoCollection().remove(historialestado);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = historialestado.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getHistorialestadoCollection().remove(historialestado);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(historialestado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historialestado> findHistorialestadoEntities() {
        return findHistorialestadoEntities(true, -1, -1);
    }

    public List<Historialestado> findHistorialestadoEntities(int maxResults, int firstResult) {
        return findHistorialestadoEntities(false, maxResults, firstResult);
    }

    private List<Historialestado> findHistorialestadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historialestado.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Historialestado findHistorialestado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historialestado.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialestadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historialestado> rt = cq.from(Historialestado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
