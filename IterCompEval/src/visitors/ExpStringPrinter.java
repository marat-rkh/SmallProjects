package visitors;

import model.Assign;
import model.Div;
import model.Mul;
import model.Num;
import model.Sum;
import model.Var;

public class ExpStringPrinter implements ExpVisitor {
    private String buffer = "";
    public String flushBuffer() {
        String bufferCopy = buffer;
        buffer = "";
        return bufferCopy;
    }

    public void visit(Num exp) { buffer += exp.number; }

    public void visit(Div div) {
        div.left.accept(this);
        buffer += " / ";
        div.right.accept(this);
    }

    public void visit(Mul mul) {
        mul.left.accept(this);
        buffer += " * ";
        mul.right.accept(this);
    }

    public void visit(Sum sum) {
        buffer += "(";
        sum.left.accept(this);
        buffer += " + ";
        sum.right.accept(this);
        buffer += ")";
    }

    public void visit(Var exp) { buffer += exp.name; }

    public void visit(Assign assign) {
        buffer += assign.name;
        buffer += " = ";
        assign.expr.accept(this);
    }
}
