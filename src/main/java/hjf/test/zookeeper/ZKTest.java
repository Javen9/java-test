package hjf.test.zookeeper;

import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * <p>
 * 注意：zookeeper 临时节点不支持创建子节点，create不支持同时创建多层目录
 * </p>
 * Created by javen on 2017/11/8.
 */
public class ZKTest {
    public static void main(String[] args) {
        ZKHolder.init("localhost:2181");
        try {
            ZKHolder.create("/node", "v1", CreateMode.PERSISTENT);
            System.out.println(ZKHolder.getData("/node"));
            int index = 1;
            while (true) {
                Thread.sleep(2000);
                index++;
                ZKHolder.setData("/node", "v" + index);

                //子目录测试
                ZKHolder.create("/node/c_", "child", CreateMode.EPHEMERAL_SEQUENTIAL);
                System.out.println("=======打印子节点======");
                List<String> list = ZKHolder.getChildren("/node");
                if (list != null) {
                    for (String v : list) {
                        System.out.println(v);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
