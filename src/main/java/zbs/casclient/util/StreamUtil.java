package zbs.casclient.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zbs
 * @since 2022/8/22 18:09
 */
public class StreamUtil {
    public static void main(String[] args) {
        limit();
//        concat();
    }
    
    //分页 limit
    public static void limit(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        int limit = 5, page = 1;
        for(int i=0; i<list.size(); i+=limit){
            System.out.println("page: " + page++);
            list.stream().skip(i).limit(limit).forEach(System.out::println);
        }
    }
    
    //合并流
    public static void concat(){
        List<Integer> l1 = Arrays.asList(1,2,3);
        List<Integer> l2 = Arrays.asList(2,3,4,5);
        List<Integer> collect = Stream.concat(l1.stream(), l2.stream()).distinct().collect(Collectors.toList());
        System.out.println(collect);
    }
}
