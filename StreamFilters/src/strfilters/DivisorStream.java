package strfilters;

public class DivisorStream extends IntegerStream {
    private int divisor;

	public DivisorStream(int divisor, IIntegerStream stream) {
		super(stream);
		this.divisor = divisor;
	}

	@Override
	public int getNextInt() {
		while (true) {
			int i = stream.getNextInt();
			if (i % divisor == 0) {
				return i;
			}
		}
	}
}
