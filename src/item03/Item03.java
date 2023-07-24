package item03;

public  class Item03 {
    private static final Item03 INSTANCE = new Item03();

    public static Item03 getInstance() {
        return INSTANCE;
    }

    private Object readResolve(){
        return INSTANCE;
    }
    private Item03() {

    }

    void attacked() {
        System.out.println("YOU LOSE");
    }
}
