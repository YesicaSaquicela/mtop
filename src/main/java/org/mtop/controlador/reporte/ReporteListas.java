/*
 * Copyright 2013 cesar.
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
package org.mtop.controlador.reporte;

import com.smartics.common.action.report.JasperReportAction;
//import edu.sgssalud.cdi.Web;
//import edu.sgssalud.model.farmacia.Medicamento;
//import edu.sgssalud.model.farmacia.Receta;
//import edu.sgssalud.model.labClinico.ExamenLabClinico;
//import edu.sgssalud.model.labClinico.PedidoExamenLaboratorio;
//import edu.sgssalud.model.labClinico.ResultadoExamenLabClinico;
//import edu.sgssalud.model.labClinico.ResultadoParametro;
//import edu.sgssalud.model.medicina.ConsultaMedica;
//import edu.sgssalud.model.medicina.FichaMedica;
//import edu.sgssalud.model.odontologia.ConsultaOdontologica;
//import edu.sgssalud.model.paciente.Paciente;
//import edu.sgssalud.model.profile.Profile;
//import edu.sgssalud.profile.ProfileService;
//import edu.sgssalud.service.farmacia.MedicamentoService;
//import edu.sgssalud.service.farmacia.RecetaServicio;
//import edu.sgssalud.service.labClinico.ExamenLabService;
//import edu.sgssalud.service.labClinico.ResultadoExamenLCService;
//import edu.sgssalud.service.medicina.ConsultaMedicaServicio;
//import edu.sgssalud.service.medicina.FichaMedicaServicio;
//import edu.sgssalud.service.odontologia.ConsultaOdontologicaServicio;
//import edu.sgssalud.service.paciente.PacienteServicio;
//import edu.sgssalud.util.FechasUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import org.jboss.seam.security.Identity;
import org.mtop.cdi.Web;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.profile.Profile;
import org.mtop.profile.ProfileService;
import org.mtop.servicios.ServicioGenerico;


@RequestScoped
@Named(value = "reporteListas")
public class ReporteListas {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(ReporteListas.class);

    private static final String REPORTE_USUARIOS = "reporteUsuario";  //nombre del reporte .jasper   
    private static final String REPORTE_VEHICULOS = "reporteVehiculo";
    private static final String REPORTE_PERSONAL = "reportePersonal";
    private static final String REPORTE_PRODUCTOS = "reporteProductos";
    private static final String REPORTE_PARTIDA = "reportePartida";


    @Inject
    @Web
    private EntityManager em;
    @Inject
    JasperReportAction JasperReportAction;
    @Inject
    private ProfileService profileService;
//    @Inject
//    private PacienteServicio pacienteServicio;
//    @Inject
//    private FichaMedicaServicio fichaMedicaServicio;
//    @Inject
//    private ConsultaMedicaServicio consultaMedicaServicio;
//    @Inject
//    private ConsultaOdontologicaServicio consultaOdontologicaServicio;
//    @Inject
//    private RecetaServicio recetaServicio;
//    @Inject
//    private MedicamentoService medService;
//    @Inject
//    private ExamenLabService examenesService;
    @Inject
    private ServicioGenerico servgen;
    @Inject
    private Identity identity;

    private Date fechaInf;
    private Date fechaSup;
    private boolean estado;
    private String parametroBusqued;
    private Profile pLoggeado;

    private Vehiculo pedido;
//    private ResultadoExamenLabClinico resultadoExamen = new ResultadoExamenLabClinico();

    /**
     * Default constructor.
     */
    public ReporteListas() {
    }
 public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Vehiculo getPedido() {
        return pedido;
    }

    public void setPedido(Vehiculo pedido) {
        this.pedido = pedido;
    }

  
    
    @PostConstruct
    public void init() {
        profileService.setEntityManager(em);
//        pacienteServicio.setEntityManager(em);
//        medService.setEntityManager(em);
//        recetaServicio.setEntityManager(em);
//        examenesService.setEntityManager(em);
//        fichaMedicaServicio.setEntityManager(em);
//        consultaMedicaServicio.setEntityManager(em);
//        consultaOdontologicaServicio.setEntityManager(em);
//        resultadoEService.setEntityManager(em);
        pLoggeado = profileService.getProfileByIdentityKey(identity.getUser().getKey());
        //resultadoExamen = new ResultadoExamenLabClinico();

    }

    private String getRealPath(String path) {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return context.getRealPath(path);
    }

  

    
      public void renderUsuario(List<Profile> pf) {

        final String attachFileName = "usuarios.pdf";
        
        Map<String, Object> _values = new HashMap<String, Object>();
//        _values.put("numeroP", p.size());
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = context.getRealPath("/reportes/unl.png");
//        _values.put("logo", logo);
//        _values.put("usuarioResponsable", pLoggeado.getFullName());
//        _values.put("usd", "$");
        //Exportar a pdf 
        JasperReportAction.exportToPdf(REPORTE_USUARIOS, pf, _values, attachFileName);

        if (log.isDebugEnabled()) {
            log.debug("export as pdf");
        }
    }
    public void renderProducto(List<Producto> pd) {

        final String attachFileName = "productos.pdf";
        
        Map<String, Object> _values = new HashMap<String, Object>();
//        _values.put("numeroP", p.size());
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = context.getRealPath("/reportes/unl.png");
//        _values.put("logo", logo);
//        _values.put("usuarioResponsable", pLoggeado.getFullName());
//        _values.put("usd", "$");
        //Exportar a pdf 
        JasperReportAction.exportToPdf(REPORTE_PRODUCTOS, pd, _values, attachFileName);

        if (log.isDebugEnabled()) {
            log.debug("export as pdf");
        }
    }
    public void renderVehiculo(List<Vehiculo> p) {

        final String attachFileName = "vehiculos.pdf";
        
        Map<String, Object> _values = new HashMap<String, Object>();
//        _values.put("numeroP", p.size());
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//        String logo = context.getRealPath("/reportes/unl.png");
//        _values.put("logo", logo);
//        _values.put("usuarioResponsable", pLoggeado.getFullName());
//        _values.put("usd", "$");
        //Exportar a pdf 
        JasperReportAction.exportToPdf(REPORTE_VEHICULOS, p, _values, attachFileName);

        if (log.isDebugEnabled()) {
            log.debug("export as pdf");
        }
    }
    
      public void renderPersonal(List<Profile> p) {

        final String attachFileName = "personal.pdf";
        
        Map<String, Object> _values = new HashMap<String, Object>();
//        _values.put("numeroP", p.size());
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = context.getRealPath("/reportes/unl.png");
//        _values.put("logo", logo);
//        _values.put("usuarioResponsable", pLoggeado.getFullName());
//        _values.put("usd", "$");
        //Exportar a pdf 
        JasperReportAction.exportToPdf(REPORTE_PERSONAL, p, _values, attachFileName);

        if (log.isDebugEnabled()) {
            log.debug("export as pdf");
        }
    }
    
        
    
    public Date getFechaInf() {
        return fechaInf;
    }

    public void setFechaInf(Date fechaInf) {
        this.fechaInf = fechaInf;
    }

    public Date getFechaSup() {
        return fechaSup;
    }

    public void setFechaSup(Date fechaSup) {
        this.fechaSup = fechaSup;
    }

    public boolean isEstado() {
        return estado;
    }

   

    public String getParametroBusqued() {
        return parametroBusqued;
    }

    public void setParametroBusqued(String parametroBusqued) {
        this.parametroBusqued = parametroBusqued;
    }

}
