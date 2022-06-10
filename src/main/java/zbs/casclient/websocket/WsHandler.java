package zbs.casclient.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author zbs
 * @since 2022/6/10 15:22
 */
@Slf4j
public class WsHandler extends TextWebSocketHandler {

    /**
     * socket建立成功
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("连接成功,{}", session.getId());
        System.out.println("握手前放入attribute中的uid：" + session.getAttributes().get("uid"));
    }

    /**
     * 接受所有消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) message);
        }
        else if (message instanceof BinaryMessage) {
            handleBinaryMessage(session, (BinaryMessage) message);
        }
        else if (message instanceof PongMessage) {
            handlePongMessage(session, (PongMessage) message);
        }
        else if (message instanceof PingMessage) {
            System.out.println("ping, id=" + session.getId());
        }
        else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }

    /**
     * 接受消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("textMsg,{},{}", session.getId(), message);
        session.sendMessage(new TextMessage("服务器收到：" + message.getPayload()));
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("pongMsg,{},{}", session.getId(), message);
    }

    /**
     * 断开连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("断开连接，{},status={}", session.getId(), status);
    }

    /**
     * 连接异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("连接异常，" + session.getId(), exception);
    }
}
