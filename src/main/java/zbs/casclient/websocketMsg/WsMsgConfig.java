package zbs.casclient.websocketMsg;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP
 * @author zbs
 * @since 2022/6/10 16:35
 */
@Configuration
@EnableWebSocketMessageBroker
public class WsMsgConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/wsMsg")
               .setAllowedOrigins("*")
               .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //设置广播节点
        registry.enableSimpleBroker("/topic","/user");
        //服务器前缀：/app 
        registry.setApplicationDestinationPrefixes("/app");
        //指定用户发送（一对一）的前缀 /user/
        registry.setUserDestinationPrefix("/user/");
    }
}
