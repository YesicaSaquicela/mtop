/*
 * Copyright 2014 yesica.
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
package org.mtop.servicios.listas;

/**
 *
 * @author yesica
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.mtop.cdi.Web;
import org.mtop.controlador.ControladorVehiculo;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;

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
public class VehiculoListaServicios extends LazyDataModel<Vehiculo> {

    private static final long serialVersionUID = 4819808125494695200L;
    private static final int MAX_RESULTS = 5;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(VehiculoListaServicios.class);
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<Vehiculo> resultList;
    private int firstResult = 0;
    private Vehiculo[] vehiculosSeleccionados;
    private Vehiculo vehiculoSeleccionado; //Filtro de cuenta schema
    ControladorVehiculo controladorVehiculo;
    private String palabrab = "";
    Integer c=0;
    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), query);

        for (Vehiculo vehiculo : lv) {
            System.out.println("econtro uno " + vehiculo.getNumRegistro());
            ced.add(vehiculo.getNumRegistro());
        }
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), query);
        for (Vehiculo vehiculo : lc) {
            System.out.println("econtro uno " + vehiculo.getPlaca());
            ced.add(vehiculo.getPlaca());
        }

//         List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);
//          for (Vehiculo vehiculo : lk) {
//            System.out.println("econtro uno " + vehiculo.getPlaca());
//            String cad=vehiculo.getKilometraje().toString();
//            ced.add(cad);
//        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscar() {
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        System.out.println("\n\n\nkjshxckjds\n\n\n");
        //buscando por coincidencia descripciion
        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), palabrab);
        //buscando por codigo
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), palabrab);
//        List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);

        for (Vehiculo vehiculo : lc) {
            if (!lv.contains(vehiculo)) {
                lv.add(vehiculo);
            }
        }
        System.out.println("\n\n\nkjshxck2222jds\n\n\n");
//        for (Vehiculo vehiculo : lk) {
//            if (!lv.contains(vehiculo)) {
//                lv.add(vehiculo);
//            }
//        }
        System.out.println("\n\n\nkjshxckjds\n\n\n" + lv);
        if (lv.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            this.setResultList(lv);
        }

    }

    public void limpiar() {
        System.out.println("entro limpiandoooooo"
               );
        resultList = new ArrayList<Vehiculo>();
    }

    public VehiculoListaServicios() {
        setPageSize(MAX_RESULTS);
        
      //  resultList = new ArrayList<Vehiculo>();
        
    }

    public List<Vehiculo> getResultList() {
//        if (resultList.isEmpty() /*&& getSelectedBussinesEntityType() != null*/) {
//            resultList = servgen.find(Vehiculo.class, Vehiculo_.numRegistro.getName(), this.getPageSize(), firstResult);
//        }

        return resultList;
    }

    public void setResultList(List<Vehiculo> resultList) {
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
        return "/paginas/requisicion/crear";
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

    public Vehiculo[] getVehiculosSeleccionados() {
        return vehiculosSeleccionados;
    }

    public void setVehiculosSeleccionados(Vehiculo[] vehiculosSeleccionados) {
        this.vehiculosSeleccionados = vehiculosSeleccionados;
    }

    public Vehiculo getVehiculoSeleccionado() {
        return vehiculoSeleccionado;
    }

    public void setVehiculoSeleccionado(Vehiculo vehiculoSeleccionado) {
        this.vehiculoSeleccionado = vehiculoSeleccionado;
    }

    @PostConstruct
    public void init() {
        log.info("Setup entityManager into bussinesEntityTypeService...");
        controladorVehiculo=new ControladorVehiculo();
        controladorVehiculo.setEntityManager(em);
        servgen.setEm(em);
        
        controladorVehiculo.setServgen(servgen);
        
        System.out.println("el resul antes"+resultList);
        if(resultList==null){
            if(!palabrab.equals("")){
                System.out.println("entro busacrndo");
                controladorVehiculo.setPalabrab(palabrab);
                resultList=controladorVehiculo.getListaVehiculos();
            }else{
                System.out.println("enTRO A FIJAR TODOS");
            resultList=controladorVehiculo.getServgen().buscarTodos(Vehiculo.class);
            }
            
        }
        System.out.println("volvio al initcon resullisttt"+resultList);

    }
    public void inicializar(){
        System.out.println("entro inicializar");
        palabrab="";
        resultList=new ArrayList<Vehiculo>();
        System.out.println("sjkhdjsa"+resultList);
        
    }

    @Override
    public Vehiculo getRowData(String rowKey) {
        return servgen.buscarPorId(Vehiculo.class, Long.parseLong(rowKey));

    }

    @Override
    public Object getRowKey(Vehiculo entity) {
        return entity.getId();
    }

    @Override
    public List<Vehiculo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        
            ControladorVehiculo csr = new ControladorVehiculo();
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
            QueryData<Vehiculo> qData = new QueryData<Vehiculo>();

            try {
                csr.setEntityClass(Vehiculo.class);
                qData = csr.find(first, end, sortField, order, _filters);

                this.setRowCount(qData.getTotalResultCount().intValue());
                if(resultList.isEmpty()){
                    System.out.println("estuvo vacio"+this.resultList);
                    this.setResultList(qData.getResult());
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("\n\n\n\fijo en resullist\n\n\n" + resultList);
            return qData.getResult();
        

    }

    public void onRowSelect(SelectEvent event) {
//        if (fe == null) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: ", " Debe ingresar fecha de entrada");
//            
//            FacesContext.getCurrentInstance().addMessage("", msg);
//        } else {
//            if (fs == null) {
//                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: ", " Debe ingresar fecha de salida");
//                FacesContext.getCurrentInstance().addMessage("", msg);
//            } else {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha seleccionado el vehiculo con número de registro "), ((Vehiculo) event.getObject()).getNumRegistro());
        FacesContext.getCurrentInstance().addMessage("", msg);
//            }
//        }

    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha deseleccionado el vehiculo con número de registro "), ((Vehiculo) event.getObject()).getNumRegistro());

        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setVehiculoSeleccionado(null);

    }

}
