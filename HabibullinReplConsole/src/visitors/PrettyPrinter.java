package visitors;

import model.*;

public class PrettyPrinter implements ExpVisitor {

    public void visit(Num exp) {
        if(exp.number.doubleValue() < 0) {
            System.out.println("(" + exp.number + ")");
        } else {
            System.out.print(exp.number);
        }
    }

    public void visit(Div div) {
        div.left.accept(this);
        System.out.print(" / ");
        div.right.accept(this);
    }

    public void visit(Mul mul) {
        mul.left.accept(this);
        System.out.print(" * ");
        mul.right.accept(this);
    }

    public void visit(Sum sum) {
        System.out.print("(");
        sum.left.accept(this);
        System.out.print(" + ");
        sum.right.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Substr substr) {
        System.out.print("(");
        substr.left.accept(this);
        System.out.print(" - ");
        substr.right.accept(this);
        System.out.print(")");
    }

    public void visit(Var exp) {
        System.out.print(exp.name);
    }

    public void visit(Assign assign) {
        System.out.print(assign.name);
        System.out.print(" = ");
        assign.expr.accept(this);
    }
}
