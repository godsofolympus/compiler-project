package compiler.scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor {
    private final Pattern definePattern = Pattern.compile("^\\s*define\\s+([a-zA-Z][\\w_]*)\\s+(\\S.*)$");

    private final Pattern importPattern = Pattern.compile("^\\s*import\\s+\\S+$");
    private final Map<String, String> defineStatements = new HashMap<>();
    private final ArrayList<String> strings = new ArrayList<>();
    private final File inputFile;
    private final String outputFileName;

    private boolean stopProcess = false;

    public Preprocessor(File inputFile, String outputFileName) {
        this.inputFile = inputFile;
        this.outputFileName = outputFileName;
    }

    public void preprocess() {
        try {
            Scanner scanner = new Scanner(inputFile);
            String definelessString = extractMacros(scanner);
            String stringlessFile = emptyStrings(definelessString);
            String replacedString = replaceMacros(stringlessFile);
            String preprocessedString = replaceStrings(replacedString); 
            FileWriter fileWriter = new FileWriter(outputFileName);
            fileWriter.write(preprocessedString);
            fileWriter.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractMacros(Scanner scanner) {
        StringBuilder sb = new StringBuilder();
        boolean stopProcess = false;
        while (!stopProcess && scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Matcher defineMatcher = definePattern.matcher(currentLine);
            Matcher importMatcher = importPattern.matcher(currentLine);
            if (defineMatcher.find()) defineStatements.put(defineMatcher.group(1), defineMatcher.group(2));
            else {
                if (!importMatcher.find()) stopProcess = true;
                sb.append(currentLine).append('\n');
            }
        }
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine()).append('\n');
        }
        return sb.toString();
    }

    private String replaceStrings(String string) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = Pattern.compile("\"\"").matcher(string);
        int counter = 0;
        while (matcher.find()) {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(strings.get(counter)));
            counter++;
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String emptyStrings(String fileWithNoDefine) {
        StringBuilder sb = new StringBuilder();
        Matcher stringMatcher = Pattern.compile("(?<!\\\\)\".*(?<!\\\\)\"").matcher(fileWithNoDefine);
        while (stringMatcher.find()) {
            strings.add(stringMatcher.group());
            stringMatcher.appendReplacement(sb, "\"\"");
        }
        stringMatcher.appendTail(sb);
        return sb.toString();
    }

    private String replaceMacros(String currentString) {
        for (String identifier : defineStatements.keySet()) {
            String regex = "(?<![a-zA-Z])" + identifier + "(?!\\w)";
            currentString = currentString.replaceAll(regex, defineStatements.get(identifier));
        }
        return currentString;
    }

}