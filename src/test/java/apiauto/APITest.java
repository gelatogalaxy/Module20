package apiauto;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class APITest {

    /*
     * Catatan: pemakaian .filter(new AllureRestAssured()) di bawah sifatnya OPSIONAL.
     * Allure report tetap ter-generate tanpa baris ini (cukup konfigurasi di build.gradle).
     *
     * Tujuannya: melampirkan detail request (URL, header, body) dan response
     * ke masing-masing test di Allure report, sehingga saat ada test gagal
     * penyebabnya bisa langsung dilihat dari report tanpa perlu membaca console log.
     */
    String myBaseUrl = "https://api.rizqifauzan.com";
    String myToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3MWU0Y2NmZi00NWJhLTQ1ZDctOTE5Yy01MjczYWIzMGMwNDYiLCJlbWFpbCI6ImVyaWVucmEwMEBnbWFpbC5jb20iLCJuYW1hIjoiRXJpZW4gQW5kZXJzb24iLCJpYXQiOjE3ODcyMzYxOTksImV4cCI6MTc4Nzg0MDk5OX0.r0zDQAB3BAlbuKkEJwDiFSJPmLZxgMHhs3dUFdokl1U";

    @Test
    public void getCurrentUserTest(){
        ValidatableResponse response = RestAssured.given()
                .filter(new AllureRestAssured()) // opsional: attach request & response ke Allure report
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
                .filter(new AllureRestAssured()) // opsional: attach request & response ke Allure report
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

