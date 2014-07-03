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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.util.Utilidades;

/**
 *
 * @author carlis
 */
@Entity
@Table(name = "EntidadGeneral")
@Inheritance(strategy = InheritanceType.JOINED)//solo en la clase padre solo y el tipo de estragia es joined
@DiscriminatorColumn(name = "ENTITY_TYPE", discriminatorType = DiscriminatorType.STRING, length = 2)
public class EntidadGeneral extends EntidadAbstracta implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
  //@Column(name = "TipoAtributoId")

    @JoinColumn(name = "tipoAtributoId")
    private TipoAtributo tipoAtributo;
    @OneToMany(mappedBy = "entidadGeneral", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValorAtributo> listaValorAtributo = new ArrayList<ValorAtributo>();

    public TipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public List<ValorAtributo> getListaValorAtributo() {
//        for (ValorAtributo valorAtributo : listaValorAtributo) {
//            if (valorAtributo.getEntidadGeneral().equals(this)) {
//                listaValorAtributo.add(valorAtributo);
//                System.out.println("Entro");
//            }
//        }
//        System.out.println("RETORNANDO >>>>>>>>>>>>>>"+ listaValorAtributo);
        return listaValorAtributo;
    }
//a cada atributo de la lista se fija el id de la entidad relacionada

    public void setListaValorAtributo(List<ValorAtributo> listaValorAtributo) {
        for (ValorAtributo valorAtributo : listaValorAtributo) {
            valorAtributo.setEntidadGeneral(this);
        }
        this.listaValorAtributo = listaValorAtributo;

    }

    //agregar solo un atributo a la lista de atributos
    public void agregarValorAtributo(ValorAtributo vAtributo) {
        if (!listaValorAtributo.contains(vAtributo)) {
            listaValorAtributo.add(vAtributo);

            System.out.println("agrego valor atributo a la lista");
        }

    }

    //obtener un solo atributo de la lista de atriibutos
    public ValorAtributo obtenetValorAtributo(String nombre) {
        ValorAtributo vAtributo = null;
        for (ValorAtributo valorAtributo : listaValorAtributo) {
            if (valorAtributo.getNombre().equals(nombre)) {
                vAtributo = valorAtributo;
            }
        }
        return vAtributo;
    }

    public void construirAtributos(ServicioEntidadGenerica seg) {
        if (getTipoAtributo() == null) {
            //sale automaticamente del metodo
            return;

        }
        for (Propiedad p : getTipoAtributo().getListaPropiedades()) {
            //los tipos de datos para los atributos serian org.mtop.model.TipoAtributo
            if (p.getTipoDato().equals(TipoAtributo.class.getName())) {
                cargarAtributos(p, seg);
            } else {
                this.agregarValorAtributo(construirAtributo(p));
            }
        }
    }

    //cargar de TipoAtributo y Propiedad a ValorAtributo
    public void cargarAtributos(Propiedad p, ServicioEntidadGenerica seg) {
        //pendiente
        System.out.println("propiedad creada " + p.toString());
        TipoAtributo ta = seg.buscarPorNombre1(p.getNombrePropiedad());
        
        if (getTipoAtributo() == null) {
            //sale automaticamente del metodo
            return;
        }
        System.out.println("Tipo Atributo " + ta.getListaPropiedades().toString());        
        for (Propiedad p1 : ta.getListaPropiedades()) {
            //los tipos de datos para los atributos serian org.mtop.model.TipoAtributo                           
            this.getListaValorAtributo().add(construirAtributo(p1));
        }
    }
    //cargar los atributos de propiedades 

    public ValorAtributo construirAtributo(Propiedad p) {
        ValorAtributo va = new ValorAtributo();
        va.setNombre(p.getNombrePropiedad());
        va.setTipo(p.getTipoDato());
        
        if ("java.lang.String[]".equals(p.getTipoDato())) {
            va.setValor((Serializable) Utilidades.buscarValorDefecto(p.getValorDefecto().toString()));
        } else {
            va.setValor((Serializable) p.getValorDefecto());
        }
        va.setPropiedad(p);
        System.out.println("propiedad creada " + va.toString());
        return va;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntidadGeneral)) {
            return false;
        }
        EntidadGeneral other = (EntidadGeneral) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mtop.model.EntidadGeneral[ id=" + getId() + " ]";
    }

}
