package zbs.casclient.config.customerListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import javax.annotation.Resource;

/**
 * 手动创建kafka listener
 * @author zbs
 * @since 2022/7/26 14:36
 */
@Configuration
public class CustomerListenerConfig {
    @Resource
    private ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory;

    @Resource
    private KafkaListenerEndpointRegistry registry;

    @Resource
    DefaultKafkaConsumerFactory<Object, Object> kafkaConsumerFactory;
    
    //1，手动创建 ConcurrentMessageListenerContainer
    @Bean 
    public ConcurrentMessageListenerContainer<?, ?> listener001(){
        System.out.println("listener001 ------------------------------------->");
        ConcurrentMessageListenerContainer<?, ?> listener001 = kafkaListenerContainerFactory.createContainer("top001");
        
        ContainerProperties cp = listener001.getContainerProperties();
        cp.setMessageListener((MessageListener<String, String>) data -> {
            System.out.println("listener001 consumer: " + data);
        });
        cp.setGroupId("listener001");

        System.out.println("listener001 id = " + listener001.getListenerId());
        
        return listener001;
    }
    
    //2，手动创建 KafkaMessageListenerContainer，clientId改变了
    @Bean
    public KafkaMessageListenerContainer<String, String> myKafkaListener(){
        System.out.println("myKafkaListener ------------------------------------->");
        
//        Map<String, Object> config = kafkaProperties.buildConsumerProperties();
//        System.out.println(config);
//        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(config);
        
        ContainerProperties cp = new ContainerProperties("top001");
        cp.setMessageListener((MessageListener<String, ConsumerRecord<?, ?>>) data -> {
            System.out.println("myKafkaListener consumer: " + data);
            System.out.println("myKafkaListener consumer: " + data.value());
        });
        cp.setGroupId("myKafkaListener");

        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(kafkaConsumerFactory, cp);

        System.out.println("myKafkaListener id = " + container.getListenerId());
        return container;
    }

    
}
