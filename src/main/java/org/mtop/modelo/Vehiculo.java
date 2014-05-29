/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.mtop.model.BussinesEntity;
import org.mtop.model.profile.Profile;

/**
 *
 * @author carlis
 */
@Entity
@Table(name = "Vehiculo")
@DiscriminatorValue(value = "vk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Vehiculo extends BussinesEntity implements Serializable {

    private Integer anioFabricacion;
    private String cabina;
    private String color;
    private String clase;
    private String marca;
    private String modelo;
    private String placa;
    private String numRegistro;
    private Integer numPasajeros;
    private double peso;
//    @OneToOne
//    private Persona conductor;
    @OneToOne
    private Profile persona;
    //registro hace referencia al codigo heredado
    private String tipo;
    private String cilindraje;
    private String kilometraje;
    private String tipoCombustible;
//
//    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PlanMantenimiento> listaPlanMantenimiento= new ArrayList<PlanMantenimiento>();
//    @OneToMany(mappedBy = "vehiculo" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<EstadoVehiculo> listaEstados= new ArrayList<EstadoVehiculo>();
   
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

    public String getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(String cilindraje) {
        this.cilindraje = cilindraje;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }


   
    
    
//    public List<EstadoVehiculo> getListaEstados() {
//        return listaEstados;
//    }
//    
//    public void setListaEstados(List<EstadoVehiculo> listaEstados) {
//        for (EstadoVehiculo estadoVehiculo : listaEstados) {
//            estadoVehiculo.setVehiculo(this);
//        }
//        this.listaEstados = listaEstados;
//    }
//    //agregar una sola propiedad
//    public void agregarEstado(EstadoVehiculo estadoVehiculo){
//        if(!listaEstados.contains(estadoVehiculo)){
//            listaEstados.add(estadoVehiculo);
//        }
//    }
//    public List<PlanMantenimiento> getListaPlanMantenimiento() {
//        return listaPlanMantenimiento;
//    }
//
//    public void setListaPlanMantenimiento(List<PlanMantenimiento> listaPlanMantenimiento) {
//        for (PlanMantenimiento planMantenimiento : listaPlanMantenimiento) {
//            planMantenimiento.setVehiculo(this);
//            
//        }
//        this.listaPlanMantenimiento = listaPlanMantenimiento;
//    }
    public Integer getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(Integer anioFabricacion) {
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
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
        return "org.mtop.model.Vehiculo[ "
                + "id=" + getId() + ","
                + "marca" + getMarca() + ","
                + " ]";
    }

}
