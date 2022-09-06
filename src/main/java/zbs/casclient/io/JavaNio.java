package zbs.casclient.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zbs
 * @since 2022/9/6 9:53
 */
public class JavaNio {
    public static void main(String[] args) throws IOException {
        //1，获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);//通道设置为非阻塞
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",777));//绑定端口
        
        //2，获取selector
        Selector selector = Selector.open();
        //将通道注册到selector，并指定事件为accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        //轮询select
        while (selector.select() > 0){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("select size = " + selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    System.out.println("acceptable --->");
                    SocketChannel accept = ((ServerSocketChannel) key.channel()).accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    System.out.println("readable --->");
                    SocketChannel socket = (SocketChannel) key.channel();

                    ByteBuffer bf = ByteBuffer.allocate(1024);
                    int len;
                    while ( (len = socket.read(bf)) > 0){
                        bf.flip();
                        System.out.println("receive :" + new String(bf.array(), 0, len));
                        bf.clear();
                    }
                }else {
                    System.out.println("err --->");
                }
                iterator.remove();
            }
        }
    }
}
