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
import org.mtop.controlador.ControladorSolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;

import org.mtop.servicios.ServicioGenerico;
import org.mtop.util.QueryData;
import org.mtop.util.QuerySortOrder;
import org.mtop.util.UI;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@RequestScoped
public class SolicitudListaServicios extends LazyDataModel<SolicitudReparacionMantenimiento> {

    private static final long serialVersionUID = 4819808125494695200L;
    private static final int MAX_RESULTS = 5;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(SolicitudListaServicios.class);
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<SolicitudReparacionMantenimiento> resultList;
    private int firstResult = 0;
    private SolicitudReparacionMantenimiento[] solicitudSeleccionadas;
    private SolicitudReparacionMantenimiento solicitudSeleccionada; //Filtro de cuenta schema

    public SolicitudListaServicios() {
        setPageSize(MAX_RESULTS);
        resultList = new ArrayList<SolicitudReparacionMantenimiento>();
    }

    public List<SolicitudReparacionMantenimiento> getResultList() {
        if (resultList.isEmpty() /*&& getSelectedBussinesEntityType() != null*/) {
            resultList = servgen.find(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento_.numSolicitud.getName(), this.getPageSize(), firstResult);
        }

        return resultList;
    }

    public void setResultList(List<SolicitudReparacionMantenimiento> resultList) {
        this.resultList = resultList;
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
    public SolicitudReparacionMantenimiento getRowData(String rowKey) {
        return servgen.buscarPorId(SolicitudReparacionMantenimiento.class, Long.parseLong(rowKey));

    }

    @Override
    public Object getRowKey(SolicitudReparacionMantenimiento entity) {
        return entity.getId();
    }

    public SolicitudReparacionMantenimiento[] getSolicitudSeleccionadas() {
        return solicitudSeleccionadas;
    }

    public void setSolicitudSeleccionadas(SolicitudReparacionMantenimiento[] solicitudSeleccionadas) {
        this.solicitudSeleccionadas = solicitudSeleccionadas;
    }

    public SolicitudReparacionMantenimiento getSolicitudSeleccionada() {
        return solicitudSeleccionada;
    }

    public void setSolicitudSeleccionada(SolicitudReparacionMantenimiento solicitudSeleccionada) {
        this.solicitudSeleccionada = solicitudSeleccionada;
    }

    @Override
    public List<SolicitudReparacionMantenimiento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        ControladorSolicitudReparacionMantenimiento csr = new ControladorSolicitudReparacionMantenimiento();
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
        QueryData<SolicitudReparacionMantenimiento> qData=new QueryData<SolicitudReparacionMantenimiento>();
        
        try {
            csr.setEntityClass(SolicitudReparacionMantenimiento.class);
            qData = csr.find(first, end, sortField, order, _filters);
            this.setRowCount(qData.getTotalResultCount().intValue());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qData.getResult();

    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha seleccionado una solicitud con id "), ((SolicitudReparacionMantenimiento) event.getObject()).getNumSolicitud());

        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha deseleccionado la solicitud con id "), ((SolicitudReparacionMantenimiento) event.getObject()).getNumSolicitud());

        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setSolicitudSeleccionada(null);

    }

}