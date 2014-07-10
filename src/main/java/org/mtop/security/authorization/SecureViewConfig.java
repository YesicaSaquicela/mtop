
package org.mtop.security.authorization;

import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ViewConfig
public interface SecureViewConfig {

    static enum Pages {

        @FacesRedirect
        @ViewPattern("/paginas/inicio.xhtml")
        @Owner
        USER,        
        @ViewPattern("/paginas/admin/*")
        @Admin
        ADMIN,
      
        
//        @ViewPattern("/paginas/planMantenimiento/*")
//        @Admin
//        ADMIN3,
//        @ViewPattern("/paginas/personal/*")
//        @Admin
//        ADMIN4,
        
        
        @ViewPattern("/paginas/secretario/*")
        @Secretario
        SECRETARIO,
//        @ViewPattern("/paginas/requisicion/*")
//        @Secretario
//        SECRETARIO1,
//        @ViewPattern("/paginas/partidaContabilida/*")
//        @Secretario
//        SECRETARIO2,
//        @ViewPattern("/paginas/producto/*")
//        @Secretario
//        SECRETARIO3,
        @ViewPattern("/paginas/*")        
        @AccessDeniedView("/paginas/denied.xhtml")
        @LoginView("/paginas/login.xhtml")
        ALL;
    }
}