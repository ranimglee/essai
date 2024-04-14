package esprit.tn.projetspring;

import esprit.tn.projetspring.Configuration.RsakeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsakeysConfig.class)
public class ProjetSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetSpringApplication.class, args);
    }

}
