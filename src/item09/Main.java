package item09;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        MyResource myResource1 = null;

        try{
            myResource1 = new MyResource();
            myResource1.doSomeThing();
        } finally {
            if (myResource1 != null) {
                myResource1.close();
            }
        }

        try (MyResource myResource = new MyResource()) {
            myResource.doSomeThing();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
