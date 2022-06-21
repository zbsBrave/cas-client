package zbs.casclient.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author zbs
 * @since 2022/6/10 15:05
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new WsHandler(), "/wsa")
                .addInterceptors(new WsInterceptor())
                //允许跨域
                .setAllowedOrigins("*");
        
        registry.addHandler(new WsHandler(),"/sjs").withSockJS();
    }
}
