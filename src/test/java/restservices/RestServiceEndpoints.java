package restservices;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.http.*;


import static io.gatling.javaapi.http.HttpDsl.*;

public class RestServiceEndpoints extends Simulation {
    private final HttpProtocolBuilder protocol = http.baseUrl("https://reqres.in/api/");
    ScenarioBuilder authToken = scenario("GetAuthToken")
            .exec(Authentication.getAuthToken); // #1 Get auth_token request
    ScenarioBuilder allUsers = scenario("getAllUsers")
            .exec(GetUsers.getAllUsers); // // #4 and # 5
    ScenarioBuilder createusers = scenario("CreateUser")
            .repeat(3).on(exec(CreateUsers.createUser).pause(1)); // #6 #7 repeat
    ScenarioBuilder allendpoints = scenario("AllEndpointsSequential")
            .exec(Authentication.getAuthToken.pace(1),
                    CreateUsers.createUser.pace(1),
                    GetUsers.getAllUsers.pace(1)); // #8 all endpoints at once
    ScenarioBuilder scn = scenario("Random choice with waitage")
            .during(10).on(
                    randomSwitch().on(
                            new Choice.WithWeight(20d, exec(CreateUsers.createUser)),
                            new Choice.WithWeight(40d, exec(GetUsers.getAllUsers))
                    )
            );
    /*ScenarioBuilder scn = scenario("CreateUser")
           // .exec(Authentication.getAuthToken)
            .forever().on(pace(1).exec(CreateUsers.createUser)); */// #9 Execute token endpoint once and create user forever

    {
        setUp(allendpoints.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(10);

       /*setUp(authToken.injectOpen(atOnceUsers(1)).protocols(protocol),
                allUsers.injectOpen(atOnceUsers(1)).protocols(protocol),
                createusers.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(15);*/

        /* setUp(authToken.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(1); // #1 Get auth_token request
         setUp(allUsers.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(1); // #2 Get all users request
         setUp(createusers.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(1); // #6 #7 repeat
         setUp(allendpoints.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(10); // #8 all endpoints at once
         setUp(scn.injectOpen(atOnceUsers(1))).protocols(protocol).maxDuration(10); // #9
         setUp(authToken.injectOpen(atOnceUsers(1)),allUsers.injectOpen(atOnceUsers(2)),
            createusers.injectOpen(atOnceUsers(3)).protocols(protocol)).maxDuration(15);*/ // #12 assign threads for each endpoint
    }
}
