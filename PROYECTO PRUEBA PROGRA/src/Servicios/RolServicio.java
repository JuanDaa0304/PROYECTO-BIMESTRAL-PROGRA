package Servicios;

import Clases.Rol;
import Controladores.RolJpaController;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RolServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private RolJpaController rolController;

    public RolServicio() {
        this.emf           = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.rolController = new RolJpaController(emf);
    }

    public void crear(String nomRol) throws Exception {
        // Evitar duplicados
        for (Rol r : rolController.findRolEntities()) {
            if (r.getNomRol().equalsIgnoreCase(nomRol)) {
                throw new Exception("El rol '" + nomRol + "' ya existe.");
            }
        }
        Rol rol = new Rol();
        rol.setNomRol(nomRol);
        rolController.create(rol);
    }

    public Rol buscarPorId(Integer idRol) {
        return rolController.findRol(idRol);
    }

    public Rol buscarPorNombre(String nomRol) {
        for (Rol r : rolController.findRolEntities()) {
            if (r.getNomRol().equalsIgnoreCase(nomRol)) {
                return r;
            }
        }
        return null;
    }

    public List<Rol> listarTodos() {
        return rolController.findRolEntities();
    }
}