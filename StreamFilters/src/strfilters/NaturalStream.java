package strfilters;

public class NaturalStream implements IIntegerStream {
    private int currentNumber = 0;

    @Override
	public int getNextInt() {
		return currentNumber++;
	}
}
