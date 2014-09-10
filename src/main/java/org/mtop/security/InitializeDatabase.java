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

import org.mtop.modelo.dinamico.Structure;
import org.mtop.modelo.dinamico.Group;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.dinamico.BussinesEntityType;
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
import org.mtop.modelo.config.Setting;
import org.mtop.modelo.profile.Profile;
import org.mtop.modelo.security.IdentityObjectAttribute;
import org.mtop.modelo.security.IdentityObjectCredentialType;
import org.mtop.modelo.security.IdentityObjectRelationshipType;
import org.mtop.modelo.security.IdentityObjectType;
import org.mtop.service.BussinesEntityService;
import org.mtop.util.Dates;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
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

            g = session.getPersistenceManager().createGroup("SECRETARIO", "GROUP");
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
            p.setCode("1104009871");
            Date d = new Date(1990,5,13);
           
            p.setFechanacimiento(d);
            p.setName("Administrador");
            p.setFirstname("Mtop");
            p.setSurname("Software de Vehiculos");
            p.setTipo("administrador");
            p.setEstado(true);
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
        validarEstructuraParaChasisV();
        validarEstructuraParaMotorV();
        validarEstructuraParaNeumaticosV();
        validarEstructuraParaTransmisionV();
        validarEstructuraParaSuspensionV();
        validarEstructuraParaDireccionV();
        validarEstructuraParaFrenosV();
        validarEstructuraParaCarroceriaV();

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
            bussinesEntityType.setLabel("Personal");

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
//            attributes.add(buildGroupTypeProperty("Education", "Educación", false, null, 1L, 0L, "Detalle sus logros académicos", 4L));
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
            bussinesEntityType.setLabel("Vehículo");
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

            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Historial", "Historial", "Información del Historial", "/paginas/vehiculo/crear", 1L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "SistemaElectrico", "Sistema Eléctrico", "Información del Sistema Eléctrico", "/paginas/vehiculo/crear", 2L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Lubricantes", "Lubricantes", "Información de Lubricantes", "/paginas/vehiculo/crear", 3L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Carroceria", "Carrocería", "Información de Carrocería", "/paginas/vehiculo/crear", 4L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Direccion", "Dirección", "Información de Dirección", "/paginas/vehiculo/crear", 5L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Frenos", "Frenos", "Información de Frenos", "/paginas/vehiculo/crear", 6L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Chasis", "Chasis", "Información de Chasís", "/paginas/vehiculo/crear", 7L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Motor", "Motor", "Información del Motor", "/paginas/vehiculo/crear", 8L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Neumaticos", "Neumáticos", "Información de los Neumáticos", "/paginas/vehiculo/crear", 3L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Transmision", "Transmisión", "Información de la transmisión", "/paginas/vehiculo/crear", 9L));
            attributes.add(buildStructureTypeProperty("org.mtop.modelo.Vehiculo", "Suspension", "Suspensión", "Información de la suspensión", "/paginas/vehiculo/crear", 10L));

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
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildProperty("Historial", "fechaAdquisicion", Date.class.getName(), ago.getTime(), false, "Fecha Adquisición", "Fecha en que se adquirio vehículo", false, 1L));
            attributes.add(buildProperty("Historial", "tiempoGarantia", Date.class.getName(), ago.getTime(), false, "Tiempo de Garantía", "Tiempo de garantía del vehículo", false, 2L));
//            attributes.add(buildProperty("Historial", "fechaAltaVehiculo", Date.class.getName(), ago.getTime(), false, "Fecha Alta de Vehículo", "Fecha de alta del vehículo", false, 3L));
//            attributes.add(buildProperty("Historial", "fechaBajaVehiculo", Date.class.getName(), ago.getTime(), false, "Fecha Baja de Vehículo", "Fecha de baja del vehículo", false, 4L));
            attributes.add(buildProperty("Historial", "anioServicio", Date.class.getName(), ago.getTime(), false, "Año de servicio", "Año de servicio del vehículo", false, 5L));
            attributes.add(buildProperty("Historial", "chatarrizacion", "java.lang.String[]", "Si,No*", false, "Chatarrización", "Seleccione una opción", false, 6L));

//Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaChasisV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Chasis";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel("Chasís");
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
            attributes.add(buildProperty("Chasis", "serie", String.class.getName(), null, true, "Serie", "Ingrese la serie para el chasís", false, 1L));
            attributes.add(buildProperty("Chasis", "tipo", String.class.getName(), null, false, "Tipo", " Ingrese el tipo de chasís", false, 2L));
            attributes.add(buildProperty("Chasis", "capacidadTolva", String.class.getName(), null, false, "Capacidad Tolva", "Ingrese la capacidad de Tolva del chasís", false, 3L));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaMotorV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Motor";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildProperty("Motor", "serie", String.class.getName(), null, true, "Serie", "Ingrese la serie para el Motor", false, 1L));
            attributes.add(buildProperty("Motor", "marca", String.class.getName(), null, false, "Marca", " Ingrese la marca del Motor", false, 2L));
            attributes.add(buildProperty("Motor", "modelo", String.class.getName(), null, false, "Modelo Tolva", "Ingrese el modelo del Motor", false, 3L));
            attributes.add(buildProperty("Motor", "tipo", String.class.getName(), null, false, "Tipo", "Ingrese el tipo de Motor", false, 4L));
            attributes.add(buildPropertyParteMecanica("Motor", "soporte", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Soporte", " Ingrese el valor para soporte del Motor", false, 5L, 100));
            attributes.add(buildProperty("Motor", "tanque", String.class.getName(), null, false, "Tanque", "Ingrese el valor para tanque del Motor", false, 6L));

            attributes.add(buildPropertyParteMecanica("Motor", "sistemaCombustible", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Sistema de Combustible", "Ingrese el Sistema de Combustible para el Motor", false, 7L, 100));
            attributes.add(buildProperty("Motor", "potencia", String.class.getName(), null, false, "Potencia", " Ingrese la potencia del Motor", false, 8L));
            attributes.add(buildProperty("Motor", "sistemaRefrigeracion", String.class.getName(), null, false, "Sistema de Refrigeración", "Ingrese el valor para sistema de refrigeración de Motor", false, 10L));
            attributes.add(buildProperty("Motor", "admicionEscape", String.class.getName(), null, false, "Admición de Escape", " Ingrese la admición de escape del Motor", false, 11L));
            attributes.add(buildPropertyParteMecanica("Motor", "instrumentosControl", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Instrumentos de control", "Ingrese instrumentos de control del Motor", false, 12L, 100));
            attributes.add(buildPropertyParteMecanica("Motor", "potencia", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Potencia", "Escoja el estado para potencia del motor", false, 13L, 100));
            attributes.add(buildPropertyParteMecanica("Motor", "sistemaLubricacion", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Sistema de Lubricación", "Escoja el estado para sistema de lubricación del motor", false, 13L, 100));
            attributes.add(buildPropertyParteMecanica("Motor", "sistemaRefrigeracion", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Sistema de Refrigeración", "Escoja el estado para sistema de refrigeración del motor", false, 14L, 100));
            attributes.add(buildPropertyParteMecanica("Motor", "sistemaAdmEsc", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Sistema de Admición y Escape", "Escoja el estado para sistema de admición y escape del motor", false, 15L, 100));
            attributes.add(buildPropertyParteMecanica("Motor", "sistemaInsCon", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Sistema de Instrumentos de Control", "Escoja el estado para sistema de instrumentos de control y escape del motor", false, 16L, 100));
            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaTransmisionV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Transmision";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildPropertyParteMecanica("Transmision", "embrague", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Embrague", "Ingrese valor para embrague", false, 1L, 100));
            attributes.add(buildPropertyParteMecanica("Transmision", "cajaCambios", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Caja de Cambios", " Ingrese valor para caja de cambios", false, 2L, 100));
            attributes.add(buildPropertyParteMecanica("Transmision", "diferenciales", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Diferenciales", "Ingrese valor para diferenciales", false, 3L, 100));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaSuspensionV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Suspension";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildPropertyParteMecanica("Suspension", "muelles-amortiguador", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Muelles-amortiguador", "Ingrese valor para muelles-amortiguador", false, 1L, 80));
            attributes.add(buildPropertyParteMecanica("Suspension", "ejeDelantero", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Eje delantero", " Ingrese valor para eje delantero", false, 2L, 100));
            attributes.add(buildPropertyParteMecanica("Suspension", "ejePosterior", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Eje posterior", "Ingrese valor para eje posterior", false, 3L, 100));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaDireccionV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Direccion";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildPropertyParteMecanica("Direccion", "cajaDireccion", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Caja de dirección", "Ingrese valor para dirección", false, 1L, 80));
            attributes.add(buildPropertyParteMecanica("Direccion", "articulacionesTerminales", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Articulaciones y terminales", " Ingrese valor para articulaciones y terminales", false, 2L, 100));
            attributes.add(buildPropertyParteMecanica("Direccion", "pinesBocines", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Pines bocines y rótulas", "Ingrese valor para pines bocines y rótulas", false, 3L, 100));
            attributes.add(buildPropertyParteMecanica("Direccion", "bombaDireccion", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Bomba de la dirección", "Ingrese valor para bomba de la dirección", false, 4L, 100));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaFrenosV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Frenos";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildPropertyParteMecanica("Frenos", "cilindroPrincipal", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Cilindro principal", "Ingrese valor para cilindro principal", false, 1L, 100));
            attributes.add(buildPropertyParteMecanica("Frenos", "cilindroSecundario", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Cilindros secundarios", " Ingrese valor para cilindros secundarios", false, 2L, 100));
            attributes.add(buildPropertyParteMecanica("Frenos", "forros", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Forros de freno", "Ingrese valor para forros de freno", false, 3L, 80));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaCarroceriaV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Carroceria";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildPropertyParteMecanica("Carroceria", "forma", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Forma", "Ingrese valor para forma de la carrocería", false, 1L, 100));

            //Agregar atributos
            structure.setProperties(attributes);
            bussinesEntityType.addStructure(structure);
            entityManager.persist(bussinesEntityType);
            entityManager.flush();
        }
    }

    private void validarEstructuraParaNeumaticosV() {
        BussinesEntityType bussinesEntityType = null;
        String name = "Neumaticos";
        try {
            TypedQuery<BussinesEntityType> query = entityManager.createQuery("from BussinesEntityType b where b.name=:name",
                    BussinesEntityType.class);
            query.setParameter("name", name);
            bussinesEntityType = query.getSingleResult();
        } catch (NoResultException e) {
            bussinesEntityType = new BussinesEntityType();
            bussinesEntityType.setName(name);
            bussinesEntityType.setLabel("Neumáticos");
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
            attributes.add(buildProperty("Neumaticos", "llantas", String.class.getName(), null, false, "Llantas", "Ingrese valor para llantas", false, 1L));
            attributes.add(buildProperty("Neumaticos", "aros", String.class.getName(), null, false, "Aros", " Ingrese valor para aros", false, 2L));
            attributes.add(buildProperty("Neumaticos", "presion", String.class.getName(), null, false, " Presión", "Ingrese valor para presión", false, 3L));
            attributes.add(buildPropertyParteMecanica("Neumaticos", "llantas", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Llantas", "Escoja el estado para las llantas", false, 13L, 100));
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
            bussinesEntityType.setLabel(name);
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
            attributes.add(buildProperty("Lubricantes", "motor", String.class.getName(), null, false, "Motor", "Ingrese el motor de lubricante para el vehículo", false, 1L));
            attributes.add(buildProperty("Lubricantes", "caja", String.class.getName(), null, false, "Caja", "Ingrese la caja de lubricantes del vehículo", false, 2L));
            attributes.add(buildProperty("Lubricantes", "hidraulico", String.class.getName(), null, false, "Hidráulico", "Ingrese el hidráulico de lubricantes del vehículo", false, 3L));
            attributes.add(buildProperty("Lubricantes", "corona", String.class.getName(), null, false, "Corona", "Ingrese la corona de hidráulico para el vehículo", false, 4L));

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
            bussinesEntityType.setLabel("Sistema Eléctrico");
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
            attributes.add(buildProperty("SistemaElectrico", "luces", String.class.getName(), null, false, "Luces", "Ingrese valor para las luces del vehículo", false, 8L));
            attributes.add(buildPropertyParteMecanica("SistemaElectrico", "alterador", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Alterador", "Ingrese el estado para alterador", false, 5L, 100));
            attributes.add(buildPropertyParteMecanica("SistemaElectrico", "baterias", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Baterias", "Ingrese el estado para bateria", false, 6L, 100));
            attributes.add(buildPropertyParteMecanica("SistemaElectrico", "motorArranque", "org.mtop.modelo.EstadoParteMecanica", "Malo,Bueno*", false, "Arranque", "Ingrese el estado para motor de arranque", false, 7L, 100));
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
            bussinesEntityType.setLabel("Solicitud Reparación y Mantenimiento");
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
            attributes.add(buildProperty(SolicitudReparacionMantenimiento.class.getName(), "viNumSolicitud", String.class.getName(), "8000", false, "Valor Inicial para numero de solicitud", "Ingrese el valor inicial para el número de la solicitud", false, 1L));

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
            bussinesEntityType.setLabel("Requisición");
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
            attributes.add(buildProperty(Requisicion.class.getName(), "viNumRequisicionReparacion", String.class.getName(), "900", false, "Valor Inicial para numero de requisición tipo reparación", "Ingrese el valor inicial para el número de requisición tipo reparación", false, 1L));
            attributes.add(buildProperty(Requisicion.class.getName(), "viNumRequisicionBienes", String.class.getName(), "200", false, "Valor Inicial para numero de requisición tipo bienes y servicios", "Ingrese el valor inicial para el número de requisición tipo bienes y servicios", false, 1L));

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
            bussinesEntityType.setLabel("Kardex");
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
            bussinesEntityType.setLabel("Producto");
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
            bussinesEntityType.setLabel("Partida de Contabilidad");
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
            bussinesEntityType.setLabel("Plan de Mantenimiento");
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
            bussinesEntityType.setLabel("Estado del Vehículo");
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

    private Property buildStructureTypeProperty(String groupName, String name, String label, String helpinline, String customForm, Long sequence) {
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

    private Property buildPropertyParteMecanica(String groupName, String name, String type, Serializable value, boolean required, String label, String helpinline, boolean showInColumns, Long sequence, Integer evaluacionParte) {
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
        property.setEvaluacionParte(evaluacionParte);
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
