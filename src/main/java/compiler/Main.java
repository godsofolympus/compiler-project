package compiler;


import java.io.*;


import compiler.AST.Program;
import compiler.Exceptions.SemanticError;
import compiler.codegenerator.CodeGenerator;
import compiler.parser.Parser;
import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;
import compiler.visitors.CodeGeneratorVisitor;
import compiler.visitors.TypeCheckerVisitor;


public class Main {

    public static void run(File inputFile, File outputFile) {
        Preprocessor preprocessor = new Preprocessor(inputFile, ".pp_tmp");
        preprocessor.preprocess();
        inputFile = new File(".pp_tmp");
        Lexer scanner;
        Parser parser;
        String result;
        try {
            scanner = new Lexer(new FileReader(inputFile));
            parser = new Parser(scanner);
            Program program = (Program) parser.parse().value;
            TypeCheckerVisitor visitor = new TypeCheckerVisitor();
            visitor.visit(program);
            CodeGeneratorVisitor cgenVisitor = new CodeGeneratorVisitor();
            cgenVisitor.visit(program);
            result = CodeGenerator.getInstance().getResult();
        } catch (SemanticError e) {
            e.printStackTrace();
            result = "Semantic Error";
        } catch (Exception e) {
            e.printStackTrace();
            result = "Syntax Error";
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputFile);
            fileWriter.write(result);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        run(new File("input.txt"), new File("res.s"));
    }
}
