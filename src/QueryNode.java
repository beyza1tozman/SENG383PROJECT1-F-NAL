public class QueryNode {
    String wordValue;
    QueryNode nextWord;
    DocsLinkedList docsLinkedList;
    public QueryNode(String word){
        this.wordValue=word;

    }
}
