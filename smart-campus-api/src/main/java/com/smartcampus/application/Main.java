package com.smartcampus.application;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir("temp");

        // Explicitly set connector
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        // Register Jersey servlet
        ServletContainer servletContainer = new ServletContainer(new AppConfig());
        tomcat.addServlet(ctx, "jersey", servletContainer);

        ctx.addServletMappingDecoded("/api/v1/*", "jersey");
        // Set default servlet
        ctx.addParameter("reloadable", "false");

        tomcat.start();
        System.out.println("System running on port 8080");
        tomcat.getServer().await();
    }
}