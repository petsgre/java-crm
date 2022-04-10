import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Test {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//                .setConnectTimeout(timeout) // 未设置使用默认
//                .setDatabase(redisProperties.getDatabase()) // 未设置使用默认
//                .setPassword(redisProperties.getPassword()); // 未设置使用默认
        RedissonClient redissonClient = Redisson.create(config);
    }
}
