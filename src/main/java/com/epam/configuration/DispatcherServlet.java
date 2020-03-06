package com.epam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Configuration
public class DispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

//    //регистрируется стандартная цепочка фильтров Spring
//    @Override
//    protected Filter[] getServletFilters() {
//        DelegatingFilterProxy delegateFilterProxy = new DelegatingFilterProxy();
//        delegateFilterProxy.setTargetBeanName("springSecurityFilterChain");
//        return new Filter[]{delegateFilterProxy};
//    }
}
