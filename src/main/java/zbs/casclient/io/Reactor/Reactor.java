package zbs.casclient.io.Reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * java实现的reactor模型
 * 参考：Scalable IO in Java
 * https://www.cnblogs.com/fxjwind/p/3363329.html
 *
 * @author zbs
 * @since 2022/9/6 17:23
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;

    public static void main(String[] args) throws IOException {
        new Reactor(777).run();
        ;
    }

    public Reactor(int port) throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);

        selector = Selector.open();
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(getAcceptor());
    }

    private Runnable getAcceptor() {
        return () -> {
            try {
                SocketChannel accept = serverSocket.accept();
                if (accept != null) {
                    new Handler(selector, accept);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    dispatcher(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispatcher(SelectionKey sk) {
        System.out.println("dispatcher -----------------------------------------------------");
        Runnable acceptor = (Runnable) sk.attachment();
        if (acceptor != null) {
            System.out.println(acceptor.getClass());
            //这里很巧妙，没有判断是 accept、read、write，因为acceptor和handler都实现了runnable
            acceptor.run();
        }
    }

}
