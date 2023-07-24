# 아이템2 : 생성자 매개변수가 많은 경우 빌더 사용을 고려해 볼 것

---

- 빌더 패턴은 JavaBeans 패턴의 장점인 가독성을 가지며, 끼워넣기 생성자 패턴의 장점인 객체의 불변성을 가진다.
- 빌더 패턴을 사용하는 클라이언트는 빌더 객체를 얻어 객체를 생성한다

## 1. 빌더 패턴 만들기
- 아래 클래스는 회원을 표현한다
- 회원 클래스의 인스턴스 변수 중 아이디,이름 은 필수다.
- 회원 클래스의 인스턴스 변수 중 이메일과 키, 몸무게는 필수적인 정보가 아니므로 부가적(optional)이다.

```java
public class Member {
    private Long id;       // required
    private String name;   // required
    private String email;  // optional
    private int height;    // optional
    private int weight;    // optional
}
```

- 가장 먼저 필요한 것은, 빌더 클래스를 따로 만드는 것이다
- 이 클래스는 빌드하려는 객체와 강하게 연관되어 있으므로 내부 정적 클래스로 선언한다.

```java
public class Member {
    private Long id;       // required
    private String name;   // required
    private String email;  // optional
    private int height;    // optional
    private int weight;    // optional
    
    public static class Builder{
        
        // required params
        private final Long id;
        private final String name;
        
        // optional params
        private String email;
        private int height;
        private int weight;
        
        // 1. 빌더 객체를 사용하려면 가장 먼저 필수 파라매터를 입력하도록 한다.
        public Builder(Long id, String name){
            this.id = id;
            this.name = name;
        }
        
        // 2. 옵셔널 파라매터는 선택적으로 호출되도록 한다.
        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        // 3. optional 세팅 작업이 완료되면 완성 메서드를 호출한다.
        public Member build() {
            return new Member(this);
        }
        
        // 4. 객체는 반드시 해당 객체의 Builder 객체로만 생성 할 수 있도록 제한한다.
        private Member(Builder builder){
            id = builder.id;
            name = builder.name;
            email = builder.email;
            height = builder.height;
            weight = builder.weight;
        }
        
        
    }
}
```

빌더 객체를 만드는 방법은 다음 4가지 과정을 거친다.
1. 빌더 객체를 사용하러면 가장 먼저 필수 파라매터를 입력하도록 한다.
2. 옵셔털 파라매터는 선택적으로 호출되도록 한다
3. 옵셔널 세팅 작업이 완료되면 완성 메서드를 호출한다.
4. 객체는 반드시 해당 객체의 Builder 객체로만 생성할 수 있도록 한다. 
```java
private Member(){
        //etc...
  }
```

### 빌더 패턴으로 생성하는 객체는 불변인가? 
- 위에서 정의한 Member 클래스의 필수 필드 모두를 final 로 정의했다
- Member 클래스의 생성자 또한 private 이며 setter 메서드는 존재하지 않는다.

### 빌더 패턴을 사용하는 클라이언트는 가독성 있게 개ㄱ체를 생성할 수 있는가?
- 다음 코드를 통해 클라이언트가 Member 객체를 생성하는 예를 살펴보자
- 클라이언트는 옵셔널 파라매터 중 height 값에만 값을 지정할 수 있으며 명시적으로 값을 설정했다.
```java
Member member1 = new Builder(
        1L,
        "Son"
        )
        .height(190)
        .build();
```

---
## 2. 빌더 패턴을 서브타입에서 사용하려면?
- 빌더 패턴은 클래스 계층 구성에도 적합하다.
- 다음 회원과 손님을 구분하는 예제를 보자
- 회원과 손님은 같은 부모 타입을 상속한다.
- 부모 타입은 이 두 타입에 공통적인 "권한" 속성을 가지고 있다.

```java
import java.util.Objects;

public abstract class Member {
    // 모든 서브 타입 객체에 공통적으로 필요한 타입
    public enum Role {READ, WRITE, DELETE}

    final Set<Role> roles;

    // 재귀적 타입 파라미터를 가진 제네릭 타입
    // 즉 자기 자신의 하위타입을 매개변수로 받음
    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Role> roles = EnumSet.noneOf(Role.class);

        // 서브타입에서 권한을 정의할 수 있음
        public T addRoles(Role types) {
            roles.add(Objects.requireNonNull(types));
            return self();
        }
        
        abstract Member build();
        
        protected abstract T self();
        
    }
    
    Member(Builder<T> builder) {
        roles = builder.roles.clone();
    }
}

```

```java

import java.util.Objects;

public class Guest extends Member {
    private final String name;

    public static class Builder extends Member.Builder<Builder> {
        private final String name;

        // 1. Guest 객체가 가져야 할 인스턴스 변수
        public Builder(String name) {
            this.name = Objects.requireNonNull(name);
        }
        
        // 2. 마지막으로 호출되는 빌드 완성 메서드
        @Override 
        Guest build() {
            return new Guest(this); 
        }

        // 3. 부모 타입에서 필요한 서브 타입의 참조
        @Override 
        protected Builder self() { 
            return this; 
        }

        private Guest(Builder builder) {
            super(builder);
            name = builder.name;
        }
    }
}

```


```java
public class AuthenticatedMember extends Member{
    private final String name; // 필수
    private final String email; // 필수
    
    public static class Builder extends Member.Builder<Builder> {
        private String name;
        private String email;
        
        // 1. 필수 속성은 빌더 생성자에서 바로 받도록 한다.
        public Builder(String name, String email){
            this.name = name;
            this.email = email;
        }
        
        // 2. 최종 빌더 완성 메서드
        @Override 
        AuthenticatedMember build() { 
            return new AuthenticatedMember(this); 
        }

        // 3. 부모 타입에서 필요한 메서드
        @Override 
        protected Builder self() {
            return null; 
        }
        public AuthenticatedMember(Builder builder) {
            super(builder);
            name = builder.name;
            email = builder.email;
        }
    }
    
}



```
- Main 코드
- 상위 타입 Member 에서 정의한 Roles 를 원하는 대로 추가 할 수 있다

```java
public static void main(String[]args){
    Guest guest = new Guest.builder("myGuests")
        .addRoles(Member.Role.READ).build();
}

```

---
## 정리
> 생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는게 좋다. 매개변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그러다. 빌더는 점층적 생성자보다 클라이언트코드를 읽고 쓰기가 훨씬 간결하고, 자바빈즈보다 훨씬 안전하다