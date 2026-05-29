package Servicios;

import Clases.Rol;
import Clases.Usuario;
import Controladores.RolJpaController;
import Controladores.UsuarioJpaController;
import Controladores.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UsuarioServicio {

    private static final String PERSISTENCE_UNIT = "PROYECTO_PRUEBA_PROGRAPU";
    private EntityManagerFactory emf;
    private UsuarioJpaController usuarioController;
    private RolJpaController rolController;

    public UsuarioServicio() {
        this.emf               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.usuarioController = new UsuarioJpaController(emf);
        this.rolController     = new RolJpaController(emf);
    }

    // ---------------------------------------------------------------
    // LOGIN — sin restricción de rol (es previo al login)
    // ---------------------------------------------------------------
    public Usuario login(String correo, String contrasenia) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo no puede estar vacio.");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new Exception("La contrasenia no puede estar vacia.");
        }
        List<Usuario> todos = usuarioController.findUsuarioEntities();
        for (Usuario u : todos) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())
                    && u.getContrasenia().equals(contrasenia)) {
                System.out.println(" Bienvenido, " + u.getNombre()
                        + " [" + u.getRolidRol().getNomRol() + "]");
                return u;
            }
        }
        throw new Exception("Correo o contrasenia incorrectos.");
    }

    // ---------------------------------------------------------------
    // REGISTRAR USUARIO — solo Recepcionista (para crear otros usuarios)
    // El InicializadorDatos también lo usa internamente sin sesión activa
    // ---------------------------------------------------------------
    public void registrarUsuario(String nombre, String apellido, String correo,
                                  String contrasenia, Integer idRol) throws Exception {

        // Si hay sesión activa, solo Recepcionista puede registrar usuarios
        if (SesionActual.haySesion()) {
            ValidarRol.validar(ValidarRol.RECEPCIONISTA);
        }

        // Validar correo único
        for (Usuario u : usuarioController.findUsuarioEntities()) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                throw new Exception("Ya existe un usuario con el correo: " + correo);
            }
        }

        Rol rol = rolController.findRol(idRol);
        if (rol == null) {
            throw new Exception("El rol con ID " + idRol + " no existe.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setApellido(apellido.trim());
        usuario.setCorreo(correo.trim().toLowerCase());
        usuario.setContrasenia(contrasenia);
        usuario.setRolidRol(rol);

        usuarioController.create(usuario);
        System.out.println(" Usuario registrado: " + nombre + " con rol " + rol.getNomRol());
    }

    // ---------------------------------------------------------------
    // LISTAR — solo Recepcionista
    // ---------------------------------------------------------------
    public List<Usuario> listarTodos() throws Exception {
        // Permitir sin sesión para el InicializadorDatos
        if (SesionActual.haySesion()) {
            ValidarRol.validar(ValidarRol.RECEPCIONISTA);
        }
        return usuarioController.findUsuarioEntities();
    }

    // ---------------------------------------------------------------
    // BUSCAR POR ID — Recepcionista
    // ---------------------------------------------------------------
    public Usuario buscarPorId(Integer idUsuario) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);
        return usuarioController.findUsuario(idUsuario);
    }

    // ---------------------------------------------------------------
    // ELIMINAR — solo Recepcionista
    // ---------------------------------------------------------------
    public void eliminarUsuario(Integer idUsuario) throws Exception {
        ValidarRol.validar(ValidarRol.RECEPCIONISTA);

        // No puede eliminarse a sí mismo
        if (SesionActual.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new Exception("No puede eliminar su propio usuario mientras tiene sesion activa.");
        }

        Usuario usuario = usuarioController.findUsuario(idUsuario);
        if (usuario == null) {
            throw new NonexistentEntityException("No existe usuario con ID: " + idUsuario);
        }
        usuarioController.destroy(idUsuario);
        System.out.println("Usuario eliminado.");
    }
}