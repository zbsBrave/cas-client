package zbs.casclient.websocket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zbs.casclient.MemoryUtil;

import java.io.IOException;
import java.lang.management.MemoryMXBean;
import java.util.Set;

/**
 * @author zbs
 * @since 2022/6/21 17:28
 */
@RestController
public class WsManagerController {
    @GetMapping("/onlineNum")
    public String onlineNum(){
        return "在线人数：" + WsManager.onlineNum();
    }
    
    @GetMapping("/onlineUser")
    public Set<String> onlineUser(){
        return WsManager.onlineUserId();
    }
    
    @RequestMapping("/sentToUser")
    public String sentToUser(String uid, String msg) throws IOException {
        WsManager.sentToUser(uid, msg);
        return "ok";
    }
    
    @RequestMapping("/memory")
    public MemoryMXBean memory(){
        return MemoryUtil.logMemory();
    }
}
