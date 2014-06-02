public class Main {
    public static void main(String[] args) {
        ClassCreator classCreator = new ClassCreator();
        classCreator.create(Main.class, "/home/mrx/Desktop/");
    }

    private final static void foo(final int a) {
        return;
    }

    volatile int x = 0;
}
