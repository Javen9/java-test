package hjf.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * ZooKeeper持有者
 * Created by javen on 2018/7/26.
 */
public class ZKHolder {

    private static Watcher watcher = new WatcherImp();

    private static ZooKeeper zk;

    public static void init(String connectString) {
        try {
            zk = new ZooKeeper(connectString, 10000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录节点
     */
    public static String create(String path, String value, CreateMode createMode) {
        try {
            return zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取值，同时添加观察者
     * <p>
     * 因为观察者只生效一次，所以每次都要重新绑定观察者
     * </p>
     */
    public static String getData(String path) {
        try {
            byte[] bytes = zk.getData(path, watcher, new Stat());
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获子节点
     */
    public static List<String> getChildren(String path) {
        try {
            return zk.getChildren(path, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置值
     */
    public static void setData(String path, String value) {
        try {
            zk.setData(path, value.getBytes(), -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
