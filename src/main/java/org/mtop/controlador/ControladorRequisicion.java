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
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;
import org.mtop.util.UI;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.Property;
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
    private Integer maximo;
    private ControladorItemRequisicion cir = new ControladorItemRequisicion();
    List<Producto> listaProductos = new ArrayList<Producto>();
    private String palabrab;
    private List<SolicitudReparacionMantenimiento> listaSolicitudes;
    private SolicitudReparacionMantenimiento solicitudrep;
    List<ItemRequisicion> listaItemsRequisicion = new ArrayList<ItemRequisicion>();

    public List<ItemRequisicion> getListaItemsRequisicion() {
        return listaItemsRequisicion;
    }

    public void setListaItemsRequisicion(List<ItemRequisicion> listaItemsRequisicion) {
        this.listaItemsRequisicion = listaItemsRequisicion;
    }
    
    

    Producto pro;

    public SolicitudReparacionMantenimiento getSolicitudrep() {
        return solicitudrep;
    }

    public void setSolicitudrep(SolicitudReparacionMantenimiento solicitudrep) {
        this.solicitudrep = solicitudrep;
    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitudes() {
        for (SolicitudReparacionMantenimiento sol : listaSolicitudes) {
            System.out.println("id vehiculo soli" + sol.getVehiculo().getId());
            System.out.println("id vehiculo req" + getInstance().getVehiculo().getId());
            if (sol.getVehiculo().getId() != this.vehiculo.getId()) {
                listaSolicitudes.remove(sol);
            }
        }
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<SolicitudReparacionMantenimiento> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public void buscar() {
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<Requisicion> le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrab);
        //buscando por fechas
        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), palabrab);
        for (Requisicion requisicion : lef) {
            if (!le.contains(requisicion)) {
                le.add(requisicion);
            }
        }

        Vehiculo v = new Vehiculo();
        UI ui = new UI();
        for (Requisicion requisicion : findAll(Requisicion.class)) {
            v = findById(Vehiculo.class, requisicion.getVehiculo().getId());
            List<Property> lp = ui.getProperties(v);
            for (Property p : lp) {
                if (p.getType().equals("org.mtop.modelo.dinamico.Structure") && p.getName().equals("chasis")) {

                    List<BussinesEntityAttribute> lbea = v.findBussinesEntityAttribute(p.getName());
                    for (BussinesEntityAttribute a : lbea) {
                        if (a.getName().equals("serie")) {
                            if (a.getValue().toString().contains(palabrab)) {
                                if (!le.contains(requisicion)) {
                                    le.add(requisicion);
                                }

                            }
                        }
                    }
                }
            }
            if (v.getNumRegistro().contains(palabrab)) {
                if (!le.contains(requisicion)) {
                    le.add(requisicion);
                }
            }

        }

        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaRequisicion = le;
        }

    }

    public void limpiar() {
        palabrab = "";
        listaRequisicion = findAll(Requisicion.class);
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        System.out.println("PAlabrafijando >>" + palabrab);
        this.palabrab = palabrab;
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), query);

        for (Requisicion requisicion : lr) {
            System.out.println("econtro uno " + requisicion.getNumRequisicion());
            ced.add(requisicion.getNumRequisicion());
        }

        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), query);
        for (Requisicion requisicion : lef) {
            ced.add(requisicion.getFechaRequisicion().toString());
        }

        Vehiculo v = new Vehiculo();
        UI ui = new UI();
//        for (Requisicion requisicion : findAll(Requisicion.class)) {
//            v = findById(Vehiculo.class, requisicion.getVehiculo().getId());
//            BussinesEntityAttribute bea=v.getBussinessEntityAttribute("chasis");
//            bea=bea.getBussinesEntity().getBussinessEntityAttribute("serie");
//            System.out.println("beaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+bea.getName());
//            List<Property> lp = ui.getProperties(v);
//            for (Property p : lp) {
//                if (p.getType().equals("org.mtop.modelo.dinamico.Structure") && p.getName().equals("chasis")) {
//
//                    List<BussinesEntityAttribute> lbea = v.findBussinesEntityAttribute(p.getName());
//                    for (BussinesEntityAttribute a : lbea) {
//                        if (a.getName().equals("serie")) {
//                            if (a.getValue().toString().contains(query)) {
//                                ced.add(a.getValue().toString());
//                            }
//                        }
//                    }
//                }
//            }
        if (v.getNumRegistro().contains(query)) {
            ced.add(v.getNumRegistro());
        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public Integer getMaximo() {
        if (getInstance().getTipoAdquisicion().equals("bodega")) {
            if (pro.getId() != null) {
                System.out.println("enrra a obtener cantidaaaaa>>>>>>>>>>>>>>>>>>>>>>>");
                setMaximo(pro.getCantidad());
            } else {
                setMaximo(100);
            }

        } else {
            setMaximo(100);
        }
        System.out.println("lista itemmmm"+cir.getListaItemRequisicion());
        System.out.println("maaaaaaaximo" + maximo);
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Producto getPro() {
        System.out.println("reto::::::::::::::::");

        System.out.println(pro);
        return pro;
    }

    public void setPro(Producto pro) {
        System.out.println("fijandooooooooooooooooooooooooooooo" + pro);
        this.pro = pro;
        cir.getInstance().setDescription(pro.getDescription());
        cir.getInstance().setCantidad(1);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(pro.getDescription());
        cir.getInstance().setProducto(pro);

    }

    public void guardarProducto(Producto p) {
        System.out.println("entra guarrrrrrrrrrrrrrrrrrrrrrrdarrrrrrrrrrrrrrr");
        cir.getInstance().setProducto(pro);
        pro = cir.getInstance().getProducto();
        if (cir.getInstance().getProducto() != null) {
            System.out.println("pro");
            System.out.println("producto " + cir.getInstance().getProducto().getCodigo());
        } else {
            System.out.println("no hay producto");
        }

    }

    public List<Producto> getListaProductos() {
        if (listaProductos.isEmpty()) {
            for (Producto p : findAll(Producto.class)) {

                if (p.getCantidad() > 0) {

                    listaProductos.add(p);

                }
            }
        }

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

    public void guardarItem() {
        for (ItemRequisicion apm : listaItemsRequisicion) {
            Date now = Calendar.getInstance().getTime();
            apm.setRequisicion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cir.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            cir.getInstance().setLastUpdate(now);
            cir.guardar();
        }
        getInstance().setListaItems(listaItemsRequisicion);//fija la lista de actividades al plan de mantenimietno

    }

    public void agregarItem() {

        if (cir.getInstance().getCantidad().equals("") || cir.getInstance().getUnidadMedida().equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios."));

        } else {
            int i = listaProductos.lastIndexOf(pro);

            pro.setCantidad(pro.getCantidad() - cir.getInstance().getCantidad());
            listaProductos.set(i, pro);

            listaItemsRequisicion.add(cir.getInstance());
            cir.setInstance(new ItemRequisicion());

        }

        pro = new Producto();
        cir.getInstance().setCantidad(1);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(" ");
        editarItem(cir.getInstance());

    }

    public void editarItem(ItemRequisicion itemReq) {

        int con = 0;
        for (ItemRequisicion i : listaItemsRequisicion) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
                listaItemsRequisicion.remove(con);
                System.out.println("a fijar cant" + i.getCantidad());
                System.out.println("a fijar unid" + i.getUnidadMedida());
                cir.setInstance(i);
                pro = itemReq.getProducto();

                int j = listaProductos.lastIndexOf(pro);
                System.out.println("pro" + pro.getCantidad());
                System.out.println("pro de lista" + pro.getCantidad());
                pro.setCantidad(itemReq.getCantidad() + listaProductos.get(j).getCantidad());
                listaProductos.set(j, pro);
                pro = listaProductos.get(j);

                System.out.println("a fijada" + cir.getInstance().getCantidad());
                System.out.println("a fijada" + cir.getInstance().getUnidadMedida());
                break;

            }
            con++;

        }

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
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }
            if (event.getOldStep().equals("address") && this.vehiculo.getId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                return event.getOldStep();
            } else {

                if (event.getOldStep().equals("items") && this.listaItemsRequisicion.isEmpty()) {
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
        vehiculo = getInstance().getVehiculo();
        listaItemsRequisicion = new ArrayList<ItemRequisicion>();
        for (ItemRequisicion itr : findAll(ItemRequisicion.class)) {

            if (getInstance().getId() != null) {
                System.out.println("entro a itemsSSSSSSSSSSSSSSSSSSS" + getInstance().getId());

                System.out.println("idde itr" + itr.getRequisicion().getId());
                if (getInstance().getId().equals(itr.getRequisicion().getId())) {
                    System.out.println("fijo un ide");
                    System.out.println("get id de productoooo"+itr.getProducto().getId());
                    listaItemsRequisicion.add(itr);
                }

            }
        }
        System.out.println("lista de items" + listaItemsRequisicion);

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
        System.out.println("jsjsjsjsjsjsjjsjsjsjjsjsjjs");

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());

        this.vehiculo = vehiculo;
        getInstance().setVehiculo(vehiculo);
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
        listaItemsRequisicion = getInstance().getListaItems();
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
        listaSolicitudes = new ArrayList<SolicitudReparacionMantenimiento>();
        System.out.println("lista de solicitudesantteeees" + listaSolicitudes);
        List<Long> l = new ArrayList<Long>();
        for (Requisicion requisicion : listaRequisicion) {
            if (requisicion.getSolicitudReparacionId() != null) {
                if (!l.contains(requisicion.getSolicitudReparacionId().getId())) {
                    System.out.println("añade la solicitudddDDDD" + requisicion.getSolicitudReparacionId());
                    l.add(requisicion.getSolicitudReparacionId().getId());
                }
            }

            if (requisicion.getAprobado()) {

                listaRequisicion.remove(requisicion);
            }

        }
        System.out.println("lista de solicitudesdespuessss" + listaSolicitudes);
        List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);
        for (Long soli : l) {
            ls.remove(findById(SolicitudReparacionMantenimiento.class, soli));
        }
        System.out.println("lista de solicitudesdespuessss final" + listaSolicitudes);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;

        listaPartida = findAll(PartidaContabilidad.class);
        listaProductos = new ArrayList<Producto>();
        cir = new ControladorItemRequisicion();
        cir.setInstance(new ItemRequisicion());
        maximo = 100;
        pro = new Producto();
        palabrab = "";
        solicitudrep = new SolicitudReparacionMantenimiento();

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

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        getInstance().setVehiculo(vehiculo);
        System.out.println("PRESENTADNOIDE requisicion>>>>" + vehiculo);
        PartidaContabilidad p = servgen.buscarPorId(PartidaContabilidad.class, idPartidaC);
        getInstance().setPartidaContabilidad(p);
        System.out.println("id de ala partidaaaaaaaaaaaaa" + p.getId());
        if (solicitudrep.getId() != null) {
            getInstance().setSolicitudReparacionId(solicitudrep);
        }
        try {
            if (getInstance().isPersistent()) {
                guardarItem();
                System.out.println("ingresa a editar>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ingresa a creaaar>>>>>>>");
                //  getInstance().setEstado(true);
                create(getInstance());
                guardarItem();
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
//        if (solicitudrep.getId() != null) {
//            ControladorSolicitudReparacionMantenimiento csrm = new ControladorSolicitudReparacionMantenimiento();
//
//            csrm.fijarRequisicion(getInstance(), getInstance().getSolicitudReparacionId());
//        }

        return "/paginas/requisicion/lista.xhtml?faces-redirect=true";

    }

    public void fijarSolicitud(SolicitudReparacionMantenimiento soli, Requisicion requ) {
        Date now = Calendar.getInstance().getTime();
        setInstance(requ);
        getInstance().setSolicitudReparacionId(soli);
        getInstance().setLastUpdate(now);
        try {
            servgen.actualizar(getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
