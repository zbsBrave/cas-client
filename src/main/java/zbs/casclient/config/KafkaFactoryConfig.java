package zbs.casclient.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;

/**
 * 1，监听kafka生产者/消费者的生命周期
 * 2，创建topic
 * @author zbs
 * @since 2022/7/25 12:04
 */
@Configuration
public class KafkaFactoryConfig implements InitializingBean {
    @Resource
    DefaultKafkaProducerFactory<Object, Object> kafkaProducerFactory;

    @Resource
    DefaultKafkaConsumerFactory<Object, Object> kafkaConsumerFactory;
    
    

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("---------------------");

        System.out.println(kafkaProducerFactory.getListeners());
        //监听kafka生产者
        kafkaProducerFactory.addListener(new ProducerFactory.Listener<Object, Object>() {
            @Override
            public void producerAdded(@NonNull String id, @NonNull Producer<Object, Object> producer) {
                System.out.println("producerAdded ------------------------------");
                System.out.println(id + ":" + producer);
            }

            @Override
            public void producerRemoved(@NonNull String id, @NonNull Producer<Object, Object> producer) {
                System.out.println("producerRemoved ------------------------------");
                System.out.println(id + ":" + producer);
            }
        });
        
        //监听kafka消费者
        kafkaConsumerFactory.addListener(new ConsumerFactory.Listener<Object, Object>() {
            @Override
            public void consumerAdded(@NonNull String id, @NonNull Consumer<Object, Object> consumer) {
                System.out.println("consumerAdded ------------------------------");
                System.out.println(id + ":" + consumer);
            }

            @Override
            public void consumerRemoved(@NonNull String id, @NonNull Consumer<Object, Object> consumer) {
                System.out.println("consumerRemoved ------------------------------");
                System.out.println(id + ":" + consumer);
            }
        });
    }

    //创建topic
    @Bean
    public NewTopic topic4(){
        return TopicBuilder.name("topic4").build();
    }
    
    //Starting with version 2.7, you can declare multiple s in a single bean definition:NewTopicKafkaAdmin.NewTopics
    @Bean
    public KafkaAdmin.NewTopics topic123() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("topic1").partitions(1).replicas(1).build(),
                TopicBuilder.name("topic2").build(),
                TopicBuilder.name("topic3").build(),
                TopicBuilder.name("top003").partitions(2).build()
        );
    }

}
