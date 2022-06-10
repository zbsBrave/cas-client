package zbs.casclient.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截
 * @author zbs
 * @since 2022/6/10 15:09
 */
@Slf4j
public class WsInterceptor implements HandshakeInterceptor {
    /**
     * 握手前，可以在这里做认证
     *
     * @param request    request
     * @param response   response
     * @param wsHandler  wsHandler
     * @param attributes 属性域
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手开始-------------------------------------------");
        log.info("uri:{}", request.getURI());
        log.info("head:{}", request.getHeaders());
        log.info("localAddress:{}", request.getLocalAddress());
        attributes.put("uid","123456");
        return true;
    }

    //握手后
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成-------------------------------------------");
    }
}
