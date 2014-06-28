/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.modelo;

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
import org.mtop.modelo.dinamico.BussinesEntity;

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "ItemSolicitudReparacion")
@DiscriminatorValue(value = "isk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class ItemSolicitudReparacion extends BussinesEntity implements Serializable{
    private String descripcionElementoRevisar;
    private String descripcionFalla;
   @ManyToOne()
    @JoinColumn(name = "solicitudReparacionId")
    private SolicitudReparacionMantenimiento solicitudReparacion;


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

    public SolicitudReparacionMantenimiento getSolicitudReparacion() {
        return solicitudReparacion;
    }

    public void setSolicitudReparacion(SolicitudReparacionMantenimiento solicitudReparacion) {
        this.solicitudReparacion = solicitudReparacion;
    }
    


    @Override
    public String toString() {
        return "org.mtop.model.ItemSolicicitudReparacion[ id=" + getId() + " ]";
    }
    
}
