/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.SerializationUtils;
import org.mtop.util.Utilidades;

/**
 *
 * @author carlis
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "Propiedad")
@NamedQueries(value = {
    @NamedQuery(name = "Propiedad.buscarPorId",
            query = "select t from Propiedad t where t.id=:id"),

    @NamedQuery(name = "Propiedad.buscarPorTipoAtributoId",
            query = "select t from Propiedad t where t.tipoAtributo.id=:id")

})
@DiscriminatorValue(value = "prk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Propiedad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String clasePadre;//clase a la que pertenece
    @Column(length = 100)
    @NotNull(message = "Es necesario un nombre de la propiedad")
    private String nombrePropiedad;
    @NotNull(message = "Es necesario un tipo de dato")
    private String tipoDato;
    @Transient
    private Object valorDefecto;
    private String etiquetaVista;
    private String textoAyuda;
    private Long posicion;
    private boolean requerido;
    private String convertidor;
    private String rutaVista;
    private String validador;
    private Boolean habilitarEstado;
    @Transient
    private List<String> listaValores;
    //private Map<String, Object> listaAux1 = new HashMap<String, Object>();
    private byte[] valorPersistencia;
    @ManyToOne
    @JoinColumn(name = "tipoAtributoId")
    private TipoAtributo tipoAtributo;
//    

    public String getClasePadre() {
        return clasePadre;
    }

    public Boolean getHabilitarEstado() {
        return habilitarEstado;
    }

    public void setHabilitarEstado(Boolean habilitarEstado) {
        this.habilitarEstado = habilitarEstado;
    }

    public TipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public void setClasePadre(String clasePadre) {
        this.clasePadre = clasePadre;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public void setNombrePropiedad(String nombrePropiedad) {
        this.nombrePropiedad = nombrePropiedad;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Object getValorDefecto() {
        if (getValorPersistencia() == null) {
            return null;
        } else {
            return SerializationUtils.deserialize(getValorPersistencia());
        }

    }

    public void setValorDefecto(Object valorDefecto) {
        setValorPersistencia(SerializationUtils.serialize((Serializable) getValorDefecto()));
        this.valorDefecto = valorDefecto;
    }

    public String getEtiquetaVista() {
        return etiquetaVista;
    }

    public void setEtiquetaVista(String etiquetaVista) {
        this.etiquetaVista = etiquetaVista;
    }

    public String getTextoAyuda() {
        return textoAyuda;
    }

    public void setTextoAyuda(String textoAyuda) {
        this.textoAyuda = textoAyuda;
    }

    public Long getPosicion() {
        return posicion;
    }

    public void setPosicion(Long posicion) {
        this.posicion = posicion;
    }

    public boolean isRequerido() {
        return requerido;
    }

    public void setRequerido(boolean requerido) {
        this.requerido = requerido;
    }

    public String getConvertidor() {
        return convertidor;
    }

    public void setConvertidor(String convertidor) {
        this.convertidor = convertidor;
    }

    public String getRutaVista() {
        return rutaVista;
    }

    public void setRutaVista(String rutaVista) {
        this.rutaVista = rutaVista;
    }

    public String getValidador() {
        return validador;
    }

    public void setValidador(String validador) {
        this.validador = validador;
    }

    public List<String> getListaValores() {
        //convertir de cadena a la lista a traves de un delimitador (,)
        return Utilidades.cadenaALista(getValorDefecto().toString());
    }

    public void setListaValores(List<String> listaValores) {
        this.listaValores = listaValores;
    }

    public byte[] getValorPersistencia() {
        return valorPersistencia;
    }

    public void setValorPersistencia(byte[] valorPersistencia) {
        this.valorPersistencia = valorPersistencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "org.mtop.model.Propiedad[ id=" + id + " ]";
    }

}
