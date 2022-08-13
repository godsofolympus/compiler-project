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

    // A0 = destPtr, A1 = srcPtr, A2 = #words to be copied
    public void memCpyWords() {
        this.generate("move", T0, A0); // current dest ptr
        this.generate("move", T1, A1); // current src ptr
        this.generate("li", T2, "0"); // counter
        String loopLabel = this.nextLabel();
        String endLabel = this.nextLabel();
        this.genLabel(loopLabel);
        this.generate("beq", T2, A2, endLabel); // counter ?= #bytes
        this.generateIndexed("lw", T3, T1, 0); // set mem[currentPtr] = A1 (content)
        this.generateIndexed("sw", T3, T0, 0); // set mem[currentPtr] = A1 (content)
        this.generate("addi", T0, "4"); // current dest ptr ++
        this.generate("addi", T1, "4"); // current src ptr ++
        this.generate("addi", T2, "1"); // counter ++
        this.generate("j", loopLabel); // jump to loopLabel
        this.genLabel(endLabel);
    }

    private void memCpy(boolean isByte) {
        this.generate("move", T0, A0); // current dest ptr
        this.generate("move", T1, A1); // current src ptr
        this.generate("li", T2, "0"); // counter
        String loopLabel = this.nextLabel();
        String endLabel = this.nextLabel();
        this.genLabel(loopLabel);
        this.generate("beq", T2, A2, endLabel); // counter ?= #bytes
        this.generate("move", T3, R0); // reset T0
        this.generateIndexed(isByte ? "lb" : "lw", T3, T1, 0); // set mem[currentPtr] = A1 (content)
        this.generateIndexed(isByte ? "sb" : "sw", T3, T0, 0); // set mem[currentPtr] = A1 (content)
        this.generate("addi", T0, isByte ? "1" : "4"); // current dest ptr ++
        this.generate("addi", T1, isByte ? "1" : "4"); // current src ptr ++
        this.generate("addi", T2, "1"); // counter ++
        this.generate("j", loopLabel); // jump to loopLabel
        this.genLabel(endLabel);
    }

    // A0 = destPtr, A1 = srcPtr, A2 = #bytes to be copied
    public void memCpyBytes() {
        this.memCpy(true);
    }

    // A0 == destPtr, A1 = content, A2 = #bytes
    public void memSetBytes() {
        this.memCpy(false);
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
