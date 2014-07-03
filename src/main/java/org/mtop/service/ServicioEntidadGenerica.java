/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.mtop.model.EntidadAbstracta_;
import org.mtop.model.Propiedad;
import org.mtop.model.Propiedad_;
import org.mtop.model.TipoAtributo;
import org.mtop.model.TipoAtributo_;

/**
 *
 * @author carlis
 */
@Stateless // son utilizados entre controladores para k estos metods pudan ser utilizados en otros controladores
public class ServicioEntidadGenerica {

    @Inject
    private EntityManager em;

    public TipoAtributo buscarPorNombre(String nombre) {
        try {
            TypedQuery<TipoAtributo> consulta = em.createNamedQuery("TipoAtributo.buscarPorNombre", TipoAtributo.class);
            consulta.setParameter("nombre", nombre);
            return consulta.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public TipoAtributo buscarPorNombre1(String nombre) {
        //lo mismo que el type del metodo anterior
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TipoAtributo> consulta= builder.createQuery(TipoAtributo.class);
        Root<TipoAtributo> objeto= consulta.from(TipoAtributo.class);
        consulta.where(builder.equal(objeto.get(TipoAtributo_.nombre), nombre));
        return em.createQuery(consulta).getSingleResult();
    }
    
    public List<Propiedad> buscarPorTipoAtributo(TipoAtributo ta){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Propiedad> consulta= builder.createQuery(Propiedad.class);
        Root<Propiedad> objeto= consulta.from(Propiedad.class);
        consulta.where(builder.equal(objeto.get(Propiedad_.tipoAtributo), ta));
        return em.createQuery(consulta).getResultList();
    }
    
     public List<Propiedad> buscarPorTipoAtributo1(TipoAtributo ta){
        try {
            TypedQuery<Propiedad> consulta = em.createNamedQuery("Propiedad.buscarPorTipoAtributoId", Propiedad.class);
            consulta.setParameter("id", ta.getId());
            return consulta.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
