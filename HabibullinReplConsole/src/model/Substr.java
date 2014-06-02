package model;

import visitors.ExpVisitor;

import java.util.Map;

public class Substr extends BiExp {
    public Substr(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public void accept(ExpVisitor prettyPrinter) { prettyPrinter.visit(this); }

    @Override
    public Exp evaluate(Map<String, Exp> context) throws Exception {
        Exp leftExp = left.evaluate(context);
        Exp rightExp = right.evaluate(context);
        if(leftExp instanceof Num && rightExp instanceof Num) {
            return new Num(((Num) leftExp).number.doubleValue() - ((Num) rightExp).number.doubleValue());
        }
        return new Substr(leftExp, rightExp);
    }
}