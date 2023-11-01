public class QueryLinkedList {
    QueryNode head;

    public void addNewQueryNode(String word, String docName) {
        QueryNode newQueryNode = new QueryNode(word);
        DocsLinkedList newDocsLinkedList = new DocsLinkedList();
        newDocsLinkedList.addNewDocNode(docName);
        newQueryNode.docsLinkedList = newDocsLinkedList;

        if (this.head == null) {
            this.head = newQueryNode;
        } else if (word.compareTo(head.wordValue) < 0) {
            newQueryNode.nextWord = head;
            this.head = newQueryNode;
        } else {
            QueryNode current = head;
            QueryNode prev = null;
            while (current != null && word.compareTo(current.wordValue) > 0) {
                prev = current;
                current = current.nextWord;
            }
            if (prev != null) {
                prev.nextWord = newQueryNode;
            }
            newQueryNode.nextWord = current;
        }
    }

    public boolean queryWordExists(String query){
        if(head==null) return false;
        QueryNode walk=head;
        while(walk!=null){
            if(walk.wordValue.equals(query)){
                return true;
            }
            walk=walk.nextWord;
        }
        return false;
    }
    public void makeLinkedListEmpty() {
        while (head != null) {
            QueryNode temp = head;
            head = head.nextWord;
            temp.nextWord = null;
        }
    }
    public QueryNode get(String word){
        if(head==null) return null;
        QueryNode walk=head;
        while(!walk.wordValue.equals(word)){
            walk=walk.nextWord;
        }
        return walk;

    }

}
