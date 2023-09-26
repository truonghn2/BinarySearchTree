/*
 * Project 6
 * 
 * Heather Truong
 * 6/26/23
 * CMSC 256 Section: C01
 */

package cmsc256;

public class BinarySearchTree <E extends Comparable<? super E>> {
    //instance variables
    private int size;
    private BinaryNode<E> root;

    //default constructor
    public BinarySearchTree() {
        size = 0;
        root = null;
    }

    //addToParent Helper Method
    //a private recursive helper method that adds a new node to a specific parent node
    //The method will return false if the value is equal to an element already present in the tree based on natural ordering (using the compareTo method of the value)
    private boolean addToParent(BinaryNode<E> parentNode, BinaryNode<E> addNode) {
        //integer variable that will hold the result of comparing the parent's node value to the child node's value
        int compare = addNode.value.compareTo(parentNode.value);
        //boolean to represent whether the node was added
        boolean wasAdded = false;

        if (compare < 0) { //the addNode value is less than the parentNode value
            //if parent has no left node, add new node as left
            if (parentNode.left == null) {
                parentNode.left = addNode;
                wasAdded = true;
            } else {
                //otherwise, add to parentNode's left (recursive)
                wasAdded = addToParent(parentNode.left, addNode);
            }
        }
        //similar statement to add to the right node of the parent if the value of the added node's value is greater
        else if (compare > 0) { //the addNode value is greater than the parentNode value
            //if parent has no right node, add new node as right
            if (parentNode.right == null) {
                parentNode.right = addNode;
                wasAdded = true;
            } else {
                //otherwise, add to parentNode's right (recursive)
                wasAdded = addToParent(parentNode.right, addNode);
            }
        }
        return wasAdded;
    }

    //add Method
    public boolean add(E inValue) {
        //node to hold the new element and set up the boolean return value
        BinaryNode<E> node = new BinaryNode<>(inValue);
        boolean wasAdded = true;
        //if the tree is empty (if root node is null) then set root to the new node
        if(root == null) {
            root = node;
        }
        //otherwise, add the value to the tree using the root as the parent
        else {
            wasAdded = addToParent(root, node);
        }
        //if element was successfully added to the tree, then increment the size of the tree by 1 and return whether the element was added
        if(wasAdded) {
            size++;
        }
        return wasAdded;
    }

    //remove Method
    public boolean remove(E removeValue) {
        //if the tree is empty(the root is null), then return false
        if (root == null) {
            return false;
        }
        //if the node to remove is the root, then set a new root node
        //if the left child is null then make the root's right child the root
        //if the right child is null then make the root's left child the root
        if (removeValue.compareTo(root.value) == 0) {
            if (root.left == null) {
                root = root.right;
            }
            else if (root.right == null) {
                root = root.left;
            }
            //otherwise (if neither the left child nor the right child are null), set the left node to the root and then add the right node to the left node using the addToParent method
            //the addToParent method will add to the most appropriate spot in the tree
            else {
                BinaryNode<E> formerRight = root.right;
                root = root.left;
                addToParent(root, formerRight);
            }
            size--;
            return true;
        }
        //if the node to remove is not the root, then another recusive method must be invoked to find the element in the tree and remove it
        return removeSubNode(root, removeValue);
    }

    //removeSubNode method
    //will remove a node that was not determined to be the root, or work its way down the branches of the tree to remove the appropriate node
    private boolean removeSubNode(BinaryNode<E> parent, E removeValue) {
        //integer variable that will hold the result of comparing the parent's node value to the child node's value
        int compareParent = removeValue.compareTo(parent.value);
        //determine if the value to be removed is on the right side or left of the parent
        BinaryNode<E> subTree = null;
        if (compareParent > 0) {
            subTree = parent.right;
        }
        else {
            subTree = parent.left;
        }
        //if branch is null then the value doesnt exist in the tree
        if (subTree == null) {
            return false;
        }

        //if the value is equal to the value in the current branch
        //selected node can be deleted
        if (subTree.value.compareTo(removeValue) == 0) {
            BinaryNode<E> replacement;
            if (subTree.left == null) {
                replacement = subTree.right;
            }
            else if (subTree.right == null) {
                replacement = subTree.left;
            }
            else {
                BinaryNode<E> formerRight = subTree.right;
                replacement = subTree.left;
                addToParent(replacement, formerRight);
            }
            if (compareParent > 0) {
                parent.right = replacement;
            }
            else {
                parent.left = replacement;
            }
            size--;
            return true;
        }
        //if the value could still exist in the tree but is not in the current branch, then invoke the method recursively with the branch node
        return removeSubNode(subTree, removeValue);
    }

    //getRoot method
    public BinaryNode<E> getRoot() {
        return root;
    }

    //size() method
    public int size() {
        return size;
    }

    //clear() method
    public void clear() {
        root = null;
        size = 0;
    }

    // findLargest method
    /** Returns the node containing the largest entry in the tree */
    public BinaryNode<E> findLargest() {
        if (root == null) {
            return null;
        }

        BinaryNode<E> current = root;
        while (current.right != null) {
            current = current.right;
        }

        return current;
    }

    // removeLargest method
    /** Removes and returns the node containing the largest entry in the tree */
    public BinaryNode<E> removeLargest() {
        if (root == null) {
            return null;
        }

        BinaryNode<E> parent = null;
        BinaryNode<E> current = root;
        while (current.right != null) {
            parent = current;
            current = current.right;
        }

        if (parent == null) {
            root = current.left;
        } else {
            parent.right = current.left;
        }

        size--;
        return current;
    }

    // getHeight method
    /** Return the height of this binary tree */
    public int getHeight() {
        return getHeight(root);
    }

    //private recursive helper method to calculate the height of a node
    private int getHeight(BinaryNode<E> node) {
        if (node == null) {
            return -1;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    // isFullBinaryTree method
    /** Returns true if the tree is a full binary tree */
    public boolean isFullBinaryTree() {
        return isFullBinaryTree(root);
    }

    //private recursive helper method to check if a node and its subtrees form a full binary tree
    private boolean isFullBinaryTree(BinaryNode<E> node) {
        if (node == null) {
            return false;
        }

        if ((node.left == null && node.right == null)) {
            return true;
        }

        if (node.left != null && node.right != null) {

        //recursively check if the left and right subtrees are full binary trees
        return isFullBinaryTree(node.left) && isFullBinaryTree(node.right);
        }

        //if one child is null and the other is not, it is not a full binary tree
        return false;
    }

    // getNumberOfLeaves method
    /** Return the number of leaf nodes */
    public int getNumberOfLeaves() {
        return getNumberOfLeaves(root);
    }

    //private recursive helper method to calculate the number of leaf nodes in a tree
    private int getNumberOfLeaves(BinaryNode<E> node) {
        if (node == null) {
            return 0;
        }

        if (node.left == null && node.right == null) {
            // The current node is a leaf node
            return 1;
        }

        //recursively calculate the number of leaf nodes in the left and right subtrees
        int leftLeaves = getNumberOfLeaves(node.left);
        int rightLeaves = getNumberOfLeaves(node.right);

        return leftLeaves + rightLeaves;
    }

    // getNumberOfInternalNodes method
    /** Return the number of internal nodes */
    public int getNumberOfInternalNodes() {
        return getNumberOfInternalNodes(root);
    }

    //private recursive helper method to calculate the number of internal nodes in a tree
    private int getNumberOfInternalNodes(BinaryNode<E> node) {
        if (node == null || (node.left == null && node.right == null)) {
            // Base case: If the node is null or a leaf node, it is not an internal node
            return 0;
        }

        //recursively calculate the number of internal nodes in the left and right subtrees
        int leftInternalNodes = getNumberOfInternalNodes(node.left);
        int rightInternalNodes = getNumberOfInternalNodes(node.right);

        //count the current node as an internal node
        return leftInternalNodes + rightInternalNodes + 1;
    }

        public class BinaryNode<E> {
            //instance variables. declared protected so the outer class can access them without getters and setters
            protected E value;
            protected BinaryNode<E> right;
            protected BinaryNode<E> left;

            //constructor that will store that node's data
            public BinaryNode(E valueIn) {
                value = valueIn;
            }

            //getter for the value held inside the node for external classes to use
            public E getValue() {
                return value;
            }
        }
}
