package zbs.casclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import zbs.casclient.mapper.MySQLGeneratorMapper;

import javax.annotation.Resource;

/**
 * @author zbs
 * @since 2022/4/28 11:35
 */
@SpringBootTest
public class MySQLGeneratorMapperTest {
    @Resource
    private MySQLGeneratorMapper mapper;
    
    @Test
    public void t1(){
        mapper.queryList(null).forEach(System.out::println);
    }

    @Test
    public void t2(){
        mapper.queryTable("app").forEach((key, value) -> System.out.println(key + " : " + value));
    }
    
    @Test
    public void t3(){
        mapper.queryColumns("app").forEach(System.out::println);
    }
}
