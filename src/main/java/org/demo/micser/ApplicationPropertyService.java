package org.demo.micser;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Josh on 7/6/2015.
 */
@RestController
public class ApplicationPropertyService {

    private ApplicationPropertyRepository applicationPropertyRepository;

    private PropertyFilterRepository propertyFilterRepository;

    public ApplicationPropertyService() {}

    public ApplicationPropertyService(ApplicationPropertyRepository applicationPropertyRepository, PropertyFilterRepository propertyFilterRepository) {
        this.applicationPropertyRepository = applicationPropertyRepository;
        this.propertyFilterRepository = propertyFilterRepository;
    }

    @RequestMapping(value = "/properties/get", method = GET)
    public String getProperty(@RequestParam("key") String propertyName) {
        return applicationPropertyRepository.findOne(propertyName).getValue();
    }

    @RequestMapping(value = "/properties/set", method = POST)
    public ResponseEntity<String> setProperty(@RequestBody ApplicationPropertyDTO request) {
        new ApplicationProperty.Builder(request.getKey(),
                request.getValue(),
                applicationPropertyRepository,
                propertyFilterRepository).build();
        return ResponseEntity.ok("All good.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(HttpServletRequest request, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception.getMessage());
        mav.addObject("url", request.getRequestURI());
        mav.setViewName("error");
        return new ResponseEntity<Object>(mav.getModel(), HttpStatus.BAD_REQUEST);
    }

    public static class ApplicationPropertyDTO {
        private String key;
        private String value;

        public ApplicationPropertyDTO() {
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
