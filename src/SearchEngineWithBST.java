import java.io.*;
import java.util.HashSet;

public class SearchEngineWithBST implements SearchEngine {
    private final PerformanceMetrics performanceMetrics=new PerformanceMetrics();
    MyBinarySearchTree bst = new MyBinarySearchTree();
    String outputFilePath = "C:/Users/LENOVO/Desktop/output.txt";

    public void load(String filePath) {
        long startTime = System.nanoTime();
        String docName = null;
        boolean isFirstLine = true;
        int lineWithHashTagCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals("###")) {
                    lineWithHashTagCount++;
                    continue;
                }
                if (lineWithHashTagCount == 2) {
                    docName = line.toLowerCase();
                    lineWithHashTagCount = 0;
                    continue;
                }
                if (isFirstLine) {
                    docName = line.toLowerCase();
                    isFirstLine = false;
                } else {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        //word boş deilse binary search treeye ekle
                        if (!word.isEmpty()) {
                            bst.insert(word);
                            BSTNode currnetNode = bst.get(word);
                            currnetNode.docList.add(docName);
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

    public void remove(String docName) {
        long startTime = System.nanoTime();
        docName = docName.toLowerCase();

        bst.removeDocName(docName);

        System.out.println(docName +" document is successfully removed from our structure!\n");
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateRemoveTime(elapsedTime);


    }
    public void clearList() {
        long startTime = System.nanoTime();

        try {
            FileWriter writer = new FileWriter(outputFilePath);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bst.removeAll();
        System.out.println("Structure is successfully emptied out.\n");
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateClearTime(elapsedTime);
    }


    public void search(String word) {
        long startTime = System.nanoTime();
        if(bst.root==null){
            System.out.println("query "+word+"\nStructure is empty.Please load a file before searching!\n");
            return;
        }
        HashSet<String> result = new HashSet<>();
        HashSet<String> notWantedElements=new HashSet<>();
        boolean wordExists=false;
        int existentWordCount=0;
        int notExistentWordCount=0;


        String[] words = word.split(",");
        for (String query : words) {
            if (query.charAt(0) == '!') {
                query = query.substring(1);
                BSTNode currentNode = bst.get(query);
                if (currentNode != null) {
                    notWantedElements.addAll(currentNode.docList);
                    wordExists=true;
                }
            } else {//istediğimiz kelime ise. result empty ise ad all dicez. result empty deilse retainall dicez
                BSTNode currentNode = bst.get(query);
                if (currentNode != null) {
                    if(result.isEmpty()){
                        result.addAll(currentNode.docList);
                    }else{
                        result.retainAll(currentNode.docList);
                    }
                    wordExists=true;

                }
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

        result.removeAll(notWantedElements);

        System.out.println("query " + word + "\n" + result+"\n");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath,true))) {
            //when we say append true. it opens the file in append mode and everything we add will be in end
            writer.write("query " + word + "\n" + result);
            writer.newLine();


        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        //double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000; dont do it like this because calculations will not be so long that it reaches a second

        performanceMetrics.accumulateSearchTime(elapsedTime);
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

}



