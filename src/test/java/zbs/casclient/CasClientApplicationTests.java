package zbs.casclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.Resource;

@SpringBootTest
class CasClientApplicationTests {
    @Resource
    private KafkaAdmin kafkaAdmin;
    
    @Resource
    private KafkaListenerEndpointRegistry registry;

    @Resource
    DefaultKafkaProducerFactory<?, ?> kafkaProducerFactory;

    @Resource
    DefaultKafkaConsumerFactory<Object, Object> kafkaConsumerFactory;

    @Test
    void contextLoads() {
        System.out.println(kafkaAdmin);
        //stop
        System.out.println(registry);
        // DefaultKafkaProducerFactory DefaultKafkaConsumerFactory Listener
        System.out.println(kafkaProducerFactory);
        System.out.println(kafkaConsumerFactory.isAutoCommit());
    }

}
