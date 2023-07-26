# 아이템4 : 인스턴스화를 막으려면 private 생성자를 사용하라

---

static 메서드와 static 필드를 모아둔 클래스를 만든 경우 해당 클래스를 굳이 인스턴스를 만들필요 없으므로 만들 필요가 없다. 그리고 만약 abstract 로 클래스를 추상클래스로 만들어도 인스턴스를 막드는 걸 막을수는 없다.
왜냐하면 해당 클래스를 상속받으면 상속받은 클래스의 인스턴스를 생성 할 수 있기 때문이다.
> 대표적으로 static 메서드들을 모아둔 Utils 클래스들이 있다. 또한 상속받은 클래스는 부모 클래스의 생성자를 호출한다.

그리고 아무런 생성자를 만들지 않은 경우 컴파일러가 기본적으로 아무 인가 없는 public 생성자를 만들어주기 때문에 그런 경우에도 인스턴스를 만들 수 있다.

명시적으로 private 생성자를 추가해야 한다.

- abstract 를 사용할 경우 코드 예

```java

import item04.UtilClass;

public abstract class UtilityClass {
    public void getName() {
        "Hello";
    }

    public UtilityClass() {

    }

    public static class ChildUtilClass extends UtilClass {

    }

}

public class Main {
    // 인스턴스화 가능
    UtilClass.ChildUtilClass child = new UtilClass.ChildUtilClass();
}

```

- 생성자를 private
```java

public class UtilityClass {
    public void getName() {
        "Hello";
    }

    // 기본 생성자를 private 로 설정하여 인스턴스화를 불가능하게만듦
    private UtilityClass() {

    }

    public static class ChildUtilClass extends UtilClass {

    }

}

public class Main {
    // 인스턴스화 불가능
    UtilClass.ChildUtilClass child = new UtilClass.ChildUtilClass();
}

```