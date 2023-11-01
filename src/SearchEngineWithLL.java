import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class SearchEngineWithLL implements SearchEngine {
    private final PerformanceMetrics performanceMetrics=new PerformanceMetrics();

    String outputFilePath="output.txt";
    QueryLinkedList queryLinkedList=new QueryLinkedList();

    public void load(String txtFilePath) throws IOException {
        long startTime = System.nanoTime();


        String docName = null;
        boolean isFirstLine = true;
        int lineWithHashTagCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals("###")) {
                    lineWithHashTagCount++;
                    continue;
                }
                if (lineWithHashTagCount == 2) {
                    docName = line;
                    docName=docName.toLowerCase();
                    lineWithHashTagCount = 0;
                    continue;
                }
                if (isFirstLine) {
                    docName = line;
                    docName=docName.toLowerCase();
                    isFirstLine = false;
                } else {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(!word.isEmpty()) {
                            if (!queryLinkedList.queryWordExists(word)) {
                                queryLinkedList.addNewQueryNode(word, docName);
                            } else {
                                QueryNode currentNode = queryLinkedList.get(word);
                                if (!currentNode.docsLinkedList.docsNameExist(docName)) {
                                    currentNode.docsLinkedList.addNewDocNode(docName);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;


        performanceMetrics.accumulateLoadTime(elapsedTime);
    }

    public void remove(String documentName){
        long startTime = System.nanoTime();
        if(queryLinkedList.head==null) return;
        documentName=documentName.toLowerCase();
        QueryNode walk=queryLinkedList.head;
        while(walk!=null){
            walk.docsLinkedList.removeDocNode(documentName);
            walk=walk.nextWord;
        }
        System.out.println(documentName +" document is successfully removed from our structure!\n");

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateRemoveTime(elapsedTime);

    }

    public void clearList(){
        long startTime = System.nanoTime();
        queryLinkedList.makeLinkedListEmpty();

        try {
            FileWriter writer = new FileWriter(outputFilePath);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Structure is successfully emptied out.\n");
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateClearTime(elapsedTime);

    }

    public void search(String query) {
        long startTime = System.nanoTime();
        if (queryLinkedList.head == null) {
            System.out.println("query "+query+"\nStructure is empty.Please load a file before searching!\n");
            return;
        }

        HashSet<String> result = new HashSet<>();
        HashSet<String> notWantedDocs = new HashSet<>();
        String[] words = query.split(",");
        boolean wordExists=false;
        int existentWordCount=0;
        int notExistentWordCount=0;

        for (String word : words) {
            boolean isNotWanted = word.startsWith("!");
            if (isNotWanted) {
                word = word.substring(1);
            }

            QueryNode walk = queryLinkedList.head;
            while (walk != null) {
                if (walk.wordValue.equals(word)) {
                    String[] docs = walk.docsLinkedList.docsLinkedListToString().split(",");
                    if (isNotWanted) {
                        notWantedDocs.addAll(Arrays.asList(docs));
                    } else {
                        if (result.isEmpty()) {
                            result.addAll(Arrays.asList(docs));
                        } else {
                            result.retainAll(Arrays.asList(docs));
                        }
                    }
                    wordExists=true;
                    break;
                }
                walk = walk.nextWord;
            }
            if(wordExists){
                existentWordCount++;
                wordExists=false;
            }else{
                notExistentWordCount++;
            }

        }
        if(notExistentWordCount>0){
            result.clear();
        }
        result.removeAll(notWantedDocs);
        System.out.println("query " + query + "\n" + result + "\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            writer.write("query " + query + "\n" + result);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;


        performanceMetrics.accumulateSearchTime(elapsedTime);
    }

    @Override
    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

}
