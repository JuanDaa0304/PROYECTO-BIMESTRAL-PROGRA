package Servicios;

import Clases.Entrega;
import Clases.Paquete;
import Controladores.EntregaJpaController;
import Controladores.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntregasServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private EntregaJpaController entregaController;
    private PaqueteServicio paqueteService;

    public EntregasServicio() {
        this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.entregaController = new EntregaJpaController(emf);
        this.paqueteService = new PaqueteServicio();
    }

    // ---------------------------------------------------------------
    // REGISTRAR ENTREGA — solo Repartidor
    // ---------------------------------------------------------------
    public Entrega registrarEntrega(String numSeguimiento, String nombreReceptor,
            String observaciones) throws Exception {

        ValidarRol.validar(ValidarRol.REPARTIDOR);

        if (nombreReceptor == null || nombreReceptor.trim().isEmpty()) {
            throw new Exception("El nombre de quien recibe es obligatorio.");
        }

        // Cambia estado (PaqueteService valida que sea Repartidor y que esté En transito)
        paqueteService.entregarPaquete(numSeguimiento);

        Paquete paquete = paqueteService.buscarPorNumSeguimientoInterno(numSeguimiento);

        Entrega entrega = new Entrega();
        entrega.setFechaEntrega(new Date());
        entrega.setNombreReceptor(nombreReceptor.trim());
        entrega.setObservaciones(observaciones);
        entrega.setPaqueteidPaquete(paquete);
        entrega.setUsuarioidUsuario(SesionActual.getUsuario());

        entregaController.create(entrega);
        System.out.println("✔ Entrega registrada. Receptor: " + nombreReceptor);
        return entrega;
    }

    // ---------------------------------------------------------------
    // LISTAR TODAS — solo Repartidor
    // ---------------------------------------------------------------
    public List<Entrega> listarTodas() throws Exception {
        ValidarRol.validar(ValidarRol.REPARTIDOR);
        return entregaController.findEntregaEntities();
    }

    // ---------------------------------------------------------------
    // LISTAR POR PAQUETE — Repartidor
    // ---------------------------------------------------------------
    public List<Entrega> listarPorPaquete(String numSeguimiento) throws Exception {
        ValidarRol.validar(ValidarRol.REPARTIDOR);

        Paquete paquete = paqueteService.buscarPorNumSeguimientoInterno(numSeguimiento);
        if (paquete == null) {
            throw new Exception("No existe paquete con número: " + numSeguimiento);
        }

        List<Entrega> todas = entregaController.findEntregaEntities();
        List<Entrega> resultado = new ArrayList<>();
        for (Entrega e : todas) {
            if (e.getPaqueteidPaquete().getIdPaquete().equals(paquete.getIdPaquete())) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // EDITAR OBSERVACIONES — solo Repartidor
    // ---------------------------------------------------------------
    public void editarObservaciones(Integer idEntrega, String nuevasObservaciones) throws Exception {
        ValidarRol.validar(ValidarRol.REPARTIDOR);

        Entrega entrega = entregaController.findEntrega(idEntrega);
        if (entrega == null) {
            throw new NonexistentEntityException("No existe entrega con ID: " + idEntrega);
        }
        entrega.setObservaciones(nuevasObservaciones);
        entregaController.edit(entrega);
        System.out.println("✔ Observaciones de entrega actualizadas.");
    }
}
