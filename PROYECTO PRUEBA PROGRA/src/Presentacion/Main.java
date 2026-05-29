package Presentacion;

import Clases.Cliente;
import Clases.Historialestado;
import Clases.Paquete;
import Clases.Rol;
import Clases.Usuario;
import Servicios.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner tcl = new Scanner(System.in);
    static UsuarioServicio usuarioService = new UsuarioServicio();
    static ClienteServicio clienteService = new ClienteServicio();
    static PaqueteServicio paqueteService = new PaqueteServicio();
    static DespachoServicio despachoService = new DespachoServicio();
    static EntregasServicio entregaService = new EntregasServicio();
    static RolServicio rolService = new RolServicio();

    public static void main(String[] args) {

        // Verifica roles y crea usuario base si la BD esta vacia
        new InicializadorDatos().inicializar();

        System.out.println("==========================================");
        System.out.println("   SISTEMA DE GESTION DE PAQUETES        ");
        System.out.println("==========================================");

        while (true) {
            if (!SesionActual.haySesion()) {
                menuLogin();
            } else {
                String rol = SesionActual.getRolActual();
                switch (rol) {
                    case ValidarRol.RECEPCIONISTA:
                        menuRecepcionista();
                        break;
                    case ValidarRol.OPERADOR:
                        menuOperador();
                        break;
                    case ValidarRol.REPARTIDOR:
                        menuRepartidor();
                        break;
                    case ValidarRol.CLIENTE:
                        menuCliente();
                        break;
                    default:
                        System.out.println("Rol no reconocido. Cerrando sesion.");
                        SesionActual.cerrarSesion();
                }
            }
        }
    }

    // ================================================================
    // LOGIN
    // ================================================================
    static void menuLogin() {
        System.out.println("\n--- INICIAR SESION ---");
        System.out.print("Correo    : ");
        String correo = tcl.nextLine().trim();
        System.out.print("Contrasenia: ");
        String contrasenia = tcl.nextLine().trim();
        try {
            Usuario usuario = usuarioService.login(correo, contrasenia);
            SesionActual.iniciarSesion(usuario);
        } catch (Exception e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ================================================================
    // MENU RECEPCIONISTA
    // ================================================================
    static void menuRecepcionista() {
        System.out.println("\n==========================================");
        System.out.println("  MENU RECEPCIONISTA                      ");
        System.out.println("==========================================");
        System.out.println("  CLIENTES                                ");
        System.out.println("  1. Registrar cliente                    ");
        System.out.println("  2. Listar clientes                      ");
        System.out.println("  3. Buscar cliente por nombre            ");
        System.out.println("  4. Actualizar cliente                   ");
        System.out.println("------------------------------------------");
        System.out.println("  PAQUETES                                ");
        System.out.println("  5. Registrar paquete                    ");
        System.out.println("  6. Listar todos los paquetes            ");
        System.out.println("  7. Consultar paquete por seguimiento    ");
        System.out.println("  8. Ver historial de un paquete          ");
        System.out.println("------------------------------------------");
        System.out.println("  USUARIOS                                ");
        System.out.println("  9. Registrar nuevo usuario              ");
        System.out.println(" 10. Listar usuarios                      ");
        System.out.println("------------------------------------------");
        System.out.println("  0. Cerrar sesion                        ");
        System.out.println("==========================================");
        System.out.print("Opcion: ");
        String op = tcl.nextLine().trim();
        System.out.println();
        try {
            switch (op) {
                case "1":
                    registrarCliente();
                    break;
                case "2":
                    listarClientes();
                    break;
                case "3":
                    buscarClienteNombre();
                    break;
                case "4":
                    actualizarCliente();
                    break;
                case "5":
                    registrarPaquete();
                    break;
                case "6":
                    listarPaquetes();
                    break;
                case "7":
                    consultarPaquete();
                    break;
                case "8":
                    verHistorial();
                    break;
                case "9":
                    registrarUsuario();
                    break;
                case "10":
                    listarUsuarios();
                    break;
                case "0":
                    SesionActual.cerrarSesion();
                    break;
                default:
                    System.out.println("[!] Opcion no valida.");
            }
        } catch (Exception e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ================================================================
    // MENU OPERADOR DE DESPACHO
    // ================================================================
    static void menuOperador() {
        System.out.println("\n==========================================");
        System.out.println("  MENU OPERADOR DE DESPACHO               ");
        System.out.println("==========================================");
        System.out.println("  1. Registrar despacho de paquete        ");
        System.out.println("  2. Ver paquetes pendientes de despacho  ");
        System.out.println("  3. Ver paquetes en transito             ");
        System.out.println("  4. Consultar paquete por seguimiento    ");
        System.out.println("  5. Ver historial de un paquete          ");
        System.out.println("------------------------------------------");
        System.out.println("  0. Cerrar sesion                        ");
        System.out.println("==========================================");
        System.out.print("Opcion: ");
        String op = tcl.nextLine().trim();
        System.out.println();
        try {
            switch (op) {
                case "1":
                    registrarDespacho();
                    break;
                case "2":
                    listarPaquetesPorEstado(PaqueteServicio.ESTADO_REGISTRADO);
                    break;
                case "3":
                    listarPaquetesPorEstado(PaqueteServicio.ESTADO_EN_TRANSITO);
                    break;
                case "4":
                    consultarPaquete();
                    break;
                case "5":
                    verHistorial();
                    break;
                case "0":
                    SesionActual.cerrarSesion();
                    break;
                default:
                    System.out.println("[!] Opcion no valida.");
            }
        } catch (Exception e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ================================================================
    // MENU REPARTIDOR
    // ================================================================
    static void menuRepartidor() {
        System.out.println("\n==========================================");
        System.out.println("  MENU REPARTIDOR                         ");
        System.out.println("==========================================");
        System.out.println("  1. Registrar entrega de paquete         ");
        System.out.println("  2. Ver paquetes en transito             ");
        System.out.println("  3. Ver paquetes ya entregados           ");
        System.out.println("  4. Consultar paquete por seguimiento    ");
        System.out.println("  5. Ver historial de un paquete          ");
        System.out.println("------------------------------------------");
        System.out.println("  0. Cerrar sesion                        ");
        System.out.println("==========================================");
        System.out.print("Opcion: ");
        String op = tcl.nextLine().trim();
        System.out.println();
        try {
            switch (op) {
                case "1":
                    registrarEntrega();
                    break;
                case "2":
                    listarPaquetesPorEstado(PaqueteServicio.ESTADO_EN_TRANSITO);
                    break;
                case "3":
                    listarPaquetesPorEstado(PaqueteServicio.ESTADO_ENTREGADO);
                    break;
                case "4":
                    consultarPaquete();
                    break;
                case "5":
                    verHistorial();
                    break;
                case "0":
                    SesionActual.cerrarSesion();
                    break;
                default:
                    System.out.println(" Opcion no valida.");
            }
        } catch (Exception e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ================================================================
    // MENU CLIENTE - solo consulta, no modifica nada
    // ================================================================
    static void menuCliente() {
        System.out.println("\n==========================================");
        System.out.println("  MENU CLIENTE                            ");
        System.out.println("==========================================");
        System.out.println("  1. Consultar estado de mi paquete       ");
        System.out.println("  2. Ver historial de movimientos         ");
        System.out.println("------------------------------------------");
        System.out.println("  0. Cerrar sesion                        ");
        System.out.println("==========================================");
        System.out.print("Opcion: ");
        String op = tcl.nextLine().trim();
        System.out.println();
        try {
            switch (op) {
                case "1":
                    consultarPaquete();
                    break;
                case "2":
                    verHistorial();
                    break;
                case "0":
                    SesionActual.cerrarSesion();
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }
        } catch (Exception e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ================================================================
    // OPERACIONES - CLIENTES
    // ================================================================
    static void registrarCliente() throws Exception {
        System.out.println("--- Registrar Cliente ---");
        System.out.print("Nombre    : ");
        String nombre = tcl.nextLine().trim();
        System.out.print("Apellido  : ");
        String apellido = tcl.nextLine().trim();
        System.out.print("Telefono  : ");
        String telefono = tcl.nextLine().trim();
        System.out.print("Direccion : ");
        String direccion = tcl.nextLine().trim();
        clienteService.registrarCliente(nombre, apellido, telefono, direccion);
    }

    static void listarClientes() throws Exception {
        System.out.println("--- Clientes Registrados ---");
        List<Cliente> lista = clienteService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Sin clientes registrados.");
            return;
        }
        System.out.printf("%-6s %-18s %-18s %-15s %-20s%n",
                "ID", "Nombre", "Apellido", "Telefono", "Direccion");
        System.out.println("--------------------------------------------------------------------------------");
        for (Cliente c : lista) {
            System.out.printf("%-6d %-18s %-18s %-15s %-20s%n",
                    c.getIdCliente(), c.getNombre(), c.getApellido(),
                    c.getTelefono(), c.getDireccion());
        }
    }

    static void buscarClienteNombre() throws Exception {
        System.out.print("Nombre a buscar: ");
        String nombre = tcl.nextLine().trim();
        List<Cliente> resultado = clienteService.buscarPorNombre(nombre);
        if (resultado.isEmpty()) {
            System.out.println("  Sin resultados.");
            return;
        }
        System.out.printf("%-6s %-18s %-18s %-15s%n", "ID", "Nombre", "Apellido", "Telefono");
        System.out.println("------------------------------------------------------------");
        for (Cliente c : resultado) {
            System.out.printf("%-6d %-18s %-18s %-15s%n",
                    c.getIdCliente(), c.getNombre(), c.getApellido(), c.getTelefono());
        }
    }

    static void actualizarCliente() throws Exception {
        listarClientes();
        System.out.print("\nID del cliente a actualizar: ");
        Integer id = leerEntero();
        System.out.println("(Enter para no cambiar el campo)");
        System.out.print("Nuevo nombre     : ");
        String nombre = tcl.nextLine().trim();
        System.out.print("Nuevo apellido   : ");
        String apellido = tcl.nextLine().trim();
        System.out.print("Nuevo telefono   : ");
        String telefono = tcl.nextLine().trim();
        System.out.print("Nueva direccion  : ");
        String direccion = tcl.nextLine().trim();
        clienteService.actualizarCliente(
                id,
                nombre.isEmpty() ? null : nombre,
                apellido.isEmpty() ? null : apellido,
                telefono.isEmpty() ? null : telefono,
                direccion.isEmpty() ? null : direccion
        );
    }

    // ================================================================
    // OPERACIONES - PAQUETES
    // ================================================================
    static void registrarPaquete() throws Exception {
        System.out.println("--- Registrar Paquete ---");
        listarClientes();
        System.out.print("\nID del cliente destinatario : ");
        Integer idCliente = leerEntero();
        System.out.print("Peso en kg (ej: 1.50)       : ");
        BigDecimal peso = leerDecimal();
        System.out.println("Tipo de envio:");
        System.out.println("  1. Estandar");
        System.out.println("  2. Express");
        System.out.println("  3. Internacional");
        System.out.print("Opcion: ");
        String opTipo = tcl.nextLine().trim();
        String tipoEnvio;
        switch (opTipo) {
            case "2":
                tipoEnvio = "Express";
                break;
            case "3":
                tipoEnvio = "Internacional";
                break;
            default:
                tipoEnvio = "Estandar";
        }
        System.out.print("Direccion de entrega        : ");
        String direccion = tcl.nextLine().trim();
        paqueteService.registrarPaquete(idCliente, peso, tipoEnvio, direccion);
    }

    static void listarPaquetes() throws Exception {
        System.out.println("--- Todos los Paquetes ---");
        imprimirPaquetes(paqueteService.listarTodos());
    }

    static void listarPaquetesPorEstado(String estado) throws Exception {
        System.out.println("--- Paquetes [" + estado + "] ---");
        imprimirPaquetes(paqueteService.listarPorEstado(estado));
    }

    static void consultarPaquete() throws Exception {
        System.out.print("Numero de seguimiento: ");
        String num = tcl.nextLine().trim();
        Paquete p = paqueteService.buscarPorNumSeguimiento(num);
        if (p == null) {
            System.out.println("  Paquete no encontrado.");
            return;
        }
        System.out.println("\n  -- Detalle del Paquete ---------------------");
        System.out.println("  Seguimiento  : " + p.getNumSeguimiento());
        System.out.println("  Estado       : " + p.getEstadoAct());
        System.out.println("  Tipo envio   : " + p.getTipoEnvio());
        System.out.println("  Peso         : " + p.getPeso() + " kg");
        System.out.println("  Direccion    : " + p.getDireccionEntrega());
        System.out.println("  Cliente      : " + p.getClienteidCliente().getNombre()
                + " " + p.getClienteidCliente().getApellido());
        System.out.println("  Registrado   : " + formatFecha(p.getFechaRegistro()));
        System.out.println("  --------------------------------------------");
    }

    static void verHistorial() throws Exception {
        System.out.print("Numero de seguimiento: ");
        String num = tcl.nextLine().trim();
        List<Historialestado> historial = paqueteService.verHistorial(num);
        if (historial.isEmpty()) {
            System.out.println("  Sin historial.");
            return;
        }
        System.out.println("\n  Historial de movimientos: " + num);
        System.out.println("------------------------------------------------------------");
        System.out.printf("  %-18s %-22s %-15s%n", "Estado", "Fecha y Hora", "Registrado por");
        System.out.println("------------------------------------------------------------");
        for (Historialestado h : historial) {
            System.out.printf("  %-18s %-22s %-15s%n",
                    h.getEstado(),
                    formatFecha(h.getFechaCambio()),
                    h.getUsuarioidUsuario().getNombre() + " " + h.getUsuarioidUsuario().getApellido());
        }
    }

    // ================================================================
    // OPERACIONES - DESPACHO
    // ================================================================
    static void registrarDespacho() throws Exception {
        System.out.println("--- Registrar Despacho ---");
        listarPaquetesPorEstado(PaqueteServicio.ESTADO_REGISTRADO);
        System.out.print("\nNumero de seguimiento a despachar  : ");
        String num = tcl.nextLine().trim();
        System.out.print("Observaciones (Enter para omitir)  : ");
        String obs = tcl.nextLine().trim();
        despachoService.registrarDespacho(num, obs.isEmpty() ? null : obs);
    }

    // ================================================================
    // OPERACIONES - ENTREGA
    // ================================================================
    static void registrarEntrega() throws Exception {
        System.out.println("--- Registrar Entrega ---");
        listarPaquetesPorEstado(PaqueteServicio.ESTADO_EN_TRANSITO);
        System.out.print("\nNumero de seguimiento a entregar   : ");
        String num = tcl.nextLine().trim();
        System.out.print("Nombre de quien recibe             : ");
        String receptor = tcl.nextLine().trim();
        System.out.print("Observaciones (Enter para omitir)  : ");
        String obs = tcl.nextLine().trim();
        entregaService.registrarEntrega(num, receptor, obs.isEmpty() ? null : obs);
    }

    // ================================================================
    // OPERACIONES - USUARIOS
    // ================================================================
    static void registrarUsuario() throws Exception {
        System.out.println("--- Registrar Nuevo Usuario ---");
        System.out.print("Nombre     : ");
        String nombre = tcl.nextLine().trim();
        System.out.print("Apellido   : ");
        String apellido = tcl.nextLine().trim();
        System.out.print("Correo     : ");
        String correo = tcl.nextLine().trim();
        System.out.print("Contrasena : ");
        String contrasenia = tcl.nextLine().trim();
        System.out.println("Roles disponibles:");
        for (Rol r : rolService.listarTodos()) {
            System.out.println("  " + r.getIdRol() + ". " + r.getNomRol());
        }
        System.out.print("ID del rol : ");
        Integer idRol = leerEntero();
        usuarioService.registrarUsuario(nombre, apellido, correo, contrasenia, idRol);
    }

    static void listarUsuarios() throws Exception {
        System.out.println("--- Usuarios del Sistema ---");
        List<Usuario> lista = usuarioService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Sin usuarios.");
            return;
        }
        System.out.printf("%-6s %-22s %-25s %-15s%n", "ID", "Nombre Completo", "Correo", "Rol");
        System.out.println("------------------------------------------------------------------------");
        for (Usuario u : lista) {
            System.out.printf("%-6d %-22s %-25s %-15s%n",
                    u.getIdUsuario(),
                    u.getNombre() + " " + u.getApellido(),
                    u.getCorreo(),
                    u.getRolidRol().getNomRol());
        }
    }

    // ================================================================
    // UTILIDADES
    // ================================================================
    static void imprimirPaquetes(List<Paquete> lista) {
        if (lista.isEmpty()) {
            System.out.println("  Sin paquetes para mostrar.");
            return;
        }
        System.out.printf("%-22s %-15s %-14s %-8s %-18s%n",
                "Num. Seguimiento", "Estado", "Tipo Envio", "Peso", "Cliente");
        System.out.println("--------------------------------------------------------------------------------");
        for (Paquete p : lista) {
            System.out.printf("%-22s %-15s %-14s %-8s %-18s%n",
                    p.getNumSeguimiento(),
                    p.getEstadoAct(),
                    p.getTipoEnvio(),
                    p.getPeso() + " kg",
                    p.getClienteidCliente().getNombre() + " " + p.getClienteidCliente().getApellido());
        }
    }

    static Integer leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(tcl.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Ingrese un numero valido: ");
            }
        }
    }

    static BigDecimal leerDecimal() {
        while (true) {
            try {
                return new BigDecimal(tcl.nextLine().trim());
            } catch (Exception e) {
                System.out.print("  Ingrese un decimal valido (ej: 2.5): ");
            }
        }
    }

    static String formatFecha(java.util.Date fecha) {
        if (fecha == null) {
            return "-";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
    }
}
