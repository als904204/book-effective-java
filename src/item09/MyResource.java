package item09;

public class MyResource implements AutoCloseable{


    public void doSomeThing() throws FirstError {
        System.out.println("FirstError");
        throw new FirstError();
    }
    @Override
    public void close() throws SecondError {
        System.out.println("Clean my resource");
        throw new SecondError();
    }
}
