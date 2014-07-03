/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author carlis
 */
@Entity
@DiscriminatorValue(value = "pk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Persona extends EntidadGeneral implements Serializable {
//no existe constructor

//    @NotNull 
//    @Size(min = 6, max = 20)  // tama;o
//    @Pattern(regexp = "[^0-9]*", message = "Ingrese solo letras")
//    @Column(length = 50)  //
    private String apellido;
    private String cedula;
    private String numTelefono;
    private String correo;
    private Integer Edad;
    private String tipo;

//    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SolicitudReparacion> listaSolicitud=new ArrayList<SolicitudReparacion>();
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public Integer getEdad() {
        return Edad;
    }

    public void setEdad(Integer Edad) {
        this.Edad = Edad;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "org.demoee6.model.Persona[ id=" + getId() + " ]";
    }

//    public void agregarSolicitante(SolicitudReparacion solc){
//        if(!this.listaSolicitud.contains(solc)){
//            solc.setSolicitante(this);
//            listaSolicitud.add(solc);
//           }
//        
//    }
//    public void agregarRecibidor(SolicitudReparacion solc){
//        if(!this.listaSolicitud.contains(solc)){
//            solc.setRecibidor(this);
//            listaSolicitud.add(solc);
//           }
//        
//    }
}