public class TestFourClass {

    @CsvSource("word,34")
    public void testCsvWithArgs(String string, int a) {
        System.out.println("Test csv with args: \n string = " + string + "\n a = " + a);
    }

    @CsvSource("11,string,23")
    public void printCsv(String word) {
        System.out.println("Word = " + word);
    }
}
