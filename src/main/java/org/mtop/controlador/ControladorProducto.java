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
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Producto_;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.dinamico.PersistentObject_;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorProducto extends BussinesEntityHome<Producto> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @EJB
    private ServicioGenerico servgen;
    private List<Producto> listaProducto;
    private String codigo;
    private String palabrab = "";
    private List<Producto> listaproductos2 = new ArrayList<Producto>();
    private List<String> images;
    private String msj = "";

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        if (msj.substring(0, 2).equals("tr")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Se creó  producto de código " + msj.substring(4, msj.length()) + " con éxito");
            FacesContext.getCurrentInstance().addMessage("", msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Se actualizó producto de código " + msj.substring(5, msj.length()) + " con éxito");
            FacesContext.getCurrentInstance().addMessage("", msg);

        }

        System.out.println("fijo msj+" + msj);
        this.msj = msj;
    }
    

    public List<Producto> getListaproductos2() {
        return listaproductos2;
    }

    public void setListaproductos2(List<Producto> listaproductos2) {
        this.listaproductos2 = listaproductos2;
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public void buscar() {
        palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        List<Producto> lp = new ArrayList<Producto>();
        //buscando por coincidencia descripciion

            for (Producto p : listaproductos2) {
                if (p.getCodigo().contains(palabrab)) {
                    lp.add(p);
                } else {
                    if (p.getDescripcion().toLowerCase().contains(palabrab.toLowerCase())) {
                        lp.add(p);
                    }
                }
            }
        if (lp.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaProducto = lp;

        }

    }

    public void limpiar() {
        palabrab = "";
        listaProducto = listaproductos2;

    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (Producto producto : listaproductos2) {
            if (producto.isEstado()) {
                if (producto.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                    ced.add(producto.getDescripcion());
                }
                if (producto.getCodigo().contains(query)) {
                    ced.add(producto.getCodigo());
                }

            }
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public ControladorRequisicion getCrequisicion() {
        return crequisicion;
    }

    public void setCrequisicion(ControladorRequisicion crequisicion) {
        this.crequisicion = crequisicion;
    }
    private ControladorRequisicion crequisicion;

    public String getCodigo() {
        if (getId() == null) {
            System.out.println("numero" + getInstance().getCodigo());
            List<Producto> lista = findAll(Producto.class);
            int t = lista.size();
            System.out.println("valor de t :::::::::::" + t);
            if (t < 9) {
                setCodigo("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setCodigo("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setCodigo("0".concat(String.valueOf(t + 1)));
                    } else {
                        setCodigo(String.valueOf(t + 1));
                    }
                }
            }
        } else {
            setCodigo(getInstance().getCodigo());
        }

        return codigo;

    }

    public void setCodigo(String numRegistro) {
        this.codigo = numRegistro;

        getInstance().setCodigo(this.codigo);

    }

    public Long getProductoId() {
        return (Long) getId();
    }

    public void setProductoId(Long productoId) {
        setId(productoId);
    }

    @TransactionAttribute   //
    public Producto load() {
        if (isIdDefined()) {
            wire();
        }
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();

    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaProducto = servgen.buscarTodos(Producto.class);
        System.out.println("lisssssstaa...a. de remover." + listaProducto);
        crequisicion = new ControladorRequisicion();
        System.out.println("tama;o listaaaa" + listaProducto.size());

        List<Producto> lp = servgen.buscarTodos(Producto.class);
        listaProducto.clear();
        System.out.println("lppp" + lp);

        for (Producto produ : lp) {
            System.out.println("iddddd" + produ.getId());
            System.out.println("entro a for lista>>>>" + produ.isEstado());
            if (produ.isEstado()) {
                System.out.println("listatesssa" + listaProducto);
                listaProducto.add(produ);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + listaProducto);

            }

        }
        listaproductos2 = listaProducto;

        System.out.println("Lista dfinaaaaaaalllll>>>" + listaProducto);
    }

    @Override
    protected Producto createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Producto.class.getName());
        Date now = Calendar.getInstance().getTime();
        Producto producto = new Producto();
        producto.setCreatedOn(now);
        producto.setLastUpdate(now);
        producto.setActivationTime(now);

        //fichaMedic.setResponsable(null);    //cambiar atributo a 
        producto.setType(_type);
        producto.buildAttributes(bussinesEntityService);  //
        return producto;
    }

    @Override
    public Class<Producto> getEntityClass() {
        return Producto.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        String ms="";

        try {
            if (getInstance().isPersistent()) {
                System.out.println("Entro a Editar>>>>>>>>");
                save(getInstance());
                ms= "false" + getInstance().getCodigo();
            
            } else {
                System.out.println("Entro a crear>>>>>>>>");
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
              ms= "true" + getInstance().getCodigo() ;
              
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/secretario/producto/lista.xhtml?faces-redirect=true&msj="+ms;
    }

    public boolean verificarPoducto(Long idproducto) {
        boolean ban = true;
        System.out.println("Entro a dar de baja>>>>>>" + idproducto);
        List<ItemRequisicion> listaItems = findAll(ItemRequisicion.class);
        for (ItemRequisicion itemreq : listaItems) {
            if (itemreq.getProducto() != null) {
                if (itemreq.getProducto().getId() != null) {
                    if (itemreq.getProducto().getId().equals(idproducto)) {
                        ban = false;
                        break;
                    }
                }
            }
        }

        if (ban) {
            ban = true;
        } else {
            ban = false;
        }
        return ban;
    }

    @Transactional
    public String darDeBaja(Long idproducto) {
        boolean ban = true;
        System.out.println("Entro a dar de baja>>>>>>" + idproducto);
        List<ItemRequisicion> listaItems = findAll(ItemRequisicion.class);
        System.out.println("lista de itens>>..." + listaItems);
        for (ItemRequisicion itemreq : listaItems) {
            System.out.println("entro al for>>>>>");
            System.out.println("producto k llega" + idproducto);

            if (itemreq.getProducto() != null) {
                System.out.println("producto que obtiene" + itemreq.getProducto().getId());
                if (itemreq.getProducto().getId().equals(idproducto)) {
                    ban = false;
                    break;
                }
            }

        }
        System.out.println("retornando ban" + ban);
        if (ban) {
            setId(idproducto);
            setInstance(servgen.buscarPorId(Producto.class, idproducto));
            Date now = Calendar.getInstance().getTime();
            getInstance().setLastUpdate(now);
            getInstance().setEstado(false);
            save(getInstance());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El producto seleccionado ha sido dado de baja ", "exitosamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El producto seleccionado no se lo puede dar de baja", "porque ya se encuentra agregado en una requisición"));
        }

        return "/paginas/secretario/producto/lista.xhtml?faces-redirect=true";
    }

}
