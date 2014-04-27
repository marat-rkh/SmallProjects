package model;

import visitors.ExpVisitor;

import java.util.Map;

public class Div extends BiExp {
    public Div(Exp left, Exp right) {
        super(left, right);
    }
    @Override
    public void accept(ExpVisitor visitor) {
        visitor.visit(this);
    }
//    @Override
//    public void traverse(ExpVisitor visitor) {
//        left.traverse(visitor);
//        visitor.visit(this);
//        right.traverse(visitor);
//    }
    @Override
    public Exp evaluate(Map<String, Exp> context) {
        Exp leftExp = left.evaluate(context);
        Exp rightExp = right.evaluate(context);
        if(leftExp instanceof Var && context.containsKey(((Var) leftExp).name)) {
            leftExp = context.get(((Var) leftExp).name).evaluate(context);
        }
        if(rightExp instanceof Var && context.containsKey(((Var) rightExp).name)) {
            rightExp = context.get(((Var) rightExp).name).evaluate(context);
        }
        if(rightExp instanceof Num) {
            if (((Num) rightExp).number.doubleValue() == 0) {
                throw new RuntimeException("StaticCheck failed: Divide by zero");
            }
            if(leftExp instanceof Num) {
                return new Num(((Num) leftExp).number.doubleValue() / ((Num)rightExp).number.doubleValue());
            }
        }
        return new Div(leftExp, rightExp);
    }
}
