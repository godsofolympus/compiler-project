import Scanner.Lexer;
import Scanner.Preprocessor;
import java_cup.sym;
import java_cup.runtime.Symbol;

import java.io.*;
import java.util.*;

public class Main {

    public static String run(java.io.File inputFile) throws Exception {
        StringBuilder str = new StringBuilder();
        
        String inputFileName = inputFile.getName();

        Preprocessor preprocessor = new Preprocessor(inputFile, inputFileName);
        preprocessor.preprocess();

        Lexer scanner = new Lexer(new FileReader(inputFile));
        while (scanner.hasToken()) {
            Symbol token = scanner.next_token();
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
        System.out.println(output);
    }
}
