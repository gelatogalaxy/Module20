package apiauto;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
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
    String myToken;

    /*
     * Token diambil saat runtime supaya test tidak gagal ketika token kedaluwarsa.
     * Kredensial bisa diisi lewat environment variable (API_EMAIL / API_PASSWORD),
     * kalau tidak ada maka test mendaftarkan user baru yang unik lalu login.
     */
    @BeforeClass
    public void setUpToken() {
        String email = System.getenv("API_EMAIL");
        String password = System.getenv("API_PASSWORD");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            email = "ci" + System.currentTimeMillis() + "@example.com";
            password = "Password123";

            JSONObject registerBody = new JSONObject();
            registerBody.put("nama", "CI User");
            registerBody.put("email", email);
            registerBody.put("password", password);

            RestAssured.given()
                    .baseUri(myBaseUrl)
                    .header("Content-Type", "application/json")
                    .body(registerBody.toString())
                    .post("/api/auth/register")
                    .then()
                    .assertThat().statusCode(201);
        }

        JSONObject loginBody = new JSONObject();
        loginBody.put("email", email);
        loginBody.put("password", password);

        myToken = RestAssured.given()
                .baseUri(myBaseUrl)
                .header("Content-Type", "application/json")
                .body(loginBody.toString())
                .post("/api/auth/login")
                .then()
                .assertThat().statusCode(200)
                .extract().path("data.token");
    }

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
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post("/api/auth/register")
                .then()
                .log().all()
                .assertThat().statusCode(409)
                .assertThat().body("error", Matchers.equalTo("Email sudah terdaftar"));
    }
}
