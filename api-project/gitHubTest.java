package APIProject;

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class gitHubTest {

    RequestSpecification resSpec;
    Response response;
    String sshKey;
    int id;

    @BeforeClass
    public void setUp() {
        resSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://api.github.com")
                .addHeader("Authorization", "token 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")
                .build();


    }

    @Test(priority = 0 )
    public void postMethod() {

        String reqBody ="{\n" +
                "    \"title\": \"TestAPIKey\",\n" +
                "    \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCashkqet1gjzzFFGbgEC1xlCNfwHAL2CrkPNFjGt0X9mKwjvpZqouURCAY8tkD1cuCR/bR1XkiqubpeBDnr4cPp/egCd677/wovRteC8PU3zCPH2frZJz6hakVaN6gaWXnzF7BGnXTCv4F4f0OpHUf2GOxO+o8Mhw/qGslnKH0ZJKJt8KbRcKcExia0YS5H2tcTLsKu7Fya3BwyuBJl9QjbX+r5Xy9VPaleVCa8YOpD9euLQQWWp1qh9CHWy2tBdj7OkAMKKmt5D610bwgYzbpH+3gxQPLLlpcJUoS6uR2VEizrJdOZjfvMyccc/hOfUtKMv9/VB1NYjIDdfsRXPQJ\"}";
        response = given().spec(resSpec).body(reqBody).when().post("/user/keys");

        System.out.println("response from post  "+response.getBody().asPrettyString());
        id=response.then().extract().path("id");
        System.out.println("id"+id);
        System.out.println(response.statusCode());
        int postcode=response.getStatusCode();
        Assert.assertEquals(postcode,201);

    }
    @Test(priority = 1)
    public void getMethod()
    {
        System.out.println("id"+id);
        response=given().spec(resSpec)
                .pathParam("keyId",id)
                .when().get("/user/keys/{keyId}");
        System.out.println("GET response "+response.getBody().asPrettyString());
        int getCode = response.getStatusCode();
        Assert.assertEquals(getCode,200);
    }

    @Test(priority = 2)
    public void deleteMethod()
    {
        response=given().spec(resSpec).pathParam("keyId",id).when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());
        int deleteCode = response.getStatusCode();
        Assert.assertEquals(deleteCode,204);
    }
}
