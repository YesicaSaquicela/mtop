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
import org.mtop.modelo.dinamico.BussinesEntity;
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
    private String numRequisicion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRequisicion;
    private String tipoAdquisicion;
    private String observaciones;
    private String tipoRequisicion;

    @ManyToOne
    @JoinColumn(name = "vehiculoId1")
    private Vehiculo vehiculo;
    @OneToOne
    private PartidaContabilidad partidaContabilidad;
    @OneToMany(mappedBy = "requisicion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemRequisicion> listaItems = new ArrayList<ItemRequisicion>();

    @OneToOne
    private SolicitudReparacionMantenimiento solicitudReparacions;

////    
////    @ManyToOne
////    @JoinColumn(name = "personaId")
////    private Persona psolicita;
    @ManyToOne
    @JoinColumn(name = "kardexId")
    private Kardex kardex;
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public SolicitudReparacionMantenimiento getSolicitudReparacions() {
        return solicitudReparacions;
    }

    public void setSolicitudReparacions(SolicitudReparacionMantenimiento solicitudReparacions) {
        this.solicitudReparacions = solicitudReparacions;
    }
//    public Persona getPsolicita() {
//        return psolicita;
//    }
//
//    public void setPsolicita(Persona psolicita) {
//        this.psolicita = psolicita;
//    }
//  

    public List<ItemRequisicion> getListaItems() {
        return listaItems;
    }
   

    public void setListaItems(List<ItemRequisicion> listaItems) {
        for (ItemRequisicion itemRequisicion : listaItems) {
            itemRequisicion.setRequisicion(this);
        }
        this.listaItems = listaItems;
    }

    public PartidaContabilidad getPartidaContabilidad() {
        return partidaContabilidad;
    }

    public void setPartidaContabilidad(PartidaContabilidad partidaContabilidad) {
        this.partidaContabilidad = partidaContabilidad;
    }

    public String getNumRequisicion() {
        return numRequisicion;
    }

    public void setNumRequisicion(String numRequisicion) {
        this.numRequisicion = numRequisicion;
    }

    public String getTipoAdquisicion() {
        return tipoAdquisicion;
    }

    public void setTipoAdquisicion(String tipoAdquisicion) {
        this.tipoAdquisicion = tipoAdquisicion;
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
        return "org.mtop.modelo.Requisicion[ id=" + getId() + " ]";
    }

}
