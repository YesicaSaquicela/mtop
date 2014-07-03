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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author carlis
 */
@Entity
@Table(name = "TipoAtributo")
@NamedQueries(value = {
    @NamedQuery(name = "TipoAtributo.buscarPorId",
                query = "select t from TipoAtributo t where t.id=:id"),
    
    @NamedQuery(name = "TipoAtributo.buscarPorNombre",
                query = "select t from TipoAtributo t where t.nombre=:nombre")

})
//para hacer consultas
public class TipoAtributo extends EntidadAbstracta implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "tipoAtributo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Propiedad> listaPropiedades= new ArrayList<Propiedad>();
    //al momento de crear la vista crear un validador para que el nombre no se duplique 
    @Override
    public String toString() {
        return "org.mtop.model.TipoAtributo[ id=" + getId() + " ]";
    }

    public List<Propiedad> getListaPropiedades() {
        return listaPropiedades;
    }
    //fijar lista de propiedades
    public void setListaPropiedades(List<Propiedad> listaPropiedades) {
        for (Propiedad propiedad : listaPropiedades) {
            propiedad.setTipoAtributo(this);
        }
        this.listaPropiedades = listaPropiedades;
    }
    //agregar una sola propiedad
    public void agregarPropiedad(Propiedad propiedad){
        if(!listaPropiedades.contains(propiedad)){
            listaPropiedades.add(propiedad);
        }
    }
    

}
