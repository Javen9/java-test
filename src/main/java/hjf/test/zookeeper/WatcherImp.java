package hjf.test.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * 观察者
 * Created by javen on 2017/11/8.
 */
public class WatcherImp implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("观察到改变路径：" + watchedEvent.getPath());
        System.out.println("操作类型：" + watchedEvent.getType());
        System.out.println("版本号：" + watchedEvent.getState().getIntValue());
        if (watchedEvent.getPath() != null) {
            System.out.println("改变后的值为：" + ZKHolder.getData(watchedEvent.getPath()));
        }
    }
}
