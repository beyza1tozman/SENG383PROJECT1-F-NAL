import java.util.HashSet;

public class BSTNode {
    String word;
    BSTNode left,right;
    HashSet<String> docList;
    public BSTNode(String word){
        this.word=word;
        docList=new HashSet<>();
        left=right=null;
    }

}
