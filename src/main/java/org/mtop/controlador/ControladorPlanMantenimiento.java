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
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.PlanMantenimiento_;
import org.mtop.modelo.Vehiculo;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.event.FlowEvent;

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

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public String formato() {
        String fechaFormato = "";
        if (getInstance().getCreatedOn() != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(getInstance().getCreatedOn());
        }

        return fechaFormato;

    }

    public void buscar() {
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        System.out.println("Palabra ingresada?>>>>>"+palabrab);
        //buscando por coincidencia descripciion
        List<PlanMantenimiento> lp = servgen.buscarTodoscoincidencia(PlanMantenimiento.class, PlanMantenimiento.class.getSimpleName(), PlanMantenimiento_.registro.getName(), palabrab);
        //buscando por codigo
        List<PlanMantenimiento> lc = servgen.buscarPlanMporFecha(PlanMantenimiento_.createdOn.getName(), palabrab);

        for (PlanMantenimiento planMantenimiento : lc) {
            if (!lp.contains(planMantenimiento)) {
                System.out.println("entro a if>>>>>> agregar"+planMantenimiento);
                lp.add(planMantenimiento);
            }
        }
        System.out.println("lisssssssta lp"+lp);
        if (lp.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaPlanMantenimiento = lp;
        }

    }

    public void limpiar() {
        palabrab = "";
        List<PlanMantenimiento> lp = servgen.buscarTodos(PlanMantenimiento.class);
        listaPlanMantenimiento.clear();
        System.out.println("lppp" + lp);

        for (PlanMantenimiento planM : lp) {
            System.out.println("iddddd" + planM.getId());
            System.out.println("entro a for lista>>>>" + planM.isEstado());
            if (planM.isEstado()) {
                System.out.println("listatesssa" + listaPlanMantenimiento);
                listaPlanMantenimiento.add(planM);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + listaPlanMantenimiento);

            }

        }
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<PlanMantenimiento> lp = servgen.buscarTodoscoincidencia(PlanMantenimiento.class, PlanMantenimiento.class.getSimpleName(), PlanMantenimiento_.registro.getName(), palabrab);

        for (PlanMantenimiento planM : lp) {
            System.out.println("econtro uno " + planM.getRegistro());
            ced.add(planM.getRegistro());
        }
          List<PlanMantenimiento> lc = servgen.buscarPlanMporFecha(PlanMantenimiento_.createdOn.getName(), query);
        for (PlanMantenimiento planM : lc) {
            System.out.println("econtro uno " + planM.getCreatedOn());
            setInstance(planM);
            ced.add(formato());
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

            addMessage("Información", "El Plan de Mantenimiento seleccionado ha sido activado");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return "/paginas/planMantenimiento/lista.xhtml?faces-redirect=true";
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

    public void editarActividad(ActividadPlanMantenimiento actividad) {
        System.out.println("ACTIVIDAD>>>>>" + cactividadpm.getInstance().getActividad());;
        System.out.println("REMOVE>>>>>>>>>>>." + actividad);

        int con = 0;
        for (ActividadPlanMantenimiento apm : cactividadpm.listaActividades) {

            if (apm.getKilometraje().equals(actividad.getKilometraje())
                    && apm.getActividad().equals(actividad.getActividad())) {
                cactividadpm.listaActividades.remove(con);
                cactividadpm.setInstance(apm);
                System.out.println("OBJETO LISTA>>>>>>>>>>>>>" + apm.getKilometraje());
                System.out.println("FIJAR>>>>>>" + cactividadpm.getInstance().getKilometraje());
                break;

            }
            con++;

        }
        System.out.println("tama;o de la lista" + cactividadpm.listaActividades.size());

    }

    public ControladorActividadPlanMantenimiento getCactividadpm() {
        return cactividadpm;
    }

    public void setCactividadpm(ControladorActividadPlanMantenimiento cactividadpm) {
        this.cactividadpm = cactividadpm;

    }

    public void agregarActividad() {
        boolean ban = true;

        for (ActividadPlanMantenimiento apm : cactividadpm.listaActividades) {
            System.out.println("ENTRO>>>>>>>" + apm.getKilometraje());
            if (apm.getKilometraje().equals(cactividadpm.getInstance().getKilometraje())) {
                System.out.println("Entro al if>>>>>>" + cactividadpm.getInstance().getKilometraje());

                ban = false;
                break;

            }
        }
        if (ban == true) {
            cactividadpm.listaActividades.add(cactividadpm.getInstance());
            cactividadpm.setInstance(new ActividadPlanMantenimiento());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "EL kilometraje escojido ya se encuentra agregado en la lista"));
        }

//                }
//            }
//        }
    }

    public void guardarActividad() {

        for (ActividadPlanMantenimiento apm : cactividadpm.listaActividades) {
            apm.setPlanMantenimiento(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cactividadpm.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            cactividadpm.guardar();
        }
        getInstance().setListaActividadpm(cactividadpm.listaActividades);//fija la lista de actividades al plan de mantenimietno

    }

    public Long getPlanMantenimientoId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setPlanMantenimientoId(Long planMantenimientoId) {

        setId(planMantenimientoId);
        //se fija la lista actividades que hay en el plan mantenimiento a la lista actividades.
        cactividadpm.listaActividades = getInstance().getListaActividadpm();

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
        listaPlanMantenimiento = servgen.buscarTodos(PlanMantenimiento.class);
        cv.setEntityManager(em);
        cactividadpm = new ControladorActividadPlanMantenimiento();
        cactividadpm.setInstance(new ActividadPlanMantenimiento());
        listak();

    }

    @Override
    protected PlanMantenimiento createInstance() {
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
        if (cactividadpm.listaActividades.isEmpty()) {
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

                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Plan Mantenimiento" + getInstance().getId() + " con éxito", " ");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                } else {
                    getInstance().setEstado(true);
                    getInstance().setActivado(false);
                    create(getInstance());
                    guardarActividad();
                    save(getInstance());
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo un nuevo Plan MAntenimiento" + getInstance().getId() + " con éxito", " ");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                }
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ErRor al guardarrwe: ".concat(e.toString()) + getInstance().getId(), " " + e);
                FacesContext.getCurrentInstance().addMessage("", msg);
                System.out.println("errrrrrrrrrorrrrr");
            }
            PlanMantenimiento oo = findById(PlanMantenimiento.class, getInstance().getId());
            System.out.println("entrontro" + oo.getId());
            System.out.println("entrontro" + oo.getActivado());
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
