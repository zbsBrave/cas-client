package zbs.casclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.Resource;

@SpringBootTest
class CasClientApplicationTests {
    @Resource
    private KafkaAdmin kafkaAdmin;

    @Test
    void contextLoads() {
        System.out.println(kafkaAdmin);
    }

}
