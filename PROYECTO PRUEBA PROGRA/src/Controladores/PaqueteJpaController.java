/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Cliente;
import Clases.Usuario;
import Clases.Despacho;
import java.util.ArrayList;
import java.util.Collection;
import Clases.Historialestado;
import Clases.Entrega;
import Clases.Paquete;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author A S U S
 */
public class PaqueteJpaController implements Serializable {

    public PaqueteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paquete paquete) {
        if (paquete.getDespachoCollection() == null) {
            paquete.setDespachoCollection(new ArrayList<Despacho>());
        }
        if (paquete.getHistorialestadoCollection() == null) {
            paquete.setHistorialestadoCollection(new ArrayList<Historialestado>());
        }
        if (paquete.getEntregaCollection() == null) {
            paquete.setEntregaCollection(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente clienteidCliente = paquete.getClienteidCliente();
            if (clienteidCliente != null) {
                clienteidCliente = em.getReference(clienteidCliente.getClass(), clienteidCliente.getIdCliente());
                paquete.setClienteidCliente(clienteidCliente);
            }
            Usuario usuarioidUsuario = paquete.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                paquete.setUsuarioidUsuario(usuarioidUsuario);
            }
            Collection<Despacho> attachedDespachoCollection = new ArrayList<Despacho>();
            for (Despacho despachoCollectionDespachoToAttach : paquete.getDespachoCollection()) {
                despachoCollectionDespachoToAttach = em.getReference(despachoCollectionDespachoToAttach.getClass(), despachoCollectionDespachoToAttach.getIdDespacho());
                attachedDespachoCollection.add(despachoCollectionDespachoToAttach);
            }
            paquete.setDespachoCollection(attachedDespachoCollection);
            Collection<Historialestado> attachedHistorialestadoCollection = new ArrayList<Historialestado>();
            for (Historialestado historialestadoCollectionHistorialestadoToAttach : paquete.getHistorialestadoCollection()) {
                historialestadoCollectionHistorialestadoToAttach = em.getReference(historialestadoCollectionHistorialestadoToAttach.getClass(), historialestadoCollectionHistorialestadoToAttach.getIdHistorial());
                attachedHistorialestadoCollection.add(historialestadoCollectionHistorialestadoToAttach);
            }
            paquete.setHistorialestadoCollection(attachedHistorialestadoCollection);
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : paquete.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getIdEntrega());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            paquete.setEntregaCollection(attachedEntregaCollection);
            em.persist(paquete);
            if (clienteidCliente != null) {
                clienteidCliente.getPaqueteCollection().add(paquete);
                clienteidCliente = em.merge(clienteidCliente);
            }
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getPaqueteCollection().add(paquete);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            for (Despacho despachoCollectionDespacho : paquete.getDespachoCollection()) {
                Paquete oldPaqueteidPaqueteOfDespachoCollectionDespacho = despachoCollectionDespacho.getPaqueteidPaquete();
                despachoCollectionDespacho.setPaqueteidPaquete(paquete);
                despachoCollectionDespacho = em.merge(despachoCollectionDespacho);
                if (oldPaqueteidPaqueteOfDespachoCollectionDespacho != null) {
                    oldPaqueteidPaqueteOfDespachoCollectionDespacho.getDespachoCollection().remove(despachoCollectionDespacho);
                    oldPaqueteidPaqueteOfDespachoCollectionDespacho = em.merge(oldPaqueteidPaqueteOfDespachoCollectionDespacho);
                }
            }
            for (Historialestado historialestadoCollectionHistorialestado : paquete.getHistorialestadoCollection()) {
                Paquete oldPaqueteidPaqueteOfHistorialestadoCollectionHistorialestado = historialestadoCollectionHistorialestado.getPaqueteidPaquete();
                historialestadoCollectionHistorialestado.setPaqueteidPaquete(paquete);
                historialestadoCollectionHistorialestado = em.merge(historialestadoCollectionHistorialestado);
                if (oldPaqueteidPaqueteOfHistorialestadoCollectionHistorialestado != null) {
                    oldPaqueteidPaqueteOfHistorialestadoCollectionHistorialestado.getHistorialestadoCollection().remove(historialestadoCollectionHistorialestado);
                    oldPaqueteidPaqueteOfHistorialestadoCollectionHistorialestado = em.merge(oldPaqueteidPaqueteOfHistorialestadoCollectionHistorialestado);
                }
            }
            for (Entrega entregaCollectionEntrega : paquete.getEntregaCollection()) {
                Paquete oldPaqueteidPaqueteOfEntregaCollectionEntrega = entregaCollectionEntrega.getPaqueteidPaquete();
                entregaCollectionEntrega.setPaqueteidPaquete(paquete);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldPaqueteidPaqueteOfEntregaCollectionEntrega != null) {
                    oldPaqueteidPaqueteOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldPaqueteidPaqueteOfEntregaCollectionEntrega = em.merge(oldPaqueteidPaqueteOfEntregaCollectionEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paquete paquete) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete persistentPaquete = em.find(Paquete.class, paquete.getIdPaquete());
            Cliente clienteidClienteOld = persistentPaquete.getClienteidCliente();
            Cliente clienteidClienteNew = paquete.getClienteidCliente();
            Usuario usuarioidUsuarioOld = persistentPaquete.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = paquete.getUsuarioidUsuario();
            Collection<Despacho> despachoCollectionOld = persistentPaquete.getDespachoCollection();
            Collection<Despacho> despachoCollectionNew = paquete.getDespachoCollection();
            Collection<Historialestado> historialestadoCollectionOld = persistentPaquete.getHistorialestadoCollection();
            Collection<Historialestado> historialestadoCollectionNew = paquete.getHistorialestadoCollection();
            Collection<Entrega> entregaCollectionOld = persistentPaquete.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = paquete.getEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (Despacho despachoCollectionOldDespacho : despachoCollectionOld) {
                if (!despachoCollectionNew.contains(despachoCollectionOldDespacho)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despacho " + despachoCollectionOldDespacho + " since its paqueteidPaquete field is not nullable.");
                }
            }
            for (Historialestado historialestadoCollectionOldHistorialestado : historialestadoCollectionOld) {
                if (!historialestadoCollectionNew.contains(historialestadoCollectionOldHistorialestado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historialestado " + historialestadoCollectionOldHistorialestado + " since its paqueteidPaquete field is not nullable.");
                }
            }
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its paqueteidPaquete field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteidClienteNew != null) {
                clienteidClienteNew = em.getReference(clienteidClienteNew.getClass(), clienteidClienteNew.getIdCliente());
                paquete.setClienteidCliente(clienteidClienteNew);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                paquete.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            Collection<Despacho> attachedDespachoCollectionNew = new ArrayList<Despacho>();
            for (Despacho despachoCollectionNewDespachoToAttach : despachoCollectionNew) {
                despachoCollectionNewDespachoToAttach = em.getReference(despachoCollectionNewDespachoToAttach.getClass(), despachoCollectionNewDespachoToAttach.getIdDespacho());
                attachedDespachoCollectionNew.add(despachoCollectionNewDespachoToAttach);
            }
            despachoCollectionNew = attachedDespachoCollectionNew;
            paquete.setDespachoCollection(despachoCollectionNew);
            Collection<Historialestado> attachedHistorialestadoCollectionNew = new ArrayList<Historialestado>();
            for (Historialestado historialestadoCollectionNewHistorialestadoToAttach : historialestadoCollectionNew) {
                historialestadoCollectionNewHistorialestadoToAttach = em.getReference(historialestadoCollectionNewHistorialestadoToAttach.getClass(), historialestadoCollectionNewHistorialestadoToAttach.getIdHistorial());
                attachedHistorialestadoCollectionNew.add(historialestadoCollectionNewHistorialestadoToAttach);
            }
            historialestadoCollectionNew = attachedHistorialestadoCollectionNew;
            paquete.setHistorialestadoCollection(historialestadoCollectionNew);
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getIdEntrega());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            paquete.setEntregaCollection(entregaCollectionNew);
            paquete = em.merge(paquete);
            if (clienteidClienteOld != null && !clienteidClienteOld.equals(clienteidClienteNew)) {
                clienteidClienteOld.getPaqueteCollection().remove(paquete);
                clienteidClienteOld = em.merge(clienteidClienteOld);
            }
            if (clienteidClienteNew != null && !clienteidClienteNew.equals(clienteidClienteOld)) {
                clienteidClienteNew.getPaqueteCollection().add(paquete);
                clienteidClienteNew = em.merge(clienteidClienteNew);
            }
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getPaqueteCollection().remove(paquete);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getPaqueteCollection().add(paquete);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            for (Despacho despachoCollectionNewDespacho : despachoCollectionNew) {
                if (!despachoCollectionOld.contains(despachoCollectionNewDespacho)) {
                    Paquete oldPaqueteidPaqueteOfDespachoCollectionNewDespacho = despachoCollectionNewDespacho.getPaqueteidPaquete();
                    despachoCollectionNewDespacho.setPaqueteidPaquete(paquete);
                    despachoCollectionNewDespacho = em.merge(despachoCollectionNewDespacho);
                    if (oldPaqueteidPaqueteOfDespachoCollectionNewDespacho != null && !oldPaqueteidPaqueteOfDespachoCollectionNewDespacho.equals(paquete)) {
                        oldPaqueteidPaqueteOfDespachoCollectionNewDespacho.getDespachoCollection().remove(despachoCollectionNewDespacho);
                        oldPaqueteidPaqueteOfDespachoCollectionNewDespacho = em.merge(oldPaqueteidPaqueteOfDespachoCollectionNewDespacho);
                    }
                }
            }
            for (Historialestado historialestadoCollectionNewHistorialestado : historialestadoCollectionNew) {
                if (!historialestadoCollectionOld.contains(historialestadoCollectionNewHistorialestado)) {
                    Paquete oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado = historialestadoCollectionNewHistorialestado.getPaqueteidPaquete();
                    historialestadoCollectionNewHistorialestado.setPaqueteidPaquete(paquete);
                    historialestadoCollectionNewHistorialestado = em.merge(historialestadoCollectionNewHistorialestado);
                    if (oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado != null && !oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado.equals(paquete)) {
                        oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado.getHistorialestadoCollection().remove(historialestadoCollectionNewHistorialestado);
                        oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado = em.merge(oldPaqueteidPaqueteOfHistorialestadoCollectionNewHistorialestado);
                    }
                }
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Paquete oldPaqueteidPaqueteOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getPaqueteidPaquete();
                    entregaCollectionNewEntrega.setPaqueteidPaquete(paquete);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldPaqueteidPaqueteOfEntregaCollectionNewEntrega != null && !oldPaqueteidPaqueteOfEntregaCollectionNewEntrega.equals(paquete)) {
                        oldPaqueteidPaqueteOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldPaqueteidPaqueteOfEntregaCollectionNewEntrega = em.merge(oldPaqueteidPaqueteOfEntregaCollectionNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paquete.getIdPaquete();
                if (findPaquete(id) == null) {
                    throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paquete;
            try {
                paquete = em.getReference(Paquete.class, id);
                paquete.getIdPaquete();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Despacho> despachoCollectionOrphanCheck = paquete.getDespachoCollection();
            for (Despacho despachoCollectionOrphanCheckDespacho : despachoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Despacho " + despachoCollectionOrphanCheckDespacho + " in its despachoCollection field has a non-nullable paqueteidPaquete field.");
            }
            Collection<Historialestado> historialestadoCollectionOrphanCheck = paquete.getHistorialestadoCollection();
            for (Historialestado historialestadoCollectionOrphanCheckHistorialestado : historialestadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Historialestado " + historialestadoCollectionOrphanCheckHistorialestado + " in its historialestadoCollection field has a non-nullable paqueteidPaquete field.");
            }
            Collection<Entrega> entregaCollectionOrphanCheck = paquete.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable paqueteidPaquete field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clienteidCliente = paquete.getClienteidCliente();
            if (clienteidCliente != null) {
                clienteidCliente.getPaqueteCollection().remove(paquete);
                clienteidCliente = em.merge(clienteidCliente);
            }
            Usuario usuarioidUsuario = paquete.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getPaqueteCollection().remove(paquete);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(paquete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paquete> findPaqueteEntities() {
        return findPaqueteEntities(true, -1, -1);
    }

    public List<Paquete> findPaqueteEntities(int maxResults, int firstResult) {
        return findPaqueteEntities(false, maxResults, firstResult);
    }

    private List<Paquete> findPaqueteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paquete.class));
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

    public Paquete findPaquete(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paquete.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaqueteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paquete> rt = cq.from(Paquete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
