package restservices;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetUsers {
    public static ChainBuilder getAllUsers = exec(http("T02-GetAllUsers").get("/users?page=2")
            .header("content-type", "application/json")
            .check(jsonPath("$.data[*].id").findAll().saveAs("users")))
            .foreach("#{users}", "user") // foreach loop through users list
            .on(exec(http("T02.1-GetSingleUser")
                    .get("https://reqres.in/api/users/#{user}")
                    .check(status().is(200))));
}


/*
public static ChainBuilder getAuthToken =
        doIf(session -> !session.getBoolean("ValidToken")).then(
                        exec(http("T01-GetAuthToken")
                                .post("/register")
                                .header("content-type", "application/json")
                                .body(StringBody("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol \"}"))
                                .check(status().is(200))
                                .check(jsonPath("$.token").saveAs("AUTH_TOKEN"))))
                .exec(session -> session.set("ValidToken", true));*/
