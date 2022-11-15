package com.example.demo.config;

import com.example.demo.serialization.converter.YamlJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static MediaType MEDIA_TYPE_APPLICATION_YAML = MediaType.valueOf("application/x-yaml");
    @Value(value = "${cors.originPatterns}:default")
    private String corsOriginPatters = "";

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String [] allowedOrigins = corsOriginPatters.split(",");
        registry.addMapping("/**") //** significa todas as rotas da api
                //.allowedMethods("GET", "POST")
                .allowedMethods("GET", "POST", "PUT", "DELETE")   // * significa todos os metodos
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
       // https://www.baeldung.com/spring-mvc-content-negotiation-json-xml mais info acerca de content negotiation

        // Via Query Parameter http://localhost:8080/api/person/v1?mediaType=xml
        /*
        configurer.favorParameter(true)
                .parameterName("mediaType")
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                    .mediaType("json", MediaType.APPLICATION_JSON)
                    .mediaType("xml", MediaType.APPLICATION_XML);*/

        // Via Header Parameter
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YAML);
    }


}
