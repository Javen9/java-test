package hjf.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by javen on 2018/7/25.
 */
public class Client {

    private int BLOCK = 40960;

    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);

    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);

    private Selector selector;

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.open("127.0.0.1", 8888);
            client.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void open(String host, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress(host, port));
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

    public void handleKey(SelectionKey selectionKey) throws IOException {
        SocketChannel client = (SocketChannel) selectionKey.channel();

        if (selectionKey.isConnectable()) {
            //完成Socket连接
            System.out.println("======Socket完成连接======");
            if (client.isConnectionPending()) {
                client.finishConnect();
            }
            client.register(selector, SelectionKey.OP_WRITE);
        } else if (selectionKey.isReadable()) {
            //读取响应数据
            System.out.println("======接收服务端响应数据======");
            receiveBuffer.clear();
            int count = client.read(receiveBuffer);
            if (count > 0) {
                String receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println(receiveText);
                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            System.out.println("======向服务端发送数据======");
            client = (SocketChannel) selectionKey.channel();

            String sendText = "message from client";
            sendBuffer.clear();
            sendBuffer.put(sendText.getBytes());
            sendBuffer.flip();
            client.write(sendBuffer);

            client.register(selector, SelectionKey.OP_READ); //注册读操作
        }
    }
}
