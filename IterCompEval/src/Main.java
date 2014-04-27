import model.*;

import java.util.Map;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Exp exp = new Sum(
            new Num (5),
            new Div(
                new Sum(new Num(15), new Num(88)),
                new Var("x")
            )
        );
        exp.accept(new visitors.PrettyPrinter());

        Map<String, Exp> context = new HashMap<>();
        context.put("x", new Num(103));
        Exp resExp = exp.evaluate(context);

        System.out.println();
        resExp.accept(new visitors.PrettyPrinter());
    }
}