public class MyBinarySearchTree {
    BSTNode root;

    public void insert(String word) {
        root = insertRecursive(root, word);
    }


    public BSTNode insertRecursive(BSTNode root, String word) {
        if (root == null) {
            root = new BSTNode(word);
            return root;
        }

        int comparisonResult = word.compareToIgnoreCase(root.word);//eklenicek kelimenin alfabetik olarak bi öncekine göre ne konumda olduüuna bakıyoruz

        if (comparisonResult < 0) {
            root.left = insertRecursive(root.left, word);
        } else if (comparisonResult > 0) {
            root.right = insertRecursive(root.right, word);
        }
        // duplicatelara izin vermicem yani comparisonResult 0 çıkmışsa hiç bişi yapmıcaz çünkü ztn var demek treemizde
        return root;
    }

    public BSTNode get(String word) {
        BSTNode current = root;

        while (current != null) {
            int comparisonResult = word.compareToIgnoreCase(current.word);

            if (comparisonResult == 0) { // we found the word return
                return current;
            } else if (comparisonResult < 0) {// word is smaller search left subtree
                current = current.left;
            } else { // word is larger search  right subtree
                current = current.right;
            }
        }
        return null;//kelimeyi bulamadık demek
    }

    public void removeDocName(String docName) {
        removeDocNameFromNode(root, docName);
    }

    private void removeDocNameFromNode(BSTNode node, String docName) {
        if (node != null) {

            node.docList.remove(docName);

            removeDocNameFromNode(node.left, docName);
            removeDocNameFromNode(node.right, docName);
        }
    }

    public void removeAll() {
        root = removeAll(root);
    }

    private BSTNode removeAll(BSTNode current) {
        if (current == null) {
            return null;
        }

        current.left = removeAll(current.left);
        current.right = removeAll(current.right);

        current = null;
        return current;
    }

}
