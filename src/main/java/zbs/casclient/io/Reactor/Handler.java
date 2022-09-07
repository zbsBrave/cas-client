package zbs.casclient.io.Reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author zbs
 * @since 2022/9/6 17:31
 */
public class Handler implements Runnable{
    final SocketChannel socket;
    final SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);

    static final int READING = 0, SENDING = 1;
    int state = READING;
    
    public Handler(Selector sel, SocketChannel sc) throws IOException {
        socket = sc;
        socket.configureBlocking(false);
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }
    @Override
    public void run() {
        //根据state判断读/写
        try {
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (IOException ex) { /* ... */ }
    }

    void read() throws IOException {
        input.clear();
        socket.read(input);
        System.out.println("read --->");
        System.out.println(new String(input.array()));
        if (inputIsComplete()) {
            process();
            state = SENDING;
            // Normally also do first write now
            sk.interestOps(SelectionKey.OP_WRITE); //第三步,接收write事件
        }
    }
    void send() throws IOException {
        output=input;
        output.flip();
        socket.write(output);
        if (outputIsComplete()) {
            //sk.cancel(); //write完就结束了, 关闭select key
            state = READING;
            // Normally also do first write now
            sk.interestOps(SelectionKey.OP_READ); //第三步,接收write事件
        }
    }

    boolean inputIsComplete() { /* ... */ return true;}
    boolean outputIsComplete() { /* ... */ return true;}
    void process() { /* ... */ }
}
