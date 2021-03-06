/*
 SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
 VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
 Copyright (C) 2014  Romero Carla, Saquicela Yesica

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mtop.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carla
 * @author yesica
 *
 */
@Named
@ViewScoped
public class ControladorPartidaContabilidad extends BussinesEntityHome<PartidaContabilidad> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<PartidaContabilidad> listaPartidaC = new ArrayList<PartidaContabilidad>();
    private String mensaje = "";
    private String vista = "";
    private String palabrab = "";
    private List<PartidaContabilidad> listaPartidaC2 = new ArrayList<PartidaContabilidad>();

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public void buscar() {

        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<PartidaContabilidad> lp = new ArrayList<PartidaContabilidad>();
        //buscando por numero de partidalistaPartidaC
        for (PartidaContabilidad p : listaPartidaC2) {
            String resultado = p.concatenarPartida();
            if (p.getDescripcion().toLowerCase().contains(palabrab.toLowerCase()) && !lp.contains(p)) {
                lp.add(p);
            } else {
                if (resultado.contains(palabrab) && !lp.contains(p)) {
                    lp.add(p);
                } else {
                    if (p.getTipo().toLowerCase().contains(palabrab.toLowerCase()) && !lp.contains(p)) {
                        lp.add(p);
                    }
                }
            }
        }

        if (lp.isEmpty()) {

            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:No se ha encontrado", palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaPartidaC = lp;
        }

    }

    public void limpiar() {
        palabrab = "";
        listaPartidaC = listaPartidaC2;
    }

    public ArrayList<String> autocompletar(String query) {

        ArrayList<String> ced = new ArrayList<String>();

        for (PartidaContabilidad partidaContabilidad : listaPartidaC) {

            String resultado = partidaContabilidad.concatenarPartida();

            if (resultado.contains(query)) {

                if (!ced.contains(partidaContabilidad.concatenarPartida())) {
                    ced.add(resultado);
                }
            }
            if (partidaContabilidad.getDescripcion().toLowerCase().contains(query.toLowerCase()) && !ced.contains(partidaContabilidad.getDescripcion())) {
                ced.add(partidaContabilidad.getDescripcion());
            }
            if (partidaContabilidad.getTipo().toLowerCase().contains(query.toLowerCase()) && !ced.contains(partidaContabilidad.getTipo())) {
                ced.add(partidaContabilidad.getTipo());
            }

        }
        return ced;

    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getMensaje() {

        System.out.println("mensaje a retornar" + mensaje);
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getPartidaCId() {
        return (Long) getId();

    }

    public void setPartidaCId(Long partidaCId) {
        setId(partidaCId);
    }

    @TransactionAttribute   //
    public PartidaContabilidad load() {
        if (isIdDefined()) {
            wire();
        }
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();

    }

    public List<PartidaContabilidad> getListaPartidaC() {
        return listaPartidaC;
    }

    public void setListaPartidaC(List<PartidaContabilidad> listaPartidaC) {
        this.listaPartidaC = listaPartidaC;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaPartidaC = findAll(PartidaContabilidad.class);
        listaPartidaC2 = listaPartidaC;
        System.out.println("liata en init>>>>>>>>" + listaPartidaC);

        System.out.println("inicializo el mesaje en init" + mensaje);

    }

    @Override
    protected PartidaContabilidad createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(PartidaContabilidad.class.getName());
        Date now = Calendar.getInstance().getTime();
        PartidaContabilidad partidaC = new PartidaContabilidad();
        partidaC.setCreatedOn(now);
        partidaC.setLastUpdate(now);
        partidaC.setActivationTime(now);

        partidaC.setType(_type);
        partidaC.buildAttributes(bussinesEntityService);  //
        return partidaC;
    }

    @Override
    public Class<PartidaContabilidad> getEntityClass() {
        return PartidaContabilidad.class;
    }

    public boolean verificarPartida() {
        List<PartidaContabilidad> lpc = findAll(PartidaContabilidad.class);
        ;
        boolean ban = true;
        //si viene de editar... comprueba que el ide se diferente de 

        for (PartidaContabilidad partidaContabilidad : lpc) {

            if (partidaContabilidad.concatenarPartida().equals(getInstance().concatenarPartida())
                    && !partidaContabilidad.getId().equals(getInstance().getId())) {

                ban = false;
                break;
            }
        }
        return ban;

    }

    @TransactionAttribute
    public String guardar() {

        if (verificarPartida() == true) {
            Date now = Calendar.getInstance().getTime();
            getInstance().setLastUpdate(now);

            try {

                if (getInstance().isPersistent()) {
                    save(getInstance());

                } else {
                    getInstance().setEstado(true);
                    create(getInstance());
                    save(getInstance());
                }
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

            return "/paginas/secretario/partidaContabilidad/lista.xhtml?faces-redirect=true";

        } else {
            mensaje = "La partida de contabilidad ya exite";

            return "";
        }

    }

    @Transactional
    public void inactivarPartida(Long idPartidac) {

        setId(idPartidac);
        setInstance(findById(PartidaContabilidad.class, idPartidac));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);
        servgen.actualizar(getInstance());
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La partida de contabilidad se desactivo exitosamente ");
        FacesContext.getCurrentInstance().addMessage("", msg);
        listaPartidaC = servgen.buscarTodos(PartidaContabilidad.class);
        listaPartidaC2 = listaPartidaC;

    }

    @Transactional
    public void activarPartida(Long idPartidac) {

        setId(idPartidac);
        getInstance().setEstado(true);
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        servgen.actualizar(getInstance());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La partida de contabilidad se activo exitosamente"));

        listaPartidaC = servgen.buscarTodos(PartidaContabilidad.class);
        listaPartidaC2 = listaPartidaC;
    }

}
