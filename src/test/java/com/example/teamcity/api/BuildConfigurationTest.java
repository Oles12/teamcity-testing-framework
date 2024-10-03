package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest{

    @Test
    public void buildConfigurationTest(){

        var user = User.builder()
                .username("admin")
                .password("admin")
                .build();
        var token = RestAssured
                .given(Specifications.getSpec().authSpec(user))
                .get("http://admin:admin@192.168.1.98:8111/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        System.out.println(token);
    }

}
