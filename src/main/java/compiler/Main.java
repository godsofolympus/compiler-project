package compiler;


import java.io.*;


import compiler.AST.Decl;
import compiler.AST.Program;
import compiler.parser.Parser;
import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;


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
            Program program = (Program) parser.parse().value;
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
