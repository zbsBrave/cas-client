package zbs.casclient.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * kafka简单使用，只需要配置 spring.kafka.bootstrap-servers
 * @author zbs
 * @since 2022/7/25 10:06
 */
@RestController
@RequestMapping("/kafka")
public class KafkaTestController {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;
    
    @GetMapping("/send")
    public String send(String msg){
        kafkaTemplate.send("topic-easy", msg);
        kafkaTemplate.send("top001", msg);
        return msg;
    }
    
    @KafkaListener(id = "listener-test1", topics = "topic-easy")
    public void listen(String msg){
        System.out.println("listen msg: " + msg);
    }

    @KafkaListener(id = "listener-test2", topics = "topic-easy")
    public void listen2(String msg){
        System.out.println("listen msg: " + msg);
    }
}
