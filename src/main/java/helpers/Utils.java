package helpers;

import com.github.javafaker.Faker;
import models.game.AdditionalData;
import models.game.DLC;
import models.game.Game;
import models.game.Requirements;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    public static final Random random = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static int getRandomInt() {
        return Math.abs(new Random().nextInt());
    }

    private static String getRandomFrom(String[] values){
        return values[ThreadLocalRandom.current().nextInt(values.length) % values.length];

    }

    public static String getRandomOS() {
        String[] os = new String[]{"Mac OS", "Windows 7", "Windows 8", "Playstation 3", "Playstation 4",
                "Playstation 5", "XBOX 360", "XBOX ONE", "WINDOWS 10"};
        return getRandomFrom(os);
    }

    public static String getRandomGameCompany() {
        String[] companies = new String[]{"Valve", "Microsoft", "DICE", "Ubisoft", "RockStar",
                "Activision", "Blizzard", "EA Games", "Sony"};
        return getRandomFrom(companies);
    }

    public static List<String> getRandomTags() {
        List<String> tags = Arrays.asList("race", "multiplayer", "shooter", "zombie", "open world",
                "rpg", "story", "company", "detective", "simulator", "vr", "kids");
        return tags.stream().skip(random.nextInt(tags.size())).collect(Collectors.toList());
    }

    public static String getRandomGenre() {
        List<String> tags = getRandomTags();
        return getRandomFrom(tags.toArray(new String[0]));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double getRandomPrice() {
        return round(100.0 + (10.000 - 100.0) * random.nextDouble(), 3);
    }

    public static Game generateRandomGame(boolean withDlc, long gameId){
        Game game = generateRandomGame(withDlc);
        game.setGameId(gameId);
        return game;
    }

    public static Game generateRandomGame(){
        return generateRandomGame(rnd.nextBoolean());
    }

    public static Game generateGameForDb(boolean withDlc){
        Faker faker = new Faker();
        Random random = new Random();
        Game game = new Game();
        game.setCompany(getRandomGameCompany());
        game.setDescription(faker.gameOfThrones().character() + " in main character in this game. Also some strange words " + faker.hacker().adjective());
        game.setTitle(faker.commerce().productName());
        game.setTags(getRandomTags());
        game.setGenre(getRandomGenre());
        game.setRequiredAge(random.nextBoolean());
        game.setRating(random.nextInt(10));
        boolean isFree = random.nextBoolean();
        if (isFree) {
            game.setIsFree(true);
            game.setPrice(0.0);
        } else {
            game.setIsFree(false);
            game.setPrice(getRandomPrice());
        }

        game.setPublish_date(LocalDateTime.now());

        game.setRequirements(new Requirements(getRandomOS(), random.nextInt(18), random.nextInt(90), "Nvidea RTX " + 1000 + random.nextInt(2050)));
        List<DLC> listOfDls = new ArrayList<>();
        if (withDlc) {
            for (int i = 1; i <= 1+random.nextInt(5); i++) {
                boolean isDlcFree = random.nextBoolean();
                if (isDlcFree) {
                    listOfDls.add(new DLC(true, "Random DLC Name " + faker.funnyName().name(), random.nextInt(10), "Random DLC Description " + randomString(20), 0.0,
                            new AdditionalData("Recommended another game with dlc: " + faker.beer().name(), true)));
                } else {
                    listOfDls.add(new DLC(false, "Random DLC Name " + faker.animal().name(), random.nextInt(10), "Random DLC Description " + randomString(20), getRandomPrice(),
                            new AdditionalData("Recommended another game with dlc: " + faker.beer().name(), false)));
                }
            }
        }
        game.setDlcs(listOfDls);

        return game;
    }

    public static Game generateRandomGame(boolean withDlc) {
        Faker faker = new Faker();
        Random random = new Random();
        Game game = new Game();
        game.setCompany(getRandomGameCompany());
       // game.setGameId(getRandomInt());
        game.setDescription(faker.gameOfThrones().character() + " in main character in this game. Also some strange words " + faker.hacker().adjective());
        game.setTitle(faker.commerce().productName());
        game.setTags(getRandomTags());
        game.setGenre(getRandomGenre());
        game.setRequiredAge(random.nextBoolean());
        game.setRating(random.nextInt(10));
        boolean isFree = random.nextBoolean();
        if (isFree) {
            game.setIsFree(true);
            game.setPrice(0.0);
        } else {
            game.setIsFree(false);
            game.setPrice(getRandomPrice());
        }

        game.setPublish_date(LocalDateTime.now());

        game.setRequirements(new Requirements(getRandomOS(), random.nextInt(18), random.nextInt(90), "Nvidea RTX " + 1000 + random.nextInt(2050)));
        List<DLC> listOfDls = new ArrayList<>();
        if (withDlc) {
            for (int i = 1; i <= 1+random.nextInt(5); i++) {
                boolean isDlcFree = random.nextBoolean();
                if (isDlcFree) {
                    listOfDls.add(new DLC(true, "Random DLC Name " + faker.funnyName().name(), random.nextInt(10), "Random DLC Description " + randomString(20), 0.0,
                            new AdditionalData("Recommended another game with dlc: " + faker.beer().name(), true)));
                } else {
                    listOfDls.add(new DLC(false, "Random DLC Name " + faker.animal().name(), random.nextInt(10), "Random DLC Description " + randomString(20), getRandomPrice(),
                                    new AdditionalData("Recommended another game with dlc: " + faker.beer().name(), false)));
                }
            }
        }
        game.setDlcs(listOfDls);

        return game;
    }
}
