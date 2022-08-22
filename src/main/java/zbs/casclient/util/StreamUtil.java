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
        concat();
    }
    
    //合并流
    public static void concat(){
        List<Integer> l1 = Arrays.asList(1,2,3);
        List<Integer> l2 = Arrays.asList(2,3,4,5);
        List<Integer> collect = Stream.concat(l1.stream(), l2.stream()).distinct().collect(Collectors.toList());
        System.out.println(collect);
    }
}
