# 아이템3 : private 생성자나 열거 타입으로 싱글턴임을 보증하라

---

- 싱글턴을 만드는 방식은 보통 둘 중 하나다.
- 두 방식 모두 생성자는 private 으로 감춰두고 유일한 인스턴스에 접근할 수 있는 수단으로 public static apaqj fmf gksk akfusgksek.


## 1. 첫번째 방법
- public static 멤버가 final 필드

```java
public  class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
        
    }
}
```

- private 생성자는 public static final 필드인 Elvis.INSTANCE 를 초기화 할 때 딱 한번만 호출된다.
- public 이나 protected 생성자가 없으므로 Elvis 클래스가 초기화활 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장된다.
> 다만 한가지 예외가 있다. 권한이 있는 클라이언트는 리플렉션을 사용해 private 생성자를 호출 할 수 있다. 이러한 공격을 방어하려면 생성자를 수정하여 두 번째 객체가 생성되려 할 때 예외처리를 하면된다.

#### 장점
이런 API 사용이 static 팩토리 메소드를 사용하는 방법에 비해 더 명확하고 더 간단하다.

- 리플렉션 예시
```java
public class 리플렉션 {
    public static void main(String[] args) {
        Constructor<Elvis> constructor = Item03.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Elvis obj = constructor.newInstance();

        obj.hello();
    }
}
```

## 2. 두번째 방법
- 정적 팩터리 메서드를 public static 멤버로 제공

```java

public  class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
        
    }
    public static Elvis getInstance() {
        return INSTANCE;
    }
}

```
- Elvis.getInstance 는 항상 같은 객체의 참조를 반환하므로 제2의 Elvis 인스턴스란 결코 만들어지지 않는다(리플렉션 예외처리해야함)
#### 장점
API를 변경하지 않고로 싱글톤으로 쓸지 안쓸지 변경할 수 있다. 처음엔 싱글톤으로 쓰다가 나중엔 쓰레드당 새 인스턴스를 만든다는 등 클라이언트 코드를 고치지 않고도 변경할 수 있다.


