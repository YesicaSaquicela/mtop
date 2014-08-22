package org.mtop.servicios.listas;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.mtop.cdi.Web;
import org.mtop.controlador.ControladorRequisicion;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.Vehiculo;

import org.mtop.servicios.ServicioGenerico;
import org.mtop.util.QueryData;
import org.mtop.util.QuerySortOrder;
import org.mtop.util.UI;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.mtop.modelo.config.Setting;

@Named
@RequestScoped
public class RequisicionListaServicios extends LazyDataModel<Requisicion> {

    private static final long serialVersionUID = 4819808125494695200L;
    private static final int MAX_RESULTS = 5;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(RequisicionListaServicios.class);
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<Requisicion> resultList;
    private int firstResult = 0;
    private Requisicion[] requisicionSeleccionadas;
    private Requisicion requisicionSeleccionada; //Filtro de cuenta schema
    private String estado;
    private Vehiculo vehiculo;

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public RequisicionListaServicios() {
        setPageSize(MAX_RESULTS);
        resultList = new ArrayList<Requisicion>();
    }

    public List<Requisicion> getResultList() {

        return resultList;
    }

    public void setResultList(List<Requisicion> resultList) {
        this.resultList = resultList;
    }

    public String guardarRequisicion(Kardex k) {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando Requisicion en guardar en lista de Requisicion");

        if (requisicionSeleccionada != null) {

            try {

                System.out.println("recupero Requisicion" + requisicionSeleccionada);
                requisicionSeleccionada.setKardex(k);
                requisicionSeleccionada.setLastUpdate(now);
                requisicionSeleccionada.setAprobado(true);
                servgen.actualizar(requisicionSeleccionada);
                Date now1 = Calendar.getInstance().getTime();
                System.out.println("guando Requisicion con kardex cooon" + requisicionSeleccionada.getKardex());
                k.setLastUpdate(now1);
                k.getListaRequisicion().add(requisicionSeleccionada);
               
                servgen.actualizar(k);
                System.out.println("guardo kardex con Requisicion" + k.getListaRequisicion());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("fijanddsasadasdadassssssssssss");
        return "/paginas/admin/kardex/crear.xhtml?faces-redirect=true";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
//        if ("noAprobada".equals(estado)) {
//            resultList = servgen.find(Requisicion.class, Requisicion_.numRequisicion.getName(), this.getPageSize(), firstResult);
//            List<Requisicion> resultado = servgen.find(Requisicion.class, Requisicion_.numRequisicion.getName(), this.getPageSize(), firstResult);
//            resultList.clear();
//            System.out.println("\n>>>>\n>>>\nentro a noAprobada>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>\n>>>\n");
//            for (Requisicion requisicion : resultado) {
//                if (requisicion.getAprobado() == false) {
//                    resultList.add(requisicion);
//                }
//            }
//        } else {
//            resultList = servgen.find(Requisicion.class, Requisicion_.numRequisicion.getName(), this.getPageSize(), firstResult);
//            List<Requisicion> resultado = servgen.find(Requisicion.class, Requisicion_.numRequisicion.getName(), this.getPageSize(), firstResult);
//            resultList.clear();
//            System.out.println("\n>>>>\n>>>\nentro a aprobada>>>>>>>>>>>>>>>>>>>>>>>\n>>>>\n>>>\n");
//            for (Requisicion requisicion : resultado) {
//                if (requisicion.getAprobado() == true) {
//                    resultList.add(requisicion);
//                }
//            }
//        }
        System.out.println("entro a fijR ESTADOOOOO" + estado);
        this.estado = estado;
    }

    public String find() {
        /*
         try {
         init();
         resultList = bussinesEntityTypeService.findAll();
         String summary = "Encontrados! ";             
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));
         System.out.println("lista "+resultList.get(0));
         } catch (Exception e) {
            
         }
         */
        this.getResultList();
        return "/paginas/kardex/crear";
    }

    public int getNextFirstResult() {
        return firstResult + this.getPageSize();
    }

    public int getPreviousFirstResult() {
        return this.getPageSize() >= firstResult ? 0 : firstResult - this.getPageSize();
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        log.info("set first result + firstResult");
        this.firstResult = firstResult;
        this.resultList = null;
    }

    public boolean isPreviousExists() {
        return firstResult > 0;
    }

    @PostConstruct
    public void init() {
        log.info("Setup entityManager into bussinesEntityTypeService...");
        servgen.setEm(em);

    }

    @Override
    public Requisicion getRowData(String rowKey) {
        return servgen.buscarPorId(Requisicion.class, Long.parseLong(rowKey));

    }

    @Override
    public Object getRowKey(Requisicion entity) {
        return entity.getId();
    }

    public Requisicion[] getRequisicionSeleccionadas() {
        return requisicionSeleccionadas;
    }

    public void setRequisicionSeleccionadas(Requisicion[] requisicionSeleccionadas) {
        this.requisicionSeleccionadas = requisicionSeleccionadas;
    }

    public Requisicion getRequisicionSeleccionada() {
        return requisicionSeleccionada;
    }

    public void setRequisicionSeleccionada(Requisicion requisicionSeleccionada) {
        this.requisicionSeleccionada = requisicionSeleccionada;
    }

    @Override
    public List<Requisicion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        ControladorRequisicion csr = new ControladorRequisicion();
        csr.setEntityManager(em);

        servgen.setEm(em);
        log.info("load results for ..." + first + ", " + pageSize);

        int end = first + pageSize;

        QuerySortOrder order = QuerySortOrder.ASC;
        if (sortOrder == SortOrder.DESCENDING) {
            order = QuerySortOrder.DESC;
        }
        Map<String, Object> _filters = new HashMap<String, Object>();
        /*_filters.put(BussinesEntityType_.name, getSelectedBussinesEntityType()); //Filtro por defecto
         _filters.putAll(filters);*/
        System.out.println("crssssssssssssssssssssssssssssss" + csr);
        QueryData<Requisicion> qData = new QueryData<Requisicion>();

        try {
            csr.setEntityClass(Requisicion.class);
            qData = csr.find(first, end, sortField, order, _filters);
            List<Requisicion> lr = new ArrayList<Requisicion>();

            for (Requisicion qd : qData.getResult()) {
                System.out.println("\n\n\n\n\n\nentro recorrido\n\n\n\n\n\n");
                if (qd.getVehiculo() != null && vehiculo != null) {

                    System.out.println("vehiculo en kardex>>>>>" + vehiculo.getId());
                    System.out.println("vehiculo requisicion>>>>>>" + qd.getVehiculo().getId());
                    System.out.println("estado requisiscion>>>>>>" + qd.isEstado());
                    if (qd.getAprobado() == false && qd.isEstado() && qd.getVehiculo().getId().equals(vehiculo.getId())) {
                        System.out.println("\n\n\n\n\n\nagregooooo\n\n\n\n\n\n" + qd);
                        lr.add(qd);
                    }
                }

            }
            if (lr.isEmpty()) {
                System.out.println("entro empty");
            } else {
                System.out.println("no entra empty");
                qData.setResult(lr);
                this.setRowCount(qData.getTotalResultCount().intValue());
            }

        } catch (Exception e) {
            System.out.println("eROOOOR");
            e.printStackTrace();
        }
        System.out.println("listatattatata" + qData.getResult());
        return qData.getResult();

    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha seleccionado una requisición con id "), ((Requisicion) event.getObject()).getNumRequisicion());

        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha deseleccionado la requisición con id "), ((Requisicion) event.getObject()).getNumRequisicion());

        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setRequisicionSeleccionada(null);

    }

}
