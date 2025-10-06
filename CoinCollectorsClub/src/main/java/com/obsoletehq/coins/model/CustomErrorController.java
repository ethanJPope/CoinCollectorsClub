// File: `src/main/java/com/obsoletehq/coins/controller/CustomErrorController.java`
package com.obsoletehq.coins.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errors = errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.defaults());
        Object status = errors.get("status");
        Object error = errors.get("error");
        Object message = errors.get("message");

        log.error("Error status: {}", status);
        log.error("Error error: {}", error);
        log.error("Error message: {}", message);

        return "error"; // Return your error view
    }
}
