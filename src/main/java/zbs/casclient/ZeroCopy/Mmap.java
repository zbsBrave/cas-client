package zbs.casclient.ZeroCopy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * mmap是一种内存映射文件的方法，即文件磁盘地址和进程虚拟地址的映射
 * 实现这种映射后，进程就可以采用指针的方式来操作这段内存，而系统会自动回写脏页到磁盘，从而实现对文件的操作
 *
 * @author zbs
 * @since 2022/9/2 9:49
 */
public class Mmap {
    public static void main(String[] args) throws IOException {
        //用 FileInputStream 会抛出 NonWritableChannelException 异常，使用RandomAccessFile就正常了
        FileChannel fileChannel = new RandomAccessFile(new File("D:\\data\\config-repo\\a.txt"), "rw").getChannel();
        //写
        byte[] bytes = "hello mmap".getBytes(StandardCharsets.UTF_8);
        MappedByteBuffer mmap = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);//size最大2gb
        mmap.put(bytes);
        //强制写入磁盘
        mmap.force();
        fileChannel.close();
    }
}
