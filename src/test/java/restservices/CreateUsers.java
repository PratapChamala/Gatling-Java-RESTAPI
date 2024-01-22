package restservices;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CreateUsers {
    public static ChainBuilder createUser = exec(http("T03-CreateUser")
            .post("/api/users")
            .body(StringBody("{\n" +
                    "    \"name\": \"morpheus\",\n" +
                    "    \"job\": \"leader\"\n" +
                    "}")).asJson()
            .header("content-type", "application/json")
            .check(status().is(201)));
}
