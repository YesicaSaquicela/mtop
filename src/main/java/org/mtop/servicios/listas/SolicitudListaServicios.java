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
import org.mtop.controlador.ControladorSolicitudReparacionMantenimiento;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;
import org.mtop.modelo.Vehiculo;

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
    private Vehiculo vehiculo;

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

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

    public String guardarSolicitud(Kardex k) {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando SOlidituuzzxxzzuuud en guardar en lista de solicitud");

        if (solicitudSeleccionada != null) {

            try {

                System.out.println("recupero solicitud" + solicitudSeleccionada);
                solicitudSeleccionada.setKardex(k);
                solicitudSeleccionada.setLastUpdate(now);
                solicitudSeleccionada.setAprobado(true);

                servgen.actualizar(solicitudSeleccionada);

                System.out.println("guando solicicitud con kardex cooon" + solicitudSeleccionada.getKardex());
                k.setLastUpdate(now);
                k.getListaSolicitudReparacion().add(solicitudSeleccionada);

                servgen.actualizar(k);
                System.out.println("guardo kardex con solicitudes" + k.getListaSolicitudReparacion());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println("fijanddsasadasdadassssssssssss" + resultList);
        return "/paginas/admin/kardex/crear.xhtml?faces-redirect=true";
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
        QueryData<SolicitudReparacionMantenimiento> qData = new QueryData<SolicitudReparacionMantenimiento>();

        try {
            csr.setEntityClass(SolicitudReparacionMantenimiento.class);
            qData = csr.find(first, end, sortField, order, _filters);

            List<SolicitudReparacionMantenimiento> lr = new ArrayList<SolicitudReparacionMantenimiento>();
            System.out.println("skjdsjdshb"+qData.getResult());
            for (SolicitudReparacionMantenimiento qd : qData.getResult()) {
                System.out.println("id de req"+qd);
                System.out.println("\n\n\n\n\n\nentro recorrido"+vehiculo.getId());
                System.out.println("qd.getVehiculo()"+qd.getVehiculo());
                
                
                System.out.println("estado aprobado>>>"+ qd.getAprobado());
                if (qd.getVehiculo() != null && vehiculo!=null) {
                    System.out.println("entro>>>>>>>1if");
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
            System.out.println("EROOOR");
            e.printStackTrace();
        }
        System.out.println("listatattatata"+qData.getResult());
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
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha seleccionado una solicitud con id "), ((SolicitudReparacionMantenimiento) event.getObject()).getNumSolicitud());
        FacesContext.getCurrentInstance().addMessage("", msg);
//            }
//        }

    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Se ha deseleccionado la solicitud con id "), ((SolicitudReparacionMantenimiento) event.getObject()).getNumSolicitud());

        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setSolicitudSeleccionada(null);

    }

}
