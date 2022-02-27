public class Main {
    private static final String TEST_SRC = "test/";
    private static final int TEST_COUNT = 5;
    private static final String TEST_PREPROCESSOR_PRETEXT = "preprocessor";
    private static final String TEST_FORMAT = ".txt";
    private static final String TEST_OUTPUT_FORMAT = ".out.txt";

    public static void main(String[] args) {
        for (int i = 0; i < TEST_COUNT; i++) {
            Preprocessor preprocessor = new Preprocessor(
                TEST_SRC + TEST_PREPROCESSOR_PRETEXT + i + TEST_FORMAT,
                TEST_SRC + TEST_PREPROCESSOR_PRETEXT + i + TEST_OUTPUT_FORMAT);
            preprocessor.preprocess();
        }
    }
}
