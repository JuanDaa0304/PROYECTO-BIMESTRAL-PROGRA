package Servicios;

import Clases.Rol;

/**
 * Se ejecuta UNA VEZ al arrancar el Main.
 * Verifica si los roles y el usuario administrador base existen en la BD.
 * Si no existen, los crea. Esto resuelve el problema de BD vacía en primer uso.
 */
public class InicializadorDatos {

    private RolServicio rolService;
    private UsuarioServicio usuarioService;

    public InicializadorDatos() {
        this.rolService     = new RolServicio();
        this.usuarioService = new UsuarioServicio();
    }

    public void inicializar() {
        System.out.println("--- Verificando datos base del sistema ---");

        inicializarRoles();
        inicializarUsuarioAdmin();

        System.out.println("--- Sistema listo ---\n");
    }

    // ---------------------------------------------------------------
    // Crea los 4 roles si no existen
    // ---------------------------------------------------------------
    private void inicializarRoles() {
        String[] roles = {
            ValidarRol.RECEPCIONISTA,
            ValidarRol.OPERADOR,
            ValidarRol.REPARTIDOR,
            ValidarRol.CLIENTE
        };

        for (String nomRol : roles) {
            try {
                Rol existente = rolService.buscarPorNombre(nomRol);
                if (existente == null) {
                    rolService.crear(nomRol);
                    System.out.println(" Rol creado: " + nomRol);
                }
            } catch (Exception e) {
                System.out.println(" Error creando rol " + nomRol + ": " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // Crea un usuario Recepcionista por defecto para primer acceso
    // ---------------------------------------------------------------
    private void inicializarUsuarioAdmin() {
        try {
            // Si ya existe al menos un usuario no hace nada
            if (!usuarioService.listarTodos().isEmpty()) {
                return;
            }

            Rol rolRecepcionista = rolService.buscarPorNombre(ValidarRol.RECEPCIONISTA);
            if (rolRecepcionista == null) return;

            usuarioService.registrarUsuario(
                "Admin",
                "Sistema",
                "admin@sistema.com",
                "admin123",
                rolRecepcionista.getIdRol()
            );
            System.out.println(" Usuario base creado: admin@sistema.com / admin123");
            System.out.println("  (Cambie esta contrasenia en produccion)");

        } catch (Exception e) {
            System.out.println(" Error creando usuario base: " + e.getMessage());
        }
    }
}