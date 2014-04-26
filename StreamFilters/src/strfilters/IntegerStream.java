package strfilters;

public abstract class IntegerStream implements IIntegerStream {
    protected IIntegerStream stream;

	protected IntegerStream(IIntegerStream stream) {
		this.stream = stream;
	}
}
