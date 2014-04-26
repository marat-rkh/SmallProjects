package strfilters;

public class FibonacciStream extends IntegerStream {
    private int f0 = 0;
    private int f1 = 1;

    public FibonacciStream(IIntegerStream stream) {
		super(stream);
	}

	@Override
	public int getNextInt() {
        f0 = 0;
        f1 = 1;
		while (true) {
			int i = stream.getNextInt();
			setFibsAround(i);
			if (i == f0) {
				return i;
			}
			if (i == f1) {
				return i;
			}
		}
	}

	private void setFibsAround(int bound) {
		while (f1 < bound) {
			int tmp = f0;
			f0 = f1;
			f1 = f0 + tmp;
		}
	}
}
