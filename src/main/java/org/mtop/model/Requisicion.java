/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.model;

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

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "Requisicion")
@DiscriminatorValue(value = "rk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Requisicion extends EntidadGeneral implements Serializable {
    @OneToOne
    private SolicitudReparacion solicitudReparacions;
    

//  @Pattern(regexp = "[^0-9]*", message = "Ingrese solo letras")
    private String observaciones;
    
    @OneToMany(mappedBy = "requisicion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemRequisicion> listaItems = new ArrayList<ItemRequisicion>();
    
    @OneToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;
    @ManyToOne
    @JoinColumn(name = "personaId")
    private Persona psolicita;
    @ManyToOne
    @JoinColumn(name = "kardexId")
    private Kardex kardex;
    private boolean aprobado;
    private Integer numRequisicion;
    private String tipoAdquisicion;
    private String partida;

    public Kardex getKardex() {
        return kardex;
    }

    public SolicitudReparacion getSolicitudReparacions() {
        return solicitudReparacions;
    }

    public void setSolicitudReparacions(SolicitudReparacion solicitudReparacions) {
        this.solicitudReparacions = solicitudReparacions;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public Persona getPsolicita() {
        return psolicita;
    }

    public void setPsolicita(Persona psolicita) {
        this.psolicita = psolicita;
    }
    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }
    
    
    
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public String getTipoAdquisicion() {
        return tipoAdquisicion;
    }

    public void setTipoAdquisicion(String tipoAdquisicion) {
        this.tipoAdquisicion = tipoAdquisicion;
    }
    
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
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
    
    public List<ItemRequisicion> getListaItems() {
        return listaItems;
    }
    
    public void setListaItems(List<ItemRequisicion> listaItems) {
        for (ItemRequisicion itemRequisicion : listaItems) {
            itemRequisicion.setRequisicion(this);
        }
        this.listaItems= listaItems;
    }

    

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "org.mtop.model.Requisicion[ id=" + getId() + " ]";
    }

}
