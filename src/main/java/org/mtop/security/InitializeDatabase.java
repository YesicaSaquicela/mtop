/**
 * This file is part of Glue: Adhesive BRMS
 * 
* Copyright (c)2012 José Luis Granda <jlgranda@eqaula.org> (Eqaula Tecnologías
 * Cia Ltda) Copyright (c)2012 Eqaula Tecnologías Cia Ltda (http://eqaula.org)
 * 
* If you are developing and distributing open source applications under the GNU
 * General Public License (GPL), then you are free to re-distribute Glue under
 * the terms of the GPL, as follows:
 * 
* GLue is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
* Glue is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
* You should have received a copy of the GNU General Public License along with
 * Glue. If not, see <http://www.gnu.org/licenses/>.
 * 
* For individuals or entities who wish to use Glue privately, or internally,
 * the following terms do not apply:
 * 
* For OEMs, ISVs, and VARs who wish to distribute Glue with their products, or
 * host their product online, Eqaula provides flexible OEM commercial licenses.
 * 
* Optionally, Customers may choose a Commercial License. For additional
 * details, contact an Eqaula representative (sales@eqaula.org)
 */
package org.mtop.security;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import org.mtop.model.*;
import org.mtop.model.config.Setting;
import org.mtop.model.profile.Profile;
import org.mtop.model.security.IdentityObjectAttribute;
import org.mtop.model.security.IdentityObjectCredentialType;
import org.mtop.model.security.IdentityObjectRelationshipType;
import org.mtop.model.security.IdentityObjectType;
import org.mtop.service.BussinesEntityService;
import org.mtop.util.Dates;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import org.jboss.seam.security.management.picketlink.IdentitySessionProducer;
import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;
import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;
import org.mtop.modelo.EstadoVehiculo;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.Vehiculo;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;

/**
 * Validates that the database contains the minimum required entities to
 * function from SocialPM
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @adapter <a href="mailto:jlgranda81@gmail.com">José Luis Granda</a>
 * @adapter <a href="mailto:cesar06ar@gmail.com">César Abad Ramos</a>
 */
@Transactional(TransactionPropagation.REQUIRED)
public class InitializeDatabase {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    @Inject
    private IdentitySessionFactory identitySessionFactory;
    @Inject
    protected BussinesEntityService bussinesEntityService;

    @Transactional
    public void validate(@Observes @Initialized final WebApplication webapp) throws IdentityException {
        bussinesEntityService.setEntityManager(entityManager);
        validateDB();
        validateStructure();
        validateIdentityObjectTypes();
        validateSecurity();

    }

    private void validateDB() {
        Setting singleResult = null;
        try {
            TypedQuery<Setting> query = entityManager.createQuery("from Setting s where s.name='schemaVersion'",
                    Setting.class);
            singleResult = query.getSingleResult();
        } catch (NoResultException e) {
            Date now = Calendar.getInstance().getTime();
            singleResult = new Setting("schemaVersion", "0");
            singleResult.setCreatedOn(now);
            singleResult.setLastUpdate(now);
            entityManager.persist(singleResult);
            entityManager.flush();
        }

        System.out.println("Current database schema version is [" + singleResult.getValue() + "]");

    }

    private void validateIdentityObjectTypes() {
        if (entityManager.createQuery("select t from IdentityObjectType t where t.name = :name")
                .setParameter("name", "USER")
                .getResultList().size() == 0) {

            IdentityObjectType user = new IdentityObjectType();
            user.setName("USER");
            entityManager.persist(user);
        }

        if (entityManager.createQuery("select t from IdentityObjectType t where t.name = :name")
                .setParameter("name", "GROUP")
                .getResultList().size() == 0) {
            IdentityObjectType group = new IdentityObjectType();
            group.setName("GROUP");
            entityManager.persist(group);
        }

        if (entityManager.createQuery("select t from IdentityObjectRelationshipType t where t.name = :name")
                .setParameter("name", "JBOSS_IDENTITY_MEMBERSHIP")
                .getResultList().size() == 0) {
            IdentityObjectRelationshipType relation = new IdentityObjectRelationshipType();
            relation.setName("JBOSS_IDENTITY_MEMBERSHIP");
            entityManager.persist(relation);
        }

        if (entityManager.createQuery("select t from IdentityObjectCredentialType t where t.name = :name")
                .setParameter("name", "PASSWORD")
                .getResultList().size() == 0) {

            IdentityObjectCredentialType PASSWORD = new IdentityObjectCredentialType();
            PASSWORD.setName("PASSWORD");
            entityManager.persist(PASSWORD);
        }
    }

    private void validateSecurity() throws IdentityException {
        // Validate credential types
        Map<String, Object> sessionOptions = new HashMap<String, Object>();
        sessionOptions.put(IdentitySessionProducer.SESSION_OPTION_ENTITY_MANAGER, entityManager);

        IdentitySession session = identitySessionFactory.createIdentitySession("default", sessionOptions);
        /*
         * Create our test user (me!)
         */
        Date now = Calendar.getInstance().getTime();
        BussinesEntityType bussinesEntityType = null;
        TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                BussinesEntityType.class);
        query.setParameter("name", Profile.class.getName());
        Profile p = null;
        Profile admin = null;
        //List<User> members = new ArrayList<User>();
        org.picketlink.idm.api.Group g = session.getPersistenceManager().findGroup("ADMIN", "GROUP");
        //session.getAttributesManager().
        if (g == null) {
            g = session.getPersistenceManager().createGroup("ADMIN", "GROUP");
        }

        bussinesEntityType = query.getSingleResult();
        if (session.getPersistenceManager().findUser("admin") == null) {
            User u = session.getPersistenceManager().createUser("admin");
            session.getAttributesManager().updatePassword(u, "adminadmin");
            session.getAttributesManager().addAttribute(u, "email", "mtop@unl.edu");
            session.getAttributesManager().addAttribute(u, "estado", "ACTIVO");
            //members.add(u);
            //TODO revisar error al implementar la relacion entre un grupo y usuario.... 
            session.getRelationshipManager().associateUser(g, u);

            p = new Profile();
            p.setEmail("mtop@unl.edu");
            p.setUsername("admin");
            p.setPassword("adminadmin");
            p.getIdentityKeys().add(u.getKey());
            p.setUsernameConfirmed(true);
            p.setShowBootcamp(true);

            p.setName("Administrador");
            p.setFirstname("Mtop");
            p.setSurname("Software de Vehiculos");
            p.setCreatedOn(now);
            p.setLastUpdate(now);
            p.setActivationTime(now);
            p.setExpirationTime(Dates.addDays(now, 364));
            p.setResponsable(null); //Establecer al usuario actual
            p.setType(bussinesEntityType); //Relacionar con un tipo de entidad de negocio y su estructura
            p.buildAttributes(bussinesEntityService); //Crear la estructura de datos glue
            entityManager.persist(p);
            entityManager.flush();
            admin = p;

        }
    }

    private void validateStructure() {
        validarEstructuraParaPerfilDeUsuario();
        //    validarEstructuraEducacionDelPerfilDeUsuarios();       
        validarEstructuraParaVehiculo();
        validarEstructuraParaHistorialV();
        validarEstructuraParaLubricantesV();
        validarEstructuraParaSistemaElectricoV();
        validarEstructuraParaKardex();
        validarEstructuraParaProducto();
        validarEstructuraParaSolicitud();
        validarEstructuraParaRequisicion();
        validarEstructuraParaPartidaC();
        validarEstructuraParaPlanMantenimiento();

                
        //validarEstructuraParaPerfilDeUsuario();
    }

    private void validarEstructuraParaPerfilDeUsuario() {
        BussinesEntityType bussinesEntityType = null;

        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", Profile.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(Profile.class.getName());

            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(Profile.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);

            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();

            attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            attributes.add(buildGroupTypeProperty("Education", "Educación", false, null, 1L, 0L, "Detalle sus logros académicos", 4L));
            //attributes.add(buildGroupTypeProperty("TrayectoriaLaboral", "Trayectoria Laboral", false, null, 1L, 0L, "Detalle de la trayectoria laboral desde el año 2000 en adelante", 5L));
            //Agregar atributos
            structure.setProperties(attributes);

            bussinesEntityType.addStructure(structure);

            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

        System.out.println("Structure for Profile [" + bussinesEntityType + "]");
    }

    private void validarEstructuraEducacionDelPerfilDeUsuarios() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Education";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);

            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(name);
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);

            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();

            attributes.add(buildProperty("title", String.class.getName(), "", true, "Titulo", "¿Qué titulación obtuviste?", true, 1L));
            attributes.add(buildProperty("country", String.class.getName(), "", true, "País", "¿En que país obtuvo este título?", true, 2L));
            attributes.add(buildProperty("institution", String.class.getName(), "", true, "Institución", "¿En que centro de estudios?", true, 3L));
            attributes.add(buildProperty("graduationDate", Date.class.getName(), now, false, "Fecha de Graduación", "¿Cuándo finalizó sus estudios?", 4L));
            attributes.add(buildProperty("atPresent", "java.lang.String[]", "Sí,No*", true, "Al presente", "¿Esta cursando actualmente esta titulación?", 5L));
            attributes.add(buildProperty("level", "java.lang.String[]", "Secundario,Terciario,Universitario,Postgrado,Master,Doctorado,Otro", true, "Nivel de estudio", "Nivel de los estudios cursados", true, 6L));

            //Agregar atributos
            structure.setProperties(attributes);

            bussinesEntityType.addStructure(structure);

            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaVehiculo() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", Vehiculo.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(Vehiculo.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(Vehiculo.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            
            attributes.add(buildStructureTypeProperty("Vehiculo","Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("Vehiculo","SistemaElectrico", "Sistema Electrico", "Información del SistemaElectrico", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("Vehiculo","Lubricantes", "Lubricantes", "Información de Lubricantes", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("Vehiculo","Carroceria", "Carroceria", "Información de Carroceria", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("Vehiculo","Direccion", "Dirección", "Información de dirección", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("Vehiculo","Frenos", "Frenos", "Información de Frenos", "/paginas/vehiculo/crear", 1L));
//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaHistorialV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Historial";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(name);
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //Para inicializar estructuras llamar [buildProperty()]
            attributes.add(buildProperty("Historial", "fechaAdquisicion", Date.class.getName(), ago.getTime(), false, "Fecha Adquisición", "Fecha en que se adquirio vehículo", false, 5L));
            attributes.add(buildProperty("Historial", "tiempoGarantia", Date.class.getName(), ago.getTime(), false, "Tiempo de Garantia", "Tiempo de garantia del vehículo", false, 5L));
            attributes.add(buildProperty("Historial", "fechaAltaVehiculo", Date.class.getName(), ago.getTime(), false, "Fecha Alta de Vehiculo", "Fecha de alta del vehículo", false, 6L));
            attributes.add(buildProperty("Historial", "fechaBajaVehiculo", Date.class.getName(), ago.getTime(), false, "Fecha Baja de Vehiculo", "Fecha de baja del vehículo", false, 7L));
            attributes.add(buildProperty("Historial", "anioServicio", Date.class.getName(), ago.getTime(), false, "Año de servicio", "Año de servicio del vehículo", false, 8L));
            attributes.add(buildProperty("Historial", "chatarrizacion", "java.lang.String[]", "Si,No*", false, "Chatarrización", "", false, 2L));
            attributes.add(buildProperty("Historial", "inicioVigenciaSoat", Date.class.getName(), ago.getTime(), false, "Inicio de Vigencia de Soat", "Ingrese el inicio de vigencia del soat", false, 12L));
            attributes.add(buildProperty("Historial", "finVigenciaSoat", Date.class.getName(), ago.getTime(), false, "Fin de Vigencia de Soat", "Ingrese el fin de vigencia del soat", false, 12L));
//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaLubricantesV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Lubricantes";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(name);
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //Para inicializar estructuras llamar [buildProperty()]
            attributes.add(buildProperty("Lubricantes", "motor", String.class.getName(), null, false, "Motor", "Ingrese el motor de lubricante para el vehículo", false, 5L));
            attributes.add(buildProperty("Lubricantes", "caja", String.class.getName(), null, false, "Caja", "Ingrese la caja de lubricantes del vehículo", false, 8L));
            attributes.add(buildProperty("Lubricantes", "hidraulico", String.class.getName(), null, false, "Hidraulico", "Ingrese el hidraulico de lubricantes del vehiculo", false, 5L));
            attributes.add(buildProperty("Lubricantes", "corona", String.class.getName(), null, false, "Corona", "Ingrese la corona de hidraulico paa el vehículo", false, 6L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaSistemaElectricoV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "SistemaElectrico";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(name);
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //Para inicializar estructuras llamar [buildProperty()]
            attributes.add(buildProperty("SistemaElectrico", "voltaje", String.class.getName(), null, false, "Voltaje", "Ingrese el voltaje del vehículo", false, 5L));
            attributes.add(buildProperty("SistemaElectrico", "luces", String.class.getName(), null, false, "Luces", "Ingrese las luces del vehículo", false, 8L));
            attributes.add(buildProperty("SistemaElectrico", "alterador", "java.lang.String[]", "Bueno,Malo*", false, "Alterador", "Ingrese el estado para alterador", false, 5L));
            attributes.add(buildProperty("SistemaElectrico", "baterias", "java.lang.String[]", "Bueno,Malo*", false, "Baterias", "Ingrese el estado para bateria", false, 6L));
            attributes.add(buildProperty("SistemaElectrico", "motorArranque", "java.lang.String[]", "Bueno,Malo*", false, "Arranque", "Ingrese el estado para motor de arranque", false, 7L));
//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaSolicitud() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", SolicitudReparacionMantenimiento.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(SolicitudReparacionMantenimiento.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(SolicitudReparacionMantenimiento.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaRequisicion() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", Requisicion.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(Requisicion.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(Requisicion.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaKardex() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", Kardex.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(Kardex.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(Kardex.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaProducto() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", Producto.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(Producto.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(Producto.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaPartidaC() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", PartidaContabilidad.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(PartidaContabilidad.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(PartidaContabilidad.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaPlanMantenimiento() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", PlanMantenimiento.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(PlanMantenimiento.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(PlanMantenimiento.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }

    private void validarEstructuraParaEstadoVehiculo() {
        BussinesEntityType bussinesEntityType = null;
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", EstadoVehiculo.class.getName());
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(EstadoVehiculo.class.getName());
            //Agrupaciones de propiedades
            Date now = Calendar.getInstance().getTime();
            Calendar ago = Calendar.getInstance();
            ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
            Structure structure = null;
            structure = new Structure();
            structure.setName(EstadoVehiculo.class.getName());
            structure.setCreatedOn(now);
            structure.setLastUpdate(now);
            //Lista de atributos de entidad de negocios
            List<Property> attributes = new ArrayList<Property>();
            //attributes.add(buildStructureTypeProperty("PersonalData", "Datos personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
            //Para inicializar estructuras llamar [buildGroupTypeProperty()]
            // attributes.add(buildStructureTypeProperty("Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }

    }


    /*FIN Estructuras*/
    private Property buildGroupTypeProperty(String name, String label, boolean showDefaultBussinesEntityProperties, String generatorName, Long minimumMembers, Long maximumMembers, String helpinline, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(Group.class.getName());
        property.setValue(null);
        property.setRequired(true);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setShowDefaultBussinesEntityProperties(showDefaultBussinesEntityProperties);
        property.setGeneratorName(generatorName);
        property.setMinimumMembers(minimumMembers);
        property.setMaximumMembers(maximumMembers);
        property.setSequence(sequence);
        return property;
    }

    private Property buildStructureTypeProperty(String name, String label, String helpinline, String customForm, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(Structure.class.getName());
        property.setValue(null);
        property.setRequired(true);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setCustomForm(customForm);
        property.setShowDefaultBussinesEntityProperties(false);
        property.setGeneratorName(null);
        property.setMaximumMembers(null);
        property.setSequence(sequence);
        return property;
    }
    private Property buildStructureTypeProperty(String groupName,String name, String label, String helpinline, String customForm, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(Structure.class.getName());
        property.setValue(null);
        property.setRequired(true);
        property.setGroupName(groupName);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setCustomForm(customForm);
        property.setShowDefaultBussinesEntityProperties(false);
        property.setGeneratorName(null);
        property.setMaximumMembers(null);
        property.setSequence(sequence);
        return property;
    }

    private Property buildProperty(String name, String type, Serializable value, boolean required, String label, String helpinline, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(type);
        property.setValue(value);
        property.setRequired(required);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setShowInColumns(false);
        property.setSequence(sequence);
        return property;
    }

    private Property buildProperty(String name, String type, Serializable value, boolean required, String label, String helpinline, boolean showInColumns, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(type);
        property.setValue(value);
        property.setRequired(required);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setRender(null);
        property.setCustomForm(null);
        property.setShowInColumns(showInColumns);
        property.setSequence(sequence);
        return property;
    }

    private Property buildProperty(String name, String type, Serializable value, boolean required, String label, String helpinline, boolean showInColumns, String validator, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(type);
        property.setValue(value);
        property.setRequired(required);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setRender(null);
        property.setCustomForm(null);
        property.setShowInColumns(showInColumns);
        property.setValidator(validator);
        property.setSequence(sequence);
        return property;
    }

    private Property buildProperty(String groupName, String name, String type, Serializable value, boolean required, String label, String helpinline, boolean showInColumns, Long sequence) {
        Property property = new Property();
        property.setGeneratorName(null);
        property.setGroupName(groupName);
        property.setName(name);
        property.setType(type);
        property.setValue(value);
        property.setRequired(required);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setRender(null);
        property.setCustomForm(null);
        property.setShowInColumns(showInColumns);
        property.setSequence(sequence);
        return property;
    }

    private Property buildPropertyAsSurvey(String name, String type, Serializable value, boolean required, String label, String helpinline, Long sequence) {
        Property property = new Property();
        property.setName(name);
        property.setType(type);
        property.setValue(value);
        property.setRequired(required);
        property.setLabel(label);
        property.setHelpInline(helpinline);
        property.setRender(null);
        property.setCustomForm(null);
        property.setShowInColumns(false);
        property.setSequence(sequence);
        property.setSurvey(true);
        return property;
    }
}
