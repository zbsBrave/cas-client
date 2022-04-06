package zbs.casclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/")
    public String index(){
        return "index";
    }
    
    @GetMapping("/t1")
    public String t1(){
        return "t1";
    }

    @GetMapping("/test/t3")
    public String t3(){
        return "/test/t3";
    }

    @GetMapping("/test/t4")
    public String t4(){
        return "/test/t4";
    }
}
