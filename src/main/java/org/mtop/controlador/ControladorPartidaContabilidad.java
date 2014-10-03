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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.PartidaContabilidad_;
import org.mtop.modelo.Producto;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
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
        System.out.println("entro a buscar>>>>>>>>>>>" + palabrab);
        for (PartidaContabilidad p : listaPartidaC2) {
            String resultado = p.concatenarPartida();
            if (p.getDescripcion().toLowerCase().contains(palabrab.toLowerCase())) {
                lp.add(p);
            } else {
                if (resultado.contains(palabrab)) {
                    lp.add(p);
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
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (PartidaContabilidad partidaContabilidad : listaPartidaC) {

            String resultado = partidaContabilidad.concatenarPartida();

            if (resultado.contains(query)) {

                if (!ced.contains(partidaContabilidad.concatenarPartida())) {
                    ced.add(resultado);
                }
            } else {
                if (partidaContabilidad.getDescripcion().toLowerCase().contains(query.toLowerCase()) && !ced.contains(partidaContabilidad.getDescripcion().toLowerCase())) {
                    ced.add(partidaContabilidad.getDescripcion());
                }
            }
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void vistaC(ActionEvent event) {
        System.out.println("entro a evento>>>>>>>");
        final Long id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("partidaId"));
        getInstance().setId(id);
        System.out.println("se fijo el id>>>>>" + getInstance().getId());

    }

    public String vistaC() {
        System.out.println("retornando vista crear>>>>>>");
        return "/paginas/secretario/partidaContabilidad/crear.xhtml?faces-redirect=true";
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getMensaje() {
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
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaPartidaC = servgen.buscarTodos(PartidaContabilidad.class);
        listaPartidaC2 = listaPartidaC;
        System.out.println("liata en init>>>>>>>>" + listaPartidaC);
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

        //fichaMedic.setResponsable(null);    //cambiar atributo a 
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
        System.out.println("LISTA PARTIDA>>>>>>." + lpc);
        boolean ban = true;
        System.out.println("lista antes de borrar" + lpc);
        //si viene de editar... comprueba que el ide se diferente de 
        System.out.println("ide antes de borrar del k llega>>>>" + getInstance().getId());
        if (getInstance().getId() != null) {
            System.out.println("ENTRO A BORRAR>>>>>>>");
            lpc.remove(findById(PartidaContabilidad.class, getInstance().getId()));

        }
        System.out.println("lista despues de borrar" + lpc);
        for (PartidaContabilidad partidaContabilidad : lpc) {
            System.out.println("ENTRANDO AL FOR>>>>.");
            if (partidaContabilidad.concatenarPartida().equals(getInstance().concatenarPartida())) {
                System.out.println("entro a verificar>>>>>>>" + ban);
                ban = false;
                break;
            }
        }
        System.out.println("salio del for>>>>>");
        return ban;

    }

    @TransactionAttribute
    public String guardar() {

        if (verificarPartida() == true) {
            Date now = Calendar.getInstance().getTime();
            getInstance().setLastUpdate(now);

            try {

                if (getInstance().isPersistent()) {
                    System.out.println("Entro a Editar>>>>>>>>");
                    save(getInstance());

                } else {
                    System.out.println("Entro a crear>>>>>>>>");
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
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! ", "La partida de contabilidad ya exite ");
            FacesContext.getCurrentInstance().addMessage("", msg);
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
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La partida de contabilidad se inactivo exitosamente:  ");
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La partida de contabilidad se activo exitosamente" ));

      listaPartidaC = servgen.buscarTodos(PartidaContabilidad.class);
        listaPartidaC2 = listaPartidaC;
    }

}
