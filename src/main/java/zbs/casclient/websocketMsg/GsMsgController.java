package zbs.casclient.websocketMsg;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author zbs
 * @since 2022/6/10 17:01
 */
@Controller
public class GsMsgController {

    /**
     * 1，@MessageMapping来暴露节点路径，注意这里虽然写的是 msg ，但是客户端调用的真正地址是 /app/msg
     * 因为在WsMsgConfig里配置了registry.setApplicationDestinationPrefixes("/app")
     * 2，@SendTo会把返回值的内容发送给订阅了 /topic/msg 的客户端.与之类似的还有@SendToUser,只不过他是发送给用户端一对一通信的
     *
     * @param msg 消息内容，可以是任意对象
     */
    @MessageMapping("/msg")
    @SendTo("/topic/msg")
    public String msg(String msg) {
        System.out.println("get msg: " + msg);
        return "服务端收到：" + msg;
    }
}
