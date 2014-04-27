package model;

import visitors.ExpVisitor;

import java.util.Map;

public interface Exp {
    public void accept(ExpVisitor visitor);
    //public void traverse(ExpVisitor visitor);
    //public Iterator<model.Exp> iterator();
    public Exp evaluate(Map<String, Exp> context);
}
