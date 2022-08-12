package compiler.codegenerator;

import static compiler.models.Register.*;

public class CodeGenerator {
    private static final CodeGenerator instance = new CodeGenerator();

    public static CodeGenerator getInstance() {
        return instance;
    }

    public final StringBuilder generated;
    private int labelCount = 0;

    private CodeGenerator() {
        this.generated = new StringBuilder();
    }

    public void generate(String opcode, String... args) {
        generated.append("\t").append(opcode).append(" ").append(String.join(" ", args)).append("\n");
    }

    public void generateIndexed(String opcode, String R1, String R2, int offset) {
        generated.append("\t").append(opcode).append(" ").append(R1).append(" ").append(offset).append("(").append(R2).append(")").append("\n");
    }

    public String nextLabel() {
        String label = "L" + labelCount;
        labelCount++;
        return label;
    }

    public void genPush(String register) {
        String opcode = isFloatingPointRegister(register) ? "sdc1" : "sw";
        generateIndexed(opcode, register, SP, 0);
        generate("subu", SP, SP, "4");
    }

    public void genPop(String register) {
        String opcode = isFloatingPointRegister(register) ? "ldc1" : "lw";
        generateIndexed(opcode, register, SP, 4);
        generate("addu", SP, SP, "4");
    }

    public void genLabel(String label) {
        generated.append(label).append(": ").append("\n");
    }

    private static boolean isFloatingPointRegister(String register) { 
        return register.startsWith("$f") && !register.equals("$fp"); 
    }

}
