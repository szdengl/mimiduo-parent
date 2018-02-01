package net.mimiduo.boot;

import net.mimiduo.boot.common.util.Applications;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2018-02-01 12:41
 **/

@SpringBootApplication
public class ManagerApplication extends SpringBootServletInitializer {


    public static void main(String[] args){
        SpringApplication springApplication=new SpringApplication(ManagerApplication.class);
        Applications.supports(springApplication);
        springApplication.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return super.configure(application);
    }
}
