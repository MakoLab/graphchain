package com.makolab.rnd.mammoth.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class Bootstrap implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext sc) throws ServletException {
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.register(RootContextConfiguration.class);
    sc.addListener(new ContextLoaderListener(rootContext));
    
    AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
    webContext.register(ServletContextConfiguration.class);
    ServletRegistration.Dynamic dispatcher = sc.addServlet(
        "springDispatcher", new DispatcherServlet(webContext)
    );
    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/");
  }
}