package compiler.codegenerator;

import static compiler.models.Register.*;

public class CodeGenerator {
    private static final CodeGenerator instance = new CodeGenerator();

    public static CodeGenerator getInstance() {
        return instance;
    }

    public final StringBuilder generated;

    private CodeGenerator() {
        this.generated = new StringBuilder();
    }

    public void generate(String opcode, String... args) {
        generated.append("\t").append(opcode).append(" ").append(String.join(" ", args)).append("\n");
    }

    public void generateIndexed(String opcode, String R1, String R2, int offset) {
        generated.append("\t").append(opcode).append(" ").append(R1).append(" ").append(offset).append("(").append(R2).append(")").append("\n");
    }

    public void genPush(String register) {
        generateIndexed("sw", register, SP, 0);
        generate("subu", SP, SP, "4");
    }

    public void genPop(String register) {
        generateIndexed("lw", register, SP, 0);
        generate("addi", SP, SP, "4");
    }

    public void genLabel(String label) {
        generated.append(label).append(": ").append("\n");
    }

}