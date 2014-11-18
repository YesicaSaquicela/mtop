/*
    SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
    VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
    Copyright (C) 2014  Romero Carla, Saquicela Yesica

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.profile.Profile;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Entity
@Table(name = "Vehiculo")
@DiscriminatorValue(value = "vk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Vehiculo extends BussinesEntity implements Serializable {

//    @Pattern(regexp = "[0-9]+", message = "No se admiten letras")
    private String anioFabricacion;
    private String cabina;
    private String color;
    private String clase;
    private String marca;
    private String modelo;
    @Column(unique = true)
    private String placa;
//    @Pattern(regexp = "[0-9-]+", message = "No se admiten letras.")
    @Column(unique = true)
    private String numRegistro;
    private Integer numPasajeros;
    private Double peso;
    @OneToOne
    private Profile persona;
    //registro hace referencia al codigo heredado
    private String tipo;
    private Double cilindraje;

    private Integer kilometraje;
    private String tipoCombustible;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechainiciosoat;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechafinsoat;
    @ManyToOne
    @JoinColumn(name = "planMantenimientoId")
    private PlanMantenimiento planM;
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstadoVehiculo> listaEstados = new ArrayList<EstadoVehiculo>();

    private String traccion;
    private String numCilindros;
    private Double avaluo;

    public Double getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(Double avaluo) {
        this.avaluo = avaluo;
    }
    
    

    public String getTraccion() {
        return traccion;
    }

    public void setTraccion(String traccion) {
        this.traccion = traccion;
    }

    public String getNumCilindros() {
        return numCilindros;
    }

    public void setNumCilindros(String numCilindros) {
        this.numCilindros = numCilindros;
    }


   
    public Date getFechainiciosoat() {
        return fechainiciosoat;
    }

    public void setFechainiciosoat(Date fechainiciosoat) {
        this.fechainiciosoat = fechainiciosoat;
    }

    public Date getFechafinsoat() {
        return fechafinsoat;
    }

    public void setFechafinsoat(Date fechafinsoat) {
        this.fechafinsoat = fechafinsoat;
    }

    public PlanMantenimiento getPlanM() {
        return planM;
    }

    public void setPlanM(PlanMantenimiento planM) {
        this.planM = planM;
    }

    public Profile getPersona() {
        return persona;
    }

    public void setPersona(Profile persona) {
        this.persona = persona;
    }

    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Double getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(Double cilindraje) {
        this.cilindraje = cilindraje;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public List<EstadoVehiculo> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<EstadoVehiculo> listaEstados) {
        for (EstadoVehiculo estadoVehiculo : listaEstados) {
            
            estadoVehiculo.setVehiculo(this);
        }
        this.listaEstados = listaEstados;
    }

    public String getAnioFabricacion() {

        return anioFabricacion;
    }

    public void setAnioFabricacion(String anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public String getCabina() {
        return cabina;
    }

    public void setCabina(String cabina) {
        this.cabina = cabina;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getNumPasajeros() {
        return numPasajeros;
    }

    public void setNumPasajeros(Integer numPasajeros) {
        this.numPasajeros = numPasajeros;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.Vehiculo[ "
                + "id=" + getId() + ","
                + "marca" + getMarca() + ","
                + " ]";
    }

}
