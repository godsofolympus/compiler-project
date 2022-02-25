import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor {

    private final StringBuilder stringBuilder = new StringBuilder();
    private final Pattern definePattern = Pattern.compile("^\\s*define\\s+([a-zA-Z][\\w_]*)\\s+(\\S.*)$");
    private final Map<String, String> defineStatements = new HashMap<>();
    private final File inputFile;
    private final String outputFileName;

    public Preprocessor(String inputFileName, String outputFileName) {
        this.inputFile = new File(inputFileName);
        this.outputFileName = outputFileName;
    }

    public void preprocess() {
        try {
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) processLine(scanner.nextLine());
            String preprocessedString = replaceMacros();
            FileWriter fileWriter = new FileWriter(outputFileName);
            fileWriter.write(preprocessedString);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
        Matcher matcher = definePattern.matcher(line);
        if (matcher.find()) defineStatements.put(matcher.group(1), matcher.group(2));
        else stringBuilder.append(line).append('\n');
    }

    private String replaceMacros() {
        String currentString = stringBuilder.toString();
        for (String identifier : defineStatements.keySet()) {
            String regex = "(?<![a-zA-Z])" + identifier + "(?!\\w)";
            currentString = currentString.replaceAll(regex, defineStatements.get(identifier));
        }
        return currentString;
    }

}