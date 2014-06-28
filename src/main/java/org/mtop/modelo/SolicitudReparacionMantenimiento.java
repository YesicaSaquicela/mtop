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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.Persona;
import org.mtop.modelo.Vehiculo;

/**
 *
 * @author carlis
 */
@Entity
@Table(name = "SolicitudReparacion")
@DiscriminatorValue(value = "srk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase

public class SolicitudReparacionMantenimiento extends BussinesEntity implements Serializable {

    @Enumerated(EnumType.STRING)//anotacion de tipos de datos
    private SolicitudReparacionMantenimiento.datosEnumerador listaEnum;

    private String observacion;
    private boolean aprobado;
    private String numSolicitud;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaSolicitud;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntradaTaller;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaSalidaTaller;
 //   @ManyToOne
    // @JoinColumn(name = "personaId")
    // private Persona psolicita;
    private String recibidor;
//    @ManyToOne
//    @JoinColumn(name = "kardexId")
//    private Kardex kardex;
    @ManyToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;
    @OneToMany(mappedBy = "solicitudReparacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemSolicitudReparacion> listaItemSR = new ArrayList<ItemSolicitudReparacion>();
//    @OneToOne(mappedBy = "solicitudReparacions")
//    Requisicion requisicions;

//    public Requisicion getRequisicions() {
//        return requisicions;
//    }
//
//    public void setRequisicions(Requisicion requisicions) {
//        this.requisicions = requisicions;
//    }
    public Date getFechaEntradaTaller() {
        return fechaEntradaTaller;
    }

    public void setFechaEntradaTaller(Date fechaEntradaTaller) {
        this.fechaEntradaTaller = fechaEntradaTaller;
    }

    public Date getFechaSalidaTaller() {
        return fechaSalidaTaller;
    }

    public void setFechaSalidaTaller(Date fechaSalidaTaller) {
        this.fechaSalidaTaller = fechaSalidaTaller;
    }

    public String getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(String numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

//    public Persona getPsolicita() {
//        return psolicita;
//    }
//
//    public void setPsolicita(Persona psolicita) {
//        this.psolicita = psolicita;
//    }
    public String getRecibidor() {
        return recibidor;
    }

    public void setRecibidor(String recibidor) {
        this.recibidor = recibidor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<ItemSolicitudReparacion> getListaItemSR() {
       return listaItemSR;
   }

    public void setListaItemSR(List<ItemSolicitudReparacion> listaItemSR) {
        for (ItemSolicitudReparacion itemSolicicitudReparacion : listaItemSR) {
            itemSolicicitudReparacion.setSolicitudReparacion(this);

        }
        this.listaItemSR = listaItemSR;
    }

//    public Kardex getKardex() {
//        return kardex;
//    }
//
//    public void setKardex(Kardex kardex) {
//        this.kardex = kardex;
//    }
    public datosEnumerador getListaEnum() {
        return listaEnum;
    }

    public void setListaEnum(datosEnumerador listaEnum) {
        this.listaEnum = listaEnum;
    }

    @Override
    public String toString() {
        return "org.mtop.model.Solicitud[ id=" + getId() + " ]";
    }

    //enum tclase k representa tipos de datos enumerados
    public enum datosEnumerador {

        CHOFER1(0),
        CHOCHE2(1),
        CHOFER3(2);

        private int tipo;//para recorre datos 
        //constructor para inicializar y llamar

        private datosEnumerador(int tipo) {
            this.tipo = tipo;
        }

        //devolver la llave del tipo
        public int getTipo() {
            return tipo;
        }
    }

}
