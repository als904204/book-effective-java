# 아이템9 : Try-Finally 대신 Try-With-Resources 사용하라

---

- 자바 라이브러리에는 InputStream, OutputStream 그리고 java.sql.Connection 과 같이 정리(close) 가 가능한 리소스가 많은데,
그런 리소스를 사용하는 클라이언트 코드가 보통 리소스 정리를 안하거나 잘못하는 경우가 있다.

```java
public class FirstError extends RuntimeException{

}


public class SecondError extends RuntimeException{

}



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

// try-finally 사용
public class Main {

    public static void main(String[] args) {
        MyResource myResource1 = null;

        try {
            myResource1 = new MyResource();
            myResource1.doSomeThing();
        } finally {
            if (myResource1 != null) {
                myResource1.close();
            }
        }

    }
}


```

- 이 코드에서 예외가 발생하면 SecondError 가 출력되고 FirstError 는 출력이 안된다.
그러면 문제를 디버깅하기 힘들어진. 또한 중복으로 try-catch 만들어야 하는 경우에도 실수를 할 가능성이 높다

- 자바7에서 추가된 try-with-resources 를 사용하면 코드 가독성도 좋고, 문제를 분석할 때도 훨씬 좋다.왜냐하면 try-finally 를 사용할 때 처음에 발생한 예외가 뒤에 발생한 에러에 덮히지 않는다.
  뒤에 발생한 에러는 첫번째 발생한 에러 뒤에다 쌓아두고(suppressed) 처음 발생한 에러를 중요시 여긴다. 그리고 Throwable의 getSuppressed 메소드를 사용해서 뒤에 쌓여있는 에러를 코딩으로 사용할 수도 있다.

- catch 블록은 try-finally 와 동일하게 사용 할 수 있다.
```java

// try-with-resource 사용
public class Main {

    public static void main(String[] args) {
      try (MyResource myResource = new MyResource()) {
            myResource.doSomeThing();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
```