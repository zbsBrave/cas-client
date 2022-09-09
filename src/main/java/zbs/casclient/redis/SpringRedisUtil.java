package zbs.casclient.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zbs
 * @since 2022/9/8 17:22
 */
public class SpringRedisUtil {
    private static final String host = "10.10.1.30";
    private static final int port = 6379;
    private static final int database = 0;

    public static void main(String[] args) {
        StringRedisTemplate template = new StringRedisTemplate(getLettuceConnectionFactory());
        System.out.println(template);
        template.opsForValue().set("a1","a2");
        System.out.println(template.opsForValue().get("a1"));


    }
    
    public static StringRedisTemplate getStringRedisTemplate(){
        return new StringRedisTemplate(getLettuceConnectionFactory());
    }
    
    private static LettuceConnectionFactory getLettuceConnectionFactory(){
        GenericObjectPoolConfig<?> pool = new GenericObjectPoolConfig<>();
        pool.setMaxTotal(10);
        pool.setMaxIdle(3);
        pool.setMinIdle(3);
        LettuceClientConfiguration poolConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(pool)
                .build();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setDatabase(database);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, poolConfig);
        factory.afterPropertiesSet();
        return factory;
    }
}
