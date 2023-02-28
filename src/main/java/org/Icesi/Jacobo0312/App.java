package org.Icesi.Jacobo0312;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Redis test
 */
public class App {

    public static void main(String[] args) {

        boolean useSsl = true;
        String cacheHostname = System.getenv("REDISCACHEHOSTNAME");
        String cachekey = System.getenv("REDISCACHEKEY");

        // Connect to the Azure Cache for Redis over the TLS/SSL port using the key.
        Jedis jedis = new Jedis(cacheHostname, 6380, DefaultJedisClientConfig.builder().password(cachekey).ssl(useSsl).build());

        // Get all keys in cache
        Set<String> keys = jedis.keys("*");

        // Iterate over keys and print values
        for (String key : keys) {
            System.out.println("Key: " + key + ", Value: " + jedis.get(key));
        }


        jedis.close();
    }

}