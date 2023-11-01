public class DocsLinkedList {
    DocsNode head;
    int length;

    public void addNewDocNode(String docName){
        docName=docName.toLowerCase();
        DocsNode newDocNode=new DocsNode(docName);
        if(head==null) {
            head= newDocNode;
            length++;
            return;
        }
        DocsNode walk=head;
        while(walk.nextDoc!=null){
            walk=walk.nextDoc;
        }
        walk.nextDoc=newDocNode;
        length++;
    }
    public void removeDocNode(String docName){//linked listteki her kelime için bu method çağırılanacak ve eğer doclinked listte varsa bu kelimeyi çıkaracağız. bu method doc linkedlisti gezmek ve o stringe eşit olan kelime varsa doclinked listten çıkarmak için yani
        if(head==null) return;
        docName=docName.toLowerCase();
        DocsNode walk=head;
        DocsNode prev=head;
        int index=0;
        while(walk!=null){
            if(walk.docName.equals(docName)&&index==0){
                walk=walk.nextDoc;
                head.nextDoc=null;
                head=walk;
                return;
            }else if(walk.docName.equals(docName)&&index==length-1){
                prev.nextDoc=null;
            }else if(walk.docName.equals(docName)){
                prev.nextDoc=walk.nextDoc;
                walk.nextDoc=null;
                return;
            }
            prev=walk;
            walk=walk.nextDoc;
            index++;
        }
    }
    public String docsLinkedListToString(){
        DocsNode walk=head;
        if(walk==null){
            return null;
        }
        String docsString="";
        while(walk!=null){
            if(walk.nextDoc==null){//next null ise virgülsüz ekle
                docsString+=walk.docName;
            }else{
                docsString+=walk.docName+',';
            }
            walk=walk.nextDoc;
        }
        return docsString;
    }
    public boolean docsNameExist(String docName){
        docName=docName.toLowerCase();
        if (head==null) return false;
        DocsNode walk=head;
        while(walk!=null){
            if(walk.docName.equals(docName)){
                return true;
            }
            walk=walk.nextDoc;

        }
        return false;
    }

}
