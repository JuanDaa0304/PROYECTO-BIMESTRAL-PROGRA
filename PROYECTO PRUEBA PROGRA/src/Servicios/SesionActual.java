package Servicios;

import Clases.Usuario;

/**
 * Guarda el usuario que inició sesión durante la ejecución del programa.
 * Se usa como "contexto global" para saber qué rol tiene el usuario activo.
 */
public class SesionActual {

    private static Usuario usuarioActivo = null;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActivo = usuario;
    }

    public static void cerrarSesion() {
        usuarioActivo = null;
        System.out.println("Sesion cerrada.");
    }

    public static Usuario getUsuario() {
        return usuarioActivo;
    }

    public static boolean haySesion() {
        return usuarioActivo != null;
    }

    public static String getRolActual() {
        if (usuarioActivo == null) return null;
        return usuarioActivo.getRolidRol().getNomRol();
    }

    // Verifica si el usuario activo tiene el rol indicado
    public static boolean tieneRol(String rol) {
        if (usuarioActivo == null) return false;
        return usuarioActivo.getRolidRol().getNomRol().equalsIgnoreCase(rol);
    }
}