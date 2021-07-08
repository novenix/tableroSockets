package co.edu.escuelaing.interactivebalckboardlife.configurator;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

public class MvcConfig {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
    }
}
