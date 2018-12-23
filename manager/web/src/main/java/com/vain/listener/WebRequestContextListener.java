package com.vain.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletContext;


@Slf4j
public class WebRequestContextListener extends SpringBootServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);
        if (rootAppContext != null) {
            servletContext.addListener(new RequestContextListener());
        } else {
            this.logger.debug("No ContextLoaderListener registered, as "
                    + "createRootApplicationContext() did not "
                    + "return an application context");
        }
    }
}