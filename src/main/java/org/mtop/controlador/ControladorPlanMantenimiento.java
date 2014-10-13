/*
 * Copyright 2014 carlis.
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.PlanMantenimiento_;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.dinamico.PersistentObject_;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorPlanMantenimiento extends BussinesEntityHome<PlanMantenimiento> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @EJB
    private ServicioGenerico servgen;
    @Inject
    ControladorVehiculo cv;
    private List<PlanMantenimiento> listaPlanMantenimiento = new ArrayList<PlanMantenimiento>();
    private ControladorActividadPlanMantenimiento cactividadpm;
    private String numeroPlanMantenimiento;
    private List<Integer> listaKilometraje = new ArrayList();
    private String palabrab = "";
    private ActividadPlanMantenimiento it;
    private ActividadPlanMantenimiento actividadPlanM;
    private List<ActividadPlanMantenimiento> listaActividades;
    private List<ActividadPlanMantenimiento> listaActividades2;
    private List<ActividadPlanMantenimiento> listaactividadesPlan = new ArrayList<ActividadPlanMantenimiento>();
    private List<PlanMantenimiento> listaPlanM2 = new ArrayList<PlanMantenimiento>();
    private PlanMantenimiento planMantvisualizar = new PlanMantenimiento();
    String s = "";

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public PlanMantenimiento getPlanMantvisualizar() {
        return planMantvisualizar;
    }

    public ActividadPlanMantenimiento getActividadPlanM() {
        return actividadPlanM;
    }

    public void setActividadPlanM(ActividadPlanMantenimiento actividadPlanM) {
        this.actividadPlanM = actividadPlanM;
    }

    public void setPlanMantvisualizar(PlanMantenimiento planMantvisualizar) {
        this.planMantvisualizar = planMantvisualizar;
    }

    public List<ActividadPlanMantenimiento> getListaactividadesPlan() {
        return listaactividadesPlan;
    }

    public void setListaactividadesPlan(List<ActividadPlanMantenimiento> listaactividadesPlan) {
        this.listaactividadesPlan = listaactividadesPlan;
    }

    public List<ActividadPlanMantenimiento> getListaActividades() {
        System.out.println("obteniendo la lista1 en get");
        for (ActividadPlanMantenimiento actividadPlanMantenimiento : listaActividades) {
            System.out.println("kilome   " + actividadPlanMantenimiento.getKilometraje());
            System.out.println("activ   " + actividadPlanMantenimiento.getActividad());
        }

        return listaActividades;
    }

    public void setListaActividades(List<ActividadPlanMantenimiento> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public ActividadPlanMantenimiento getIt() {
        return it;
    }

    public void setIt(ActividadPlanMantenimiento it) {
        this.it = it;
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    public void buscar() {

        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            System.out.println("entro vacio");
            palabrab = "Ingrese algun valor a buscar";
        }
        System.out.println("entra palabra" + palabrab);
        List<Long> lplan = new ArrayList<Long>();

        for (PlanMantenimiento pm : listaPlanM2) {
            if (pm.isEstado() && !lplan.contains(pm)) {

                String s = formato(pm.getCreatedOn());
                System.out.println("entro al forrrrrmmm" + s);
                if (pm.getRegistro().contains(palabrab) || s.contains(palabrab)) {
                    lplan.add(pm.getId());
                }

            }

        }

        System.out.println("LEeeee" + lplan);

        if (lplan.isEmpty()) {

            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);

                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        } else {
            listaPlanMantenimiento.clear();

            PlanMantenimiento plamMant = new PlanMantenimiento();
            for (Long p : lplan) {
                plamMant = findById(PlanMantenimiento.class, p);
                listaPlanMantenimiento.add(plamMant);
            }

            palabrab = "";
        }
    }

    public void limpiar() {
        palabrab = "";
//      listaPlanMantenimiento=listaPlanM2;
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (PlanMantenimiento planM : listaPlanM2) {
            System.out.println("econtro uno " + planM.getRegistro());
            ced.add(planM.getRegistro());
        }

        for (PlanMantenimiento planM : listaPlanM2) {

            if (!ced.contains(formato(planM.getCreatedOn()))) {
                System.out.println("no lo contienes" + formato(planM.getCreatedOn()));
                ced.add(formato(planM.getCreatedOn()));
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void descativarPlan() {
        Date now = Calendar.getInstance().getTime();
        try {
            List<PlanMantenimiento> pms = servgen.buscarTodos(PlanMantenimiento.class); //findAll(PlanMantenimiento.class);

            for (PlanMantenimiento planMantenimiento : pms) {
                if (planMantenimiento.getActivado()) {
//               setInstance(planMantenimiento);
                    setInstance(planMantenimiento);
                    System.out.println("plan " + getInstance().getId());
                }

                // setInstance(planMantenimiento);
                //getInstance().setActivado(false);
                //           guardarCambioPlan();
            }
            if (getInstance().getId() != null) {
                getInstance().setActivado(false);
                getInstance().setLastUpdate(now);
                //getInstance().setActivado(false);
                System.out.println("Ingreso a actualizar el id a fijar false" + getInstance().getId());

                save(getInstance());
            }

            listaPlanMantenimiento = servgen.buscarTodos(PlanMantenimiento.class);
//            planm = findById(PlanMantenimiento.class, planm.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

//update();
    }

    public void activarPlan(Long idplan) {
        System.out.println("llego id" + idplan);
//String ms1="";
        try {
            descativarPlan();

            setInstance(findById(PlanMantenimiento.class, idplan));
            System.out.println("salio del for>>>>>>>>>>>>>>>>>>");

            Date now = Calendar.getInstance().getTime();
            getInstance().setLastUpdate(now);
            getInstance().setActivado(true);
            servgen.actualizar(getInstance());
            System.out.println("Ingreso a actualizar el id a fijar trueeeee" + getInstance().getId());
            List<Vehiculo> lv = findAll(Vehiculo.class);
            System.out.println("lista de vehiculosss" + lv);
            for (Vehiculo v : lv) {
                if (null != getInstance().getId()) {
                    System.out.println("forr vehiculo>>>>>>>>");

                    v.setPlanM(getInstance());
                    v.setLastUpdate(now);
                    servgen.actualizar(v);

                }

            }
            for (PlanMantenimiento planMantenimiento : listaPlanMantenimiento) {
                if (planMantenimiento.getActivado()) {
                    planMantvisualizar = planMantenimiento;
                    listaactividadesPlan = planMantenimiento.getListaActividadpm();
                }
            }
            //     msj = "";
            //    ms1 = "true" + getInstance().getRegistro();
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "El Plan de Mantenimiento seleccionado ha sido activado");
//                FacesContext.getCurrentInstance().addMessage("", msg);

            addMessage("INFORMACIÓN:", "El Plan de Mantenimiento seleccionado ha sido activado");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return "/paginas/planMantenimiento/lista.xhtml?faces-redirect=true&msj1=" + ms1;
    }

    public List<Integer> getListaKilometraje() {

        return listaKilometraje;
    }

    public void setListaKilometraje(List<Integer> listaKilometraje) {
        this.listaKilometraje = listaKilometraje;
    }

    public String getNumeroPlanMantenimiento() {
        if (getId() == null) {
            List<PlanMantenimiento> lista = findAll(PlanMantenimiento.class);
            int t = lista.size();
            System.out.println("valor de t :::::::::::" + t);
            if (t < 9) {
                setNumeroPlanMantenimiento("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setNumeroPlanMantenimiento("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setNumeroPlanMantenimiento("0".concat(String.valueOf(t + 1)));
                    } else {
                        setNumeroPlanMantenimiento(String.valueOf(t + 1));
                    }
                }
            }
        } else {
            setNumeroPlanMantenimiento(getInstance().getRegistro());
        }

        return numeroPlanMantenimiento;
    }

    public void setNumeroPlanMantenimiento(String numRegistro) {
        this.numeroPlanMantenimiento = numRegistro;
        getInstance().setRegistro(this.numeroPlanMantenimiento);

    }

    public void agregarActividad() {

        boolean ban = true;
        System.out.println("entra guardar");

        for (ActividadPlanMantenimiento apm : listaActividades) {
            System.out.println("ENTRO>>>>>>>" + apm.getKilometraje());

            if (apm.getKilometraje().equals(actividadPlanM.getKilometraje())) {
                ban = false;
                break;

            }
        }

        if (ban == true) {
            s="";
//            System.out.println("entro a bandera true con it" + it);
//
//            if (it != null) {
//
//                if (!it.getActividad().equals(actividadPlanM.getActividad())
//                        && !it.getKilometraje().equals(actividadPlanM.getKilometraje())) {
//                    System.out.println("actualizo 1");
//                    listaActividades.remove(it);
//                    listaActividades.add(actividadPlanM);
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Se actualizó la actividad " + actividadPlanM.getActividad() + " con éxito "));
//
//                } else {
//                    it = null;
//                }
//            } else {
            System.out.println("creo");
            listaActividades.add(actividadPlanM);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Se creó la actividad " + actividadPlanM.getActividad() + " con éxito "));

//            }
            actividadPlanM = new ActividadPlanMantenimiento();

        } else {
//            if (it != null) {
//                System.out.println("entro it dif de null");
//                if (it.getKilometraje().equals(actividadPlanM.getKilometraje())) {
//                    System.out.println("actualizo 2 antes");
//                    for (ActividadPlanMantenimiento acti : listaActividades) {
//                        System.out.println("kilo" + acti.getKilometraje());
//                        System.out.println("acti" + acti.getKilometraje());
//                    }
//                    if (!it.getActividad().equals(actividadPlanM.getActividad())) {
//                        System.out.println("actualizo 2");
//                        listaActividades.remove(it);
//                        listaActividades.add(actividadPlanM);
//                    } else {
//
//                        System.out.println("entro a caso contrario");
//                        actividadPlanM = new ActividadPlanMantenimiento();
//                        for (ActividadPlanMantenimiento acti : listaActividades) {
//                            System.out.println("kilo" + acti.getKilometraje());
//                            System.out.println("acti" + acti.getKilometraje());
//                        }
//
//                    }
//
//                } else {
//                    System.out.println("it" + it);
//                    System.out.println("instance" + actividadPlanM.getKilometraje());
//                    System.out.println("entro a kilometraje escojido");
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "EL kilometraje que ha escojido ya se encuentra agregado en la lista"));
//                }
//
//            } else {
            System.out.println("it" + it);
            System.out.println("instance" + actividadPlanM.getKilometraje());
            System.out.println("entro a kilometraje escojido");
            s = "ERROR! EL kilometraje que ha escojido ya se encuentra agregado en la lista";
            System.out.println("el mesjae" + s);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "EL kilometraje que ha escojido ya se encuentra agregado en la lista"));
//            }

        }
        it = null;

    }

    public void editar() {

        System.out.println("it" + it.getActividad());

        System.out.println("entor a editar con tamanio" + listaActividades.size());
        it = new ActividadPlanMantenimiento();

    }

//    public void editarActividad(ActividadPlanMantenimiento actividad) {
//        System.out.println("ACTIVIDAD>>>>>" + actividadPlanM.getActividad());;
//        System.out.println("REMOVE>>>>>>>>>>>." + actividad);
//        List<ActividadPlanMantenimiento> la = listaActividades;
//        ActividadPlanMantenimiento ap = new ActividadPlanMantenimiento();
//        for (ActividadPlanMantenimiento apm : la) {
//
//            if (apm.getKilometraje().equals(actividad.getKilometraje())
//                    && apm.getActividad().equals(actividad.getActividad())) {
//                ap = apm;
//
//                System.out.println("OBJETO LISTA>>>>>>>>>>>>>" + apm.getKilometraje());
//                System.out.println("FIJAR>>>>>>" + actividadPlanM.getKilometraje());
//                break;
//
//            }
//
//        }
//        getInstance().setListaActividadpm(listaActividades);
//
//        la = null;
//        it = ap;//variable para saber si se esta editando
//        actividadPlanM = it;
//        System.out.println("al salir de editar");
//        for (ActividadPlanMantenimiento actividadPlanMantenimiento : listaActividades2) {
//            System.out.println("kilometraje en editar" + actividadPlanMantenimiento.getKilometraje());
//            System.out.println("actividad en editar" + actividadPlanMantenimiento.getActividad());
//        }
//
//    }
    public void guardarActividad() {

        for (ActividadPlanMantenimiento apm : listaActividades) {
            apm.setPlanMantenimiento(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cactividadpm.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            cactividadpm.guardar();
        }
        getInstance().setListaActividadpm(listaActividades);//fija la lista de actividades al plan de mantenimietno

    }

    public void darDeBaja(ActividadPlanMantenimiento act) {
        List<ActividadPlanMantenimiento> lact=new ArrayList<ActividadPlanMantenimiento>();
        System.out.println("lista actividades antes");
        for (ActividadPlanMantenimiento ac : listaActividades) {
            System.out.println("kilometraje k llega"+act.getKilometraje());
            System.out.println("kilometraje a compRARA"+ac.getKilometraje());
            if(!act.getKilometraje().equals(ac.getKilometraje())){
                lact.add(ac);
                System.out.println("enro>>>>>");
            }
            System.out.println("kilo" + ac.getKilometraje());
            System.out.println("act" + ac.getActividad());
        }
        listaActividades=lact;

    }

    public Long getPlanMantenimientoId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setPlanMantenimientoId(Long planMantenimientoId) {

        setId(planMantenimientoId);
        //se fija la lista actizvidades que hay en el plan mantenimiento a la lista actividades.

        listaActividades = getInstance().getListaActividadpm();
        String s = actividadPlanM.getActividad();
        System.out.println("entro a set plan" + listaActividades.size());
//        for (ActividadPlanMantenimiento acti : listaActividades) {
//            System.out.println("kilo en set id" + acti.getKilometraje());
//            System.out.println("acti" + acti.getActividad());
//            listaActividades2.add(acti);
//        }

    }

    @TransactionAttribute   //
    public PlanMantenimiento load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<PlanMantenimiento> getListaPlanMantenimiento() {
        return listaPlanMantenimiento;
    }

    public void setListaPlanMantenimiento(List<PlanMantenimiento> listaPlanMantenimiento) {
        this.listaPlanMantenimiento = listaPlanMantenimiento;
    }

    public void listak() {
        listaKilometraje.add(5000);
        listaKilometraje.add(10000);
        listaKilometraje.add(15000);
        listaKilometraje.add(20000);
        listaKilometraje.add(25000);
        listaKilometraje.add(30000);
        listaKilometraje.add(35000);
        listaKilometraje.add(40000);
        listaKilometraje.add(45000);
        listaKilometraje.add(50000);
        listaKilometraje.add(55000);
        listaKilometraje.add(60000);
        listaKilometraje.add(65000);
        listaKilometraje.add(70000);
        listaKilometraje.add(75000);
        listaKilometraje.add(80000);
        listaKilometraje.add(85000);
        listaKilometraje.add(90000);
        listaKilometraje.add(95000);
        listaKilometraje.add(100000);
        listaKilometraje.add(105000);
        listaKilometraje.add(110000);
        listaKilometraje.add(115000);
        listaKilometraje.add(120000);
        listaKilometraje.add(125000);
        listaKilometraje.add(130000);
        listaKilometraje.add(135000);
        listaKilometraje.add(140000);
        listaKilometraje.add(145000);
        listaKilometraje.add(150000);

    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaPlanMantenimiento = findAll(PlanMantenimiento.class);
        listaPlanM2 = listaPlanMantenimiento;
        cv.setEntityManager(em);

        cactividadpm = new ControladorActividadPlanMantenimiento();

        actividadPlanM = new ActividadPlanMantenimiento();
        listak();
        

        for (PlanMantenimiento planMantenimiento : listaPlanMantenimiento) {
            if (planMantenimiento.getActivado()) {
                planMantvisualizar = planMantenimiento;
                listaactividadesPlan = planMantenimiento.getListaActividadpm();
            }
        }
        it = new ActividadPlanMantenimiento();
        listaActividades = new ArrayList<ActividadPlanMantenimiento>();
//        listaActividades2 = new ArrayList<ActividadPlanMantenimiento>();
        System.out.println("lista de actividades en el inirt" + listaActividades);

    }

    @Override
    protected PlanMantenimiento
            createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(PlanMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        PlanMantenimiento planM = new PlanMantenimiento();
        planM.setCreatedOn(now);
        planM.setLastUpdate(now);
        planM.setActivationTime(now);
        planM.setType(_type);
        planM.buildAttributes(bussinesEntityService);  //
        return planM;
    }

    @Override
    public Class<PlanMantenimiento> getEntityClass() {
        return PlanMantenimiento.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        if (listaActividades.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La Lista de activiades se encuentra vacia"));
            return "";
        } else {
            try {
                if (getInstance().isPersistent()) {
                    System.out.println("entro actualizar>>>>>>>>>>>>>>>>>>>>.");
                    guardarActividad();
                    save(getInstance());
                    System.out.println("actualizo el ide" + getInstance().getId());
                    System.out.println("actualizoooooooo" + getInstance().getActivado());

                } else {
                    System.out.println("entro a crear:::::");
                    getInstance().setEstado(true);
                    getInstance().setActivado(false);
                    create(getInstance());
                    guardarActividad();

                    save(getInstance());

                    System.out.println("va a presnetar mensj");

                }
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ErRor al guardarrwe: ".concat(e.toString()) + getInstance().getId(), " " + e);
                FacesContext.getCurrentInstance().addMessage("", msg);
                System.out.println("errrrrrrrrrorrrrr");

            }
            //   PlanMantenimiento oo = findById(PlanMantenimiento.class, getInstance().getId());

            return "/paginas/admin/planMantenimiento/lista.xhtml?faces-redirect=true";
        }

    }

    @Transactional
    public String borrarEntidad() {

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
        return "/paginas/admin/planMantenimient/lista.xhtml?faces-redirect=true";
    }

}
