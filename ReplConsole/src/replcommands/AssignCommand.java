package replcommands;

public class AssignCommand implements REPLCommand {
    private String varName;

    public AssignCommand(String varName) {
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }
}
