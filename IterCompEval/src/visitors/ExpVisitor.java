package visitors;

import model.*;

public interface ExpVisitor {
    void visit(Num num);
    void visit(Sum sum);
    void visit(Substr substr);
    void visit(Mul mul);
    void visit(Div div);
    void visit(Var var);
    void visit(Assign assign);
}