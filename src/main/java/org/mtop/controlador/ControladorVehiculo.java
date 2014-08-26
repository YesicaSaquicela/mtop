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
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.exolab.castor.types.DateTime;
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
import org.mtop.modelo.Vehiculo_;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;

import org.mtop.modelo.profile.Profile;
import static org.mtop.modelo.profile.Profile_.username;

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
    private String mensaje = "";
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

    private List<Vehiculo> listVehiculos2 = new ArrayList<Vehiculo>();

    public String obtenerAlerta(Integer kma) {
        Integer kmt = kma + 1000;
        System.out.println("kilometraje actual +1000" + kmt);
        Integer proKmj = obtenerKilometraje(kma);
        System.out.println("prokilometra" + proKmj);
        if (kmt.equals(proKmj)) {
            System.out.println("entro a 1if");
            return "Atencion..! Necesita acercarse a realizar el mantenimiento";
        } else {
            if (kmt > proKmj) {
                System.out.println("entro a obtener 2oif");
                return "Urgente necesita hacer mantenimiento del vehículo";
            } else {
                System.out.println("3 if");
                return "";
            }
        }
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
            System.out.println("entro>>>>>>>>v null");
            try {
                obtenerPlacaVehiculo(placa);
                return false;
            } catch (NoResultException e) {
                return true;
            }
        } else {
            System.out.println("cual envia a elim" + getInstance().getId());
            System.out.println("lista v antes" + listav);

            List<Vehiculo> lv = new ArrayList<Vehiculo>();
            for (Vehiculo v : listav) {
                if (!v.getPlaca().equals(findById(Vehiculo.class, getInstance().getId()).getPlaca())) {
                    lv.add(v);
                }
            }
            listav = lv;
            System.out.println("lista v" + listav);
            System.out.println("ide instance" + getInstance().getId());
            for (Vehiculo v : listav) {
                System.out.println("entro al for" + v.getId());

                if (v.getPlaca().equals(getInstance().getPlaca())) {
                    System.out.println("entro al if>>>");
                    obtenerPlacaVehiculo(placa);
                    return false;
                }
            }
            System.out.println("nunca entro al for");
            return true;
        }

    }

    public Vehiculo obtenerNumRegistro(final String numregistro) throws NoResultException {
        System.out.println("obtener numregisto");
        TypedQuery<Vehiculo> query = em.createQuery("SELECT p FROM Vehiculo p WHERE p.numRegistro = :numregistro", Vehiculo.class);
        System.out.println("query" + query);
        query.setParameter("numregistro", numregistro);
        System.out.println("query.getSingleResult()" + query.getSingleResult());
        Vehiculo result = query.getSingleResult();
        System.out.println("resultdo" + result);
        return result;
    }

    public boolean numRegistroUnico(String numregistro) {
        System.out.println("entro al validador");
        List<Vehiculo> listavs = findAll(Vehiculo.class);
        if (getInstance().getId() == null) {
            System.out.println("entro>>>>>>>>>>");
            try {
                obtenerNumRegistro(numregistro);
                return false;
            } catch (NoResultException e) {
                return true;
            }
        } else {
            System.out.println("cual envia a elim" + getInstance().getId());
            System.out.println("lista v antes" + listavs);

            List<Vehiculo> lv = new ArrayList<Vehiculo>();
            for (Vehiculo v : listavs) {
                if (!v.getNumRegistro().equals(findById(Vehiculo.class, getInstance().getId()).getNumRegistro())) {
                    lv.add(v);
                }
            }
            listavs = lv;
            System.out.println("lista v" + listavs);
            System.out.println("ide instance" + getInstance().getId());
            for (Vehiculo v : listavs) {
                System.out.println("entro al for" + v.getId());

                if (v.getNumRegistro().equals(getInstance().getNumRegistro())) {
                    System.out.println("entro al if>>>");
                    obtenerNumRegistro(numregistro);
                    return false;
                }
            }
            System.out.println("nunca entro al if");
            return true;
        }

    }

    public boolean verificarPlaca(String placa) {
        System.out.println("placa ingresada" + placa);
        String l1 = "ABUCXHOEWGILRMVNSPQYJKTZ";
        String l2 = "ABCDEFGHIFKLMNOPQRSTUWXYZ";
        String l3 = "ABCDEFGHIFKLMNOPQRSTUWXYZ";
        String n = "1234567890";
        String t = "CDOIAT";
        System.out.println("placa ingersa fuera>>>" + placa);

        if (placa.length() >= 7) {

            if (placa.length() == 8) {
                if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                        && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "") && n.contains(placa.charAt(7) + "")) {
                    System.out.println("placa ingresada" + placa);
                    return true;
                } else {
                    System.out.println("entro 1false");
                    return false;
                }

            } else {
                System.out.println("entro al no");
                if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                        && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                    System.out.println("placa ingresada" + placa);
                    return true;
                } else {
                    if (l1.contains(placa.charAt(0) + "") && l2.contains(placa.charAt(1) + "") && l3.contains(placa.charAt(2) + "")
                            && placa.charAt(3) == '-' && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "") && n.contains(placa.charAt(7) + "")) {
                        System.out.println("placa ingresada" + placa);
                        return true;
                    } else {
                        if (placa.substring(0, 2).equals("CC") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                            System.out.println("placa ingresada" + placa);
                            return true;
                        } else {
                            if (placa.substring(0, 2).equals("CD") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                                System.out.println("placa ingresada" + placa);
                                return true;
                            } else {
                                if (placa.substring(0, 2).equals("OI") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                                    System.out.println("placa ingresada" + placa);
                                    return true;
                                } else {
                                    if (placa.substring(0, 2).equals("AT") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                                        System.out.println("placa ingresada" + placa);
                                        return true;
                                    } else {
                                        if (placa.substring(0, 2).equals("IT") && placa.charAt(2) == '-' && n.contains(placa.charAt(3) + "") && n.contains(placa.charAt(4) + "") && n.contains(placa.charAt(5) + "") && n.contains(placa.charAt(6) + "")) {
                                            System.out.println("placa ingresada" + placa);
                                            return true;
                                        } else {
                                            System.out.println("entro 2dofalse");
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
            System.out.println("entra 3 false");
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

    public boolean obtenerAñoV(String fanio) {

        String fechaFormato = "";
        Date fEntrada = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy");
        fechaFormato = formatter.format(fEntrada);
        System.out.println("fecha antes>>>" + fechaFormato);
        Integer fa = Integer.parseInt(fechaFormato);
        Integer fv = Integer.parseInt(fanio);
        System.out.println("fecha k llega" + fv);
        System.out.println("fecha formato" + fa);
        if (fv <= fa && fv >= 1980) {
            System.out.println("entro a true");
            return true;
        } else {
            System.out.println("entro a false");
            return false;
        }

    }

    public String obtenerUltimoEstadoV(Vehiculo vehiculo) {
        String estado = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getNombre();
        return estado;
    }

    public String obtenerUltimaFechaV(Vehiculo vehiculo) {
        String fechaFormato = "";
        System.out.println("lista de vehiculos" + listaVehiculos);
        System.out.println("vehiculooooo" + vehiculo);
        Date fEntrada = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getFechaEntrada();

        if (fEntrada != null) {
            System.out.println("entro a fecha");
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fEntrada);
        }
        System.out.println("fecha a reotornanr" + fechaFormato);
        return fechaFormato;
    }

    public String obtenerUltimaUbicacionV(Vehiculo vehiculo) {
        System.out.println("lista estadoooos\n\n\n" + listaEstados);
        for (EstadoVehiculo ev : listaEstados) {
            System.out.println("ev" + ev.getVehiculo().getListaEstados());

        }
        System.out.println("vehiculo" + vehiculo);
        System.out.println("vehiculoestados" + vehiculo.getListaEstados().get(0));
        String ubicacion = vehiculo.getListaEstados().get(vehiculo.getListaEstados().size() - 1).getUbicacion();
        return ubicacion;
    }

    public String obtenerUltimaUbicacionV2(Vehiculo vehiculo, List<EstadoVehiculo> list) {
        System.out.println("lista estadoooos\n\n\n" + list);
        for (EstadoVehiculo ev : list) {
            System.out.println("ev" + ev.getVehiculo().getListaEstados());

        }
        System.out.println("vehiculo" + vehiculo);
        System.out.println("vehiculoestados" + vehiculo.getListaEstados().get(0));
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
        List<Vehiculo> lvs = new ArrayList<Vehiculo>();
        for (Vehiculo veh1 : listVehiculos2) {
            if (veh1.getNumRegistro().contains(palabrab)) {
                lvs.add(veh1);
            } else {
                if (veh1.getPlaca().contains(palabrab)) {
                    lvs.add(veh1);
                }
            }
        }

        if (lvs.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
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
            if (vh.getPlaca().contains(query)) {
                ced.add(vh.getPlaca());
            }
        }
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

    public Long getVehiculoId() {
        System.out.println("IIIIDEE" + getId());

        return (Long) getId();
    }

    public void setVehiculoId(Long vehiculoId) {

        System.out.println("id a fijar" + vehiculoId);

        setId(vehiculoId);
        idPersona = getInstance().getPersona().getId();
    }

    public void fijarNullVehiculo() {
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
        listVehiculos2 = listaVehiculos;
        idPersona = 0l;
        listaPersonas = findAll(Profile.class);
        List<Profile> lp = new ArrayList<Profile>();
        for (Profile persona : findAll(Profile.class)) {
            System.out.println("personas>>>>" + persona.getTipo());
            if (persona.getTipo() != null) {
                if (persona.getTipo().equals("Conductor")) {
                    getInstance().setPersona(persona);
                    lp.add(persona);
                }
            }

        }
        listaPersonas = lp;
        System.out.println("lista de personas>>>>" + listaPersonas);
        listaEstados = findAll(EstadoVehiculo.class);
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
        Profile psolicita = servgen.buscarPorId(Profile.class, idPersona);
        getInstance().setPersona(psolicita);
        System.out.println("persona conductor" + getInstance().getPersona());
//        if (!getInstance().getPlaca().equals("")) {
//            System.out.println("entro>>>>>" + getInstance().getPlaca());
//            listaVehiculos.remove(getInstance().getPlaca());
//        }
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
//        getInstance().findBussinesEntityAttribute(numeroRegistro).size();
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

    public String[] getTipoSeleccionados() {
        System.out.println("tipos seleccionados en vehiculo"+tipoSeleccionados);
        return tipoSeleccionados;
    }

    public void setTipoSeleccionados(String[] tipoSeleccionados) {
        this.tipoSeleccionados = tipoSeleccionados;
    }

    public List<String> getTipos() {
        tipos = new ArrayList<String>();
        tipos.add("tanquero");
        tipos.add("volquete");
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
        System.out.println("mes i anio"+mesyanioSeleccionados);
        return mesyanioSeleccionados;
    }

    public void setMesyanioSeleccionados(String[] mesyanioSeleccionados) {
        this.mesyanioSeleccionados = mesyanioSeleccionados;
    }

    public List<String> getMesyanio() {

        Calendar fechamayor = Calendar.getInstance();
        Calendar fechamenor = Calendar.getInstance();
        System.out.println("fecha ahora geyt time>>>" + fechamayor.getTime());

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
            System.out.println("entro a menor anio presentando cant meses antes for" + cantMeses);
            for (int i = anioMenor; i < anioMayor; i++) {

                aux = aux + 12;
                System.out.println("aumento dose presentando aux" + aux);
            }
            aux = aux - (12 - mesMayor);
            System.out.println("restando aux desde mes uno sigueinte total meses" + aux);
            cantMeses = cantMeses + aux;

        } else {
            System.out.println("NOOOO entro a anio menor");
            cantMeses = mesMayor - mesMenor;
        }
        System.out.println("total meses a presentar" + cantMeses);
        for (int i = 1; i <= cantMeses; i++) {
            fechamenor.add(Calendar.MONTH, 1);
            mesMenor = fechamenor.get(Calendar.MONTH) + 1;
            System.out.println("siginete fecha menor" + sdf.format(fechamenor.getTime()));
            listaFechas.add(sdf.format(fechamenor.getTime()));
        }

//        while (mesMenor < mesMayor) {
//// Haces lo que tu quieres, para ese dia;
//// Incrementas una semana (86400000 es un dia en milisegundos)
//           
//        }
        return listaFechas;
    }

    public void setMesyanio(List<String> mesyanio) {
        this.mesyanio = mesyanio;
    }

}
