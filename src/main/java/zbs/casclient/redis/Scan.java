package zbs.casclient.redis;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zbs
 * @since 2022/9/8 18:17
 */
public class Scan {
    //todo
    public static void main(String[] args) {

        String key = "gameSdk:package";
        int count = 3;
        StringRedisTemplate template = SpringRedisUtil.getStringRedisTemplate();
        Cursor<String> scan = template.scan(ScanOptions.scanOptions().match(key + "*").count(count).build());
        System.out.println("--------------------------------------------------->");
        System.out.println(scan.getCursorId());
        System.out.println(scan.getCursorId() == 0);
        
        while (scan.getCursorId() != 0){
            System.out.println("id = " + scan.getCursorId());
            scan.stream().limit(count).forEach(System.out::println);
        }
        System.out.println(scan.getCursorId());
        System.out.println(scan.getCursorId() == 0);

        scan.close();
    }
    
    public static void setLogLevel(){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.valueOf("INFO"));
    }
}
