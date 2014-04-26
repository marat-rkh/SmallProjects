package strfilters;

public class Main {
	public static void main(String[] args) {
        IIntegerStream natStream = new NaturalStream();
        IIntegerStream divStream = new DivisorStream(3, natStream);
        IIntegerStream rangeStream = new RangeStream(10, 20, natStream);
        IIntegerStream fibStream = new FibonacciStream(natStream);

        for(int i = 0; i < 10; i++) {
            System.out.print(rangeStream.getNextInt());
            System.out.print(" ");
        }
        System.out.println();
        for(int i = 0; i < 20; i++) {
            System.out.print(divStream.getNextInt());
            System.out.print(" ");
        }
        System.out.println();

        for(int i = 0; i < 20; i++) {
            System.out.print(fibStream.getNextInt());
            System.out.print(" ");
        }
        System.out.println();

        IIntegerStream natStream2 = new NaturalStream();
        IIntegerStream fibStream2 = new FibonacciStream(natStream2);
        IIntegerStream divStream2 = new DivisorStream(2, fibStream2);
        for(int i = 0; i < 10; i++) {
            System.out.print(divStream2.getNextInt());
            System.out.print(" ");
        }
    }
}
