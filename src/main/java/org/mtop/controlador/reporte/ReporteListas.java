/*
    SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
    VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
    Copyright (C) 2014  Romero Carla, Saquicela Yesica

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.mtop.controlador.reporte;

import com.smartics.common.action.report.JasperReportAction;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
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

/**
 *
 * @author carla
 * @author yesica
 * 
 */
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

    @Inject
    private ServicioGenerico servgen;
    @Inject
    private Identity identity;

    private Date fechaInf;
    private Date fechaSup;
    private boolean estado;
    private String parametroBusqued;
    private Profile pLoggeado;

    /**
     * Default constructor.
     */
    public ReporteListas() {
    }
 public void setEstado(boolean estado) {
        this.estado = estado;
    }


  
    
    @PostConstruct
    public void init() {
        profileService.setEntityManager(em);
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
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = context.getRealPath("/reportes/lusuario.jpg");
        _values.put("logo", logo);

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
        String logo = context.getRealPath("/reportes/lproductos.jpg");
        _values.put("logo", logo);
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
        String logo = context.getRealPath("/reportes/lvehiculos.jpg");
        _values.put("logo", logo);
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
        String logo = context.getRealPath("/reportes/lpersonal.jpg");
       _values.put("logo", logo);
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
