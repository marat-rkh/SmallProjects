import java.util.Map;

/**
 * Created by mrx on 16/04/14.
 */
public class Var implements Exp {
    public final String name;
    public Var(String name) {
        this.name = name;
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
