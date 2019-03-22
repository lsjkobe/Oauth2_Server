package com.leoli.oauth2_server;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


//添加这个类是因为tomcat部署的问题，不添加访问会404
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Oauth2ServerApplication.class);
    }

}
