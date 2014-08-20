/*
 * Copyright 2014 jesica.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mtop.controlador;

import com.itextpdf.text.DocumentException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.ActividadPlanMantenimiento_;
import org.mtop.modelo.EstadoVehiculo;
import static org.mtop.modelo.EstadoVehiculo_.vehiculo;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;
import org.mtop.genreporte.GeneradorPdf;
import org.mtop.modelo.profile.Profile;
/**
 *
 * @author jesica
 */
@Named
@ViewScoped
public class ControladorVehiculo extends BussinesEntityHome<Vehiculo> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @EJB
    private ServicioGenerico servgen;
    List<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();
    private String numeroRegistro;
    private ActividadPlanMantenimiento actividadplan;
    private ControladorKardex ck;
    private String mensaje = "";
    private ControladorPlanMantenimiento cplanMantenimiento;
    private String palabrab = "";
    private EstadoVehiculo estado = new EstadoVehiculo();
    private ControladorEstadoVehiculo cev;
    private Date fechaFinal=Calendar.getInstance().getTime();
    private long idPersona;

    public long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(long idPersona) {
        for (Profile persona : findAll(Profile.class)) {
            if(persona.getTipo().equals("Conductor")){
                getInstance().setPersona(persona);
            }
        }
        this.idPersona = idPersona;
    }
    
    
    
    public Date getFechaFinal() {
       
        fechaFinal = getInstance().getListaEstados().get(getInstance().getListaEstados().size() - 1).getFechaEntrada();
       // calendario=Calendar.class.cast(fechaFinal);
//        calendario.setTime(fechaFinal);
        System.out.println("fecha final" + fechaFinal);
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public ControladorEstadoVehiculo getCev() {
        return cev;
    }

    public void setCev(ControladorEstadoVehiculo cev) {
        this.cev = cev;
    }

    public String obtenerUltimoEstadoV(Vehiculo vehiculo) {
        String estado = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getNombre();
        return estado;
    }

    public String obtenerUltimaFechaV(Vehiculo vehiculo) {
        String fechaFormato = "";
        System.out.println("lista de vehiculos" + listaVehiculos);
        System.out.println("vehiculooooo"+vehiculo);
        Date fEntrada = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getFechaEntrada();

        if (fEntrada != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fEntrada);
        }
        return fechaFormato;
    }

    public String obtenerUltimaUbicacionV(Vehiculo vehiculo) {
        String ubicacion = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getUbicacion();
        return ubicacion;
    }

    public ServicioGenerico getServgen() {
        return servgen;
    }

    public void setServgen(ServicioGenerico servgen) {
        this.servgen = servgen;
    }
    

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public void buscar() {

        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia descripciion
        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), palabrab);
        //buscando por codigo
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), palabrab);
//        List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);

        for (Vehiculo vehiculo : lc) {
            if (!lv.contains(vehiculo)) {
                lv.add(vehiculo);
            }
        }

//        for (Vehiculo vehiculo : lk) {
//            if (!lv.contains(vehiculo)) {
//                lv.add(vehiculo);
//            }
//        }
        if (lv.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaVehiculos = lv;
        }

    }

    public void limpiar() {
        palabrab = "";
        List<Vehiculo> lv = servgen.buscarTodos(Vehiculo.class);
        listaVehiculos.clear();
        System.out.println("lppp" + lv);

        for (Vehiculo vehiculo : lv) {
            System.out.println("iddddd" + vehiculo.getId());
            System.out.println("entro a for lista>>>>" + vehiculo.isEstado());
            if (vehiculo.isEstado()) {
                System.out.println("listatesssa" + listaVehiculos);
                listaVehiculos.add(vehiculo);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + listaVehiculos);

            }

        }
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), query);

        for (Vehiculo vehiculo : lv) {
            System.out.println("econtro uno " + vehiculo.getNumRegistro());
            ced.add(vehiculo.getNumRegistro());
        }
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), query);
        for (Vehiculo vehiculo : lc) {
            System.out.println("econtro uno " + vehiculo.getPlaca());
            ced.add(vehiculo.getPlaca());
        }

//         List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);
//          for (Vehiculo vehiculo : lk) {
//            System.out.println("econtro uno " + vehiculo.getPlaca());
//            String cad=vehiculo.getKilometraje().toString();
//            ced.add(cad);
//        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscarEstadoV() {
        palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia descripciion
        List<Vehiculo> lv1 = new ArrayList<Vehiculo>();
        for (Vehiculo vehiculo : listaVehiculos) {
            EstadoVehiculo ev = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1);
            if (ev.getNombre().contains(palabrab)) {
                lv1.add(vehiculo);
            }
            if (ev.getUbicacion().contains(palabrab)) {

                lv1.add(vehiculo);
            }
            String s = ev.getFechaEntrada().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            if (s.contains(palabrab)) {
                lv1.add(vehiculo);
            }

        }

        if (lv1.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaVehiculos = lv1;
        }

    }

    public ArrayList<String> autocompletarEstadoV(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (Vehiculo vehiculo : listaVehiculos) {
            EstadoVehiculo ev = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1);
            if (ev.getNombre().contains(query)) {
                ced.add(ev.getNombre());

            }
            if (ev.getUbicacion().contains(query)) {
                ced.add(ev.getUbicacion());
            }
            String s = ev.getFechaEntrada().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            if (s.contains(query)) {
                ced.add(ev.getFechaEntrada().toString());
            }
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public boolean verificarPlan() {
        boolean ban = false;
        for (PlanMantenimiento pm : findAll(PlanMantenimiento.class)) {
            if (pm.getActivado()) {
                ban = true;
            } else {
                ban = false;
            }
        }
        return ban;
    }

    public void vizualizarActividades(Long vehiculoid) {
        System.out.println("entro a vizualiza>>>>>>>." + vehiculoid);
        Vehiculo v = findById(Vehiculo.class, vehiculoid);
        Integer proKilometraje = obtenerKilometraje(v.getKilometraje());
        System.out.println("prokilooooooooo" + proKilometraje);
        actividadplan = new ActividadPlanMantenimiento();

        System.out.println("vehiculo iddddddd" + v.getId());

        actividadplan.setKilometraje(proKilometraje);
        PlanMantenimiento pMantenimiento = findById(PlanMantenimiento.class, v.getPlanM().getId());
        List<ActividadPlanMantenimiento> la = new ArrayList<ActividadPlanMantenimiento>();
        //encontrar listas del plan
        for (ActividadPlanMantenimiento actividadPlanMantenimiento : findAll(ActividadPlanMantenimiento.class)) {
            if (actividadPlanMantenimiento.getPlanMantenimiento().getId() == pMantenimiento.getId()) {
                la.add(actividadPlanMantenimiento);
            }
        }
        System.out.println("lsiat de actividades" + la);
        //encontrar kilometraje de cada lista del plan
        for (ActividadPlanMantenimiento actividadPlanMantenimiento : la) {
            System.out.println("kilometraje de actvidad " + actividadPlanMantenimiento.getKilometraje());

            System.out.println("proximo kilometraje>>>>>>" + actividadplan.getKilometraje());
            if (actividadPlanMantenimiento.getKilometraje().equals(actividadplan.getKilometraje())) {
                System.out.println("entro..... a comparar");
                actividadplan.setActividad(actividadPlanMantenimiento.getActividad());
                System.out.println("fijo la actividad" + actividadplan.getActividad());
                break;
            }
        }

    }

    public ActividadPlanMantenimiento getActividadplan() {
        return actividadplan;
    }

    public void setActividadplan(ActividadPlanMantenimiento actividadplan) {
        this.actividadplan = actividadplan;
    }

    //Actividades por kilometraje
    //metodo para obtener proximo kilometraje
    public Integer obtenerKilometraje(Integer kilometaje) {
        Integer proKilometraje = 0;
        if (kilometaje < 5000) {
            proKilometraje = 5000;
        } else {
            if (kilometaje < 10000) {
                proKilometraje = 10000;
            } else {
                if (kilometaje < 20000) {
                    proKilometraje = 20000;
                } else {
                    if (kilometaje < 25000) {
                        proKilometraje = 25000;
                    } else {
                        if (kilometaje < 30000) {
                            proKilometraje = 30000;
                        } else {
                            if (kilometaje < 35000) {
                                proKilometraje = 35000;
                            } else {
                                if (kilometaje < 40000) {
                                    proKilometraje = 40000;
                                } else {
                                    if (kilometaje < 45000) {
                                        proKilometraje = 45000;
                                    } else {
                                        if (kilometaje < 50000) {
                                            proKilometraje = 50000;
                                        } else {
                                            if (kilometaje < 55000) {
                                                proKilometraje = 55000;
                                            } else {
                                                if (kilometaje < 60000) {
                                                    proKilometraje = 60000;
                                                } else {
                                                    if (kilometaje < 65000) {
                                                        proKilometraje = 65000;
                                                    } else {
                                                        if (kilometaje < 70000) {
                                                            proKilometraje = 70000;
                                                        } else {
                                                            if (kilometaje < 75000) {
                                                                proKilometraje = 75000;
                                                            } else {
                                                                if (kilometaje < 80000) {
                                                                    proKilometraje = 80000;
                                                                } else {
                                                                    if (kilometaje < 85000) {
                                                                        proKilometraje = 85000;
                                                                    } else {
                                                                        if (kilometaje < 90000) {
                                                                            proKilometraje = 90000;
                                                                        } else {
                                                                            if (kilometaje < 95000) {
                                                                                proKilometraje = 95000;
                                                                            } else {
                                                                                if (kilometaje < 100000) {
                                                                                    proKilometraje = 100000;
                                                                                } else {
                                                                                    if (kilometaje < 105000) {
                                                                                        proKilometraje = 105000;
                                                                                    } else {
                                                                                        if (kilometaje < 110000) {
                                                                                            proKilometraje = 110000;
                                                                                        } else {
                                                                                            if (kilometaje < 115000) {
                                                                                                proKilometraje = 115000;
                                                                                            } else {
                                                                                                if (kilometaje < 120000) {
                                                                                                    proKilometraje = 1200000;
                                                                                                } else {
                                                                                                    if (kilometaje < 130000) {
                                                                                                        proKilometraje = 130000;
                                                                                                    } else {
                                                                                                        if (kilometaje < 135000) {
                                                                                                            proKilometraje = 135000;
                                                                                                        } else {
                                                                                                            if (kilometaje < 140000) {
                                                                                                                proKilometraje = 140000;
                                                                                                            } else {
                                                                                                                if (kilometaje < 145000) {
                                                                                                                    proKilometraje = 145000;
                                                                                                                } else {
                                                                                                                    if (kilometaje < 150000) {
                                                                                                                        proKilometraje = 150000;
                                                                                                                    } else {
                                                                                                                        if (kilometaje < 15000) {
                                                                                                                            proKilometraje = 15000;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return proKilometraje;

    }

    public String getNumeroRegistro() {
        if (getId() == null) {
            System.out.println("numero" + getInstance().getNumRegistro());
            List<Vehiculo> lista = findAll(Vehiculo.class
            );
            int t = lista.size();
            if (t
                    < 9) {
                setNumeroRegistro("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setNumeroRegistro("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setNumeroRegistro("0".concat(String.valueOf(t + 1)));
                    } else {
                        setNumeroRegistro(String.valueOf(t + 1));
                    }
                }
            }
        } else {
            setNumeroRegistro(getInstance().getNumRegistro());
        }

        return numeroRegistro;

    }

    public void setNumeroRegistro(String numRegistro) {
        this.numeroRegistro = numRegistro;
        getInstance().setNumRegistro(this.numeroRegistro);

    }

    public Long getVehiculoId() {
        System.out.println("IIIIDEE" + getId());
        
        return (Long) getId();
    }

    public void setVehiculoId(Long vehiculoId) {
        System.out.println("id a fijar"+vehiculoId);
      
        setId(vehiculoId);

    }
    public void fijarNullVehiculo(){
        setInstance(new Vehiculo());
         System.out.println("fijando::::::::::::: " + getInstance());
    }

    @TransactionAttribute
    public Vehiculo load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<Vehiculo> getListaVehiculos() {
        return listaVehiculos;
    }

    public void setListaVehiculos(List<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaVehiculos = servgen.buscarTodos(Vehiculo.class);
        ActividadPlanMantenimiento actividadplan = new ActividadPlanMantenimiento();
        System.out.println("lista vehiculos" + listaVehiculos);
//        List<Vehiculo> lv = servgen.buscarTodos(Vehiculo.class);
//        listaVehiculos.clear();
//
//        for (Vehiculo vehiculo : lv) {
//            if (vehiculo.isEstado()) {
//                listaVehiculos.add(vehiculo);
//                System.out.println("Entro a remover>>>>");
//                System.out.println("a;iadia" + listaVehiculos);
//
//            }
//
//        }
        verificarPlan();

//        if (!verificarPlan()) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Necesita activar un plan de mantenimiento para visualizar ", " las actividades por kilometraje");
//            FacesContext.getCurrentInstance().addMessage("Información", msg);
//        }
    }
//    public String irCrear(){
//        setId(null);
//        setInstance(new Vehiculo());
//        setVehiculoId(getInstance().getId());
//        setId(getInstance().getId());
//        setNumeroRegistro(getNumeroRegistro());
//        setId(null);
//    return "/paginas/vehiculo/crear.xhtml?faces-redirect=true";
//    }

    public void fijarPlan(PlanMantenimiento pmat, List<Vehiculo> lv) {
        System.out.println("entro fijar plan");
        System.out.println("listallega" + lv);
//        System.out.println("listarecuperaaaada"+findAll(Vehiculo.class));
        for (Vehiculo v : lv) {
            if (null != pmat.getId()) {
                System.out.println("forr vehiculo>>>>>>>>");
                setId(v.getId());
                System.out.println("iddd" + getId());
                setInstance(v);
                getInstance().setPlanM(pmat);

                System.out.println("se actualizo con id del plan" + getInstance().getPlanM().getId());
                save(getInstance());
                System.out.println("guarrrrrrrrrrrrrrrrrrrrrddadd");
            }

        }
        System.out.println("llllllllllllllll");
    }

    @Override
    protected Vehiculo createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Vehiculo.class
                .getName());
        Date now = Calendar.getInstance().getTime();
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCreatedOn(now);
        vehiculo.setLastUpdate(now);
        vehiculo.setActivationTime(now);
        vehiculo.setType(_type);
        vehiculo.buildAttributes(bussinesEntityService);  //
        return vehiculo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        System.out.println("fijando mensaje");
        if (!mensaje.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Vehiculo" + getInstance().getId() + " con éxito", "");
            FacesContext.getCurrentInstance().addMessage("Informaciónn", msg);
        }
        this.mensaje = mensaje;
    }

    @Override
    public Class<Vehiculo> getEntityClass() {
        return Vehiculo.class;
    }

    public void crearEstadoUbicacion() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("\n\n\n entro crear estado\n\n\n\n");
        EstadoVehiculo estadoU = new EstadoVehiculo();
        System.out.println("\n\n\n entro crear nuevo\n\n\n\n");
        estadoU.setFechaEntrada(now);
        estadoU.setNombre("Equipo Trabajando Normalmente");
        estadoU.setUbicacion("Taller Mtop Loja");

        estadoU.setCreatedOn(now);
        estadoU.setLastUpdate(now);
        estadoU.setActivationTime(now);
        estadoU.setVehiculo(getInstance());
        System.out.println("\n\n\n entro crear estado AnTES\n\n\n\n" + estadoU);
        save(estadoU);
        System.out.println("\n\n\n entro crear estado despues\n\n\n\n" + estadoU);
    }

    public void crearEstadoUbicacion2() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("\n\n\n entro crear estado\n\n\n\n");
        System.out.println("\n\n\n entro crear nuevo\n\n\n\n");
        estado.setCreatedOn(now);
        estado.setLastUpdate(now);
        estado.setActivationTime(now);
        estado.setVehiculo(getInstance());
        System.out.println("\n\n\n entro crear estado AnTES\n\n\n\n" + estado);
        servgen.actualizar(estado);
        System.out.println("\n\n\n entro crear estado despues\n\n\n\n" + estado);
    }

    public void crearPlanMantenimientoActivo() {

        for (PlanMantenimiento pm : cplanMantenimiento.getListaPlanMantenimiento()) {
            if (pm.getActivado()) {
                getInstance().setPlanM(pm);
            }

        }

    }

    public void crearKardex() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        Kardex k = new Kardex();
        k.setNumero(getInstance().getNumRegistro());
        k.setCreatedOn(now);
        k.setLastUpdate(now);
        k.setActivationTime(now);
        k.setVehiculo(getInstance());
        save(k);

    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("PRESENTAR ANTES>>>>>" + getInstance().getNumRegistro());
        System.out.println("IIIIDEEEntro>>>>>>" + getInstance().getId());
        System.out.println("PRESENTAR persisten>>>>>" + getInstance().isPersistent());

        try {
            if (getInstance().isPersistent()) {

                save(getInstance());
                System.out.println("guarrrrrrrrrrrrrrrrrrrrrddadd");
                mensaje = "Se actualizó Vehiculo" + getInstance().getId() + " con éxito";

            } else {
                System.out.println("guardando Vehiculoooooooo");
                getInstance().setEstado(true);
                getInstance().setDescription("Kilometraje inicial");
                create(getInstance());
                save(getInstance());
                crearKardex();
                crearEstadoUbicacion();
                System.out.println("volcio a guardar");
                crearPlanMantenimientoActivo();
                System.out.println("finalizo");
                mensaje = "Se creó Vehiculo" + getInstance().getId() + " con éxito";
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("errrrrrrrrrrrrrrrrrrrrrrrorrrrrrrr");
        }

        return "/paginas/admin/vehiculo/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public String guardarMantenimiento() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso", "Se actualizo Vehiculo" + getInstance().getId() + " con éxito");
//                RequestContext.getCurrentInstance().showMessageInDialog(msg);
            }

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/mantenimientoVehiculo/lista.xhtml?faces-redirect=true";
    }

    public String guardarEstadoVehiculo() {
        System.out.println("entro metodo a guardarEstado::::::.");
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                System.out.println("entro a editar estado:::::: ");
                System.out.println("ante");
                crearEstadoUbicacion2();
                System.out.println("finalizo");
                System.out.println("\n\n\n\n estado\n\n\n\n " + estado);

                getInstance().getListaEstados().add(estado);
                System.out.println("lista de estado de l vehiculo" + getInstance().getListaEstados());
                save(getInstance());

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso", "Se actualizo Vehiculo" + getInstance().getId() + " con éxito");

            }

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/estadodeubicacion/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public String editarEstado() {
        getInstance().findBussinesEntityAttribute(numeroRegistro).size();
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("PRESENTAR ANTES>>>>>" + getInstance().getNumRegistro());
        System.out.println("IIIIDEEEntro>>>>>>" + getVehiculoId());
        System.out.println("PRESENTAR persisten>>>>>" + getInstance().isPersistent());
        try {
            save(getInstance());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Vehiculo" + getInstance().getId() + " con éxito", " ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/estadoVehiculo/lista.xhtml?faces-redirect=true";
    }

    @Transactional
    public String darDeBaja(Long idvehiculo) {
        ControladorEstadoVehiculo cestado = new ControladorEstadoVehiculo();
        System.out.println("Entro a dar de baja>>>>>>" + idvehiculo);
        setId(idvehiculo);
        setInstance(servgen.buscarPorId(Vehiculo.class, idvehiculo));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);
        //    ck.getInstance().setEstado(false);
        save(getInstance());
//        cestado.getInstance().setEstado(false);
//        crearEstadoUbicacion();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La vehículo seleccionado ha sido dada de baja ", "exitosamente"));
        return "/paginas/admin/vehiculo/lista.xhtml?faces-redirect=true";
    }

    @Transactional
    public String borrarEntidad() {
        //       log.info("sgssalud --> ingreso a eliminar: " + getInstance().getId());
        try {
            if (getInstance() == null) {
                throw new NullPointerException("Servicio is null");
            }
            if (getInstance().isPersistent()) {
                delete(getInstance());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getInstance().getName(), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡No existe una entidad para ser borrada!", ""));

            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.toString()));
        }
        return "/paginas/admin/vehiculo/lista.xhtml?faces-redirect=true";
    }

    public
            boolean tieneEstadosEstructura(Property propiedad) {
        for (Property p : servgen.buscarTodos(Property.class
        )) {
            if (p.getGroupName()
                    != null) {
                if (p.getGroupName().equals(propiedad.getName())) {
                    System.out.println("encontro su propiedad>>>>> " + p.getName());
                    if (p.getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
                        System.out.println("retornara true");
                        return true;
                    }
                }
            }

        }
        System.out.println("retornara false");
        return false;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }
    
    public void generaReporte(){
       GeneradorPdf pu = new GeneradorPdf();
        StringBuilder buf = new StringBuilder();

        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        buf.append("<head>");
        // buf.append("<div id='page-header' class='header'>" + "<img  "
        // + "height='" + parameters.get("templateHeaderHeight")
        // + "' width='" + parameters.get("templateHeaderWidth") + "' "
        // + "src='" + parameters.get("templateHeader") + "'/>" + "</div>");
        buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        buf.append("<title>Ppless PDF Document</title> ");
        buf.append("<style type='text/css' media='print'>  ");
        buf.append("* {font-family: Arial, 'sans-serif'  !important; font-size:12pt !important; }");
        // buf.append("@page { size:8.5in 11in; margin: 0.25in; padding:1em; @bottom-left { content: element(footer); } } ");

        buf.append("@page { size:"
                + "A4"
                + "; margin: cm; padding:1em"
                + "; margin-top:"
                + "144"
                + "px;margin-bottom:"
                + "108"
                + "px;@bottom-left{ content: element(footer); };background-image: url('"
                + ""
                + "');background-repeat: no-repeat;background-position: 0px 0px;}");
        // buf.append("#footer { font-size: 80%; font-style: italic;  position: running(footer); top: 0; left: 0;  background-image: url(http://localhost:8080/assets/img/fondo.jpg); background-repeat: no-repeat; left bottom;}");
        buf.append("#footer { font-size: 70%; font-style: italic;  padding:0em;position: running(footer); top: 0; right:0;background-image: url('"
                + "" + "')}");
        buf.append("#header { font-size: 80%; font-style: italic;  margin:0px;padding:0em;position: running(page-header); top: 0; right:0;}");
        buf.append("#pagenumber:before { content: counter(page); } ");
        buf.append("#pagecount:before { content: counter(pages); } ");
        buf.append("</style></head>");
        buf.append("<body>");

        buf.append("<table width=\"100%\"  border=\"0\" cellspacing=\"0\"  cellpadding=\"3\">");
        
        for (Vehiculo v : getListaVehiculos()) {
            buf.append("<tr>");
            //1
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getNumRegistro());
            buf.append("</p>");
            buf.append("</td>");
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append("Clase");
            buf.append("</p>");
            buf.append("</td>");
            //2
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getMarca());
            buf.append("</p>");
            buf.append("</td>");
            //3
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getModelo());
            buf.append("</p>");
            buf.append("</td>");
            //4
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(obtenerUltimaUbicacionV(v));
            buf.append("</p>");
            buf.append("</td>");

            buf.append("<td align='right'>");
            buf.append("<span style='font-size:10pt !important; font-style: \"italic\"; float:right'>");
            buf.append("TR: ");
            buf.append("CJ");
            buf.append("</span>");
            buf.append("</td>");
            buf.append("</tr>");

        }
        buf.append("<tr>");
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("2014");
        buf.append("</p>");
        buf.append("</td>");
        buf.append("<td></td>");
        buf.append("</tr>");
        buf.append("</table>");

        buf.append("<p>&nbsp;</p>\n"
                + "<table border=\"0\" cellspacing=\"0\"  cellpadding=\"3\">\n"
                + "<tr>\n"
                + "<td><strong> <span>PARA:                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
                + "<td>\n"
                + "<p><span>Dr. Gustavo Jalkh               <span>(respetar orden jer&aacute;rquico seg&uacute;n               organigrama)</span></span></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td>&nbsp;</td>\n"
                + "<td>\n"
                + "<p><strong> <span>PRESIDENTE</span> </strong></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<p><strong> <span>DE:                  <span>&nbsp;&nbsp;&nbsp;</span> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></p>\n"
                + "</td>\n"
                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td>&nbsp;</td>\n"
                + "<td>\n"
                + "<p><strong> <span>Secretario</span> </strong></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td><strong> <span>ASUNTO:               <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
                + "<td><span>PRUEBA 3</span></td>\n"
                + "<td>&nbsp;</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>\n"
                + "<p>&nbsp;&nbsp;<strong>&nbsp;</strong><span>&nbsp;</span></p>\n"
                + "<p><strong> <span>Se expone el tema directamente, conciso y         claro.</span> </strong></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>Solicito que todas las unidades utilicen las siglas de       identificaci&oacute;n aprobadas, a partir del d&iacute;a lunes 13       de mayo de 2013, con car&aacute;cter obligatorio en los       memorandos y oficios, de las unidades administrativas del       Consejo de la Judicatura.</span></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>Atentamente,</span></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong> <span>Secretario</span> </strong></td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>C.C.:</span> </strong> <strong> <span>Si se requiere que m&aacute;s unidades reciban el         documento detallar el nombre y cargo</span> </strong></p>");

        buf.append("</body>");
        buf.append("</html>");

        ByteArrayInputStream bais = new ByteArrayInputStream(buf.toString()
                .getBytes());
        try {
            OutputStream os = new FileOutputStream("/tmp/examplePie5.pdf");
            GeneradorPdf.createPDF(bais, os);
        } catch (DocumentException ex) {
            Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
