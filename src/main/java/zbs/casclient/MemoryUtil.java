package zbs.casclient;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * @author zbs
 * @since 2022/6/21 18:36
 */
public class MemoryUtil {
    
    public static MemoryMXBean logMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println("堆内存使用：" + memoryMXBean.getHeapMemoryUsage());
        System.out.println("非堆内存使用：" + memoryMXBean.getNonHeapMemoryUsage());
        return memoryMXBean;
    } 
}
