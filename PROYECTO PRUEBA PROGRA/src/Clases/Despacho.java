/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
@Table(name = "despacho")
@NamedQueries({
    @NamedQuery(name = "Despacho.findAll", query = "SELECT d FROM Despacho d"),
    @NamedQuery(name = "Despacho.findByIdDespacho", query = "SELECT d FROM Despacho d WHERE d.idDespacho = :idDespacho"),
    @NamedQuery(name = "Despacho.findByFechaDespacho", query = "SELECT d FROM Despacho d WHERE d.fechaDespacho = :fechaDespacho"),
    @NamedQuery(name = "Despacho.findByObservaciones", query = "SELECT d FROM Despacho d WHERE d.observaciones = :observaciones")})
public class Despacho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDespacho")
    private Integer idDespacho;
    @Basic(optional = false)
    @Column(name = "fechaDespacho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDespacho;
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "Paquete_idPaquete", referencedColumnName = "idPaquete")
    @ManyToOne(optional = false)
    private Paquete paqueteidPaquete;
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;

    public Despacho() {
    }

    public Despacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Despacho(Integer idDespacho, Date fechaDespacho) {
        this.idDespacho = idDespacho;
        this.fechaDespacho = fechaDespacho;
    }

    public Integer getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        hash += (idDespacho != null ? idDespacho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despacho)) {
            return false;
        }
        Despacho other = (Despacho) object;
        if ((this.idDespacho == null && other.idDespacho != null) || (this.idDespacho != null && !this.idDespacho.equals(other.idDespacho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Despacho[ idDespacho=" + idDespacho + " ]";
    }
    
}
