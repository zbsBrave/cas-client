package zbs.casclient.client;

import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * @author zbs
 * @since 2022/6/17 18:26
 */
public class OkWsClientTest {
    
    public static void main(String[] args) {
        String uri = "ws://localhost:8080/wsa";
        
        for(int i = 0; i < 1000; i++){
            int finalI = i;
            new Thread( () -> {
                WebSocket ws = getClient(uri);
                ws.send("abc" + finalI);
            }).start();
        }
        System.out.println("end");
    }
    
    public static WebSocket getClient(String url){
        OkHttpClient mClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        Request request = new Request.Builder().get().url(url).build();

        WebSocket webSocket = mClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                System.out.println("open --------------------------------");
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                System.out.println("msg test --------------------------------");
                System.out.println(text);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                System.out.println("msg byte --------------------------------");
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("closing --------------------------------");
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("closed --------------------------------");
            }
        });
        
        return webSocket;
    }
}
