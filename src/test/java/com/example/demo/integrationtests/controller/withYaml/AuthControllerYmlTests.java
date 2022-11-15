package com.example.demo.integrationtests.controller.withYaml;

import com.example.demo.configs.TestsConfigs;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTests;
import com.example.demo.integrationtests.vo.AccountCredentialsVO;
import com.example.demo.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYmlTests extends AbstractIntegrationTests {

    private static TokenVO tokenVO;
    private static YMLMapper ymlMapper;

    @BeforeAll
    public static void setup(){
        ymlMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

       RequestSpecification specification = new RequestSpecBuilder()
               .addFilter(new RequestLoggingFilter(LogDetail.ALL))
               .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
               .build(); // criar logs

        tokenVO = given()
                .spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .body(user, ymlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class, ymlMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());

    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
        var newTokenVO = given()
                .port(TestsConfigs.SERVER_PORT)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/refresh")
                .port(TestsConfigs.SERVER_PORT)
                .pathParam("username", tokenVO.getUsername())
                .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class, ymlMapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());

    }
}
