package org.demo.micser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Josh on 7/6/2015.
 */
@Repository
public interface ApplicationPropertyRepository extends JpaRepository<ApplicationProperty, String> {

}
