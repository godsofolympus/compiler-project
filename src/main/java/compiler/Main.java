package compiler;
import java_cup.runtime.*;

import java.io.*;

import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;

public class Main {

    public static boolean run(java.io.File inputFile) {
        Preprocessor preprocessor = new Preprocessor(inputFile, ".pp_tmp");
        preprocessor.preprocess();
        inputFile = new File(".pp_tmp");

        Lexer scanner;
        try {
            scanner = new Lexer(new FileReader(inputFile));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // TODO: add parser
        return true;
    }

    public static void main(String[] args) {
        System.out.println(run(new File("input.txt")));
    }
}
