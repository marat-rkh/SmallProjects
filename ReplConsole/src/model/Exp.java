package model;

import visitors.ExpVisitor;

import java.util.Map;

public interface Exp {
    public void accept(ExpVisitor visitor);
    public Exp evaluate(Map<String, Exp> context) throws Exception;
}
