package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;

import au.com.dius.pact.core.model.annotations.Pact;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class) // consider the below class as pact class

public class Consumer {

    //Headers
    // provides multiple headers in Map and it is preferrable

    Map<String, String> reqHeader = new HashMap<>();
    //Resource path
    String resourcePath = "/api/users";

    @Pact(consumer = "UserConsumer", provider = "userProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {

        //creating headers
        reqHeader.put("Content-Type", "application/json");


        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        //create contract
        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .path(resourcePath)
                .headers(reqHeader)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "userProvider", port = "8282")
    public void consumerTest() {
        String baseURI = "http://localhost:8282";
//set request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Avi");
        reqBody.put("lastName", "Kundu");
        reqBody.put("email","Avijit@gmail.com");

        Response response = given().headers(reqHeader).when().body(reqBody)
                .post(baseURI + resourcePath);

        System.out.println(response.getBody().asPrettyString());
        //response.
        System.out.println(response.getStatusCode());


    }


}

