package org.demo.micser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Microservice1Application {

    @Autowired
    private ApplicationPropertyRepository applicationPropertyRepository;

    @Autowired
    private PropertyFilterRepository propertyFilterRepository;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Microservice1Application.class, args);

        PropertyFilterRepository repository = context.getBean(PropertyFilterRepository.class);
        createFilters(repository);
    }

    private static void createFilters(PropertyFilterRepository repository) {
        PropertyFilter filter1 = new PropertyFilter.Builder("qa", "ues", "\\w*.+enabled", repository).build();
        PropertyFilter filter2 = new PropertyFilter.Builder("prod", "ues", "\\w*.+enabled", repository).build();
        PropertyFilter filter3 = new PropertyFilter.Builder("prod", "uep", "\\w*.+cron", repository).build();
        PropertyFilter invalidRegex = new PropertyFilter.Builder("prod", "ues", "[a-zA-Z0-9,\\\\.\\']", repository).build();
    }


    @Bean(name = "applicationPropertyService")
    public ApplicationPropertyService applicationPropertyService() {
        return new ApplicationPropertyService(applicationPropertyRepository, propertyFilterRepository);
    }
}
