/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Clases.Despacho;
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
public class DespachoJpaController implements Serializable {

    public DespachoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Despacho despacho) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paqueteidPaquete = despacho.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete = em.getReference(paqueteidPaquete.getClass(), paqueteidPaquete.getIdPaquete());
                despacho.setPaqueteidPaquete(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = despacho.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                despacho.setUsuarioidUsuario(usuarioidUsuario);
            }
            em.persist(despacho);
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getDespachoCollection().add(despacho);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getDespachoCollection().add(despacho);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Despacho despacho) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despacho persistentDespacho = em.find(Despacho.class, despacho.getIdDespacho());
            Paquete paqueteidPaqueteOld = persistentDespacho.getPaqueteidPaquete();
            Paquete paqueteidPaqueteNew = despacho.getPaqueteidPaquete();
            Usuario usuarioidUsuarioOld = persistentDespacho.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = despacho.getUsuarioidUsuario();
            if (paqueteidPaqueteNew != null) {
                paqueteidPaqueteNew = em.getReference(paqueteidPaqueteNew.getClass(), paqueteidPaqueteNew.getIdPaquete());
                despacho.setPaqueteidPaquete(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                despacho.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            despacho = em.merge(despacho);
            if (paqueteidPaqueteOld != null && !paqueteidPaqueteOld.equals(paqueteidPaqueteNew)) {
                paqueteidPaqueteOld.getDespachoCollection().remove(despacho);
                paqueteidPaqueteOld = em.merge(paqueteidPaqueteOld);
            }
            if (paqueteidPaqueteNew != null && !paqueteidPaqueteNew.equals(paqueteidPaqueteOld)) {
                paqueteidPaqueteNew.getDespachoCollection().add(despacho);
                paqueteidPaqueteNew = em.merge(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getDespachoCollection().remove(despacho);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getDespachoCollection().add(despacho);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = despacho.getIdDespacho();
                if (findDespacho(id) == null) {
                    throw new NonexistentEntityException("The despacho with id " + id + " no longer exists.");
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
            Despacho despacho;
            try {
                despacho = em.getReference(Despacho.class, id);
                despacho.getIdDespacho();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The despacho with id " + id + " no longer exists.", enfe);
            }
            Paquete paqueteidPaquete = despacho.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getDespachoCollection().remove(despacho);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = despacho.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getDespachoCollection().remove(despacho);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(despacho);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Despacho> findDespachoEntities() {
        return findDespachoEntities(true, -1, -1);
    }

    public List<Despacho> findDespachoEntities(int maxResults, int firstResult) {
        return findDespachoEntities(false, maxResults, firstResult);
    }

    private List<Despacho> findDespachoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Despacho.class));
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

    public Despacho findDespacho(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Despacho.class, id);
        } finally {
            em.close();
        }
    }

    public int getDespachoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Despacho> rt = cq.from(Despacho.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
