/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.MapAttribute;
import org.mtop.model.EntidadAbstracta_;
import org.mtop.model.Persona;
import org.mtop.model.Persona_;
import org.mtop.model.Propiedad;
import org.mtop.model.Propiedad_;
import org.mtop.model.TipoAtributo;
import org.mtop.model.Vehiculo;
import org.mtop.model.Vehiculo_;

/**
 *
 * @author cesar
 */
@Stateless // son utilizados entre controladores para que estos metods pudan ser utilizados en otros controladores
public class ServicioGenerico {

    @Inject
    //EntityManeger para q los datos puedan ser mapeados
    private EntityManager em;
    
       public void crear(final Serializable entidad){
        try {
            em.persist(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear");
        }
    }
    
    public void actualizar(final Serializable entidad){
        try {
            em.merge(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }
    
    public void borrar(final Serializable entidad){
        try {
            em.remove(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }   


    public <T> T buscarPorId(final Class<T> type,final Long id) throws NoResultException {
        return em.find(type, id);
    }

    public <T> List<T> buscarTodos(final Class<T> objetoTipo) {
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        query.from(objetoTipo);
        return em.createQuery(query).getResultList();
    }
//
    @SuppressWarnings("unchecked")  //Sirva para suprimir advertencias al compilar al momento de crear metodos u operaciones genericas
    public <T> List<T> buscarPor_NamedQuery(final String namedQueryName, final Class<T> type, final Object... params) {
        Query query = em.createNamedQuery(namedQueryName, type);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")  //Sirva para suprimir advertencias al compilar al momento de crear metodos u operaciones genericas
    public <T> T buscarConParametrosPor_NamedQuery(final String namedQueryName, final Class<T> objetoTipo, final String clave, final Object valor) {
        Query query = em.createNamedQuery(namedQueryName, objetoTipo);
        query.setParameter(clave, valor);        
        return (T) query.getSingleResult();
    }
    /**
     * 
     * @param <T>  representa una clase java
     * @param objetoTipo parametro que representa la clase 
     * @param at   el atributo de la clase
     * @return 
     */
    
    public <T> List<T> buscarTodos(final Class<T> objetoTipo, String at) {
        System.out.println("1"+objetoTipo.toString()+"ll"+at.toString());
        CriteriaBuilder builder = em.getCriteriaBuilder();
        System.out.println("2");
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        System.out.println("3");
        Root<T> objeto= query.from(objetoTipo);
        System.out.println("4");
        query.where(builder.equal(objeto.get(at), true));
        System.out.println("5");
        return em.createQuery(query).getResultList();
    }
    public <T> List<T> buscarTodos(final Class<T> objetoTipo, String at,String nombretipo, Object valorTipo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        Root<T> objeto= query.from(objetoTipo);
        query.where(builder.equal(objeto.get(at), true), builder.and(builder.equal(objeto.get(nombretipo),valorTipo )));
        return em.createQuery(query).getResultList();
    }
    
    
    /**
     * 
     * @param <T> reprenta el objeto al que se quiere retornar 
     * @param objetoTipo nombre de la clase Persona.class
     * @param at nombre del estado activo e inactivo
     * @param nombreatributo nombre atributo 1
     * @param valoratributo valor atributo 1
     * @param nombreatributo2 nombre atributo 2
     * @param valoratributo2 valor atributo 2
     * @return busqueda por fechas
     */
     public <T> List<T> buscarTodos(final Class<T> objetoTipo, String nombreatributo,final Object valoratributo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        Root<T> objeto= query.from(objetoTipo);
        query.where(builder.equal(objeto.get(nombreatributo), valoratributo));
         System.out.println("resultado de liiista"+em.createQuery(query).getResultList());
         
        return em.createQuery(query).getResultList();
        //builder.equal(objeto.get(at), true)
    }
    
    public List<Persona> buscarTodos( boolean activo, String valorTipo, final Date f) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Persona> query = em.getCriteriaBuilder().createQuery(Persona.class);
        Root<Persona> objeto= query.from(Persona.class);
        query.where(builder.between(objeto.get(Persona_.apellido), valorTipo, valorTipo), builder.and(builder.equal(objeto.get(Persona_.tipo), valorTipo )));
        return em.createQuery(query).getResultList();
    }
    
    public List<Vehiculo> buscarVehiculos(){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehiculo> consulta= builder.createQuery(Vehiculo.class);
        Root<Vehiculo> objeto= consulta.from(Vehiculo.class);
        consulta.where(builder.equal(objeto.get(Vehiculo_.estado), true));
        return em.createQuery(consulta).getResultList();
    }

    public EntityManager getEm() {
        return em;
    }
    
}
