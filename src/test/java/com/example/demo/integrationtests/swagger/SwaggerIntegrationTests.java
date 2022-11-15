package com.example.demo.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.configs.TestsConfigs;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTests extends AbstractIntegrationTests {

    @Test
    public void shouldDisplaySwaggerUiPage(){
        var content =
                given().basePath("/swagger-ui/index.html")
                .port(TestsConfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertTrue(content.contains("Swagger UI"));
    }
}
