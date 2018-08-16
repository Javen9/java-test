package hjf.test.intern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javen on 2018/8/1.
 */
public class InternTest {

//    private static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //“软件技术” 只在堆里面有
        String str = new StringBuilder("软件").append("技术").toString();
//        System.out.println("测试结果：" + (str.intern() == "软件技术"));
//        System.out.println("测试结果：" + ("软件技术" == str.intern()));
//        System.out.println("测试结果：" + ("软件技术" == str));
        System.out.println("jdk1.6 前为false：" + (str.intern() == str));
        //“java” 在启动/类加载时已经放入常量池
        String str1 = new StringBuilder("ja").append("va").toString();
        System.out.println(str1.intern() == str1);
        //“test” 当实参传递时已经放入常量池
        String str2 = new String("test");
        System.out.println(str2.intern() == str2);
        int index = 1;
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据" + index++);
//            ("测试数据" + index++).intern();
        }
    }
}
