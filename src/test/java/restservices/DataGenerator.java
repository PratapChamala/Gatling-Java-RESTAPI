package restservices;

import jodd.util.RandomString;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DataGenerator {

    public static Iterator<Map<String, Object>> customFeeder = Stream.generate((Supplier<Map<String, Object>>) () ->
    {
        Random randomNumber = new Random();
        int Id =randomNumber.nextInt();
        String UUID_Id = UUID.randomUUID().toString();
        String Name = generateRandomString();

        HashMap<String , Object> hmap = new HashMap<>();
        hmap.put("Id",Id);
        hmap.put("Name",Name);
        hmap.put("UUID_Id",UUID_Id);
        return hmap;
    }).iterator();

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRS";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
