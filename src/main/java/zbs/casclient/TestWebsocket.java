package zbs.casclient;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zbs
 * @since 2022/6/10 10:17
 */
@ClientEndpoint
public class TestWebsocket {
    public Session session;
    public TestWebsocket(String uri) throws URISyntaxException, DeploymentException, IOException {
        session = ContainerProvider.getWebSocketContainer().connectToServer(this, new URI(uri));
    }
    
    @OnMessage
    public void onMessage(String msg){
        System.out.println("onMessage: " + msg);
    }
    
    public void sendMsg(String msg){
        session.getAsyncRemote().sendText(msg);
    }

    public static void main(String[] args) throws URISyntaxException, DeploymentException, IOException {
//        WebSocketContainer ws = ContainerProvider.getWebSocketContainer();
        //http://114.242.25.141:8083/
        //{appId=173525&devicesId=0bf5f30beaf14a89916e62bfcb8808aa&channel=test}
        String uri = "ws://114.242.25.141:8083";
        TestWebsocket c = new TestWebsocket(uri);
    }
}
