package model;

import visitors.ExpVisitor;

import java.util.Map;

public class Sum extends BiExp {
    public Sum(Exp left, Exp right) {
        super(left, right);
    }
    @Override
    public void accept(ExpVisitor prettyPrinter) {
        prettyPrinter.visit(this);
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
        if(leftExp instanceof Num && rightExp instanceof Num) {
            return new Num(((Num) leftExp).number.doubleValue() + ((Num) rightExp).number.doubleValue());
        }
        return new Sum(leftExp, rightExp);
    }
}
