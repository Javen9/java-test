package hjf.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务端
 * Created by javen on 2018/7/25.
 */
public class NIOServer {
    private int flag = 0;
    private int BLOCK = 40960;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);
    private Selector selector;

    public static void main(String[] args) {
        try {
            NIOServer NIOServer = new NIOServer();
            NIOServer.open(8888);
            NIOServer.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void listen() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> itr = selectionKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey selectionKey = itr.next();
                itr.remove();
                this.handleKey(selectionKey);
            }
        }
    }

    private void handleKey(SelectionKey selectionKey) throws IOException {
        SocketChannel client = null;
        if (selectionKey.isAcceptable()) {
            System.out.println("======新客户请求连接======");
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            System.out.println("======读取请求数据======");
            client = (SocketChannel) selectionKey.channel();
            receiveBuffer.clear();
            int count = client.read(receiveBuffer);
            if (count > 0) {
                String receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println(receiveText);
                client.register(selector, SelectionKey.OP_WRITE);//注册写操作
            }
        } else if (selectionKey.isWritable()) {
            System.out.println("======向客户端输出数据======");
            client = (SocketChannel) selectionKey.channel();

            String sendText = "message from server--" + flag++;
            sendBuffer.clear();
            sendBuffer.put(sendText.getBytes());
            sendBuffer.flip();
            client.write(sendBuffer);
            System.out.println(sendText);

            client.register(selector, SelectionKey.OP_READ); //注册读操作
        }
    }

}
