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
 * 使用NIOSocket模拟Http请求
 * Created by javen on 2018/7/25.
 */
public class HttpClient {

    private int BLOCK = 40960;

    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);

    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);

    private Selector selector;

    public static void main(String[] args) {
        try {
            HttpClient client = new HttpClient();
            client.open("127.0.0.1", 21300);
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
            //完成Socket连接/TCP握手
            System.out.println("======Socket完成连接======");
            if (client.isConnectionPending()) {
                client.finishConnect();
            }
            client.register(selector, SelectionKey.OP_WRITE);
        } else if (selectionKey.isReadable()) {
            //读取响应数据
            System.out.println("======服务端响应数据======");
            receiveBuffer.clear();
            int count = client.read(receiveBuffer);
            if (count > 0) {
                String receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println(receiveText);
//                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            //发送请求数据
            System.out.println("======客户端完成HTTP请求======");
            client = (SocketChannel) selectionKey.channel();
            sendBuffer.clear();
            StringBuilder sb = new StringBuilder();
            sb.append("GET /info.html HTTP/1.1\n");
            sb.append("Host: 127.0.0.1:21300\n");
            sb.append("Connection: keep-alive\n");
            sb.append("Cache-Control: max-age=0\n");
            sb.append("Upgrade-Insecure-Requests: 1\n");
            sb.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\n");
            sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n");
            sb.append("Accept-Encoding: gzip, deflate, br\n");
            sb.append("Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\n");
            sb.append("\n");
            sendBuffer.put(sb.toString().getBytes());
            sendBuffer.flip();
            client.write(sendBuffer);

            client.register(selector, SelectionKey.OP_READ); //注册读操作
        }
    }
}
