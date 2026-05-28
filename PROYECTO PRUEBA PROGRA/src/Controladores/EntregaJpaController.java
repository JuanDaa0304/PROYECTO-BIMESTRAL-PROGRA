/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Clases.Entrega;
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
public class EntregaJpaController implements Serializable {

    public EntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paqueteidPaquete = entrega.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete = em.getReference(paqueteidPaquete.getClass(), paqueteidPaquete.getIdPaquete());
                entrega.setPaqueteidPaquete(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = entrega.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                entrega.setUsuarioidUsuario(usuarioidUsuario);
            }
            em.persist(entrega);
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getEntregaCollection().add(entrega);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getEntregaCollection().add(entrega);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getIdEntrega());
            Paquete paqueteidPaqueteOld = persistentEntrega.getPaqueteidPaquete();
            Paquete paqueteidPaqueteNew = entrega.getPaqueteidPaquete();
            Usuario usuarioidUsuarioOld = persistentEntrega.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = entrega.getUsuarioidUsuario();
            if (paqueteidPaqueteNew != null) {
                paqueteidPaqueteNew = em.getReference(paqueteidPaqueteNew.getClass(), paqueteidPaqueteNew.getIdPaquete());
                entrega.setPaqueteidPaquete(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                entrega.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            entrega = em.merge(entrega);
            if (paqueteidPaqueteOld != null && !paqueteidPaqueteOld.equals(paqueteidPaqueteNew)) {
                paqueteidPaqueteOld.getEntregaCollection().remove(entrega);
                paqueteidPaqueteOld = em.merge(paqueteidPaqueteOld);
            }
            if (paqueteidPaqueteNew != null && !paqueteidPaqueteNew.equals(paqueteidPaqueteOld)) {
                paqueteidPaqueteNew.getEntregaCollection().add(entrega);
                paqueteidPaqueteNew = em.merge(paqueteidPaqueteNew);
            }
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getEntregaCollection().remove(entrega);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getEntregaCollection().add(entrega);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entrega.getIdEntrega();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
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
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getIdEntrega();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            Paquete paqueteidPaquete = entrega.getPaqueteidPaquete();
            if (paqueteidPaquete != null) {
                paqueteidPaquete.getEntregaCollection().remove(entrega);
                paqueteidPaquete = em.merge(paqueteidPaquete);
            }
            Usuario usuarioidUsuario = entrega.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getEntregaCollection().remove(entrega);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(entrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
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

    public Entrega findEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
