package Servicios;

import Clases.Cliente;
import Clases.Rol;
import Controladores.ClienteJpaController;
import Controladores.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClienteServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private ClienteJpaController clienteController;

    public ClienteServicio() {
        this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.clienteController = new ClienteJpaController(emf);
    }

    // ---------------------------------------------------------------
    // REGISTRAR CLIENTE — solo Recepcionista
    // ---------------------------------------------------------------
    public Cliente registrarCliente(String nombre, String apellido,
            String telefono, String direccion) throws Exception {

        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del cliente es obligatorio.");
        }

        // Verificar teléfono duplicado
        if (telefono != null && !telefono.trim().isEmpty()) {
            for (Cliente c : clienteController.findClienteEntities()) {
                if (telefono.trim().equals(c.getTelefono())) {
                    throw new Exception("Ya existe un cliente con el telefono: " + telefono);
                }
            }
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre.trim());
        cliente.setApellido(apellido != null ? apellido.trim() : "");
        cliente.setTelefono(telefono != null ? telefono.trim() : "");
        cliente.setDireccion(direccion != null ? direccion.trim() : "");
        cliente.setUsuarioidUsuario(SesionActual.getUsuario());

        clienteController.create(cliente);
        System.out.println(" Cliente registrado: " + nombre + " (ID: " + cliente.getIdCliente() + ")");
        return cliente;
    }

    // ---------------------------------------------------------------
    // BUSCAR POR ID — Recepcionista
    // ---------------------------------------------------------------
    public Cliente buscarPorId(Integer idCliente) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);
        return clienteController.findCliente(idCliente);
    }

    // ---------------------------------------------------------------
    // BUSCAR POR NOMBRE — Recepcionista
    // ---------------------------------------------------------------
    public List<Cliente> buscarPorNombre(String nombre) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Ingrese un nombre para buscar.");
        }
        List<Cliente> todos = clienteController.findClienteEntities();
        List<Cliente> resultado = new ArrayList<>();
        for (Cliente c : todos) {
            if (c.getNombre().toLowerCase().contains(nombre.trim().toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // LISTAR TODOS — solo Recepcionista
    // ---------------------------------------------------------------
    public List<Cliente> listarTodos() throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);
        return clienteController.findClienteEntities();
    }

    // ---------------------------------------------------------------
    // ACTUALIZAR — solo Recepcionista
    // ---------------------------------------------------------------
    public void actualizarCliente(Integer idCliente, String nombre, String apellido,
            String telefono, String direccion) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        Cliente cliente = clienteController.findCliente(idCliente);
        if (cliente == null) {
            throw new NonexistentEntityException("No existe cliente con ID: " + idCliente);
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            cliente.setNombre(nombre.trim());
        }
        if (apellido != null) {
            cliente.setApellido(apellido.trim());
        }
        if (telefono != null) {
            cliente.setTelefono(telefono.trim());
        }
        if (direccion != null) {
            cliente.setDireccion(direccion.trim());
        }

        clienteController.edit(cliente);
        System.out.println(" Cliente actualizado correctamente.");
    }

    // ---------------------------------------------------------------
    // ELIMINAR — solo Recepcionista
    // ---------------------------------------------------------------
    public void eliminarCliente(Integer idCliente) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        Cliente cliente = clienteController.findCliente(idCliente);
        if (cliente == null) {
            throw new NonexistentEntityException("No existe cliente con ID: " + idCliente);
        }
        if (cliente.getPaqueteCollection() != null && !cliente.getPaqueteCollection().isEmpty()) {
            throw new Exception("No se puede eliminar el cliente porque tiene paquetes registrados.");
        }
        clienteController.destroy(idCliente);
        System.out.println("Cliente eliminado.");
    }
}
