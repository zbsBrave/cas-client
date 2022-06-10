package zbs.casclient;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zbs
 * @since 2022/6/10 10:17
 */
public class TestWebsocket {
    public static void main(String[] args) throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer ws = ContainerProvider.getWebSocketContainer();
        //http://114.242.25.141:8083/
        //{appId=173525&devicesId=0bf5f30beaf14a89916e62bfcb8808aa&channel=test}
        String uri = "ws://114.242.25.141:8083";
        Session session = ws.connectToServer(TestWebsocket.class, new URI(uri));
        session.getBasicRemote().sendText("发送信息给服务端");
    }
}
