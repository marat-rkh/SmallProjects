package visitors;

import model.*;

public class ExpStringPrinter implements ExpVisitor {
    private String buffer = "";
    public String flushBuffer() {
        String bufferCopy = buffer;
        buffer = "";
        return bufferCopy;
    }

    public void visit(Num exp) {
        if(exp.number.doubleValue() < 0) {
            buffer += ("(" + exp.number + ")");
        } else {
            buffer += exp.number;
        }
    }

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

    public void visit(Substr substr) {
        buffer += "(";
        substr.left.accept(this);
        buffer += " - ";
        substr.right.accept(this);
        buffer += ")";
    }

    public void visit(Var exp) { buffer += exp.name; }

    public void visit(Assign assign) {
        buffer += assign.name;
        buffer += " = ";
        assign.expr.accept(this);
    }
}
