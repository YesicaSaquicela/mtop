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
import javax.validation.constraints.NotNull;
import org.mtop.model.BussinesEntity;

/**
 *
 * @author jesica
 */
@SuppressWarnings("serial") 
@Entity
@Table(name = "ItemRequisicion")
@DiscriminatorValue(value = "irk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class ItemRequisicion extends BussinesEntity implements Serializable{
    
    
    @NotNull
    private Integer cantidad;
    @NotNull
    private String unidadMedida;
//    @ManyToOne
//    @JoinColumn(name = "requisicionId")
//    public Requisicion requisicion;
   
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    
    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
   //
//    public Requisicion getRequisicion() {
//        return requisicion;
//    }
//
//    public void setRequisicion(Requisicion requisicion) {
//        this.requisicion = requisicion;
//    }


    @Override
    public String toString() {
        return "org.mtop.model.ItemRequisicion[ id=" + getId() + " ]";
    }
    
}