package model;

import visitors.ExpVisitor;

import java.util.Map;

public class Assign implements Exp {
    public String name;
    public Exp expr;

    public Assign(String name, Exp expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public void accept(ExpVisitor visitor) { visitor.visit(this); }

    @Override
    public Exp evaluate(Map<String, Exp> context) {
        context.put(name, expr);
        return null;
    }
}
