package Servicios;

/**
 * Valida que el usuario activo en sesión tenga el rol requerido
 * para ejecutar una operación. Se llama al inicio de cada método
 * de servicio que tenga restricción de rol.
 */
public class ValidarRol {

    // Roles del sistema
    public static final String RECEPCIONISTA = "Recepcionista";
    public static final String OPERADOR      = "Operador";
    public static final String REPARTIDOR    = "Repartidor";
    public static final String CLIENTE       = "Cliente";

    /**
     * Lanza excepción si el usuario en sesión NO tiene alguno de los roles permitidos.
     * @param rolesPermitidos uno o más roles que pueden ejecutar la operación
     */
    public static void validar(String... rolesPermitidos) throws Exception {
        if (!SesionActual.haySesion()) {
            throw new Exception("Acceso denegado. No hay ningún usuario en sesión.");
        }

        String rolActual = SesionActual.getRolActual();

        for (String rolPermitido : rolesPermitidos) {
            if (rolActual.equalsIgnoreCase(rolPermitido)) {
                return; // tiene permiso
            }
        }

        // Construir mensaje claro de error
        StringBuilder permitidos = new StringBuilder();
        for (int i = 0; i < rolesPermitidos.length; i++) {
            permitidos.append(rolesPermitidos[i]);
            if (i < rolesPermitidos.length - 1) permitidos.append(", ");
        }

        throw new Exception("Acceso denegado. Su rol es '" + rolActual
                + "'. Esta operación solo puede realizarla: " + permitidos + ".");
    }
}