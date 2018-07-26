package hjf.test.assert0;

/**
 * Created by javen on 2017/4/10.
 */
public class AssertTest {

    private static Object object = new Object();

    public static void main(String[] args) {
        assert !Thread.holdsLock(object);
        System.out.println("============" + Thread.holdsLock(object));
        synchronized (object) {
            synchronized (object) {
                System.out.println("============" + Thread.holdsLock(object));
            }
            System.out.println("============" + Thread.holdsLock(object));
        }
        System.out.println("============" + Thread.holdsLock(object));
    }
}
