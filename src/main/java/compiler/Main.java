package compiler;
import java_cup.runtime.*;

import java.io.*;

import compiler.scanner.Lexer;
import compiler.scanner.Preprocessor;

public class Main {

    public static String run(java.io.File inputFile) throws Exception {
        StringBuilder str = new StringBuilder();

        Preprocessor preprocessor = new Preprocessor(inputFile, "pp_tmp~");
        preprocessor.preprocess();

        inputFile = new File("pp_tmp~");
        Lexer scanner = new Lexer(new FileReader(inputFile));
        while (scanner.hasToken()) {
            Symbol token = scanner.next_token();
            if (token.sym == 0)
                break;
            str.append(scanner.getTokenString(token) + "\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String output = "";
        try {
            output = run(new File("input.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(output);
    }
}
