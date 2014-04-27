package visitors;

import model.Div;
import model.Mul;
import model.Num;
import model.Sum;

public abstract class ExpVisitorAdapter implements ExpVisitor {
    @Override
    public void visit(Num num) {
    }

    @Override
    public void visit(Sum sum) {
    }

    @Override
    public void visit(Mul mul) {
    }

    @Override
    public void visit(Div div) {
    }
}