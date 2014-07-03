/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.apache.commons.lang.SerializationUtils;

/**
 *
 * @author carlis
 */
@SuppressWarnings("serial")
@Entity
public class ValorAtributo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nombre;
    private String tipo;
    @Transient
    private Object valor;
    private byte[] valorPersistencia;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    private Boolean estado;
    @ManyToOne
    @JoinColumn(name = "EntidadGeneralId")
    private EntidadGeneral entidadGeneral   ;

    @ManyToOne
    //JoinColum especifica una columna para unirse a una asociacion o entidad de elementos 
    @JoinColumn(name = "propiedadId")
    private Propiedad propiedad;

    public EntidadGeneral getEntidadGeneral() {
        return entidadGeneral;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setEntidadGeneral(EntidadGeneral entidadGeneral) {
        this.entidadGeneral = entidadGeneral;
    }

    public byte[] getValorPersistencia() {
        return valorPersistencia;
    }

    public void setValorPersistencia(byte[] valorPersistencia) {
        this.valorPersistencia = valorPersistencia;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        if (getValorPersistencia() == null) {
            return null;
        } else {
            return SerializationUtils.deserialize(getValorPersistencia());
        }
    }

    public void setValor(Object valor) {
        
        setValorPersistencia(SerializationUtils.serialize((Serializable) valor));
        this.valor = SerializationUtils.deserialize(getValorPersistencia());
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValorAtributo)) {
            return false;
        }
        ValorAtributo other = (ValorAtributo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mtop.model.ValorAtributo[ id=" + id
                + " " + getNombre()
                + " " + getTipo()
                + " " + getValor()
                + " " + getEntidadGeneral()
                + " ]";
    }

}
