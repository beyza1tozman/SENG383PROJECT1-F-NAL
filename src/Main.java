import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        SearchEngineWithLL llEngine = new SearchEngineWithLL();
        SearchEngineWithBST bstEngine = new SearchEngineWithBST();
        SearchEngineWithTM tmEngine=new SearchEngineWithTM();

        SearchEngine[] engines = new SearchEngine[]{llEngine, bstEngine, tmEngine};

        String commandFilePath = "command.txt";
        boolean isFirstLine = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(commandFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] words = line.split(" ");
                words[1] = words[1].replaceAll(";", "");
                if (words[0].equals("load")) {
                    for (SearchEngine engine : engines) {
                        engine.load(words[1]);
                    }
                } else if (words[0].equals("search")) {
                    for (SearchEngine engine : engines) {
                        System.out.println("using " + engine.getClass().getSimpleName());
                        engine.search(words[1]);
                    }
                } else if (words[0].equals("remove")) {
                    String[] docNameArray = Arrays.copyOfRange(words, 1, words.length);
                    String docName = String.join(" ", docNameArray);
                    docName = docName.substring(0, docName.length() - 1);
                    for (SearchEngine engine : engines) {
                        System.out.println("using "+engine.getClass().getSimpleName());
                        engine.remove(docName);

                    }

                } else if (words[0].equals("clear")) {
                    for (SearchEngine engine : engines) {
                        System.out.println("using "+engine.getClass().getSimpleName());
                        engine.clearList();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (SearchEngine engine : engines) {
            PerformanceMetrics metrics = engine.getPerformanceMetrics();
            String engineType = engine.getClass().getSimpleName();

            System.out.println("Performance for " + engineType);
            System.out.println("Load Time: " + metrics.getLoadTime() + " nanoseconds");
            System.out.println("Clear Time: " + metrics.getClearTime() + " nanoseconds");
            System.out.println("Search Time: " + metrics.getSearchTime() + " nanoseconds");
            System.out.println("Remove Time: " + metrics.getRemoveTime() + " nanoseconds");

            System.out.println();

        }


    }
}