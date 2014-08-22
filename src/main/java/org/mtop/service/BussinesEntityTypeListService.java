/*
 * Copyright 2012 cesar.
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
package org.mtop.service;

import org.mtop.modelo.dinamico.BussinesEntityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import org.mtop.cdi.Web;
import org.mtop.util.QueryData;
import org.mtop.util.QuerySortOrder;
import org.mtop.util.UI;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author cesar
 */
@RequestScoped
@Named
public class BussinesEntityTypeListService extends LazyDataModel<BussinesEntityType> {

    private static final long serialVersionUID = 4819808125494695200L;
    private static final int MAX_RESULTS = 5;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(BussinesEntityTypeListService.class);
    @Inject
    @Web
    private EntityManager entityManager;
    @Inject
    private BussinesEntityTypeService bussinesEntityTypeService;
    private List<BussinesEntityType> resultList;
    private int firstResult = 0;
    private BussinesEntityType[] selectedBussinesEntitiesType;
    private BussinesEntityType selectedBussinesEntityType; //Filtro de cuenta schema
    private String palabrab;
    private List<BussinesEntityType> listaEntiidades;
    private List<BussinesEntityType> entidadesFiltradas;
    private ArrayList<String> ced;

    public List<BussinesEntityType> getEntidadesFiltradas() {
        System.out.println("\n\n\n\nonteniendo"+entidadesFiltradas);
        return entidadesFiltradas;
    }

    public void setEntidadesFiltradas(List<BussinesEntityType> entidadesFiltradas) {
        this.entidadesFiltradas = entidadesFiltradas;
    }
    
    
    
    
    public ArrayList<String> getCed() {
        return ced;
    }

    public void setCed(ArrayList<String> ced) {
        this.ced = ced;
    }

    public List<BussinesEntityType> getListaEntiidades() {
        return listaEntiidades;
    }

    public void setListaEntiidades(List<BussinesEntityType> listaEntiidades) {
        this.listaEntiidades = listaEntiidades;
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public BussinesEntityTypeListService() {
        setPageSize(MAX_RESULTS);
        resultList = new ArrayList<BussinesEntityType>();
    }

    public List<BussinesEntityType> getResultList() {
        if (resultList.isEmpty() /*&& getSelectedBussinesEntityType() != null*/) {
            resultList = bussinesEntityTypeService.find(this.getPageSize(), firstResult);
        }
        return resultList;
    }

    public void setResultList(List<BussinesEntityType> resultList) {
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
        System.out.println("\n\n\n\nentrando a find\n\n\n\n" + getResultList());
        return "/paginas/admin/bussinesentitytype/list";
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ced = new ArrayList<String>();
        System.out.println("pa;abra b" + palabrab);
        System.out.println("resullist" + getResultList());
        System.out.println("result lis" + resultList);
        System.out.println("listaentidades" + listaEntiidades);

        for (BussinesEntityType bet : listaEntiidades) {
            System.out.println("nombre" + bet.getName());
            if (bet.getName().toLowerCase().contains(query.toLowerCase())) {
                ced.add(bet.getName());
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }

    public void buscar() {

        List<BussinesEntityType> le = new ArrayList<BussinesEntityType>();
        le.clear();
        System.out.println("palabra b" + palabrab);
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        System.out.println("lista de entidades a buscar" + listaEntiidades);
        for (BussinesEntityType bussinesEntityType : listaEntiidades) {

            if (bussinesEntityType.getName().toLowerCase().contains(palabrab.toLowerCase())) {
                le.add(bussinesEntityType);
            }
        }
        System.out.println("encontradas" + le);

        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            resultList = le;
            setResultList(le);
            this.setResultList(le);
            System.out.println("presentando\n\n\n" +getResultList());

        }

    }

    public void limpiar() {
        palabrab = "";
        System.out.println("lista de entidades a devolver" + listaEntiidades);
        resultList = listaEntiidades;
    }

    public int getNextFirstResult() {
        return firstResult + this.getPageSize();
    }

    public int getPreviousFirstResult() {
        return this.getPageSize() >= firstResult ? 0 : firstResult - this.getPageSize();
    }

    public int getFirstResult() {
        System.out.println("lista entidads en firstresullist" + listaEntiidades);
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
        bussinesEntityTypeService.setEntityManager(entityManager);
        listaEntiidades = bussinesEntityTypeService.findAll();
        resultList=listaEntiidades;
        System.out.println("lista entidades en init" + listaEntiidades);
        System.out.println("palabre b" + palabrab);

    }

    @Override
    public BussinesEntityType getRowData(String rowKey) {

        return bussinesEntityTypeService.findByName(rowKey);
    }

    @Override
    public Object getRowKey(BussinesEntityType entity) {
        return entity.getName();
    }

    public BussinesEntityType[] getSelectedBussinesEntitiesType() {
        return selectedBussinesEntitiesType;
    }

    public void setSelectedBussinesEntitiesType(BussinesEntityType[] selectedBussinesEntitiesType) {
        this.selectedBussinesEntitiesType = selectedBussinesEntitiesType;
    }

    public BussinesEntityType getSelectedBussinesEntityType() {
        return selectedBussinesEntityType;

    }

    public void setSelectedBussinesEntityType(BussinesEntityType selectedBussinesEntityType) {
        this.selectedBussinesEntityType = selectedBussinesEntityType;
    }

    @Override
    public List<BussinesEntityType> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        log.info("load results for ..." + first + ", " + pageSize);
        System.out.println("\n\n\n\npalabra buscando en load" + palabrab);
        System.out.println("resillist en load"+resultList);
        System.out.println("lista de entidades en load"+listaEntiidades);

        int end = first + pageSize;

        QuerySortOrder order = QuerySortOrder.ASC;
        if (sortOrder == SortOrder.DESCENDING) {
            order = QuerySortOrder.DESC;
        }
        Map<String, Object> _filters = new HashMap<String, Object>();
        /*_filters.put(BussinesEntityType_.name, getSelectedBussinesEntityType()); //Filtro por defecto
         _filters.putAll(filters);*/

        QueryData<BussinesEntityType> qData = new QueryData<BussinesEntityType>();

        if (palabrab != null) {
            System.out.println("entro a bsucar");
            buscar();
            System.out.println("vuelve resullist"+resultList);
            qData.setResult(resultList);
        } else {
            System.out.println("no entro a bsucar");
            qData = bussinesEntityTypeService.find(first, end, sortField, order, _filters);
            this.setRowCount(qData.getTotalResultCount().intValue());
            listaEntiidades = qData.getResult();
            
            System.out.println("lista de entidades " + listaEntiidades);
        }
        System.out.println("devuel;ve+"+qData.getResult());
        
        return qData.getResult();
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("module.BussinesEntityType") + " " + UI.getMessages("common.selected"), ((BussinesEntityType) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("module.BussinesEntityType") + " " + UI.getMessages("common.unselected"), ((BussinesEntityType) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setSelectedBussinesEntityType(null);
    }
}
