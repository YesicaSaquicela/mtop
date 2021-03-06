/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.servicios;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.mtop.modelo.Auxiliar;
import org.mtop.modelo.AuxiliarParReq;
import org.mtop.modelo.EstadoVehiculo;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.SolicitudReparacionMantenimiento;


/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Stateless // son utilizados entre controladores para que estos metodos pudan ser utilizados en otros controladores
public class ServicioGenerico {

    //EntityManeger para q los datos puedan ser mapeados
    private EntityManager em;

    public void crear(final Serializable entidad) {
        try {
            em.persist(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear");
        }
    }

    public void actualizar(final Serializable entidad) {
        try {
            em.merge(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void borrar(final Serializable entidad) {
        try {
            em.remove(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public <T> T buscarPorId(final Class<T> type, final Long id) throws NoResultException {
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
     * @param <T> representa una clase java
     * @param objetoTipo parametro que representa la clase
     * @param at el atributo de la clase
     * @return
     */
    public <T> List<T> buscarTodos(final Class<T> objetoTipo, String at) {
        System.out.println("1" + objetoTipo.toString() + "ll" + at.toString());
        CriteriaBuilder builder = em.getCriteriaBuilder();
        System.out.println("2");
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        System.out.println("3");
        Root<T> objeto = query.from(objetoTipo);

        query.where(builder.equal(objeto.get(at), objeto.get(at)));
        System.out.println("5");
        return em.createQuery(query).getResultList();
    }

    public <T> List<T> buscarTodos(final Class<T> objetoTipo, String at, String nombretipo, Object valorTipo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        Root<T> objeto = query.from(objetoTipo);
        query.where(builder.equal(objeto.get(at), true), builder.and(builder.equal(objeto.get(nombretipo), valorTipo)));
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
    public <T> List<T> buscarTodos(final Class<T> objetoTipo, String nombreatributo, final Object valoratributo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);
        Root<T> objeto = query.from(objetoTipo);
        query.where(builder.equal(objeto.get(nombreatributo), valoratributo));
        System.out.println("resultado de liiista" + em.createQuery(query).getResultList());

        return em.createQuery(query).getResultList();
        //builder.equal(objeto.get(at), true)
    }

    /**
     *
     * @param <T>
     * @param objetoTipo NombreClase.class
     * @param tabla NombreClase.class.getSimpleName()
     * @param nombreatributo
     * @param valoratributo
     * @return
     */
    public <T> List<T> buscarTodoscoincidencia(final Class<T> objetoTipo, String tabla, String nombreatributo, final Object valoratributo) {
        System.out.println("entro a busacra "+valoratributo);
           
        TypedQuery<T> query = em.createQuery(
                "select e from " + tabla + " e where"
                + " lower(e." + nombreatributo + ") like lower(concat('%',:clave,'%')) ", objetoTipo);

        query.setParameter("clave", valoratributo);
        System.out.println("size en serivicio generico"+query.getResultList().size());
        for (Object object : query.getResultList()) {
            System.out.println("objeto"+object);
        }
//       
        return query.getResultList();

        //builder.equal(objeto.get(at), true)
    }
    
    
    public <T> List<T> find(final Class<T> objetoTipo, String criterio, int maxresults, int firstresult) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(objetoTipo);

        Root<T> objeto = query.from(objetoTipo);
        query.select(objeto).orderBy(builder.desc(objeto.get(criterio)));
        return getResultList(query, maxresults, firstresult);
    }

    protected <T> List<T> getResultList(final CriteriaQuery<T> query,
            int maxresults, int firstresult) {
        return em.createQuery(query).setMaxResults(maxresults)
                .setFirstResult(firstresult).getResultList();
    }
     public List<Auxiliar> buscarAuxiliarPorIdReq(String nombreatributo, final Object valoratributo) {
        List<Auxiliar> l = new ArrayList<Auxiliar>();
        String s = "";
        l.clear();
        for (Auxiliar t : buscarTodos(Auxiliar.class, nombreatributo)) {
          
            if (t.getRequisicionId().getId().equals((Long)valoratributo)) {
                System.out.println("anado un)ang fecha>>>>"+s);
                l.add(t);
            }
        }
        System.out.println("retornando>>>>>fechas>>>>"+l);
        return l;

        //builder.equal(objeto.get(at), true)
    }
     public List<AuxiliarParReq> buscarAuxiliarPorIdReqPa(String nombreatributo, final Object valoratributo) {
        List<AuxiliarParReq> l = new ArrayList<AuxiliarParReq>();
        String s = "";
        l.clear();
        for (AuxiliarParReq t : buscarTodos(AuxiliarParReq.class, nombreatributo)) {
          
            if (t.getRequisicionId().getId().equals((Long)valoratributo)) {
                System.out.println("anado un)ang req>>>>"+t);
                l.add(t);
            }
        }
        System.out.println("retornando>>>>>req>>>>"+l);
        return l;

        //builder.equal(objeto.get(at), true)
    }
     
      public List<Auxiliar> buscarAuxiliarPorIdSolicitud(String nombreatributo, final Object valoratributo) {
        List<Auxiliar> l = new ArrayList<Auxiliar>();
        String s = "";
        l.clear();
        for (Auxiliar t : buscarTodos(Auxiliar.class, nombreatributo)) {
          
            if (t.getSoliciudId().getId().equals((Long)valoratributo)) {
                System.out.println("anado un)ang fecha>>>>"+s);
                l.add(t);
            }
        }
        System.out.println("retornando>>>>>fechas>>>>"+l);
        return l;

    }
     
     public List<Auxiliar> buscarAuxiliarPorIdSol(String nombreatributo, final Object valoratributo) {
        List<Auxiliar> l = new ArrayList<Auxiliar>();
        
        l.clear();
        for (Auxiliar t : buscarTodos(Auxiliar.class, nombreatributo)) {
          
            if (t.getSoliciudId().getId().equals((Long)valoratributo)) {

                l.add(t);
            }
        }
        System.out.println("retornando>>>>>fechas>>>>"+l);
        return l;

        //builder.equal(objeto.get(at), true)
    }
    public List<Requisicion> buscarRequisicionporFecha(String nombreatributo, final Object valoratributo) {
        List<Requisicion> l = new ArrayList<Requisicion>();
        String s = "";
        l.clear();
        for (Requisicion t : buscarTodos(Requisicion.class, nombreatributo)) {

            s = t.getFechaRequisicion().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(valoratributo));
            if (s.contains(String.class.cast(valoratributo))) {
                System.out.println("anado una fecha>>>>"+s);
                l.add(t);
            }
        }
        System.out.println("retornando>>>>>fechas>>>>"+l);
        return l;

        //builder.equal(objeto.get(at), true)
    }
    

    public List<EstadoVehiculo> buscarEstadoVporFecha(String nombreatributo, final Object valoratributo) {
        List<EstadoVehiculo> l = new ArrayList<EstadoVehiculo>();
        String s = "";
        l.clear();
        for (EstadoVehiculo e : buscarTodos(EstadoVehiculo.class, nombreatributo)) {

            s = e.getFechaEntrada().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(valoratributo));
            if (s.contains(String.class.cast(valoratributo))) {
                l.add(e);
            }
        }

        return l;

        //builder.equal(objeto.get(at), true)
    }

    public List<SolicitudReparacionMantenimiento> buscarSolicitudporFecha(String nombreatributo, final Object valoratributo) {
        List<SolicitudReparacionMantenimiento> l = new ArrayList<SolicitudReparacionMantenimiento>();
        String s = "";
        l.clear();
        for (SolicitudReparacionMantenimiento t : buscarTodos(SolicitudReparacionMantenimiento.class, nombreatributo)) {

            s = t.getFechaSolicitud().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(valoratributo));
            if (s.contains(String.class.cast(valoratributo))) {
                System.out.println("anado una fecha>>>>"+s);
                l.add(t);
            }
        }
        System.out.println("retornando>>>>>fechas>>>>"+l);
        return l;

        //builder.equal(objeto.get(at), true)
    }

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    public List<PlanMantenimiento> buscarPlanMporFecha(String nombreatributo, final Object valoratributo) {
        List<PlanMantenimiento> l = new ArrayList<PlanMantenimiento>();
        String s = "";
        l.clear();
        for (PlanMantenimiento t : buscarTodos(PlanMantenimiento.class, nombreatributo)) {

            s = formato(t.getCreatedOn());
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(valoratributo));
            if (s.contains(String.class.cast(valoratributo))) {
                l.add(t);
            }
        }

        return l;

        //builder.equal(objeto.get(at), true)
    }

//    public List<Vehiculo> buscarTodos( boolean activo, String valorTipo, final Date f) {
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<Vehiculo> query = em.getCriteriaBuilder().createQuery(Persona.class);
//        Root<Vehiculo> objeto= query.from(Vehiculo.class);
//        query.where(builder.between(objeto.get(Vehiculo_.apellido), valorTipo, valorTipo), builder.and(builder.equal(objeto.get(Persona_.tipo), valorTipo )));
//        return em.createQuery(query).getResultList();
//    }
//    
//    public List<Vehiculo> buscarVehiculos(){
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<Vehiculo> consulta= builder.createQuery(Vehiculo.class);
//        Root<Vehiculo> objeto= consulta.from(Vehiculo.class);
//        consulta.where(builder.equal(objeto.get(Vehiculo_.estado), true));
//        return em.createQuery(consulta).getResultList();
//    }
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
