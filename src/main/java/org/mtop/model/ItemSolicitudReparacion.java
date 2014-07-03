/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "ItemSolicitudReparacion")
@DiscriminatorValue(value = "isk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class ItemSolicitudReparacion extends  EntidadGeneral implements Serializable {
    private String descripcionElementoRevisar;
    private String descripcionFalla;
    @ManyToOne()
    @JoinColumn(name = "solicitudReparacionId")
    private SolicitudReparacion solicitudReparacion;

    public String getDescripcionElementoRevisar() {
        return descripcionElementoRevisar;
    }

    public void setDescripcionElementoRevisar(String descripcionElementoRevisar) {
        this.descripcionElementoRevisar = descripcionElementoRevisar;
    }

    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        this.descripcionFalla = descripcionFalla;
    }

    public SolicitudReparacion getSolicitudReparacion() {
        return solicitudReparacion;
    }

    public void setSolicitudReparacion(SolicitudReparacion solicitudReparacion) {
        this.solicitudReparacion = solicitudReparacion;
    }
    
 


    @Override
    public String toString() {
        return "org.mtop.model.ItemSolicicitudReparacion[ id=" + getId() + " ]";
    }
    
}
