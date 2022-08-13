package compiler.codegenerator;

import static compiler.models.Register.*;

public class CodeGenerator {
    private static final CodeGenerator instance = new CodeGenerator();


    public static final int CHAR_SIZE = 1; // in bytes
    public static final int BUFFER_SIZE = 16 * CHAR_SIZE;

    public static CodeGenerator getInstance() {
        return instance;
    }

    public final StringBuilder text;
    public final StringBuilder data;
    private int labelCount = 0;
    private int ptrCount = 0;

    private CodeGenerator() {
        this.text = new StringBuilder();
        this.data = new StringBuilder();
        data.append(".data\n");
        data.append("\tnewline: .asciiz \"\\n\"\n");
        data.append("\ttrue: .asciiz \"true\"\n");
        data.append("\tfalse: .asciiz \"false\"\n");
    }

    public String getResult(){
        return this.data.append(this.text).toString();
    }

    public void generate(String opcode, String... args) {
        text.append("\t").append(opcode).append(" ").append(String.join(" ", args)).append("\n");
    }

    public void generateIndexed(String opcode, String R1, String R2, int offset) {
        text.append("\t").append(opcode).append(" ").append(R1).append(" ").append(offset).append("(").append(R2).append(")").append("\n");
    }

    public void generateWithComment(String opcode, String comment, String ...args) {
        text.append("\t").append(opcode).append(" ").append(String.join(" ", args))
            .append("\t# ").append(comment).append("\n");
    }

    public void generateOneLineComment(String comment) {
        text.append("\t").append("# ").append(comment).append("\n");
    }

    public void generateEmptyLine() {
        text.append("\n");
    }

    public String malloc(int sizeInBytes) {
        String ptrLabel = nextPtr();
        data.append("\t").append(ptrLabel).append(":").append("\t.word").append("\t0").append("\n");
        generateWithComment("li", " allocate memory", V0, "9");
        text.append("\t").append("li").append(" ").append(A0).append(" ").append(String.valueOf(sizeInBytes)).append("\n");
        text.append("\t").append("syscall").append("\n");
        text.append("\t").append("sw").append(" ").append(V0).append(" ").append(ptrLabel).append("\n");
        text.append("\n");
        return ptrLabel;
    }

    // input is stored in A0 instead
    public String malloc() {
        String ptrLabel = nextPtr();
        data.append("\t").append(ptrLabel).append(":").append("\t.word").append("\t0").append("\n");
        generateWithComment("li", " allocate memory", V0, "9");
        text.append("\t").append("syscall").append("\n");
        text.append("\t").append("sw").append(" ").append(V0).append(" ").append(ptrLabel).append("\n");
        text.append("\n");
        return ptrLabel;
    }

    public String nextPtr() {
        String label = "_T" + ptrCount;
        ptrCount++;
        return label;
    }

    public String nextLabel() {
        String label = "L" + labelCount;
        labelCount++;
        return label;
    }

    public void genPush(String register) {
        String opcode = isFloatingPointRegister(register) ? "swc1" : "sw";
        generateIndexed(opcode, register, SP, 0);
        generate("subu", SP, SP, "4");
    }

    public void genPop(String register) {
        String opcode = isFloatingPointRegister(register) ? "lwc1" : "lw";
        generateIndexed(opcode, register, SP, 4);
        generate("addu", SP, SP, "4");
    }

    public void genLabel(String label) {
        text.append(label).append(": ").append("\n");
    }

    private static boolean isFloatingPointRegister(String register) { 
        return register.startsWith("$f") && !register.equals("$fp"); 
    }

}
