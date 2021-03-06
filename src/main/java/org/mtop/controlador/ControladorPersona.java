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

package org.mtop.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.profile.Profile;
import org.mtop.servicios.ServicioGenerico;
import org.picketlink.idm.api.AttributesManager;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.PersistenceManager;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.PasswordCredential;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Named
@ViewScoped
public class ControladorPersona extends BussinesEntityHome<Profile> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    @Inject
    private Identity identity;
    @Inject
    private Credentials credentials;
    @Inject
    private IdentitySession security;

    private String password;
    private String passwordConfirm;
    //@Inject
    //private ProfileService pservicio;
    List<Profile> listaPersona = new ArrayList<Profile>();
    List<Profile> listaPersona1;
    private String palabrab;
    private String nombusuario;
    private String mensaje1="";

    public String getMensaje1() {
        return mensaje1;
    }

    public void setMensaje1(String mensaje1) {
        this.mensaje1 = mensaje1;
    }
    

    public String getNombusuario() {
        return nombusuario;
    }

    public void setNombusuario(String nombusuario) {
        this.nombusuario = nombusuario;
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public void buscar() {
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        System.out.println("ingreso la palabra" + palabrab);
        //buscando por coincidencia descripciion
        List<Profile> lp = new ArrayList<Profile>();
        for (Profile p : listaPersona1) {
            if (p.getFullName().contains(palabrab)) {
                lp.add(p);

            } else {
                if (p.getTipo().contains(palabrab)) {
                    lp.add(p);
                } else {
                    if (p.getCedula().contains(palabrab)) {
                        lp.add(p);
                    } else {
                        if (p.getCargo() != null) {
                            if (p.getCargo().contains(palabrab)) {
                                lp.add(p);
                            }
                        }

                    }
                }
            }
        }

        if (lp.isEmpty()) {
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACION:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACION:No se ha encontrado", palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaPersona = lp;
        }

    }

    public void limpiar() {
        palabrab = "";
        listaPersona = listaPersona1;

    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        for (Profile p : listaPersona1) {
            if (p.getFullName().toLowerCase(Locale.FRENCH).contains(query.toLowerCase())) {
                ced.add(p.getFullName());
            }
            if (p.getTipo().toLowerCase().contains(query.toLowerCase()) && !ced.contains(p.getTipo())) {
                ced.add(p.getTipo());
            }
            System.out.println("p.getcode" + p.getCode());
            if (p.getCedula().contains(query)) {
                ced.add(p.getCedula());
            }
            System.out.println("cargo " + p.getCargo());
            if (p.getCargo() != null) {
                if (p.getCargo().toLowerCase().contains(query.toLowerCase()) && !ced.contains(p.getCargo())) {
                    ced.add(p.getCargo());
                }

            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public Long getPersonaId() {

        return (Long) getId();
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPersonaId(Long personaId) {

        setId(personaId);

    }

    @TransactionAttribute   //
    public Profile load() {
        if (isIdDefined()) {
            wire();
        }
       return getInstance();
    }

    public List<Profile> getListaPersona() {
        return listaPersona;
    }

    public void setListaPersona(List<Profile> listaPersona) {
        this.listaPersona = listaPersona;
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        // pservicio.setEntityManager(em);
        servgen.setEm(em);
        listaPersona = findAll(Profile.class);
        listaPersona1 = listaPersona;
    }

    @Override
    protected Profile createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Profile.class.getName());
        Date now = Calendar.getInstance().getTime();
        Profile persona = new Profile();
        persona.setCreatedOn(now);
        persona.setLastUpdate(now);
        persona.setActivationTime(now);
        persona.setResponsable(null);
        persona.setType(_type);
        // persona.buildAttributes(bussinesEntityService);  //
        return persona;
    }

    @Override
    public Class<Profile> getEntityClass() {
        return Profile.class;
    }
//tiene muchas variaciones

    @TransactionAttribute
    public String guardar() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                List<BussinesEntityAttribute> listA = getInstance().getAttributes();
                System.out.println("Attributos " + getInstance().getAttributes().size());
                for (BussinesEntityAttribute a : listA) {
                    System.out.println("ATRIB " + a.getName() + " valor " + a.getValue().toString() + " valor String " + a.getStringValue());
                    //save(a);

                }
                //update();
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Persona" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {

                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo nueva Vehiculo " + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/personal/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public void guardarUsuario() {
        try {
            crearCuentaUsuario();
        } catch (IdentityException ex) {
            Logger.getLogger(ControladorPersona.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @TransactionAttribute
    public void crearCuentaUsuario() throws IdentityException {

        // TODO validate username, email address, and user existence
        System.out.println("nombre de usuario>>>>>>>>>>>" + getInstance().getUsername());
        PersistenceManager identityManager = security.getPersistenceManager();
        User user = identityManager.createUser(getInstance().getUsername());
        System.out.println("antres atributooo managerrrr");
        AttributesManager attributesManager = security.getAttributesManager();
        System.out.println("antres usuariooooooooooooooooooooo " + password);
        PasswordCredential p = new PasswordCredential(getPassword());
        System.out.println("pasooo pasword");
        attributesManager.updatePassword(user, p.getValue());
//            attributesManager.addAttribute(user, "email", getInstance().getEmail());  //me permite agregar un atributo de cualquier tipo a un usuario
        attributesManager.addAttribute(user, "estado", "ACTIVO");
        System.out.println("finalizo creaaaaaar usuariooooo");
        em.flush();
        getInstance().setPassword(getPassword());
        getInstance().getIdentityKeys().add(user.getKey());
        getInstance().setUsernameConfirmed(true);
        getInstance().setShowBootcamp(true);
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        save(getInstance()); //

    }

    @Transactional
    public String borrarEntidad() {

        try {
            if (getInstance() == null) {
                throw new NullPointerException("Servicio is null");
            }
            if (getInstance().isPersistent()) {
                delete(getInstance());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getInstance().getName(), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡No existe una entidad para ser borrada!", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.toString()));
        }
        return "/paginas/admin/personal/lista.xhtml?faces-redirect=true";
    }
}
