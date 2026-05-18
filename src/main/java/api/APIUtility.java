package api;

import config.EnvironmentManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.LogUtility;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * REST Assured wrapper for LogixERP API + UI hybrid automation.
 */
public final class APIUtility {

    private static final org.apache.logging.log4j.Logger LOG = LogUtility.getLogger(APIUtility.class);
    private static String authToken;

    private APIUtility() {
    }

    public static void setBaseUri() {
        RestAssured.baseURI = EnvironmentManager.getApiBaseUrl();
    }

    public static RequestSpecification baseRequest() {
        setBaseUri();
        RequestSpecification spec = given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        if (authToken != null) {
            spec.header("Authorization", "Bearer " + authToken);
        }
        return spec;
    }

    public static Response post(String endpoint, Object body) {
        Response response = baseRequest().body(body).when().post(endpoint).then().extract().response();
        logResponse("POST", endpoint, response);
        return response;
    }

    public static Response get(String endpoint) {
        Response response = baseRequest().when().get(endpoint).then().extract().response();
        logResponse("GET", endpoint, response);
        return response;
    }

    public static Response put(String endpoint, Object body) {
        Response response = baseRequest().body(body).when().put(endpoint).then().extract().response();
        logResponse("PUT", endpoint, response);
        return response;
    }

    public static Response delete(String endpoint) {
        Response response = baseRequest().when().delete(endpoint).then().extract().response();
        logResponse("DELETE", endpoint, response);
        return response;
    }

    public static Response login(String username, String password) {
        Map<String, String> payload = Map.of("username", username, "password", password);
        Response response = post("/auth/login", payload);
        if (response.statusCode() == 200) {
            authToken = response.jsonPath().getString("token");
        }
        return response;
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static void clearAuth() {
        authToken = null;
    }

    public static void assertStatus(Response response, int expected) {
        if (response.statusCode() != expected) {
            throw new AssertionError("Expected status " + expected + " but got "
                    + response.statusCode() + " body=" + response.getBody().asString());
        }
    }

    public static void assertResponseTime(Response response, long maxMs) {
        if (response.time() > maxMs) {
            throw new AssertionError("Response time " + response.time() + "ms exceeded " + maxMs + "ms");
        }
    }

    private static void logResponse(String method, String endpoint, Response response) {
        LOG.info("{} {} -> status={}, time={}ms", method, endpoint,
                response.statusCode(), response.time());
    }
}
