import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Nitya Jani
 * @version 1.0
 * @userid njani8
 * @GTID 903598748
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The list is null/nothing.");
        }
        for (T i : data) {
            if (i == null) {
                throw new IllegalArgumentException("Something in the list is null.");
            } else {
                add(i);
            }
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     * 
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data points to nothing.");
        }
        root = addHelper(root, data);
    }

    /**
     * Adds the data to the tree (helper)
     *
     * @param curr is the current node
     * @param d is the data
     * @return the node to point to
     */
    private AVLNode<T> addHelper(AVLNode<T> curr, T d) {
        if (curr == null) {
            AVLNode<T> newNode = new AVLNode<>(d);
            balance(newNode);
            size = size + 1;
            return newNode;
        } else if (curr.getData().compareTo(d) < 0) {
            curr.setRight(addHelper(curr.getRight(), d));
            balance(curr);
        } else if (curr.getData().compareTo(d) > 0) {
            curr.setLeft(addHelper(curr.getLeft(), d));
            balance(curr);
        }
        if (curr.getBalanceFactor() > 1 || curr.getBalanceFactor() < -1) {
            return balanceHelper(curr);
        } else {
            return curr;
        }
    }

    /**
     * Resets the height and balance factor of all nodes
     *
     * @param curr is the current node
     *
     */
    private void balance(AVLNode<T> curr) {
        curr.setHeight(heightHelper(curr));
        curr.setBalanceFactor(balanceFactorHelper(curr));
    }

    /**
     * Does rotations
     *
     * @param curr is the current node
     * @return the node to point to
     *
     */
    private AVLNode<T> balanceHelper(AVLNode<T> curr) {
        AVLNode<T> toReturn = curr;
        if (curr.getBalanceFactor() == -2) {
            if ((curr.getRight().getBalanceFactor() == -1 || curr.getRight().getBalanceFactor() == 0)) {
                //left
                AVLNode<T> one = curr;
                AVLNode<T> two = one.getRight();
                one.setRight(two.getLeft());
                two.setLeft(one);
                balance(one);
                balance(two);
                toReturn = two;
            } else if (curr.getRight().getBalanceFactor() == 1) {
                //right left
                AVLNode<T> one = curr;
                AVLNode<T> two = one.getRight();
                AVLNode<T> three = two.getLeft();
                two.setLeft(three.getRight());
                three.setRight(two);
                one.setRight(three.getLeft());
                three.setLeft(one);
                balance(one);
                balance(two);
                balance(three);
                toReturn = three;
            }
        } else if (curr.getBalanceFactor() == 2) {
            if ((curr.getLeft().getBalanceFactor() == 1 || curr.getLeft().getBalanceFactor() == 0)) {
                //right
                AVLNode<T> one = curr;
                AVLNode<T> two = one.getLeft();
                one.setLeft(two.getRight());
                two.setRight(one);
                balance(one);
                balance(two);
                toReturn = two;
            } else if (curr.getLeft().getBalanceFactor() == -1) {
                //left right
                AVLNode<T> one = curr;
                AVLNode<T> two = one.getLeft();
                AVLNode<T> three = two.getRight();
                two.setRight(three.getLeft());
                three.setLeft(two);
                one.setLeft(three.getRight());
                three.setRight(one);
                balance(one);
                balance(two);
                balance(three);
                toReturn = three;
            }
        }
        return toReturn;
    }

    /**
     * finds the height
     *
     * @param curr is the current node
     * @return the height
     */
    private int heightHelper(AVLNode<T> curr) {
        if (curr.getLeft() == null && curr.getRight() != null) {
            return Math.max(-1, curr.getRight().getHeight()) + 1;
        } else if (curr.getRight() == null && curr.getLeft() != null) {
            return Math.max(curr.getLeft().getHeight(), -1) + 1;
        } else if (curr.getRight() == null && curr.getLeft() == null) {
            return 0;
        } else {
            return Math.max(curr.getLeft().getHeight(), curr.getRight().getHeight()) + 1;
        }
    }

    /**
     * finds the balance factor
     *
     * @param curr is the current node
     * @return the balance factor
     */
    private int balanceFactorHelper(AVLNode<T> curr) {
        if (curr.getLeft() == null && curr.getRight() != null) {
            return -1 - curr.getRight().getHeight();
        } else if (curr.getRight() == null && curr.getLeft() != null) {
            return curr.getLeft().getHeight() + 1;
        } else if (curr.getRight() == null && curr.getLeft() == null) {
            return 0;
        } else {
            return curr.getLeft().getHeight() - curr.getRight().getHeight();
        }
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data is nothing.");
        }
        AVLNode<T> dummy = new AVLNode<T>(null);
        root = removeHelper(root, data, dummy);
        size = size - 1;
        return dummy.getData();
    }

    /**
     * @return the node to point to
     * @throws java.util.NoSuchElementException if the data is not in the tree
     * @param curr is the current node
     * @param d is the data
     * @param dataToReturn is a dummy node
     */
    private AVLNode<T> removeHelper(AVLNode<T> curr, T d, AVLNode<T> dataToReturn) {
        if (curr == null) {
            throw new NoSuchElementException("The data you're trying to remove isn't in the tree.");
        } else if (curr.getData().compareTo(d) < 0) {
            curr.setRight(removeHelper(curr.getRight(), d, dataToReturn));
            balance(curr);
        } else if (curr.getData().compareTo(d) > 0) {
            curr.setLeft(removeHelper(curr.getLeft(), d, dataToReturn));
            balance(curr);
        } else if (curr.getData().compareTo(d) == 0) {
            if (curr.getRight() == null && curr.getLeft() == null) {
                dataToReturn.setData(curr.getData());
                return null;
            } else if (curr.getRight() != null && curr.getLeft() == null) {
                dataToReturn.setData(curr.getData());
                return curr.getRight();
            } else if (curr.getRight() == null && curr.getLeft() != null) {
                dataToReturn.setData(curr.getData());
                return curr.getLeft();
            } else if (curr.getRight() != null && curr.getLeft() != null) {
                dataToReturn.setData(curr.getData());
                //find predecessor
                AVLNode<T> empty = new AVLNode<T>(null);
                curr.setLeft(findPredecessor(curr.getLeft(), empty));
                curr.setData(empty.getData());
            }
            balance(curr);
        }
        if (curr.getBalanceFactor() > 1 || curr.getBalanceFactor() < -1) {
            return balanceHelper(curr);
        } else {
            return curr;
        }
    }

    /**
     * finds the predecessor by going all the way right
     *
     * @return node (predecessor) connected to all other descendants if necessary
     * @param curr is the current node
     * @param empty is an empty dummy node
     */
    private AVLNode<T> findPredecessor(AVLNode<T> curr, AVLNode<T> empty) {
        if (curr.getRight() == null) {
            empty.setData(curr.getData());
            return curr.getLeft();
        } else {
            curr.setRight(findPredecessor(curr.getRight(), empty));
            balance(curr);
        }
        if (curr.getBalanceFactor() > 1 || curr.getBalanceFactor() < -1) {
            return balanceHelper(curr);
        } else {
            return curr;
        }
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data is nothing.");
        }
        return getHelper(root, data);
    }

    /**
     * Returns the node from the tree with the matching data
     *
     * @param d the data to search for
     * @return the node in the tree with the right data
     * @param curr is the current node
     */
    private T getHelper(AVLNode<T> curr, T d) {
        if (curr == null) {
            throw new NoSuchElementException("That data is not in the tree.");
        } else if (curr.getData().compareTo(d) == 0) {
            return curr.getData();
        } else if (curr.getData().compareTo(d) < 0) {
            return getHelper(curr.getRight(), d);
        } else {
            return getHelper(curr.getLeft(), d);
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data is nothing.");
        }
        return containsHelper(root, data);
    }

    /**
     * Helper Method for contains
     * @param d the data to search for
     * @return true if the parameter is contained within the tree
     * @param curr is the current node
     */
    private boolean containsHelper(AVLNode<T> curr, T d) {
        if (curr == null) {
            return false;
        } else if (curr.getData().compareTo(d) == 0) {
            return true;
        } else if (curr.getData().compareTo(d) < 0) {
            return containsHelper(curr.getRight(), d);
        } else {
            return containsHelper(curr.getLeft(), d);
        }
    }

    /**
     * Returns the height of the root of the tree.
     * 
     * Should be O(1). 
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return root.getHeight();
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with 
     * the deepest depth. 
     * 
     * Should be recursive. 
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    public T maxDeepestNode() {
        if (root == null || size == 0) {
            return null;
        } else {
            AVLNode<T> empty = new AVLNode<>(null);
            maxDeepestNodeHelper(root, empty);
            return empty.getData();
        }
    }

    /**
     * finds the deepest node
     *
     * @param curr is the current node
     * @param storeData is a node that stores the data of the deepest node
     */
    private void maxDeepestNodeHelper(AVLNode<T> curr, AVLNode<T> storeData) {
        if (curr.getLeft() == null && curr.getRight() == null) {
            //this means it's a leaf node!
            storeData.setData(curr.getData());
        } else if (curr.getLeft() != null && curr.getRight() == null) {
            maxDeepestNodeHelper(curr.getLeft(), storeData);
        } else if (curr.getLeft() == null && curr.getRight() != null) {
            maxDeepestNodeHelper(curr.getRight(), storeData);
        } else {
            if (curr.getRight().getHeight() > curr.getLeft().getHeight()) {
                maxDeepestNodeHelper(curr.getRight(), storeData);
            } else if (curr.getLeft().getHeight() > curr.getRight().getHeight()) {
                maxDeepestNodeHelper(curr.getLeft(), storeData);
            } else {
                maxDeepestNodeHelper(curr.getRight(), storeData);
            }
        }
    }

    /**
     * In BSTs, you learned about the concept of the successor: the
     * smallest data that is larger than the current data. However, you only
     * saw it in the context of the 2-child remove case.
     *
     * This method should retrieve (but not remove) the successor of the data
     * passed in. There are 2 cases to consider:
     * 1: The right subtree is non-empty. In this case, the successor is the
     * leftmost node of the right subtree.
     * 2: The right subtree is empty. In this case, the successor is the lowest
     * ancestor of the node whose left subtree contains the data. 
     * 
     * The second case means the successor node will be one of the node(s) we 
     * traversed left from to find data. Since the successor is the SMALLEST element 
     * greater than data, the successor node is the lowest/last node 
     * we traversed left from on the path to the data node.
     *
     * This should NOT be used in the remove method.
     * 
     * Should be recursive. 
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                    76
     *                  /    \
     *                34      90
     *                  \    /
     *                  40  81
     * successor(76) should return 81
     * successor(81) should return 90
     * successor(40) should return 76
     *
     * @param data the data to find the successor of
     * @return the successor of data. If there is no larger data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T successor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data is nothing so a successor can't be found.");
        }
        AVLNode<T> empty = new AVLNode<>(null);
        successorHelper(root, data, empty);
        return empty.getData();
    }

    /**
     * finds the successor
     *
     * @param curr is the current node
     * @param d is the data
     * @param toReturn is a node that stores the data that needs to be returned
     * @throws java.util.NoSuchElementException if the data is not in the tree
     */
    private void successorHelper(AVLNode<T> curr, T d, AVLNode<T> toReturn) {
        if (curr == null) {
            throw new NoSuchElementException("The node is not in the tree.");
        }
        if (curr.getData().compareTo(d) > 0) {
            toReturn.setData(curr.getData());
            successorHelper(curr.getLeft(), d, toReturn);
        } else if (curr.getData().compareTo(d) < 0) {
            successorHelper(curr.getRight(), d, toReturn);
        } else if (curr.getData().compareTo(d) == 0) {
            if (curr.getRight() != null) {
                //smallest value
                AVLNode<T> smallestR = new AVLNode<>(null);
                smallestR.setData(curr.getRight().getData());
                smallestRight(curr.getRight(), smallestR);
                toReturn.setData(smallestR.getData());
            } //else keep toReturn as it is
        } else {
            throw new NoSuchElementException("The node is not in the tree.");
        }
    }

    /**
     * This finds the smallest right in case the right has left children
     *
     * @param curr is the current node
     * @param smallestR is the smallest node on the right
     */
    private void smallestRight(AVLNode<T> curr, AVLNode<T> smallestR) {
        if (curr.getRight() == null && curr.getLeft() == null) {
            smallestR.setData(curr.getData());
            return;
        } else if (curr.getRight() == null && curr.getLeft() != null) {
            smallestRight(curr.getLeft(), smallestR);
        } else if (curr.getRight() != null && curr.getLeft() == null) {
            smallestR.setData(curr.getData());
            return;
        } else {
            smallestRight(curr.getLeft(), smallestR);
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /*public static void main(String[] args) {
        AVL<Integer> tree = new AVL<>();
        tree.add(8);
        tree.add(10);
        tree.add(12);
        tree.add(47);
        tree.add(20);
        tree.add(11);
        tree.add(102);
        tree.add(203);
        tree.add(56);
        tree.add(378);
        tree.add(87);
        System.out.println(tree.successor(20));

    }*/
}
