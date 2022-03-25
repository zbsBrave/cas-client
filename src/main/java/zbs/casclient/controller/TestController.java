package zbs.casclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangbaisen
 * @date 2022/3/25 18:50
 */
@RestController
public class TestController {
    
    @GetMapping("/t1")
    public String t1(){
        return "t1";
    }
}
