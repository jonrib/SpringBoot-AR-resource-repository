package com.jonrib.app;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

    public static void main(String[] args) throws Exception {
    	
    	SpringApplication app = new SpringApplication(WebApplication.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "8081"));
        app.run(args);
    	
        //SpringApplication.run(WebApplication.class, args);
    }
}
