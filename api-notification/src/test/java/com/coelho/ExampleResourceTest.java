package com.coelho;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    @BeforeClass
    private void startContainer(){
        Network network = Network.newNetwork();
        KafkaContainer kafkaContainer = new KafkaContainer();

        if(!kafkaContainer.isRunning()){
            kafkaContainer.withNetwork(network)
                    .start();
        }
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}