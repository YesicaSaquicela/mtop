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
        //buscando por coincidencia descripciion
        List<Producto> lp = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), PersistentObject_.description.getName(), palabrab);
        //buscando por codigo
        List<Producto> lc = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), Producto_.codigo.getName(), palabrab);
        for (Producto producto : lc) {
            if (!lp.contains(producto)) {
                lp.add(producto);
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
            for (Producto productol : lc) {
                System.out.println("entroa al for lista productos");
                if (productol.isEstado()) {
                    System.out.println("entroa a comparar>>>");
                    listaProducto = lp;
                    System.out.println("devuelve lista>>>>" + listaProducto);
                }
            }

        }

    }

    public void limpiar() {
        palabrab = "";
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
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Producto> lp = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), PersistentObject_.description.getName(), query);

        for (Producto producto : lp) {
            if (producto.isEstado()) {
                System.out.println("econtro uno " + producto.getDescription());
                ced.add(producto.getDescription());
            }
        }
        List<Producto> lc = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), Producto_.codigo.getName(), query);
        for (Producto producto : lc) {
            if (producto.isEstado()) {
                System.out.println("econtro uno " + producto.getCodigo());
                ced.add(producto.getCodigo());
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

        System.out.println("IIIIDEEEntro>>>>>>" + getProductoId());
        System.out.println("IIIIDEPERSISTEN  >>>>>>" + getInstance().isPersistent());

        try {
            if (getInstance().isPersistent()) {
                System.out.println("Entro a Editar>>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Producto" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("Entro a crear>>>>>>>>");
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Producto" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/secretario/producto/lista.xhtml?faces-redirect=true";
    }

    @Transactional
    public String darDeBaja(Long idproducto) {
        System.out.println("Entro a dar de baja>>>>>>" + idproducto);
        setId(idproducto);
        setInstance(servgen.buscarPorId(Producto.class, idproducto));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);

        //listaProducto.remove(findById(Producto.class,idproducto));
        save(getInstance());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La partida seleccionada ha sido dada de baja ", "exitosamente"));
        return "/paginas/secretario/producto/lista.xhtml?faces-redirect=true";
    }

}
