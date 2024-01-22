package restservices;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Authentication {
    public static ChainBuilder getAuthToken =
                    exec(http("T01-GetAuthToken")
                            .post("/register")
                            .header("content-type", "application/json")
                            .body(StringBody("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol \"}"))
                            .check(status().is(200))
                            .check(jsonPath("$.token").saveAs("AUTH_TOKEN")));
    }
