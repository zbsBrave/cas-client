package zbs.casclient.config.oauth2.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zbs
 * @since 2022/4/22 15:51
 */
@RestController
public class CallBackController {
    @RequestMapping("/callback")
    public Map<String,Object> callback(@RequestParam Map<String,Object> map){
        System.out.println("oauth/authorize callback----------------------------------------------------------");
        System.out.println(map);
        return map;
    }
}
