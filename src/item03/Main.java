package item03;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 생성자가 private 불가능
        // Item03 3 = new Item03();
        Item03 instance1 = Item03.getInstance();
        Item03 instance2 = Item03.getInstance();

        boolean b = instance1 == instance2;
        System.out.println("b = " + b);

        // 리플렉션 (private 생성자 호출)
        Constructor<Item03> constructor = Item03.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Item03 obj = constructor.newInstance();

        obj.attacked();

    }
}
