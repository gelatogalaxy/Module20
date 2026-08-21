package apiauto;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class APITest {
    String myBaseUrl = "https://api.rizqifauzan.com";
    String myToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3MWU0Y2NmZi00NWJhLTQ1ZDctOTE5Yy01MjczYWIzMGMwNDYiLCJlbWFpbCI6ImVyaWVucmEwMEBnbWFpbC5jb20iLCJuYW1hIjoiRXJpZW4gQW5kZXJzb24iLCJpYXQiOjE3ODcyMzYxOTksImV4cCI6MTc4Nzg0MDk5OX0.r0zDQAB3BAlbuKkEJwDiFSJPmLZxgMHhs3dUFdokl1U";

    @Test
    public void getCurrentUserTest(){
        ValidatableResponse response = RestAssured.given()
                .baseUri(myBaseUrl)
                .header("Authorization", "Bearer " + myToken)
                .get("/api/auth/me")
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }
    @Test
    public void failPostNewUserTest(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("nama", "John Waters");
        requestBody.put("email", "johnwaters@example.com");
        requestBody.put("password", "Password123");

        ValidatableResponse response = RestAssured.given()
                .baseUri(myBaseUrl)
                .header("Authorization", "Bearer " + myToken)
                .body(requestBody.toString())
                .post("/api/auth/register")
                .then()
                .log().all()
                .assertThat().statusCode(409)
                .assertThat().body("error", Matchers.equalTo("Email sudah terdaftar"));
    }
}

