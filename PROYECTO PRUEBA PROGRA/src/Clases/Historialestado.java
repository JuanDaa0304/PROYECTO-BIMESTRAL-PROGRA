
package Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author A S U S
 */
@Entity
@Table(name = "historialestado")
@NamedQueries({
    @NamedQuery(name = "Historialestado.findAll", query = "SELECT h FROM Historialestado h"),
    @NamedQuery(name = "Historialestado.findByIdHistorial", query = "SELECT h FROM Historialestado h WHERE h.idHistorial = :idHistorial"),
    @NamedQuery(name = "Historialestado.findByEstado", query = "SELECT h FROM Historialestado h WHERE h.estado = :estado"),
    @NamedQuery(name = "Historialestado.findByFechaCambio", query = "SELECT h FROM Historialestado h WHERE h.fechaCambio = :fechaCambio")})
public class Historialestado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idHistorial")
    private Integer idHistorial;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "fechaCambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCambio;
    @JoinColumn(name = "Paquete_idPaquete", referencedColumnName = "idPaquete")
    @ManyToOne(optional = false)
    private Paquete paqueteidPaquete;
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;

    public Historialestado() {
    }

    public Historialestado(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Historialestado(Integer idHistorial, String estado, Date fechaCambio) {
        this.idHistorial = idHistorial;
        this.estado = estado;
        this.fechaCambio = fechaCambio;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Paquete getPaqueteidPaquete() {
        return paqueteidPaquete;
    }

    public void setPaqueteidPaquete(Paquete paqueteidPaquete) {
        this.paqueteidPaquete = paqueteidPaquete;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historialestado)) {
            return false;
        }
        Historialestado other = (Historialestado) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Historialestado[ idHistorial=" + idHistorial + " ]";
    }
    
}
