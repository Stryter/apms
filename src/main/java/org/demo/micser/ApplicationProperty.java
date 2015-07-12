package org.demo.micser;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
public class ApplicationProperty {

    @Id
    @Column
    private String property;

    @Column
    private String value;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private PropertyFilter propertyFilter;

    @Transient
    private ApplicationPropertyRepository repository;

    public ApplicationProperty() {}

    public ApplicationProperty(ApplicationPropertyRepository repository) {
        this.repository = repository;
    }

    public ApplicationProperty persist() {
        return repository.saveAndFlush(this);
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public static class Builder {
        ApplicationProperty applicationProperty;

        PropertyFilterRepository propertyFilterRepository;

        public Builder(String property, ApplicationPropertyRepository repository, PropertyFilterRepository propertyFilterRepository) {
            this.propertyFilterRepository = propertyFilterRepository;
            applicationProperty = new ApplicationProperty(repository);
            applicationProperty.property = property;
        }

        public Builder(String property, String value, ApplicationPropertyRepository applicationPropertyRepository, PropertyFilterRepository propertyFilterRepository) {
            this(property, applicationPropertyRepository, propertyFilterRepository);
            value(value);
        }

        public Builder value(String value) {
            applicationProperty.value = value;
            return this;
        }

        public Builder filter(PropertyFilter filter) {
            applicationProperty.propertyFilter = filter;
            return this;
        }

        public ApplicationProperty build() {
            validateProperty();
            return applicationProperty.persist();
        }

        private void validateProperty() {
            PropertyFilter pf = propertyFilterRepository.findAll()
                    .stream()
                    .filter(
                            filter -> Pattern.compile(filter.getFilterRegex(), Pattern.CASE_INSENSITIVE)
                                    .matcher(applicationProperty.getProperty())
                                    .find()
                    ).findAny().orElseThrow(() -> new IllegalArgumentException("Failed to find a validating pattern for property."));
            filter(pf);
        }
    }
}
