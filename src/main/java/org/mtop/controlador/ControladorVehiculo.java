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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
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
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.Vehiculo;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;

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
    @Inject
    private ServicioGenerico servgen;
    List<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();
    private String numeroRegistro;
    private ActividadPlanMantenimiento actividadplan;
    private Integer prokilometraje;
    ControladorKardex ck;
    String mensaje="";
    public Integer getProkilometraje() {
        return prokilometraje;
    }

    public void setProkilometraje(Integer prokilometraje) {
        System.out.println(getInstance());
        System.out.println(getInstance().getPlanM().getListaActividadpm());
        for (ActividadPlanMantenimiento actividadpm : getInstance().getPlanM().getListaActividadpm()) {
            if (actividadpm.getKilometraje() == prokilometraje) {
                actividadplan = actividadpm;
            }
        }
        this.prokilometraje = prokilometraje;
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
        Integer proKilometraje = null;
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

        setId(vehiculoId);

    }

    @TransactionAttribute   //
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
//        PlanMantenimiento pmat = new PlanMantenimiento();
//        for (PlanMantenimiento pm : findAll(PlanMantenimiento.class)) {
//            System.out.println("for plan de mantenimiento>>>>>>>>>>>");
//            if (pm.isEstado()) {
//                pmat = pm;
//            }
//
//        }
//        for (Vehiculo v : findAll(Vehiculo.class)) {
//            if (null != pmat.getId()) {
//                System.out.println("forr vehiculo>>>>>>>>");
//                v.setPlanM(pmat);
//                setInstance(v);
//                guardar();
//            }
//
//        }
        listaVehiculos = servgen.buscarTodos(Vehiculo.class);
        System.out.println("lista vehiculos"+listaVehiculos);
       
       
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

    public void fijarPlan(PlanMantenimiento pmat, List<Vehiculo> lv ){
        System.out.println("entro fijar plan");
        System.out.println("listallega"+lv);
//        System.out.println("listarecuperaaaada"+findAll(Vehiculo.class));
        for (Vehiculo v : lv) {
            if (null != pmat.getId()) {
                System.out.println("forr vehiculo>>>>>>>>");
                setId(v.getId());
                System.out.println("iddd"+getId());
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
    protected Vehiculo
            createInstance() {
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
        if(!mensaje.equals("")){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Vehiculo" + getInstance().getId() + " con éxito", "");
             FacesContext.getCurrentInstance().addMessage("Informaciónn", msg);
        }
        this.mensaje = mensaje;
    }

    @Override
    public Class<Vehiculo> getEntityClass() {
        return Vehiculo.class;
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

                System.out.println("se actualizo con id del plan" + getInstance().getPlanM().getId());
                save(getInstance());
                System.out.println("guarrrrrrrrrrrrrrrrrrrrrddadd");
                mensaje="Se actualizó Vehiculo" + getInstance().getId() + " con éxito";
            } else {
                System.out.println("guardando Vehiculoooooooo");
                getInstance().setEstado(true);
                getInstance().setDescription("Kilometraje inicial");
                create(getInstance());
                save(getInstance());
                mensaje="Se creó Vehiculo" + getInstance().getId() + " con éxito";
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("errrrrrrrrrrrrrrrrrrrrrrrorrrrrrrr");
        }
        
        return "/paginas/vehiculo/lista.xhtml?faces-redirect=true";
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
        return "/paginas/vehiculo/mantenimientoVehiculo/lista.xhtml?faces-redirect=true";
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
        return "/paginas/vehiculo/estadoVehiculo/lista.xhtml?faces-redirect=true";
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
        return "/paginas/vehiculo/lista.xhtml?faces-redirect=true";
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

}
