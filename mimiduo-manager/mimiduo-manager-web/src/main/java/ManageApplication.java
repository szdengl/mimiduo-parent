
import net.mimiduo.boot.common.util.Applications;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
public class ManageApplication extends SpringBootServletInitializer {


    public static void main(String[] args){
        SpringApplication springApplication=new SpringApplication(ManageApplication.class);
        Applications.supports(springApplication);
        springApplication.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return super.configure(application);
    }

}
