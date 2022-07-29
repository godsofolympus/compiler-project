package compiler;


import java.io.*;
import java.util.LinkedList;

import compiler.parser.Parser;
import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;
import java_cup.runtime.Symbol;


public class Main {

    public static boolean run(java.io.File inputFile) {
        Preprocessor preprocessor = new Preprocessor(inputFile, ".pp_tmp");
        preprocessor.preprocess();
        inputFile = new File(".pp_tmp");

        Lexer scanner;
        Parser parser;
        try {
            scanner = new Lexer(new FileReader(inputFile));
            parser = new Parser(scanner);
            parser.parse();

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
