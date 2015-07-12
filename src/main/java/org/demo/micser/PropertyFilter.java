package org.demo.micser;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
public class PropertyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column
    private String environment;

    @Column
    private String platform;

    @Column
    private String filterRegex;

    @Transient
    private PropertyFilterRepository repository;

    public PropertyFilter() {}

    public PropertyFilter(PropertyFilterRepository repository) {
        this.repository = repository;
    }

    public PropertyFilter persist() {
        return repository.saveAndFlush(this);
    }

    public Integer getId() {
        return id;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getPlatform() {
        return platform;
    }

    public String getFilterRegex() {
        return String.format("%s.%s.%s", platform, environment, filterRegex);
    }

    public static class Builder {
        private PropertyFilter propertyFilter;

        public Builder(String environment, String platform, String filterRegex, PropertyFilterRepository repository) {
            propertyFilter = new PropertyFilter(repository);
            propertyFilter.environment = environment;
            propertyFilter.platform = platform;
            propertyFilter.filterRegex = filterRegex;
        }

        public PropertyFilter build() {
            validateRegex();
            return propertyFilter.persist();
        }

        private void validateRegex() {
            Pattern.compile(propertyFilter.getFilterRegex());
        }
    }
}
