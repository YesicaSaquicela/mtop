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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.mtop.modelo.dinamico.BussinesEntity;


/**
 *
 * @author carla
 * @author yesica
 * 
 */

@Entity
@DiscriminatorValue(value = "pk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Persona extends BussinesEntity implements Serializable {
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

    @ManyToOne
    @JoinColumn(name = "personaId")
    private SolicitudReparacionMantenimiento SolicitudRM=new SolicitudReparacionMantenimiento();
    
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

}