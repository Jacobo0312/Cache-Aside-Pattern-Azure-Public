package org.Icesi.Jacobo0312;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Redis test
 */
public class App {

    private static boolean useSsl = true;
    private static String cacheHostname = System.getenv("REDISCACHEHOSTNAME");
    private static String cachekey = System.getenv("REDISCACHEKEY");

    // Connect to the Azure Cache for Redis over the TLS/SSL port using the key.
    private static final Jedis jedis = new Jedis(
            cacheHostname,
            6380,
            DefaultJedisClientConfig.builder().password(cachekey).ssl(useSsl).build());

    private static final Map<String, String> database = new HashMap<String, String>(
            Map.of(
                    "Colombia", "Bogota",
                    "Peru", "Lima",
                    "Chile", "Santiago",
                    "Argentina", "Buenos Aires",
                    "Brasil", "Brasilia",
                    "Venezuela", "Caracas",
                    "Uruguay", "Montevideo",
                    "Paraguay", "Asuncion",
                    "Bolivia", "La Paz",
                    "Ecuador", "Quito"
                    )
    );

    public static void main(String[] args) {


        showDatabase();

        showCache();

        getCapital("Colombia");




        jedis.close();
    }

    private static void showDatabase() {
        System.out.println("Database:");
        for (Map.Entry<String, String> entry : database.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    private static void showCache() {
        System.out.println("Cache:");
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println("Key: " + key + ", Value: " + jedis.get(key));
        }
    }

    private static void getCapital(String country) {

        System.out.println("Looking for capital of " + country);

        String capital = jedis.get(country);
        if (capital == null) {
            capital = database.get(country);
            jedis.set(country, capital);
            System.out.println("Missed cache, added to cache.");
        }else{
            System.out.println("Hit cache.");
        }
        System.out.println("Capital of " + country + " is " + capital);
    }

}