package item04;

public class UtilClass {
    public static String getName() {
        return "Hello";
    }

    private UtilClass() {
        throw new AssertionError();
    }
    public static class ChildUtilClass extends UtilClass {
    }

    public static void main(String[] args) {
    }
}
