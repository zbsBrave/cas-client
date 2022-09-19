package zbs.casclient.redis;

import org.springframework.data.redis.core.ListOperations;

import java.io.*;
import java.util.Optional;

/**
 * @author zbs
 * @since 2022/9/16 15:00
 */
public class ListRedis {
    public static void main(String[] args) throws IOException {
        String s = "173525_account123456_23422322";
        File f = new File("D:\\aaaaa\\aaa.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(f));
        for(int i=0; i<50000; i++){
            out.write(s);
            out.newLine();
        }
        out.close();
    }

    public static void main1(String[] args) {

        String key = "list1";
        ListOperations<String, String> template = SpringRedisUtil.getStringRedisTemplate().opsForList();
        System.out.println(template.range(key,0,-1));
        System.out.println("-------------------------");

        Long len = template.size(key);
        len = (len == null) ? 0 : len;
        
        //循环获取list所有数据
        int count = 3;
        for(int i=0; i<len; i+=count){
            int end = i + (count - 1);
            System.out.println(template.range(key, i, end));
        }
    }

}
