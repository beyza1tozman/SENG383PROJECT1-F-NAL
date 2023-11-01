import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class SearchEngineWithTM implements SearchEngine {
    private final PerformanceMetrics performanceMetrics=new PerformanceMetrics();
    TreeMap<String, HashSet<String>> map=new TreeMap<>();
    String outputFilePath="output.txt";

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
                    for(String word:words) {
                        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(!word.isEmpty()) {
                            if (!map.containsKey(word)) {//kelime mapte yoksa eklenicek. ve hashset oluşturulup docname ona eklenip de sete eklenicek
                                HashSet<String> set = new HashSet<>();
                                set.add(docName);
                                map.put(word, set);
                            } else {//kelime mapte varsa sadece doclistine docname eklenicek
                                map.get(word).add(docName);
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

    public void remove(String docName) {
        long startTime = System.nanoTime();
        docName=docName.toLowerCase();
        for(Map.Entry<String,HashSet<String>>mapElement:map.entrySet()) {
            mapElement.getValue().remove(docName);
        }
        System.out.println(docName +" document is successfully removed from our structure.\n");
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
        map.clear();
        System.out.println("Structure is successfully emptied out.\n");
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateClearTime(elapsedTime);
    }

    public void search(String word) {
        long startTime = System.nanoTime();
        if(map.isEmpty()){
            System.out.println("query "+word+"\nStructure is empty.Please load a file before searching!\n");
            return;
        }
        HashSet<String> result = new HashSet<>();
        HashSet<String> notWantedElements=new HashSet<>();
        boolean wordExists=false;
        int existentWordCount=0;
        int notExistentWordCount=0;

        String[]words=word.split(",");
        for(String query:words){
            if(query.charAt(0)=='!'){//istemediğimiz bir şey ise hashsetten çıkarıcaz
                query=query.substring(1);
                for(Map.Entry<String,HashSet<String>> mapElement : map.entrySet()){//kelimeyi hashmapte bulucaz
                    if(mapElement.getKey().equals(query)){//kelime hashmapte varsa eğer
                        //onun docname setini gezip her şeyini result hashsete eklicez
                        notWantedElements.addAll(mapElement.getValue());
                        wordExists=true;
                    }
                }

            }else{//olmasını istediğimiz bir kelime ise
                for(Map.Entry<String,HashSet<String>> mapElement : map.entrySet()){//kelimeyi hashmapte bulucaz
                    if(mapElement.getKey().equals(query)){//kelime hashmapte varsa eğer
                        //onun docname setini gezip her şeyini result hashsete eklicez
                        if(result.isEmpty()){
                            result.addAll(mapElement.getValue());
                        }else{
                            result.retainAll(mapElement.getValue());
                        }
                        wordExists=true;
                        break;

                    }

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
            writer.write("query " + word + "\n" + result);
            writer.newLine();


        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        performanceMetrics.accumulateSearchTime(elapsedTime);
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

}
