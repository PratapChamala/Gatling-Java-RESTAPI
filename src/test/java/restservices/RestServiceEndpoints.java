package restservices;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.http.HttpDsl.*;

public class RestServiceEndpoints extends Simulation {
    private final HttpProtocolBuilder protocol = http.baseUrl("https://reqres.in/api/");

    // #1 #2 and #3 Get AuthToken
    ChainBuilder getAuthToken = exec(http("T01-GetAuthToken")
            .post("/register")
            .header("content-type", "application/json")
            .body(StringBody("{\n" +
                    "    \"email\": \"eve.holt@reqres.in\",\n" +
                    "    \"password\": \"pistol\"\n" +
                    "}"))
            // assert for 200 status
            .check(status().is(200))
            // capture token using jsonpath and saving into a variable
            .check(jsonPath("$.token")
                    .saveAs("AUTH_TOKEN"))).exitHereIfFailed() // #11 exit here if fail
            // #13 Writer response to console and session data
            .exec(session -> {
                System.out.println("Response Body:");
                System.out.println(session.getString("AUTH_TOKEN"));
                return session;
            });
    // #4 and # 5 pause(1) , pause(100,200)
    ChainBuilder getAllUsers = exec(http("T02-GetAllUsers").get("/users?page=2")
            .header("content-type", "application/json")
            .check(jsonPath("$.data[*].id").findAll().saveAs("users")))
            .foreach("#{users}", "user") // foreach loop through users list
            .on(exec(http("T02.1-GetSingleUser")
                    .get("https://reqres.in/api/users/#{user}")
                    .check(status().is(200))).pause(1));
    ChainBuilder createUser = exec(http("T03-CreateUser")
            .post("/api/users")
            .body(StringBody("{\n" +
                    "    \"name\": \"morpheus\",\n" +
                    "    \"job\": \"leader\"\n" +
                    "}")).asJson()
            .header("content-type", "application/json")
            .check(status().is(201)));

    ScenarioBuilder authToken = scenario("GetAuthToken").exec(getAuthToken); // #1 Get auth_token request
    ScenarioBuilder allUsers = scenario("getAllUsers").exec(getAllUsers); // // #4 and # 5
    ScenarioBuilder createusers = scenario("CreateUser")
            .repeat(3).on(exec(createUser).pause(1)); // #6 #7 repeat
    ScenarioBuilder allendpoints = scenario("AllEndpointsSequential")
            .exec(getAuthToken.pace(1), createUser.pace(1), getAllUsers.pace(1)); // #8 all endpoints at once
    ScenarioBuilder scn = scenario("CreateUser")
            .exec(getAuthToken)
            .forever().on(pace(1).exec(createUser)); // #9 Execute token endpoint once and create user forever
    {

         setUp(authToken.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(1); // #1 Get auth_token request
        // setUp(allUsers.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(1); // #2 Get all users request
        // setUp(createusers.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(1); // #6 #7 repeat
        // setUp(allendpoints.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(10); // #8 all endpoints at once
        // setUp(scn.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(10); // #9
        // setUp(authToken.injectOpen(atOnceUsers(1)),allUsers.injectOpen(atOnceUsers(2)),
            //createusers.injectOpen(atOnceUsers(3)).protocols(protocol)).maxDuration(15); // #12 assign threads for each endpoint

    }

}
