/*
 * Copyright 2013 tania.
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
package edu.sgssalud.service.odontologia;

import edu.sgssalud.cdi.Web;
import edu.sgssalud.model.odontologia.ConsultaOdontologica;
import edu.sgssalud.util.QueryData;
import edu.sgssalud.util.QuerySortOrder;
import edu.sgssalud.util.UI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.solder.logging.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author tania
 */@Named("consultaOdontologicaListaServicio")
@ViewScoped
public class ConsultaOdontologicaListaServicio extends LazyDataModel<ConsultaOdontologica>{
    
     private static final long serialVersionUID = 5L;
    private static final int MAX_RESULTS = 5;
    private static Logger log = Logger.getLogger(ConsultaOdontologicaListaServicio.class);

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ConsultaOdontologicaServicio cos;    
    private List<ConsultaOdontologica> resultList;
    private int primerResult = 0;
    private ConsultaOdontologica[] consulOdontSeleccionadas;
    private ConsultaOdontologica consulOdontSeleccionada;
    private String parametroBusqueda;
    
    /*Método para inicializar tabla*/
    public ConsultaOdontologicaListaServicio() {
        setPageSize(MAX_RESULTS);
        resultList = new ArrayList<ConsultaOdontologica>();
    }
    
    @PostConstruct
    public void init() {
        cos.setEntityManager(em);
        if (resultList.isEmpty() ) {
           resultList = cos.getConsultasOdontologica(getPageSize(), primerResult);
        }
    }
    
    /*Método sobreescrito para cargar los datos desde la base de datos hacia la tabla*/
    @Override
    public List<ConsultaOdontologica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        int end = first + pageSize;

        QuerySortOrder order = QuerySortOrder.ASC;
        if (sortOrder == SortOrder.DESCENDING) {
            order = QuerySortOrder.DESC;
        }
        Map<String, Object> _filters = new HashMap<String, Object>();
        /*_filters.put(BussinesEntity_.type.getName(), getType()); //Filtro por defecto
         _filters.putAll(filters);*/

        QueryData<ConsultaOdontologica> qData = cos.find(first, end, sortField, order, _filters);
        this.setRowCount(qData.getTotalResultCount().intValue());
        this.setResultList(qData.getResult());        
        Collections.sort(resultList);
        return qData.getResult();        
    }
    
    /*Métodos que me permiten seleccionar un objeto de la tabla*/
    
    @Override
    public Object getRowKey(ConsultaOdontologica entity) {
        return entity.getId();
    }

//    @Override
//    public ConsultaOdontologica getRowData(Long t) {
//        return cms.getConsultaOdontologicaPorId(t);
//    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("ConsultaOdontologica") + " " + UI.getMessages("common.selected"), ((ConsultaOdontologica) event.getObject()).getDiagnostico());
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("ConsultaOdontologica") + " " + UI.getMessages("common.unselected"), ((ConsultaOdontologica) event.getObject()).getDiagnostico());
        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setConsulOdontSeleccionada(null);
    }
    /*.............*/

    /*Métodos GET y SET de los Atributos*/
    public int getNextFirstResult() {
        return primerResult + this.getPageSize();
    }

    public int getPreviousFirstResult() {
        return this.getPageSize() >= primerResult ? 0 : primerResult - this.getPageSize();
    }

    public int getFirstResult() {
        return primerResult;
    }

    public void setFirstResult(int firstResult) {
        this.primerResult = firstResult;
        this.resultList = null;
    }

    public List<ConsultaOdontologica> getResultList() {
//        if (resultList.isEmpty() /*&& getSelectedBussinesEntityType() != null*/) {
//            resultList = pacienteServicio.getPacientes(firstResult, firstResult);
//        }
        Collections.sort(resultList);
        return resultList;
    }

    public void setResultList(List<ConsultaOdontologica> resultList) {
        this.resultList = resultList;
    }

    public ConsultaOdontologica getConsulOdontSeleccionada() {
        return consulOdontSeleccionada;
    }

    public void setConsulOdontSeleccionada(ConsultaOdontologica consulOdontSeleccionada) {
        this.consulOdontSeleccionada = consulOdontSeleccionada;
    }

    public String getParametroBusqueda() {
        return parametroBusqueda;
    }

    public void setParametroBusqueda(String parametroBusqueda) {
        this.parametroBusqueda = parametroBusqueda;
    }

   
    
}