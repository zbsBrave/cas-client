package zbs.casclient.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zbs
 * @since 2022/6/21 17:07
 */
public class WsManager {
    private static final Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();
    
    public static void add(WebSocketSession session){
        if(session == null || !session.isOpen()){
            return;
        }
        SESSION_MAP.put(session.getId(), session);
    }
    
    public static void remove(String id){
        SESSION_MAP.remove(id);
    }
    
    public static void sentToUser(String id, String msg) throws IOException {
        WebSocketSession session = SESSION_MAP.get(id);
        if(session == null){
            throw new RuntimeException("无此用户");
        }
        session.sendMessage(new TextMessage(msg));
    }
    
    public static void sentToAll(String msg) throws IOException {
        for(Map.Entry<String, WebSocketSession> entry : SESSION_MAP.entrySet()){
            entry.getValue().sendMessage(new TextMessage(msg));
        }
    }
    
    public static int onlineNum(){
        return SESSION_MAP.size();
    }
    
    public static Set<String> onlineUserId(){
        return SESSION_MAP.keySet();
    }
    
}
