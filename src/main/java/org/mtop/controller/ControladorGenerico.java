/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.mtop.model.EntidadAbstracta_;
import org.mtop.model.Persona;
import org.mtop.model.Propiedad;
import org.mtop.model.TipoAtributo;
import org.mtop.model.TipoAtributo_;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author jesica
 */
@Named
@RequestScoped
public class ControladorGenerico {

    //presentar mensajes de alerta
    private FacesMessage msj;
    @Inject
    ServicioGenerico sg;

    @TransactionAttribute
    //hacer operaciones ejb
    public void cargar() {
        System.out.println("cargar datos");
        inicializarDatosPersona();
        inicializarDirecciones();
       inicializarAntecedentes();
//        inicializarDatosVehiculo();
        msj = new FacesMessage(FacesMessage.SEVERITY_INFO, "cargo exitosamente", null);
        //para llamar en la vista
        FacesContext.getCurrentInstance().addMessage(null, msj);

    }
    //agregar propiedades
    List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();

    public void inicializarDatosPersona() {
        String nombreClase = Persona.class.getName();
        List<TipoAtributo> tipoAtributos = sg.buscarTodos(TipoAtributo.class, TipoAtributo_.nombre.getName(), nombreClase);
        System.out.println("DE DATOS PERSONA");
        if (tipoAtributos.isEmpty()) {
            TipoAtributo tipoAtributoPersona = new TipoAtributo();
            tipoAtributoPersona.setNombre(nombreClase);
            tipoAtributoPersona.setFechaCreacion(Calendar.getInstance().getTime());
            tipoAtributoPersona.setFechaActualizacion(Calendar.getInstance().getTime());

            listaPropiedades.add(agregarPropiedad("Persona", "Direccion", TipoAtributo.class.getName(), null, "Direcciones", null, 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Persona", "Antecedentes", TipoAtributo.class.getName(), null, "Antecedentes", null, 1L, false, null, false));
            tipoAtributoPersona.setListaPropiedades(listaPropiedades);
            sg.crear(tipoAtributoPersona);
            //actualiza la base  de datos
            //sg.getEm().flush();

        }
    }

    public void inicializarDirecciones() {
        String nombreClase = "Direccion";
        List<TipoAtributo> tipoAtributos = sg.buscarTodos(TipoAtributo.class, TipoAtributo_.nombre.getName(), nombreClase);
        System.out.println("Tipo atributo class " + TipoAtributo.class);
        System.out.println("Tipo atributo nombre.getname " + TipoAtributo_.nombre.getName());
        System.out.println("DE DATOS DIRECCIONES");
        if (tipoAtributos.isEmpty()) {
            TipoAtributo tipoAtributoPersona = new TipoAtributo();
            tipoAtributoPersona.setNombre(nombreClase);
            tipoAtributoPersona.setFechaCreacion(Calendar.getInstance().getTime());
            tipoAtributoPersona.setFechaActualizacion(Calendar.getInstance().getTime());

            //agregar propiedades
            List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
            listaPropiedades.add(agregarPropiedad("Direccion", "pais", java.lang.String.class.getName(), null, "Pais", "ingrese pais natal", 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Direccion", "ciudad", java.lang.String.class.getName(), null, "Ciudad", "ingrese la ciudad donde vive natal", 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Direccion", "calles", java.lang.String.class.getName(), null, "Calles", "ingrese las calles donde vive", 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Direccion", "numeroCasa", java.lang.Boolean.class.getName(), null, "Numero de Casa", "ingrese numero de casa", 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Direccion", "numero", java.lang.Integer.class.getName(), null, "Numero de Casa", "ingrese numero de casa", 1L, false, null, false));
            tipoAtributoPersona.setListaPropiedades(listaPropiedades);
            sg.crear(tipoAtributoPersona);
            //actualiza la base  de datos
            //,m,m
            //sg.getEm().flush();
        }
    }

    public void inicializarAntecedentes() {
        String nombreClase = "Antecedentes";
        List<TipoAtributo> tipoAtributos = sg.buscarTodos(TipoAtributo.class, TipoAtributo_.nombre.getName(), nombreClase);
        System.out.println("Tipo atributo class " + TipoAtributo.class);
        System.out.println("Tipo atributo nombre.getname " + TipoAtributo_.nombre.getName());
        System.out.println("DE DATOS ANTECEDENTE");
        if (tipoAtributos.isEmpty()) {
            TipoAtributo tipoAtributoPersona = new TipoAtributo();
            tipoAtributoPersona.setNombre(nombreClase);
            tipoAtributoPersona.setFechaCreacion(Calendar.getInstance().getTime());
            tipoAtributoPersona.setFechaActualizacion(Calendar.getInstance().getTime());

            //agregar propiedades
            List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
            listaPropiedades.add(agregarPropiedad("Antecedentes", "nombredeAntecedente", java.lang.String.class.getName(), null, "Nombre", "ingrese pais natal", 1L, false, null, false));
            listaPropiedades.add(agregarPropiedad("Antecedentes", "tipoAntecedente", java.lang.String.class.getName(), null, "Tipo", "ingrese la ciudad donde vive natal", 1L, false, null, false));
            tipoAtributoPersona.setListaPropiedades(listaPropiedades);
            sg.crear(tipoAtributoPersona);
            //actualiza la base  de datos
            //sg.getEm().flush();
        }
    }

 
    public Propiedad agregarPropiedad(String clasePadre, String nombrePropiedad, String tipoDato, String valorDefecto, String etiquetaVista, String textoAyuda, Long posicion, boolean requerido, String rutaVista, Boolean habilitarEstado) {
        Propiedad propiedad = new Propiedad();
        propiedad.setClasePadre(clasePadre);
        propiedad.setNombrePropiedad(nombrePropiedad);
        propiedad.setTipoDato(tipoDato);
        propiedad.setValorDefecto(valorDefecto);
        propiedad.setEtiquetaVista(etiquetaVista);
        propiedad.setTextoAyuda(textoAyuda);
        propiedad.setPosicion(posicion);
        propiedad.setRutaVista(rutaVista);
        propiedad.setRequerido(requerido);
        propiedad.setHabilitarEstado(habilitarEstado);
        return propiedad;
    }
}
