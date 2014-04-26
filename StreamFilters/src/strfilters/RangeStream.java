package strfilters;

public class RangeStream extends IntegerStream {
    private int leftBound;
    private int rightBound;

	public RangeStream(int left, int right, IIntegerStream stream) {
		super(stream);
		this.leftBound = left;
		this.rightBound = right;
	}

	@Override
	public int getNextInt() {
		while (true) {
			int i = stream.getNextInt();
			if (i > rightBound) {
				throw new RuntimeException();
			}
            if (i >= leftBound) {
				return i;
			}
		}
	}
}
