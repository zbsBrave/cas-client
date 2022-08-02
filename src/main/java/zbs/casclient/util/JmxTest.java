package zbs.casclient.util;

import java.lang.management.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zbs
 * @since 2022/7/27 18:46
 */
public class JmxTest {
    public static void main(String[] args) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        String osName = operatingSystemMXBean.getName();
        String osVersion = operatingSystemMXBean.getVersion();
        int processors = operatingSystemMXBean.getAvailableProcessors();
        System.out.printf("操作系统：%s，版本：%s，处理器：%d 个 %n", osName, osVersion, processors);

        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        String compilationMXBeanName = compilationMXBean.getName();
        System.out.println("编译系统：" + compilationMXBeanName);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long max = heapMemoryUsage.getMax();
        long used = heapMemoryUsage.getUsed();
        System.out.printf("使用内存：%dMB/%dMB %n", used / 1024 / 1024, max / 1024 / 1024);
        System.out.printf("使用内存：%dMB/%dMB %n", used >> 20, max >> 20);

        List<GarbageCollectorMXBean> gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        String gcNames = gcMXBeans.stream()
                .map(MemoryManagerMXBean::getName)
                .collect(Collectors.joining(","));
        System.out.println("垃圾收集器：" + gcNames);
    }
}
