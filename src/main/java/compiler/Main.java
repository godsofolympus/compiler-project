package compiler;


import java.io.*;


import compiler.AST.Program;
import compiler.codegenerator.CodeGenerator;
import compiler.parser.Parser;
import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;
import compiler.visitors.CodeGeneratorVisitor;
import compiler.visitors.TypeCheckerVisitor;


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
            TypeCheckerVisitor visitor = new TypeCheckerVisitor();
            visitor.visit(program);
            CodeGeneratorVisitor cgenVisitor = new CodeGeneratorVisitor();
            cgenVisitor.visit(program);
            FileWriter fileWriter = new FileWriter("res.s");
            fileWriter.write(CodeGenerator.getInstance().text.toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // TODO: add parser
        return true;
//        Preprocessor preprocessor = new Preprocessor(inputFile, ".pp_tmp");
//        preprocessor.preprocess();
//        inputFile = new File(".pp_tmp");
//        Lexer scanner;
//        Parser parser;
//        try {
//            scanner = new Lexer(new FileReader(inputFile));
//            parser = new Parser(scanner);
//            Program program = (Program) parser.parse().value;
//            TypeCheckerVisitor visitor = new TypeCheckerVisitor();
//            visitor.visit(program);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        // TODO: add parser
//        return true;
    }

    public static void main(String[] args) {
        System.out.println(run(new File("input.txt")));
    }
}
