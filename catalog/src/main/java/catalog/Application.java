package catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication appl = new SpringApplication(Application.class);
        appl.addInitializers(new VcapProcessor());
        ApplicationContext ctx = appl.run(args);
        System.out.println("Catalog microservice is ready for business...");
    }
}
