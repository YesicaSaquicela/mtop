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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controller.BussinesEntityHome;
import org.mtop.model.BussinesEntityType;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.Producto;
//import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorRequisicion extends BussinesEntityHome<Requisicion> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<Requisicion> listaRequisicion = new ArrayList<Requisicion>();
    private Long idVehiculo;
    private Vehiculo vehiculo;
    private List<Vehiculo> vehiculos;
    private long idPartidaC = 0l;
    private PartidaContabilidad partidaC;
    private List<PartidaContabilidad> listaPartida;
    private String numeroRequisicion;
    private ControladorItemRequisicion cir = new ControladorItemRequisicion();
    List<Producto> listaProductos = new ArrayList<Producto>();
    Producto pro;

    public Producto getPro() {
        System.out.println("reto::::::::::::::::");
        System.out.println(pro);
        return pro;
    }

    public void setPro(Producto pro) {
        System.out.println("fijandooooooooooooooooooooooooooooo"+pro);
        this.pro = pro;
        cir.getInstance().setProducto(pro);
    }
    public void guardarProducto(Producto p){
        System.out.println("entra guarrrrrrrrrrrrrrrrrrrrrrrdarrrrrrrrrrrrrrr");
        cir.getInstance().setProducto(pro);
        pro=cir.getInstance().getProducto();
        if(cir.getInstance().getProducto()!=null){
            System.out.println("pro");
            System.out.println("producto "+cir.getInstance().getProducto().getCodigo());
        }else{
            System.out.println("no hay producto");
        }
        
    }

    public List<Producto> getListaProductos() {
        
        List<Producto> pro=new ArrayList<Producto>();
        
        for (Producto p : findAll(Producto.class)) {
            
            if (p.getCantidad() > 0) {
            
                pro.add(p);
                
            }
        }
        listaProductos=pro;
        
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public long getIdPartidaC() {
        if (getRequisicionId() != null && idPartidaC == 0l) {
            idPartidaC = getInstance().getPartidaContabilidad().getId();
        }

        return idPartidaC;
    }

    public void setIdPartidaC(long idPartidaC) {
        this.idPartidaC = idPartidaC;
    }

    public String getNumeroRequisicion() {
        if (getId() == null) {
            System.out.println("numero" + getInstance().getNumRequisicion());
            List<Requisicion> lista = findAll(Requisicion.class);
            int t = lista.size();
            if (t < 9) {
                setNumeroRequisicion("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setNumeroRequisicion("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setNumeroRequisicion("0".concat(String.valueOf(t + 1)));
                    } else {
                        setNumeroRequisicion(String.valueOf(t + 1));
                    }
                }
            }
        } else {
            setNumeroRequisicion(getInstance().getNumRequisicion());
        }

        return numeroRequisicion;

    }

    public void agregarItem() {
        System.out.println("lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllcraerll");
        if (cir.getInstance().getCantidad().equals("") || cir.getInstance().getUnidadMedida().equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios."));

        } else {
            cir.listaItemsRequisicion.add(cir.getInstance());
            cir.setInstance(new ItemRequisicion());

        }
        System.out.println("enviandi cantidad");
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        editarItem(cir.getInstance());

    }
    

    public void editarItem(ItemRequisicion itemReq) {
        System.out.println("lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
        System.out.println("Item>>>>>" + cir.getInstance().getCantidad());;
        System.out.println("REMOVE>>>>>>>>>>>." + itemReq);

        int con = 0;
        for (ItemRequisicion i : cir.listaItemsRequisicion) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
                cir.listaItemsRequisicion.remove(con);
                System.out.println("a fijar cant" + i.getCantidad());
                System.out.println("a fijar unid" + i.getUnidadMedida());
                cir.setInstance(i);
                System.out.println("a fijada" + cir.getInstance().getCantidad());
                System.out.println("a fijada" + cir.getInstance().getUnidadMedida());
                break;

            }
            con++;

        }
        System.out.println("tama;o de la lista" + cir.listaItemsRequisicion.size());

    }

    public void setNumeroRequisicion(String numRegistro) {
        this.numeroRequisicion = numRegistro;
        getInstance().setNumRequisicion(this.numeroRequisicion);

    }

    public List<PartidaContabilidad> getListaPartida() {
        return listaPartida;
    }

    public void setListaPartida(List<PartidaContabilidad> listaPartida) {
        this.listaPartida = listaPartida;
    }

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    private boolean skip;
    //private static Logger logger = Logger.getLogger(UserWizard.class.getName());

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    public ControladorItemRequisicion getCir() {
        return cir;
    }

    public void setCir(ControladorItemRequisicion cir) {
        this.cir = cir;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {

        if (skip) {
            skip = false;   //reset in case user goes back  

            return "confirm";
        } else {
            System.out.println("pasoooo");
            if (event.getOldStep().equals("address") && this.vehiculo.getId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                return event.getOldStep();
            } else {

                if (event.getOldStep().equals("items") && this.cir.listaItemsRequisicion.isEmpty()) {
                    System.out.println("estas vaciaaaaaa");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item al plan de mantenimiento"));

                    return event.getOldStep();
                } else {

                    return event.getNewStep();
                }
            }

        }
    }

    public List<Vehiculo> getVehiculos() {

        System.out.println("ENTRO A BUSCAR>vehiculos>>>>>>>>>>");
        vehiculos = findAll(Vehiculo.class);
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Long getRequisicionId() {
        return (Long) getId();
    }

    public void setRequisicionId(Long requisicionId) {
        setId(requisicionId);

    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
        vehiculo = findById(Vehiculo.class, idVehiculo);

    }

    public void asignarIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
        System.out.println("ID del vehiculo>>>>><<<<<<<<<<<<<<<<<<<<<" + idVehiculo);
        vehiculo = findById(Vehiculo.class, idVehiculo);

    }

    public Vehiculo getVehiculo() {
        System.out.println("jsjsjs");
        if (getRequisicionId() != null) {
            System.out.println("vehiculo " + getInstance().getVehiculo());
            vehiculo = getInstance().getVehiculo();
        }

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());

        this.vehiculo = vehiculo;
    }

    @TransactionAttribute   //
    public Requisicion load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<Requisicion> getListaRequisicion() {
        return listaRequisicion;
    }

    public void setListaRequisicion(List<Requisicion> listaRequisicion) {
        this.listaRequisicion = listaRequisicion;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaRequisicion = findAll(Requisicion.class);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;

        listaPartida = findAll(PartidaContabilidad.class);
        listaProductos = findAll(Producto.class);
        cir = new ControladorItemRequisicion();
        cir.setInstance(new ItemRequisicion());
        pro=new Producto();
    }

    @Override
    protected Requisicion createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Requisicion.class.getName());
        Date now = Calendar.getInstance().getTime();
        Requisicion requisicion = new Requisicion();
        requisicion.setCreatedOn(now);
        requisicion.setLastUpdate(now);
        requisicion.setActivationTime(now);

        requisicion.setType(_type);
        requisicion.buildAttributes(bussinesEntityService);  //
        return requisicion;
    }

    @Override
    public Class<Requisicion> getEntityClass() {
        return Requisicion.class;
    }

    @TransactionAttribute
    public String guardar() {
//        if (this.vehiculo.getId() == null) {
//            mensaje="necesita un asignar un vehiculo";
//             return "/paginas/requisicion/crear.xhtml?faces-redirect=true";
//        } else {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        getInstance().setVehiculo(vehiculo);
        System.out.println("PRESENTADNOIDE requisicion>>>>" + vehiculo);
        PartidaContabilidad p = servgen.buscarPorId(PartidaContabilidad.class, idPartidaC);
        getInstance().setPartidaContabilidad(p);
        System.out.println("id de ala partidaaaaaaaaaaaaa" + p.getId());

        try {
            if (getInstance().isPersistent()) {
                System.out.println("ingresa a editar>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ingresa a creaaar>>>>>>>");
                //  getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/requisicion/lista.xhtml?faces-redirect=true";

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
        return "/paginas/requisicion/lista.xhtml?faces-redirect=true";
    }

}
