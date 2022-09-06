package zbs.casclient.io.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zbs
 * @since 2022/9/6 11:16
 */
public class HttpServer {
    private final HttpServlet servlet;
    private final ExecutorService pool;
    private final Selector selector;

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(777);
        server.start();
    }

    public HttpServer(int port) throws IOException {
        this.servlet = new HttpServlet();
        this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));

        this.selector = Selector.open();
        server.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (selector.select() > 0) {
            System.out.println("---------------------------------------------------------------------");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectKeys size = " + selectionKeys.size());

            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    System.out.println("key.isAcceptable -------");
                    doAccept(key);

                } else if (key.isReadable()) {
                    System.out.println("key.isReadable -------");
                    doRead(key);

                } else if (key.isWritable()) {
                    System.out.println("key.isWritable -------");
                    doWrite(key);

                } else if (key.isConnectable()) {
                    System.out.println("key.isConnectable -------");

                } else if (key.isValid()) {
                    System.out.println("key.isValid -------");
                }
                it.remove();
            }
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel accept = serverSocketChannel.accept();
        accept.configureBlocking(false);
        accept.register(key.selector(), SelectionKey.OP_READ);

        System.out.println(key.selector() == selector);
    }
    
    private void doRead(SelectionKey key) throws IOException {
        SocketChannel socket = (SocketChannel) key.channel();
        //1，读取数据
        ByteArrayOutputStream out = readData(socket);
        if(out.size() == 0){
            System.out.println("浏览器空数据，关闭连接：" + socket.getRemoteAddress());
            socket.close();
            return;
        }
        
        //2，解码
        HttpRequest request = HttpUtil.decode(out.toByteArray());
        
        //3，业务处理
        socket.register(key.selector(), SelectionKey.OP_WRITE);
        HttpResponse response = new HttpResponse();
        servlet.doGet(request, response);
        key.attach(response);
        
        //todo 多线程业务
//        pool.submit(() -> {
//            HttpResponse response = new HttpResponse();
//            servlet.doGet(request, response);
//            //响应
//            key.interestOps(SelectionKey.OP_WRITE);
//            key.attach(response);
//            //异步唤醒
//            key.selector().wakeup();
//        });
    }
    private ByteArrayOutputStream readData(SocketChannel socket) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteBuffer bf = ByteBuffer.allocate(1024);
        int len;
        while ( (len = socket.read(bf)) > 0){
            bf.flip();
            out.write(bf.array(),0,len);
            bf.clear();
        }
        out.close();
        return out;
    }
    
    private void doWrite(SelectionKey key) throws IOException {
        SocketChannel socket = (SocketChannel) key.channel();
        HttpResponse response = (HttpResponse) key.attachment();
        
        //编码
        byte[] bytes = HttpUtil.encode(response);
        socket.write(ByteBuffer.wrap(bytes));
        
        key.interestOps(SelectionKey.OP_READ);
        key.attach(null);
    }
}
