/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.mtop.model.BussinesEntity;
import org.mtop.modelo.Persona;
import org.mtop.modelo.Vehiculo;

/**
 *
 * @author jesica
 */
@Entity
@Table(name = "Requisicion")
@DiscriminatorValue(value = "rk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Requisicion extends BussinesEntity implements Serializable {

    private boolean aprobado;
    private Integer numRequisicion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRequisicion;
    private String tipoAdquisicion;
    private String partida;
    private String observaciones;
    private String tipoRequisicion;
    
    //    @OneToOne
//    private SolicitudReparacionMantenimiento solicitudReparacions;

//    @OneToMany(mappedBy = "requisicion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ItemRequisicion> listaItems = new ArrayList<ItemRequisicion>();
//    
//    @OneToOne
//    @JoinColumn(name = "vehiculoId")
//    private Vehiculo vehiculo;
//    @ManyToOne
//    @JoinColumn(name = "personaId")
//    private Persona psolicita;
//    @ManyToOne
//    @JoinColumn(name = "kardexId")
//    private Kardex kardex;

//    public Kardex getKardex() {
//        return kardex;
//    }
//
//    public SolicitudReparacionMantenimiento getSolicitudReparacions() {
//        return solicitudReparacions;
//    }
//
//    public void setSolicitudReparacions(SolicitudReparacionMantenimiento solicitudReparacions) {
//        this.solicitudReparacions = solicitudReparacions;
//    }
//
//    public void setKardex(Kardex kardex) {
//        this.kardex = kardex;
//    }
//
//    public Persona getPsolicita() {
//        return psolicita;
//    }
//
//    public void setPsolicita(Persona psolicita) {
//        this.psolicita = psolicita;
//    }

 

//    public Vehiculo getVehiculo() {
//        return vehiculo;
//    }
//        public void setVehiculo(Vehiculo vehiculo) {
//        this.vehiculo = vehiculo;
//    }

    //    public List<ItemRequisicion> getListaItems() {
//        return listaItems;
//    }
//
//    public void setListaItems(List<ItemRequisicion> listaItems) {
//        for (ItemRequisicion itemRequisicion : listaItems) {
//            itemRequisicion.setRequisicion(this);
//        }
//        this.listaItems = listaItems;
//    }
    
       public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }
    
    public String getTipoAdquisicion() {
        return tipoAdquisicion;
    }

    public void setTipoAdquisicion(String tipoAdquisicion) {
        this.tipoAdquisicion = tipoAdquisicion;
    }



    public Integer getNumRequisicion() {
        return numRequisicion;
    }

    public void setNumRequisicion(Integer numRequisicion) {
        this.numRequisicion = numRequisicion;
    }

    public boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRequisicion() {
        return fechaRequisicion;
    }

    public void setFechaRequisicion(Date fechaRequisicion) {
        this.fechaRequisicion = fechaRequisicion;
    }

    public String getTipoRequisicion() {
        return tipoRequisicion;
    }

    public void setTipoRequisicion(String tipoRequisicion) {
        this.tipoRequisicion = tipoRequisicion;
    }
    
       

    @Override
    public String toString() {
        return "org.mtop.model.Requisicion[ id=" + getId() + " ]";
    }

}
