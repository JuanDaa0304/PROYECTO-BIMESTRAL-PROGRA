package Servicios;

import Clases.Cliente;
import Clases.Historialestado;
import Clases.Paquete;
import Clases.Usuario;
import Controladores.ClienteJpaController;
import Controladores.HistorialestadoJpaController;
import Controladores.PaqueteJpaController;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PaqueteServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private PaqueteJpaController paqueteController;
    private HistorialestadoJpaController historialController;
    private ClienteJpaController clienteController;

    public static final String ESTADO_REGISTRADO  = "Registrado";
    public static final String ESTADO_EN_TRANSITO = "En transito";
    public static final String ESTADO_ENTREGADO   = "Entregado";

    public PaqueteServicio() {
        this.emf               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.paqueteController = new PaqueteJpaController(emf);
        this.historialController = new HistorialestadoJpaController(emf);
        this.clienteController = new ClienteJpaController(emf);
    }

    // ---------------------------------------------------------------
    // REGISTRAR PAQUETE — solo Recepcionista
    // ---------------------------------------------------------------
    public Paquete registrarPaquete(Integer idCliente, BigDecimal peso,
                                    String tipoEnvio, String direccionEntrega) throws Exception {

        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        Cliente cliente = clienteController.findCliente(idCliente);
        if (cliente == null) {
            throw new Exception("El cliente con ID " + idCliente + " no existe.");
        }

        Usuario usuarioSesion = SesionActual.getUsuario();

        Paquete paquete = new Paquete();
        paquete.setNumSeguimiento(generarNumeroSeguimiento());
        paquete.setPeso(peso);
        paquete.setTipoEnvio(tipoEnvio);
        paquete.setDireccionEntrega(direccionEntrega);
        paquete.setEstadoAct(ESTADO_REGISTRADO);
        paquete.setFechaRegistro(new Date());
        paquete.setClienteidCliente(cliente);
        paquete.setUsuarioidUsuario(usuarioSesion);

        paqueteController.create(paquete);
        registrarHistorial(paquete, ESTADO_REGISTRADO, usuarioSesion);

        System.out.println("✔ Paquete registrado. Número de seguimiento: " + paquete.getNumSeguimiento());
        return paquete;
    }

    // ---------------------------------------------------------------
    // DESPACHAR PAQUETE — solo Operador
    // ---------------------------------------------------------------
    public void despacharPaquete(String numSeguimiento) throws Exception {

        ValidarRol.validar(ValidarRol.OPERADOR);

        Paquete paquete = buscarPorNumSeguimiento(numSeguimiento);
        if (paquete == null) {
            throw new Exception("No existe paquete con número de seguimiento: " + numSeguimiento);
        }
        if (!paquete.getEstadoAct().equals(ESTADO_REGISTRADO)) {
            throw new Exception("El paquete no puede despacharse. Estado actual: '"
                    + paquete.getEstadoAct() + "'. Solo se despachan paquetes 'Registrado'.");
        }

        paquete.setEstadoAct(ESTADO_EN_TRANSITO);
        paqueteController.edit(paquete);
        registrarHistorial(paquete, ESTADO_EN_TRANSITO, SesionActual.getUsuario());
        System.out.println("✔ Paquete " + numSeguimiento + " actualizado a 'En transito'.");
    }

    // ---------------------------------------------------------------
    // ENTREGAR PAQUETE — solo Repartidor
    // ---------------------------------------------------------------
    public void entregarPaquete(String numSeguimiento) throws Exception {

        ValidarRol.validar(ValidarRol.REPARTIDOR);

        Paquete paquete = buscarPorNumSeguimiento(numSeguimiento);
        if (paquete == null) {
            throw new Exception("No existe paquete con número de seguimiento: " + numSeguimiento);
        }
        if (!paquete.getEstadoAct().equals(ESTADO_EN_TRANSITO)) {
            throw new Exception("El paquete no puede entregarse. Estado actual: '"
                    + paquete.getEstadoAct() + "'. Solo se entregan paquetes 'En transito'.");
        }

        paquete.setEstadoAct(ESTADO_ENTREGADO);
        paqueteController.edit(paquete);
        registrarHistorial(paquete, ESTADO_ENTREGADO, SesionActual.getUsuario());
        System.out.println("✔ Paquete " + numSeguimiento + " marcado como 'Entregado'.");
    }

    // ---------------------------------------------------------------
    // CONSULTAR POR CÓDIGO — cualquier rol
    // ---------------------------------------------------------------
    public Paquete buscarPorNumSeguimiento(String numSeguimiento) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA, ValidarRol.OPERADOR,
                             ValidarRol.REPARTIDOR, ValidarRol.CLIENTE);

        List<Paquete> todos = paqueteController.findPaqueteEntities();
        for (Paquete p : todos) {
            if (p.getNumSeguimiento().equalsIgnoreCase(numSeguimiento)) {
                return p;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // HISTORIAL — cualquier rol puede consultar
    // ---------------------------------------------------------------
    public List<Historialestado> verHistorial(String numSeguimiento) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA, ValidarRol.OPERADOR,
                             ValidarRol.REPARTIDOR, ValidarRol.CLIENTE);

        Paquete paquete = buscarPorNumSeguimiento(numSeguimiento);
        if (paquete == null) {
            throw new Exception("No existe paquete con número: " + numSeguimiento);
        }

        List<Historialestado> historial = historialController.findHistorialestadoEntities();
        List<Historialestado> resultado = new ArrayList<>();
        for (Historialestado h : historial) {
            if (h.getPaqueteidPaquete().getIdPaquete().equals(paquete.getIdPaquete())) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // LISTAR TODOS — Recepcionista y Operador
    // ---------------------------------------------------------------
    public List<Paquete> listarTodos() throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA, ValidarRol.OPERADOR, ValidarRol.REPARTIDOR);
        return paqueteController.findPaqueteEntities();
    }

    // ---------------------------------------------------------------
    // LISTAR POR ESTADO — Operador y Repartidor
    // ---------------------------------------------------------------
    public List<Paquete> listarPorEstado(String estado) throws Exception {
        ValidarRol.validar(ValidarRol.OPERADOR, ValidarRol.REPARTIDOR, ValidarRol.RECEPCIONISTA);

        List<Paquete> todos = paqueteController.findPaqueteEntities();
        List<Paquete> resultado = new ArrayList<>();
        for (Paquete p : todos) {
            if (p.getEstadoAct().equalsIgnoreCase(estado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // MÉTODOS INTERNOS — sin validación de rol (uso interno)
    // ---------------------------------------------------------------
    public Paquete buscarPorNumSeguimientoInterno(String numSeguimiento) {
        List<Paquete> todos = paqueteController.findPaqueteEntities();
        for (Paquete p : todos) {
            if (p.getNumSeguimiento().equalsIgnoreCase(numSeguimiento)) {
                return p;
            }
        }
        return null;
    }

    private void registrarHistorial(Paquete paquete, String estado, Usuario usuario) throws Exception {
        Historialestado historial = new Historialestado();
        historial.setEstado(estado);
        historial.setFechaCambio(new Date());
        historial.setPaqueteidPaquete(paquete);
        historial.setUsuarioidUsuario(usuario);
        historialController.create(historial);
    }

    private String generarNumeroSeguimiento() {
        String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String aleatorio = String.valueOf((int)(Math.random() * 9000) + 1000);
        return "PKT-" + fecha + "-" + aleatorio;
    }
}