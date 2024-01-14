package restservices;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import static restservices.DataGenerator.customFeeder;

public class FeederExamples extends Simulation {
    private static final HttpProtocolBuilder protocol = http.baseUrl("https://reqres.in/api/");
    private static final FeederBuilder.FileBased<String> csvFeeder = csv("TestData/Users.csv").random();

    // #14 read data from files
    ChainBuilder bodyDataFromFile =
            exec(http("T01-BodyDataFromFile").post("/users")
                    .header("content-type", "application/json")
                    .body(RawFileBody("TestData/Createuser.json"))
                    .check(status().is(201)));
    // #15 Read data from CSV - Dynamic values
    ChainBuilder readDataFromCSV = feed(csvFeeder).exec(http("T02-BodyDataFromFile").post("/users")
            .header("content-type", "application/json")
            .body(StringBody("{\n" +
                    "    \"name\": \"#{name}\",\n" +
                    "    \"job\": \"#{job}\"\n" +
                    "}"))
            .check(status().is(201)));
    // #16 Custom data feeder and random data generator from CSV - Dynamic values
    // Name and Id can be parameterized in json / CSV files
    ChainBuilder randomData = feed(customFeeder).exec(http("T03-CustomFeederFrom").post("/users")
                    .body(StringBody("{\n" +
                            "    \"name\": \"#{Name}\",\n" +
                            "    \"job\": \"#{Id}\"\n" +
                            "}")).asJson()
                    .check(status().is(201)).check(bodyString().saveAs("ResponseBody")))
            .exec(session -> {
                System.out.println(session.getString("ResponseBody"));
                return session;
            });

    ScenarioBuilder postJsonFile = scenario("BodyDataFromFile").exec(bodyDataFromFile);
    ScenarioBuilder csvDataRead = scenario("BodyDataFromFile").exec(readDataFromCSV);
    ScenarioBuilder randomDataGenerator = scenario("BodyDataFromFile").exec(randomData);

    {
        //setUp(postJsonFile.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(1); // #14
        //setUp(csvDataRead.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(1); // #15
        setUp(randomDataGenerator.injectOpen(atOnceUsers(1)).protocols(protocol)).maxDuration(1); // #16
    }
}
