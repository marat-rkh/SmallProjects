import java.util.Map;

public class Num implements Exp {
    public final Number number;
    public Num(Number number) {
        this.number = number;
    }

    @Override
    public void accept(ExpVisitor prettyPrinter) {
        prettyPrinter.visit(this);
    }
    @Override
    public void traverse(ExpVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public Exp evaluate(Map<String, Exp> context) {
        return this;
    }
}
