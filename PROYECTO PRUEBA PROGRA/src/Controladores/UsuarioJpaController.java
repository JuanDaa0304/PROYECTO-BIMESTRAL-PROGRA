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
import Clases.Rol;
import Clases.Cliente;
import java.util.ArrayList;
import java.util.Collection;
import Clases.Despacho;
import Clases.Historialestado;
import Clases.Entrega;
import Clases.Paquete;
import Clases.Usuario;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author A S U S
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getClienteCollection() == null) {
            usuario.setClienteCollection(new ArrayList<Cliente>());
        }
        if (usuario.getDespachoCollection() == null) {
            usuario.setDespachoCollection(new ArrayList<Despacho>());
        }
        if (usuario.getHistorialestadoCollection() == null) {
            usuario.setHistorialestadoCollection(new ArrayList<Historialestado>());
        }
        if (usuario.getEntregaCollection() == null) {
            usuario.setEntregaCollection(new ArrayList<Entrega>());
        }
        if (usuario.getPaqueteCollection() == null) {
            usuario.setPaqueteCollection(new ArrayList<Paquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rolidRol = usuario.getRolidRol();
            if (rolidRol != null) {
                rolidRol = em.getReference(rolidRol.getClass(), rolidRol.getIdRol());
                usuario.setRolidRol(rolidRol);
            }
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : usuario.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getIdCliente());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            usuario.setClienteCollection(attachedClienteCollection);
            Collection<Despacho> attachedDespachoCollection = new ArrayList<Despacho>();
            for (Despacho despachoCollectionDespachoToAttach : usuario.getDespachoCollection()) {
                despachoCollectionDespachoToAttach = em.getReference(despachoCollectionDespachoToAttach.getClass(), despachoCollectionDespachoToAttach.getIdDespacho());
                attachedDespachoCollection.add(despachoCollectionDespachoToAttach);
            }
            usuario.setDespachoCollection(attachedDespachoCollection);
            Collection<Historialestado> attachedHistorialestadoCollection = new ArrayList<Historialestado>();
            for (Historialestado historialestadoCollectionHistorialestadoToAttach : usuario.getHistorialestadoCollection()) {
                historialestadoCollectionHistorialestadoToAttach = em.getReference(historialestadoCollectionHistorialestadoToAttach.getClass(), historialestadoCollectionHistorialestadoToAttach.getIdHistorial());
                attachedHistorialestadoCollection.add(historialestadoCollectionHistorialestadoToAttach);
            }
            usuario.setHistorialestadoCollection(attachedHistorialestadoCollection);
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : usuario.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getIdEntrega());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            usuario.setEntregaCollection(attachedEntregaCollection);
            Collection<Paquete> attachedPaqueteCollection = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionPaqueteToAttach : usuario.getPaqueteCollection()) {
                paqueteCollectionPaqueteToAttach = em.getReference(paqueteCollectionPaqueteToAttach.getClass(), paqueteCollectionPaqueteToAttach.getIdPaquete());
                attachedPaqueteCollection.add(paqueteCollectionPaqueteToAttach);
            }
            usuario.setPaqueteCollection(attachedPaqueteCollection);
            em.persist(usuario);
            if (rolidRol != null) {
                rolidRol.getUsuarioCollection().add(usuario);
                rolidRol = em.merge(rolidRol);
            }
            for (Cliente clienteCollectionCliente : usuario.getClienteCollection()) {
                Usuario oldUsuarioidUsuarioOfClienteCollectionCliente = clienteCollectionCliente.getUsuarioidUsuario();
                clienteCollectionCliente.setUsuarioidUsuario(usuario);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
                if (oldUsuarioidUsuarioOfClienteCollectionCliente != null) {
                    oldUsuarioidUsuarioOfClienteCollectionCliente.getClienteCollection().remove(clienteCollectionCliente);
                    oldUsuarioidUsuarioOfClienteCollectionCliente = em.merge(oldUsuarioidUsuarioOfClienteCollectionCliente);
                }
            }
            for (Despacho despachoCollectionDespacho : usuario.getDespachoCollection()) {
                Usuario oldUsuarioidUsuarioOfDespachoCollectionDespacho = despachoCollectionDespacho.getUsuarioidUsuario();
                despachoCollectionDespacho.setUsuarioidUsuario(usuario);
                despachoCollectionDespacho = em.merge(despachoCollectionDespacho);
                if (oldUsuarioidUsuarioOfDespachoCollectionDespacho != null) {
                    oldUsuarioidUsuarioOfDespachoCollectionDespacho.getDespachoCollection().remove(despachoCollectionDespacho);
                    oldUsuarioidUsuarioOfDespachoCollectionDespacho = em.merge(oldUsuarioidUsuarioOfDespachoCollectionDespacho);
                }
            }
            for (Historialestado historialestadoCollectionHistorialestado : usuario.getHistorialestadoCollection()) {
                Usuario oldUsuarioidUsuarioOfHistorialestadoCollectionHistorialestado = historialestadoCollectionHistorialestado.getUsuarioidUsuario();
                historialestadoCollectionHistorialestado.setUsuarioidUsuario(usuario);
                historialestadoCollectionHistorialestado = em.merge(historialestadoCollectionHistorialestado);
                if (oldUsuarioidUsuarioOfHistorialestadoCollectionHistorialestado != null) {
                    oldUsuarioidUsuarioOfHistorialestadoCollectionHistorialestado.getHistorialestadoCollection().remove(historialestadoCollectionHistorialestado);
                    oldUsuarioidUsuarioOfHistorialestadoCollectionHistorialestado = em.merge(oldUsuarioidUsuarioOfHistorialestadoCollectionHistorialestado);
                }
            }
            for (Entrega entregaCollectionEntrega : usuario.getEntregaCollection()) {
                Usuario oldUsuarioidUsuarioOfEntregaCollectionEntrega = entregaCollectionEntrega.getUsuarioidUsuario();
                entregaCollectionEntrega.setUsuarioidUsuario(usuario);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldUsuarioidUsuarioOfEntregaCollectionEntrega != null) {
                    oldUsuarioidUsuarioOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldUsuarioidUsuarioOfEntregaCollectionEntrega = em.merge(oldUsuarioidUsuarioOfEntregaCollectionEntrega);
                }
            }
            for (Paquete paqueteCollectionPaquete : usuario.getPaqueteCollection()) {
                Usuario oldUsuarioidUsuarioOfPaqueteCollectionPaquete = paqueteCollectionPaquete.getUsuarioidUsuario();
                paqueteCollectionPaquete.setUsuarioidUsuario(usuario);
                paqueteCollectionPaquete = em.merge(paqueteCollectionPaquete);
                if (oldUsuarioidUsuarioOfPaqueteCollectionPaquete != null) {
                    oldUsuarioidUsuarioOfPaqueteCollectionPaquete.getPaqueteCollection().remove(paqueteCollectionPaquete);
                    oldUsuarioidUsuarioOfPaqueteCollectionPaquete = em.merge(oldUsuarioidUsuarioOfPaqueteCollectionPaquete);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Rol rolidRolOld = persistentUsuario.getRolidRol();
            Rol rolidRolNew = usuario.getRolidRol();
            Collection<Cliente> clienteCollectionOld = persistentUsuario.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = usuario.getClienteCollection();
            Collection<Despacho> despachoCollectionOld = persistentUsuario.getDespachoCollection();
            Collection<Despacho> despachoCollectionNew = usuario.getDespachoCollection();
            Collection<Historialestado> historialestadoCollectionOld = persistentUsuario.getHistorialestadoCollection();
            Collection<Historialestado> historialestadoCollectionNew = usuario.getHistorialestadoCollection();
            Collection<Entrega> entregaCollectionOld = persistentUsuario.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = usuario.getEntregaCollection();
            Collection<Paquete> paqueteCollectionOld = persistentUsuario.getPaqueteCollection();
            Collection<Paquete> paqueteCollectionNew = usuario.getPaqueteCollection();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteCollectionOldCliente + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (Despacho despachoCollectionOldDespacho : despachoCollectionOld) {
                if (!despachoCollectionNew.contains(despachoCollectionOldDespacho)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despacho " + despachoCollectionOldDespacho + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (Historialestado historialestadoCollectionOldHistorialestado : historialestadoCollectionOld) {
                if (!historialestadoCollectionNew.contains(historialestadoCollectionOldHistorialestado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historialestado " + historialestadoCollectionOldHistorialestado + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (Paquete paqueteCollectionOldPaquete : paqueteCollectionOld) {
                if (!paqueteCollectionNew.contains(paqueteCollectionOldPaquete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paquete " + paqueteCollectionOldPaquete + " since its usuarioidUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (rolidRolNew != null) {
                rolidRolNew = em.getReference(rolidRolNew.getClass(), rolidRolNew.getIdRol());
                usuario.setRolidRol(rolidRolNew);
            }
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getIdCliente());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            usuario.setClienteCollection(clienteCollectionNew);
            Collection<Despacho> attachedDespachoCollectionNew = new ArrayList<Despacho>();
            for (Despacho despachoCollectionNewDespachoToAttach : despachoCollectionNew) {
                despachoCollectionNewDespachoToAttach = em.getReference(despachoCollectionNewDespachoToAttach.getClass(), despachoCollectionNewDespachoToAttach.getIdDespacho());
                attachedDespachoCollectionNew.add(despachoCollectionNewDespachoToAttach);
            }
            despachoCollectionNew = attachedDespachoCollectionNew;
            usuario.setDespachoCollection(despachoCollectionNew);
            Collection<Historialestado> attachedHistorialestadoCollectionNew = new ArrayList<Historialestado>();
            for (Historialestado historialestadoCollectionNewHistorialestadoToAttach : historialestadoCollectionNew) {
                historialestadoCollectionNewHistorialestadoToAttach = em.getReference(historialestadoCollectionNewHistorialestadoToAttach.getClass(), historialestadoCollectionNewHistorialestadoToAttach.getIdHistorial());
                attachedHistorialestadoCollectionNew.add(historialestadoCollectionNewHistorialestadoToAttach);
            }
            historialestadoCollectionNew = attachedHistorialestadoCollectionNew;
            usuario.setHistorialestadoCollection(historialestadoCollectionNew);
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getIdEntrega());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            usuario.setEntregaCollection(entregaCollectionNew);
            Collection<Paquete> attachedPaqueteCollectionNew = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionNewPaqueteToAttach : paqueteCollectionNew) {
                paqueteCollectionNewPaqueteToAttach = em.getReference(paqueteCollectionNewPaqueteToAttach.getClass(), paqueteCollectionNewPaqueteToAttach.getIdPaquete());
                attachedPaqueteCollectionNew.add(paqueteCollectionNewPaqueteToAttach);
            }
            paqueteCollectionNew = attachedPaqueteCollectionNew;
            usuario.setPaqueteCollection(paqueteCollectionNew);
            usuario = em.merge(usuario);
            if (rolidRolOld != null && !rolidRolOld.equals(rolidRolNew)) {
                rolidRolOld.getUsuarioCollection().remove(usuario);
                rolidRolOld = em.merge(rolidRolOld);
            }
            if (rolidRolNew != null && !rolidRolNew.equals(rolidRolOld)) {
                rolidRolNew.getUsuarioCollection().add(usuario);
                rolidRolNew = em.merge(rolidRolNew);
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    Usuario oldUsuarioidUsuarioOfClienteCollectionNewCliente = clienteCollectionNewCliente.getUsuarioidUsuario();
                    clienteCollectionNewCliente.setUsuarioidUsuario(usuario);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                    if (oldUsuarioidUsuarioOfClienteCollectionNewCliente != null && !oldUsuarioidUsuarioOfClienteCollectionNewCliente.equals(usuario)) {
                        oldUsuarioidUsuarioOfClienteCollectionNewCliente.getClienteCollection().remove(clienteCollectionNewCliente);
                        oldUsuarioidUsuarioOfClienteCollectionNewCliente = em.merge(oldUsuarioidUsuarioOfClienteCollectionNewCliente);
                    }
                }
            }
            for (Despacho despachoCollectionNewDespacho : despachoCollectionNew) {
                if (!despachoCollectionOld.contains(despachoCollectionNewDespacho)) {
                    Usuario oldUsuarioidUsuarioOfDespachoCollectionNewDespacho = despachoCollectionNewDespacho.getUsuarioidUsuario();
                    despachoCollectionNewDespacho.setUsuarioidUsuario(usuario);
                    despachoCollectionNewDespacho = em.merge(despachoCollectionNewDespacho);
                    if (oldUsuarioidUsuarioOfDespachoCollectionNewDespacho != null && !oldUsuarioidUsuarioOfDespachoCollectionNewDespacho.equals(usuario)) {
                        oldUsuarioidUsuarioOfDespachoCollectionNewDespacho.getDespachoCollection().remove(despachoCollectionNewDespacho);
                        oldUsuarioidUsuarioOfDespachoCollectionNewDespacho = em.merge(oldUsuarioidUsuarioOfDespachoCollectionNewDespacho);
                    }
                }
            }
            for (Historialestado historialestadoCollectionNewHistorialestado : historialestadoCollectionNew) {
                if (!historialestadoCollectionOld.contains(historialestadoCollectionNewHistorialestado)) {
                    Usuario oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado = historialestadoCollectionNewHistorialestado.getUsuarioidUsuario();
                    historialestadoCollectionNewHistorialestado.setUsuarioidUsuario(usuario);
                    historialestadoCollectionNewHistorialestado = em.merge(historialestadoCollectionNewHistorialestado);
                    if (oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado != null && !oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado.equals(usuario)) {
                        oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado.getHistorialestadoCollection().remove(historialestadoCollectionNewHistorialestado);
                        oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado = em.merge(oldUsuarioidUsuarioOfHistorialestadoCollectionNewHistorialestado);
                    }
                }
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Usuario oldUsuarioidUsuarioOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getUsuarioidUsuario();
                    entregaCollectionNewEntrega.setUsuarioidUsuario(usuario);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldUsuarioidUsuarioOfEntregaCollectionNewEntrega != null && !oldUsuarioidUsuarioOfEntregaCollectionNewEntrega.equals(usuario)) {
                        oldUsuarioidUsuarioOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldUsuarioidUsuarioOfEntregaCollectionNewEntrega = em.merge(oldUsuarioidUsuarioOfEntregaCollectionNewEntrega);
                    }
                }
            }
            for (Paquete paqueteCollectionNewPaquete : paqueteCollectionNew) {
                if (!paqueteCollectionOld.contains(paqueteCollectionNewPaquete)) {
                    Usuario oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete = paqueteCollectionNewPaquete.getUsuarioidUsuario();
                    paqueteCollectionNewPaquete.setUsuarioidUsuario(usuario);
                    paqueteCollectionNewPaquete = em.merge(paqueteCollectionNewPaquete);
                    if (oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete != null && !oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete.equals(usuario)) {
                        oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete.getPaqueteCollection().remove(paqueteCollectionNewPaquete);
                        oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete = em.merge(oldUsuarioidUsuarioOfPaqueteCollectionNewPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cliente> clienteCollectionOrphanCheck = usuario.getClienteCollection();
            for (Cliente clienteCollectionOrphanCheckCliente : clienteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Cliente " + clienteCollectionOrphanCheckCliente + " in its clienteCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<Despacho> despachoCollectionOrphanCheck = usuario.getDespachoCollection();
            for (Despacho despachoCollectionOrphanCheckDespacho : despachoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Despacho " + despachoCollectionOrphanCheckDespacho + " in its despachoCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<Historialestado> historialestadoCollectionOrphanCheck = usuario.getHistorialestadoCollection();
            for (Historialestado historialestadoCollectionOrphanCheckHistorialestado : historialestadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Historialestado " + historialestadoCollectionOrphanCheckHistorialestado + " in its historialestadoCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<Entrega> entregaCollectionOrphanCheck = usuario.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<Paquete> paqueteCollectionOrphanCheck = usuario.getPaqueteCollection();
            for (Paquete paqueteCollectionOrphanCheckPaquete : paqueteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Paquete " + paqueteCollectionOrphanCheckPaquete + " in its paqueteCollection field has a non-nullable usuarioidUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Rol rolidRol = usuario.getRolidRol();
            if (rolidRol != null) {
                rolidRol.getUsuarioCollection().remove(usuario);
                rolidRol = em.merge(rolidRol);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
