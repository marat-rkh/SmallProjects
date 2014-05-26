package model;

import visitors.ExpVisitor;

import java.util.Map;

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
    public Exp evaluate(Map<String, Exp> context) throws Exception {
        if(context.containsKey(name)) {
            return context.get(name).evaluate(context);
        }
        return this;
    }
}
