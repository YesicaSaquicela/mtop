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
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.genreporte.GeneradorPdf;
import org.mtop.modelo.ActividadPlanMantenimiento;

import org.mtop.modelo.EstadoVehiculo;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.dinamico.Property_;

import org.mtop.servicios.ServicioGenerico;

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

    private ActividadPlanMantenimiento actividadplan;
    private ControladorKardex ck;

    private ControladorPlanMantenimiento cplanMantenimiento;
    private String palabrab = "";
    private EstadoVehiculo estado = new EstadoVehiculo();
    private ControladorEstadoVehiculo cev;
    private Date fechaFinal = Calendar.getInstance().getTime();
    private long idPersona;
    private List<Profile> listaPersonas;

    private String[] tipoSeleccionados;
    private String[] mesyanioSeleccionados;
    private List<String> tipos;
    private List<String> mesyanio;
    private List<EstadoVehiculo> listaEstados;
    private String evaluacion;

    private List<Vehiculo> listVehiculos2 = new ArrayList<Vehiculo>();
    private String vista;
    private boolean desabilitar;
    private String mensaje1 = "";
    private List<EstadoVehiculo> listaEstadoV = new ArrayList<EstadoVehiculo>();
    private String palabrabe;
    private List<EstadoVehiculo> listaEstadoV2 = new ArrayList<EstadoVehiculo>();
    private String mensajef = "";
    private List<String> tiposCombustible = new ArrayList<String>(Arrays.asList("Gasolina", "Diesel"));
    private List<String> tiposCabina = new ArrayList<String>(Arrays.asList("Simple", "Doble"));

    private List<String> marcasv = new ArrayList<String>(Arrays.asList("Abarth", 
            "Alfa Romeo", "Aston Martin", "Audi","Bentley","Caterpillar",
            "Case","Cifali", "Citroen","Chevrolet", 
            "Dacia", "Dfsk","Dresser"," Dynapac", "Faco",
            "Ferrari", "Fiat","Ford","Galión","Hino", "Honda",
            "Hyundai","Infinity", "Internacional","Isuzu", "Iveco", 
            "Jaguar","Kia", "Lada", "Lamborghini", "Lancia","Land-Rover", "Lexus", "Ldv", 
            "Mazda", "Mercedes-Benz", "Mini","Mitsubishi","Muller", "Nissan", "Porsche","Scania",
            "Renault","Rosco", "Suzuki", 
            "Toyota", "Volkswagen"));

    public List<String> getMarcasv() {
        return marcasv;
    }

    public void setMarcasv(List<String> marcasv) {
        this.marcasv = marcasv;
    }

    
    

    public List<String> getTiposCabina() {
        return tiposCabina;
    }

    public void setTiposCabina(List<String> tiposCabina) {
        this.tiposCabina = tiposCabina;
    }

    public List<String> getTiposCombustible() {

        return tiposCombustible;
    }

    public void setTiposCombustible(List<String> tiposCombustible) {
        this.tiposCombustible = tiposCombustible;
    }

    public String getMensajef() {
        return mensajef;
    }

    public void setMensajef(String mensajef) {
        this.mensajef = mensajef;
    }

    public String getMensaje1() {
        return mensaje1;
    }

    public void setMensaje1(String mensaje1) {
        this.mensaje1 = mensaje1;
    }

    public String getPalabrabe() {
        return palabrabe;
    }

    public void setPalabrabe(String palabrabe) {
        this.palabrabe = palabrabe;
    }

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    public List<EstadoVehiculo> getListaEstadoV() {

        return listaEstadoV;
    }

    public void setListaEstadoV(List<EstadoVehiculo> listaEstadoV) {
        this.listaEstadoV = listaEstadoV;
    }

    public boolean isDesabilitar() {
        return desabilitar;
    }

    public void setDesabilitar(boolean desabilitar) {
        this.desabilitar = desabilitar;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }

    public String obtenerAlerta(Integer kma) {
        Integer kmt = kma + 1000;
        Integer proKmj = obtenerKilometraje(kma);
        if (kmt.equals(proKmj)) {
            return "Atención..! Necesita acercarse a realizar el mantenimiento del vehículo";
        } else {
            if (kmt > proKmj) {
                return "Urgente...! Necesita realizar el mantenimiento del vehículo";
            } else {
                return "";
            }
        }
    }

    public String evaluacionGeneral() {

        return "";
    }

    public List<Vehiculo> getListVehiculos2() {
        return listVehiculos2;
    }

    public void setListVehiculos2(List<Vehiculo> listVehiculos2) {
        this.listVehiculos2 = listVehiculos2;
    }

    public List<Profile> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Profile> listaPersonas) {

        this.listaPersonas = listaPersonas;
    }

    public Vehiculo obtenerPlacaVehiculo(final String placa) throws NoResultException {

        TypedQuery<Vehiculo> query = em.createQuery("SELECT p FROM Vehiculo p WHERE p.placa = :placa", Vehiculo.class);
        query.setParameter("placa", placa);
        Vehiculo result = query.getSingleResult();
        return result;
    }

    public boolean placaUnica(String placa) {
        List<Vehiculo> listav = findAll(Vehiculo.class);
        if (getInstance().getId() == null) {
            try {
                obtenerPlacaVehiculo(placa);
                return false;
            } catch (NoResultException e) {
                return true;
            }
        } else {
            List<Vehiculo> lv = new ArrayList<Vehiculo>();
            for (Vehiculo v : listav) {
                if (!v.getPlaca().equals(findById(Vehiculo.class, getInstance().getId()).getPlaca())) {
                    lv.add(v);
                }
            }
            listav = lv;
            for (Vehiculo v : listav) {
                if (v.getPlaca().equals(getInstance().getPlaca())) {

                    obtenerPlacaVehiculo(placa);
                    return false;
                }
            }
            return true;
        }

    }

    public Vehiculo obtenerNumRegistro(final String numregistro) throws NoResultException {

        TypedQuery<Vehiculo> query = em.createQuery("SELECT p FROM Vehiculo p WHERE p.numRegistro = :numregistro", Vehiculo.class);

        query.setParameter("numregistro", numregistro);

        Vehiculo result = query.getSingleResult();

        return result;
    }

    public boolean numRegistroUnico(String numregistro) {

        List<Vehiculo> listavs = findAll(Vehiculo.class);
        if (getInstance().getId() == null) {
            try {
                obtenerNumRegistro(numregistro);
                return false;
            } catch (NoResultException e) {
                return true;
            }
        } else {
            List<Vehiculo> lv = new ArrayList<Vehiculo>();
            for (Vehiculo v : listavs) {
                if (!v.getNumRegistro().equals(findById(Vehiculo.class, getInstance().getId()).getNumRegistro())) {
                    lv.add(v);
                }
            }
            listavs = lv;
            for (Vehiculo v : listavs) {
                if (v.getNumRegistro().equals(getInstance().getNumRegistro())) {

                    obtenerNumRegistro(numregistro);
                    return false;
                }
            }
            return true;
        }

    }

    public boolean verificarFechaSolicitud(Date fFinal) {
        Boolean ban = true;
        Date fEntrada = getInstance().getListaEstados().get(getInstance().getListaEstados().size() - 1).getFechaEntrada();

        if (!fEntrada.before(fFinal)) {  //metodo que compara si una fecha es anterior a la otra
//               
            ban = false;
        }
        return ban;
    }

    public boolean verificarPlaca(String placa) {
        String l1 = "ABUCXHOEWGILRMVNSPQYJKTZ";
        String l2 = "ABCDEFGHIFKLMNOPQRSTUWXYZ";
        String l3 = "ABCDEFGHIFKLMNOPQRSTUWXYZ";
        String n = "1234567890";
        String t = "CDOIAT";

        if (placa.length() >= 7) {

            if (placa.length() == 8) {
                if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                        && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "") && n.contains(placa.charAt(7) + "")) {

                    return true;
                } else {
                    return false;
                }

            } else {
                if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                        && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                    return true;
                } else {
                    if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                            && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "") && n.contains(placa.charAt(7) + "")) {

                        return true;
                    } else {
                        if (placa.substring(0, 2).equals("CC") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                            return true;
                        } else {
                            if (placa.substring(0, 2).equals("CD") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                                return true;
                            } else {
                                if (placa.substring(0, 2).equals("OI") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                                    return true;
                                } else {
                                    if (placa.substring(0, 2).equals("AT") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                                        return true;
                                    } else {
                                        if (placa.substring(0, 2).equals("IT") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {

                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }

    }

    public long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(long idPersona) {

        this.idPersona = idPersona;
    }

    public Date getFechaFinal() {

        fechaFinal = getInstance().getListaEstados().get(getInstance().getListaEstados().size() - 1).getFechaEntrada();

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

    public boolean obtenerAñoV(String fanio) {

        String fechaFormato = "";
        Date fEntrada = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy");
        fechaFormato = formatter.format(fEntrada);
        Integer fa = Integer.parseInt(fechaFormato);
        Integer fv = Integer.parseInt(fanio);
        if (fv <= fa && fv >= 1950) {
            System.out.println("entro a true");
            return true;
        } else {
            System.out.println("entro a false");
            return false;
        }

    }

    public EstadoVehiculo obtenerUltimoEstadoVehiculo(Vehiculo vehiculo) {
        return vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1);

    }

    public String obtenerUltimaUbicacionV(Vehiculo vehiculo) {
        String ubicacion = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getUbicacion();
        return ubicacion;
    }

    public String obtenerUltimaUbicacionV2(Vehiculo vehiculo, List<EstadoVehiculo> list) {

        for (EstadoVehiculo ev : list) {
            System.out.println("ev" + ev.getVehiculo().getListaEstados());

        }

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

    public Boolean obtenerAtributoDinamico(String tipo) {
        Boolean ban = false;
        List<BussinesEntityAttribute> la = new ArrayList<BussinesEntityAttribute>();

        la = getInstance().findBussinesEntityAttribute(tipo);
        for (BussinesEntityAttribute ba : la) {

            if (!ba.getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
                System.out.println("entro al if" + ban);
                ban = true;
                break;
            }
        }
        return ban;
    }

    public void buscar() {

        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia descripciion
        List<Vehiculo> lvs = new ArrayList<Vehiculo>();
        for (Vehiculo veh1 : listVehiculos2) {
            if (veh1.getNumRegistro().contains(palabrab)) {
                lvs.add(veh1);
            } else {
                if (veh1.getPlaca().toLowerCase().contains(palabrab.toLowerCase())) {
                    lvs.add(veh1);
                }
            }
        }
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);

        List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();
        for (Vehiculo vehiculo : listVehiculos2) {
            cv.setId(vehiculo.getId());
            bea = cv.getInstance().findBussinesEntityAttribute("Motor");

            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(palabrab.toLowerCase())) {
                        Boolean ban = false;
                        for (Vehiculo v : lvs) {
                            if (v.getId().equals(vehiculo.getId())) {
                                ban = true;
                            }
                        }
                        if (!ban) {
                            lvs.add(vehiculo);
                        }

                    }

                }
            }
            bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                if (bussinesEntityAttribute.getName().equals("serieChasis")) {

                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(palabrab)) {
                        Boolean ban = false;
                        for (Vehiculo v : lvs) {
                            if (v.getId().equals(vehiculo.getId())) {
                                ban = true;
                            }
                        }
                        if (!ban) {
                            lvs.add(vehiculo);

                        }
                    }

                }
            }

        }

        if (lvs.isEmpty()) {

            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: No se ha encontrado", palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaVehiculos = lvs;
        }

    }

    public void limpiar() {
        palabrab = "";
        listaVehiculos = listVehiculos2;

    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (Vehiculo vh : listVehiculos2) {
            if (vh.getNumRegistro().contains(query)) {
                ced.add(vh.getNumRegistro());
            }
            if (vh.getPlaca().toLowerCase().contains(query.toLowerCase())) {
                ced.add(vh.getPlaca());
            }
        }
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);
        List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();
        for (Vehiculo vehiculo : listVehiculos2) {
            cv.setId(vehiculo.getId());
            bea = cv.getInstance().findBussinesEntityAttribute("Motor");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase())
                            && !ced.contains((String) bussinesEntityAttribute.getValue())) {
                        ced.add((String) bussinesEntityAttribute.getValue());
                        System.out.println("aniadiosrie" + (String) bussinesEntityAttribute.getValue());
                    }

                }
            }
            bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                if (bussinesEntityAttribute.getName().equals("serieChasis")) {
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase()) && !ced.contains((String) bussinesEntityAttribute.getValue())) {
                        ced.add((String) bussinesEntityAttribute.getValue());
                        System.out.println("aniadiosrie" + (String) bussinesEntityAttribute.getValue());
                    }

                }
            }

        }

        return ced;

    }

    public void limpiarEstadoV() {
        palabrabe = "";
        listaEstadoV = listaEstadoV2;
    }

    public void buscarEstadoV() {
        palabrabe.trim();
        if (palabrabe == null || palabrabe.equals("")) {
            palabrabe = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia descripciion
        List<EstadoVehiculo> lv1 = new ArrayList<EstadoVehiculo>();
        for (EstadoVehiculo ev : listaEstadoV) {
            //EstadoVehiculo ev = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1);
            if (ev.getNombre().contains(palabrabe)) {
                lv1.add(ev);
            }
            if (ev.getUbicacion().contains(palabrabe)) {

                lv1.add(ev);
            }

            if (ev.getVehiculo().getNumRegistro().contains(palabrabe)) {
                lv1.add(ev);
            }
            String s = ev.getFechaEntrada().toString();
            if (s.contains(palabrabe)) {
                lv1.add(ev);
            }

        }

        if (lv1.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabe.equals("Ingrese algun valor a buscar")) {

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACION:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabe = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACION:No se ha encontrado", palabrabe);
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        } else {
            listaEstadoV = lv1;
        }

    }

    public ArrayList<String> autocompletarEstadoV(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (EstadoVehiculo ev : listaEstadoV) {
            //  EstadoVehiculo ev = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1);
            if (ev.getNombre().toLowerCase().contains(query.toLowerCase())) {
                ced.add(ev.getNombre());
            }
            if (ev.getVehiculo().getNumRegistro().contains(query)) {
                ced.add(ev.getVehiculo().getNumRegistro());
            }
            if (ev.getUbicacion().toLowerCase().contains(query.toLowerCase())) {
                ced.add(ev.getUbicacion());
            }
            String s = ev.getFechaEntrada().toString();
            if (s.contains(query) && !ced.contains(s)) {
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
                break;
            }
        }
        return ban;
    }

    public boolean vizualizarActividades(Long vehiculoid) {

        Vehiculo v = findById(Vehiculo.class, vehiculoid);
        Integer proKilometraje = obtenerKilometraje(v.getKilometraje());

        actividadplan = new ActividadPlanMantenimiento();
        actividadplan.setKilometraje(proKilometraje);

        if (v.getPlanM() != null) {
            PlanMantenimiento pMantenimiento = findById(PlanMantenimiento.class, v.getPlanM().getId());
            List<ActividadPlanMantenimiento> la = new ArrayList<ActividadPlanMantenimiento>();
            //encontrar listas del plan
            for (ActividadPlanMantenimiento actividadPlanMantenimiento : findAll(ActividadPlanMantenimiento.class)) {
                if (actividadPlanMantenimiento.getPlanMantenimiento().getId() == pMantenimiento.getId()) {
                    la.add(actividadPlanMantenimiento);
                }
            }
            //encontrar kilometraje de cada lista del plan
            for (ActividadPlanMantenimiento actividadPlanMantenimiento : la) {

                if (actividadPlanMantenimiento.getKilometraje().equals(actividadplan.getKilometraje())) {
                    System.out.println("entro..... a comparar");
                    actividadplan.setActividad(actividadPlanMantenimiento.getActividad());
                    System.out.println("fijo la actividad" + actividadplan.getActividad());
                    return true;

                }
            }
        }

        return false;

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
                                                                                                                        if (kilometaje == 150000) {
                                                                                                                            proKilometraje = 5000;
                                                                                                                        }else{
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
        }

        return proKilometraje;

    }

    public Long getVehiculoId() {
        return (Long) getId();
    }

    public void setVehiculoId(Long vehiculoId) {
        setId(vehiculoId);
        if (vehiculoId != 0) {
            idPersona = getInstance().getPersona().getId();
        } else {
            setInstance(new Vehiculo());
        }

    }

    public void fijarNullVehiculo() {
        setInstance(new Vehiculo());
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
    
        listVehiculos2 = listaVehiculos;
        idPersona = 0l;
        listaPersonas = findAll(Profile.class);
        List<Profile> lp = new ArrayList<Profile>();
        for (Profile persona : findAll(Profile.class)) {
            if (persona.getCargo() != null) {
                if (persona.getCargo().equals("Conductor")) {
                    getInstance().setPersona(persona);
                    lp.add(persona);
                }
            }

        }

        listaPersonas = lp;
        listaEstados = findAll(EstadoVehiculo.class);
        desabilitar = false;
        for (Vehiculo vehiculo : listaVehiculos) {
            EstadoVehiculo estadov = obtenerUltimoEstadoVehiculo(vehiculo);

            listaEstadoV.add(estadov);
        }
        listaEstadoV2 = listaEstadoV;

        verificarPlan();

        
    }

    public void fijarPlan(PlanMantenimiento pmat, List<Vehiculo> lv) {

//        System.out.println("listarecuperaaaada"+findAll(Vehiculo.class));
        for (Vehiculo v : lv) {
            if (null != pmat.getId()) {

                setId(v.getId());

                setInstance(v);
                getInstance().setPlanM(pmat);

                save(getInstance());
                System.out.println("guarrrrrrrrrrrrrrrrrrrrrddadd");
            }

        }
    }

    @Override
    protected Vehiculo createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Vehiculo.class.getName());
        Date now = Calendar.getInstance().getTime();
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCreatedOn(now);
        vehiculo.setLastUpdate(now);
        vehiculo.setActivationTime(now);
        vehiculo.setType(_type);
        vehiculo.buildAttributes(bussinesEntityService);  //
        return vehiculo;
    }

    @Override
    public Class<Vehiculo> getEntityClass() {
        return Vehiculo.class;
    }

    public void crearEstadoUbicacion() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        EstadoVehiculo estadoU = new EstadoVehiculo();
        estadoU.setFechaEntrada(now);
        estadoU.setNombre("Equipo trabajando normal");
        estadoU.setUbicacion("Taller Mtop Loja");

        estadoU.setCreatedOn(now);
        estadoU.setLastUpdate(now);
        estadoU.setActivationTime(now);
        estadoU.setVehiculo(getInstance());
        save(estadoU);
    }

    public void crearEstadoUbicacion2() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        estado.setCreatedOn(now);
        estado.setLastUpdate(now);
        estado.setActivationTime(now);
        estado.setVehiculo(getInstance());
        servgen.actualizar(estado);
    }

    public void crearKardex() {
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Kardex.class
                .getName());

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        Kardex k = new Kardex();
        k.setNumero(getInstance().getNumRegistro());
        k.setCreatedOn(now);
        k.setLastUpdate(now);
        k.setActivationTime(now);
        k.setVehiculo(getInstance());
        k.setType(_type);
        k.buildAttributes(bussinesEntityService);
        create(k);
        save(k);

    }

    @TransactionAttribute
    public String guardar() {
        String ms = "";
        Kardex k = new Kardex();

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        Profile psolicita = servgen.buscarPorId(Profile.class, idPersona);
        getInstance().setPersona(psolicita);
        System.out.println("persona conductor" + getInstance().getPersona());

        try {
            if (getInstance().isPersistent()) {

                for (Kardex kd : findAll(Kardex.class)) {
                    System.out.println("entro al for");
                    if (kd.getVehiculo().getId().equals(getInstance().getId())) {
                        System.out.println("entro a fijar kardex");
                        k = kd;
                        break;
                    }
                }
                k.setLastUpdate(now);
                k.setNumero(getInstance().getNumRegistro());
                save(k);
                save(getInstance());

            } else {

                List<PlanMantenimiento> lp = findAll(PlanMantenimiento.class);
                for (PlanMantenimiento planMant : lp) {
                    if (planMant.getActivado()) {
                        getInstance().setPlanM(planMant);
                        System.out.println("se actualizo con id del plan" + getInstance().getPlanM().getId());
                        break;
                    }
                }

                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                crearKardex();
                crearEstadoUbicacion();

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
        String msjm = "";
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso", "Se actualizó Vehículo" + getInstance().getId() + " con éxito");
            }

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/mantenimientoVehiculo/lista.xhtml?faces-redirect=true";
    }

    public String guardarEstadoVehiculo() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                System.out.println("entro apersistente");
                if (verificarFechaSolicitud(estado.getFechaEntrada())) {

                    crearEstadoUbicacion2();

                    getInstance().getListaEstados().add(estado);

                    save(getInstance());

                } else {

                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! ", "la fecha de inicio es incorrecta");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                }

            }

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/estadodeubicacion/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public String editarEstado() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            save(getInstance());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó Vehículo" + getInstance().getId() + " con éxito", " ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/lista.xhtml?faces-redirect=true";
    }

    @Transactional
    public String darDeBaja(Long idvehiculo) {
        ControladorEstadoVehiculo cestado = new ControladorEstadoVehiculo();

        setId(idvehiculo);
        setInstance(servgen.buscarPorId(Vehiculo.class, idvehiculo));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);

        save(getInstance());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El vehículo seleccionado ha sido dada de baja ", "exitosamente"));
        return "/paginas/admin/vehiculo/lista.xhtml?faces-redirect=true";
    }

    public boolean tieneEstadosEstructura(Property propiedad) {
        System.out.println("propiedades " + propiedad.getStructure().getProperties());

        for (Property p : findAll(Property.class)) {
            if (p.getGroupName()
                    != null) {
                if (p.getGroupName().equals(propiedad.getName())) {

                    if (p.getType().equals("org.mtop.modelo.EstadoParteMecanica")) {

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

    public String[] getTipoSeleccionados() {

        if (tipoSeleccionados != null) {

            for (String s : tipoSeleccionados) {
            }
            if (tipoSeleccionados.length == 0) {
                tipoSeleccionados = null;

            }
        }

        return tipoSeleccionados;

    }

    public void setTipoSeleccionados(String[] tipoSeleccionados) {

        this.tipoSeleccionados = tipoSeleccionados;
    }

    public List<String> getTipos() {
        tipos = new ArrayList<String>();
        tipos.add("Bachadora");
        tipos.add("Buses");
        tipos.add("Camioneta");
        tipos.add("Camión");
        tipos.add("Cargadora");
        tipos.add("Carro Taller");
        tipos.add("Distribuidor de asfalto");
        tipos.add("Distribuidor de aridos");
        tipos.add("Escoba mecánica");
        tipos.add("Excabadora");
        tipos.add("Fresadora");
        tipos.add("Gabarra");
        tipos.add("Grua");
        tipos.add("Jeep");
        tipos.add("Minicargadora");
        tipos.add("Mononiveladora");
        tipos.add("Planta asfaltica");
        tipos.add("Plataformas");
        tipos.add("Retroexcavadora");
        tipos.add("Rodillo estático");
        tipos.add("Rodillo neumático");
        tipos.add("Rodillo vibratorio");
        tipos.add("Rodillo mixto");
        tipos.add("Seleccionadora");
        tipos.add("Tanquero");
        tipos.add("Trackdrill");
        tipos.add("Tractor segador");
        tipos.add("Tractor");
        tipos.add("Trailer");
        tipos.add("Trituradora 1era");
        tipos.add("Trituradora 2da");
        tipos.add("Vehículo");
        tipos.add("Volquete");
        

        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public void generaReporte() throws IOException, DocumentException {
        GeneradorPdf pu = new GeneradorPdf();
        System.out.println("entro a generar reporte");
        pu.createPDF(tipoSeleccionados, getListaVehiculos(), findAll(EstadoVehiculo.class), mesyanioSeleccionados);
    }

    public List<EstadoVehiculo> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<EstadoVehiculo> listaEstados) {
        this.listaEstados = listaEstados;
    }

    public String[] getMesyanioSeleccionados() {

        if (mesyanioSeleccionados != null) {

            for (String s : mesyanioSeleccionados) {
                System.out.println("valor de s " + s);
            }
            if (mesyanioSeleccionados.length == 0) {
                System.out.println("fijando null en forr");

                mesyanioSeleccionados = null;

            }
        }

        return mesyanioSeleccionados;
    }

    public void setMesyanioSeleccionados(String[] mesyanioSeleccionados) {
        this.mesyanioSeleccionados = mesyanioSeleccionados;
    }

    public List<String> getMesyanio() {

        Calendar fechamayor = Calendar.getInstance();
        Calendar fechamenor = Calendar.getInstance();

        for (EstadoVehiculo ev : listaEstados) {
            if (ev.getFechaEntrada().compareTo(fechamayor.getTime()) > 0) {
                fechamayor.setTime(ev.getFechaEntrada());
            }
            if (ev.getFechaEntrada().compareTo(fechamenor.getTime()) <= 0) {
                fechamenor.setTime(ev.getFechaEntrada());
            }

        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM/yyyy");
        List<String> listaFechas = new ArrayList<String>();
        listaFechas.add(sdf.format(fechamenor.getTime()));
        Integer mesMenor = fechamenor.get(Calendar.MONTH) + 1;
        Integer mesMayor = fechamayor.get(Calendar.MONTH) + 1;
        Integer anioMenor = fechamenor.get(Calendar.YEAR) + 1;
        Integer anioMayor = fechamayor.get(Calendar.YEAR) + 1;
        Integer cantMeses = 0;
        Integer aux = 0;
        if (anioMenor < anioMayor) {

            cantMeses = 12 - mesMenor;

            for (int i = anioMenor; i < anioMayor; i++) {

                aux = aux + 12;
            }
            aux = aux - (12 - mesMayor);

            cantMeses = cantMeses + aux;

        } else {

            cantMeses = mesMayor - mesMenor;
        }

        for (int i = 1; i <= cantMeses; i++) {
            fechamenor.add(Calendar.MONTH, 1);
            mesMenor = fechamenor.get(Calendar.MONTH) + 1;

            listaFechas.add(sdf.format(fechamenor.getTime()));
        }

//  
        return listaFechas;
    }

    public void setMesyanio(List<String> mesyanio) {
        this.mesyanio = mesyanio;
    }

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String obtenerEvaluacion1() {
        obtenerEvaluacion();
        return "/paginas/admin/vehiculo/verestadoMecanico.xhtml?faces-redirect=true";

    }

    public String obtenerEvaluacion() {

        List<Property> propiedades = servgen.buscarTodoscoincidencia(Property.class, Property.class.getSimpleName(), Property_.type.getName(), "org.mtop.modelo.dinamico.Structure");
        List<BussinesEntityAttribute> bea;
        Integer suma = 0;
        Integer contador = 0;
        evaluacion = "Bueno";
        //rompe el bucle del primer for
        fuera:
        for (Property p : propiedades) {
            System.out.println("propiedadesss " + p.getName());
            bea = getInstance().findBussinesEntityAttribute(p.getName());
            for (BussinesEntityAttribute a : bea) {
                if (a.getProperty().getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
                    System.out.println("nombre propiedad " + p.getName());
                    System.out.println("nombre de la propiedad de la propiedad " + a.getName());
                    System.out.println("valor" + a.getValue());
                    //si la evaluacion parte es muy importante =100 y la evaluacion es mala
                    //inmediatamente se evalua como malo y sale del ciclo
                    if(a.getProperty().getEvaluacionParte().equals(100) && a.getValue().equals("Malo")){
                        evaluacion = "Malo";
                        //para romper los bucles anidados
                        break fuera;
                    }
                    //muelles-amortiguador 80
                    //forros 80
                    //todos parte motor 100
                    //todos parte SistemaElectrico 100
                    //todos parte transmision 100
                    //articulacionesTerminales 100
                    //ejeDelantero 100
                    //ejePosterior 100
                    //pinesBocines 100
                    // bombaDireccion 100
                    System.out.println("nombre " + a.getName() + "valor: " + a.getValue());
                    if (a.getValue().equals("Bueno")) {
                        suma += 100;
                        contador++;
                    }
                    if (a.getValue().equals("Malo")) {
                        suma += 80;
                        contador++;
                    }

                }
            }

        }
        if (!evaluacion.equals("Malo")) {

            suma = suma / contador;
            if (suma >= 90) {
                evaluacion = "Bueno";
            } else {
                evaluacion = "Malo";
            }

        }
        return evaluacion;

    }

}
