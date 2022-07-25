package zbs.casclient.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * kafka简单使用，只需要配置 spring.kafka.bootstrap-servers 就可以
 * @author zbs
 * @since 2022/7/25 10:06
 */
@RestController
@RequestMapping("/kafka")
public class KafkaTestController {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;
    
    @GetMapping("/send")
    public void send(String msg){
        kafkaTemplate.send("topic-easy", msg);
    }
    
    @KafkaListener(groupId = "group-test1", topics = "topic-easy")
    public void listen(String msg){
        System.out.println("listen msg: " + msg);
    }
}
