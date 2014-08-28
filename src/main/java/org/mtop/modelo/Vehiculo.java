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
import javax.persistence.Column;
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
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.profile.Profile;

/**
 *
 * @author carlis
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
    private String cilindraje;
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
//    private List<String> listaTipos;
    private listaTipos listatipos;
 
    public Vehiculo() {
        listatipos=listaTipos.vehiculo;
        
    }
    
    public enum listaTipos{
        vehiculo(""),
        vehiculo1("");
        
        private final String tipos;

         private listaTipos(String tipos) {
            this.tipos = tipos;
        }
         
        public String getTipos() {
            return tipos;
        }
        
        
    }
    
    
//    @Enumerated(EnumType.STRING)//anotacion de tipos de datos
//    private Vehiculo.datosEnumerador listaTipose;
//    private List<String> listaTipos= new ArrayList<String>(){{
//    add("Bachadora");  add("Buses"); add("Camión");add("Cargadora");add("Carro Taller");add("Distribuidor de asfalto");
//    add("Distribuidor de aridos"); add("Escoba mecánica"); add("Excabadora");add("Fresadora");add("Gabarra");
//    add("Grua"); add("Retroexcavadora");add("Rodillo estático");add("Rodillo neumático");add("Rodillo vibratorio");add("Rodillo mixto");add("Seleccionadora");
//    add("Minicargadora"); add("Tanqueros");add("Trackdrill");add("Tractor segador");add("Tractor");
//    add("Mononiveladora");add("Trailer");add("Trituradora 1era");add("Trituradora 2da");add("Vehículo");add("Volquetes");
//    add("Planta asfaltica");add("Plataformas");
//    
//    
//    }};
//
//    
//        public enum datosEnumerador {
//
//        CHOFER1(0),
//        CHOCHE2(1),
//        CHOFER3(2);
//
//        private int tipo;//para recorre datos 
//        //constructor para inicializar y llamar
//
//        private datosEnumerador(int tipo) {
//            this.tipo = tipo;
//        }
//
//        //devolver la llave del tipo
//        public int getTipo() {
//            return tipo;
//        }
//    }
        
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

    public String getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(String cilindraje) {
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
