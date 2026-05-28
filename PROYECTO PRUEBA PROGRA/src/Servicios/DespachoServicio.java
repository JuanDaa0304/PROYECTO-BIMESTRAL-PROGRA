package Servicios;

import Clases.Despacho;
import Clases.Paquete;
import Controladores.DespachoJpaController;
import Controladores.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DespachoServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private DespachoJpaController despachoController;
    private PaqueteServicio paqueteService;

    public DespachoServicio() {
        this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.despachoController = new DespachoJpaController(emf);
        this.paqueteService = new PaqueteServicio();
    }

    // ---------------------------------------------------------------
    // REGISTRAR DESPACHO — solo Operador
    // Cambia estado del paquete a "En transito" y guarda el despacho
    // ---------------------------------------------------------------
    public Despacho registrarDespacho(String numSeguimiento, String observaciones) throws Exception {

        ValidarRol.validar(ValidarRol.OPERADOR);

        // Cambia estado (PaqueteService valida internamente que sea Operador y que esté Registrado)
        paqueteService.despacharPaquete(numSeguimiento);

        Paquete paquete = paqueteService.buscarPorNumSeguimientoInterno(numSeguimiento);

        Despacho despacho = new Despacho();
        despacho.setFechaDespacho(new Date());
        despacho.setObservaciones(observaciones);
        despacho.setPaqueteidPaquete(paquete);
        despacho.setUsuarioidUsuario(SesionActual.getUsuario());

        despachoController.create(despacho);
        System.out.println("✔ Despacho registrado para el paquete: " + numSeguimiento);
        return despacho;
    }

    // ---------------------------------------------------------------
    // LISTAR TODOS — solo Operador
    // ---------------------------------------------------------------
    public List<Despacho> listarTodos() throws Exception {
        ValidarRol.validar(ValidarRol.OPERADOR);
        return despachoController.findDespachoEntities();
    }

    // ---------------------------------------------------------------
    // LISTAR POR PAQUETE — Operador
    // ---------------------------------------------------------------
    public List<Despacho> listarPorPaquete(String numSeguimiento) throws Exception {
        ValidarRol.validar(ValidarRol.OPERADOR);

        Paquete paquete = paqueteService.buscarPorNumSeguimientoInterno(numSeguimiento);
        if (paquete == null) {
            throw new Exception("No existe paquete con número: " + numSeguimiento);
        }

        List<Despacho> todos = despachoController.findDespachoEntities();
        List<Despacho> resultado = new ArrayList<>();
        for (Despacho d : todos) {
            if (d.getPaqueteidPaquete().getIdPaquete().equals(paquete.getIdPaquete())) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // EDITAR OBSERVACIONES — solo Operador
    // ---------------------------------------------------------------
    public void editarObservaciones(Integer idDespacho, String nuevasObservaciones) throws Exception {
        ValidarRol.validar(ValidarRol.OPERADOR);

        Despacho despacho = despachoController.findDespacho(idDespacho);
        if (despacho == null) {
            throw new NonexistentEntityException("No existe despacho con ID: " + idDespacho);
        }
        despacho.setObservaciones(nuevasObservaciones);
        despachoController.edit(despacho);
        System.out.println("✔ Observaciones actualizadas.");
    }
}
