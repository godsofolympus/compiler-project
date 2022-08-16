package compiler.codegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static compiler.models.Register.*;

public class CodeGenerator {
    private static final CodeGenerator instance = new CodeGenerator();

    public static CodeGenerator getInstance() {
        return instance;
    }

    public final StringBuilder text;
    public final StringBuilder data;
    private int labelCount = 0;
    private int ptrCount = 0;

    private CodeGenerator() {
        this.text = new StringBuilder(".text\n");
        this.data = new StringBuilder(".data\n");
        this.data.append(STRINGS);
        this.text.append(SUBROUTINES);
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

    // A0 = destPtr, A1 = srcPtr, A2 = #words to be copied
    public void memCpyWords() {
    }

    // A0 == destPtr, A1 = content, A2 = #bytes
    public void memSetBytes() {
        this.generate("move", T0, A0); // current dest ptr
        this.generate("li", T1, "0"); // counter
        String loopLabel = this.nextLabel();
        String endLabel = this.nextLabel();
        this.genLabel(loopLabel);
        this.generate("beq", T1, A2, endLabel); // counter ?= #bytes
        this.generateIndexed("sb", A1, T0, 0); // set mem[currentPtr] = A1 (content)
        this.generate("addi", T0, "1"); // current dest ptr ++
        this.generate("addi", T1, "1"); // coutner ++
        this.generate("j", loopLabel); // jump to loopLabel
        this.genLabel(endLabel);
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

    public static final int CHAR_SIZE = 1; // in bytes
    public static final int BUFFER_SIZE = 16 * CHAR_SIZE;

    public static final String STRINGS =
            "\tnewline: .asciiz \"\\n\"\n" +
                    "\ttrue: .asciiz \"true\"\n" +
                    "\tfalse: .asciiz \"false\"\n";
    public static final String SUBROUTINES =
            "strcmp:\n" +
                    "    lb      $t2,($s0)                   # get next char from str1\n" +
                    "    lb      $t3,($s1)                   # get next char from str2\n" +
                    "    bne     $t2,$t3,cmpne               # are they different? if yes, fly\n" +
                    "\n" +
                    "    beq     $t2,$zero,cmpeq             # at EOS? yes, fly (strings equal)\n" +
                    "\n" +
                    "    addi    $s0,$s0,1                   # point to next char\n" +
                    "    addi    $s1,$s1,1                   # point to next char\n" +
                    "    j       strcmp\n" +
                    "\n" +
                    "cmpne:\n" +
                    "    li     $a0,0\n" +
                    "    sw     $a0, 0($sp)\n" +
                    "    subu   $sp, $sp, 4\n" +
                    "    jr     $ra\n" +
                    "\n" +
                    "cmpeq:\n" +
                    "    li     $a0,1\n" +
                    "    sw     $a0, 0($sp)\n" +
                    "    subu   $sp, $sp, 4\n" +
                    "    jr     $ra\n";
}
