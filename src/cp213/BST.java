package cp213;

import java.util.ArrayList;

/**
 * Implements a Binary Search Tree.
 *
 * @author Giuseppe Akbari
 * @author David Brown
 * @version 2024-10-15
 */
public class BST<T extends Comparable<T>> {

	// Attributes.
	/**
	 * Count of comparisons performed by tree.
	 */
	protected int comparisons = 0;
	/**
	 * Root node of the tree.
	 */
	protected TreeNode<T> root = null;
	/**
	 * Number of nodes in the tree.
	 */
	protected int size = 0;

	/**
	 * Auxiliary method for {@code equals}. Determines whether two subtrees are
	 * identical in datas and height.
	 *
	 * @param source Node of this BST.
	 * @param target Node of that BST.
	 * @return true if source and target are identical in datas and height.
	 */
	protected boolean equalsAux(final TreeNode<T> source, final TreeNode<T> target) {

		// your code here
		if (source.getHeight() == target.getHeight()) {
			if (source.inOrder().equals(target.inOrder())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Auxiliary method for insert. Inserts data into this BST.
	 *
	 * @param node The current node (TreeNode).
	 * @param data Data to be inserted into the tree.
	 * @return The inserted node.
	 */
	protected TreeNode<T> insertAux(TreeNode<T> node, final CountedData<T> data) {

		if (node == null) {
			// Base case - add a new node containing the data.
			node = new TreeNode<T>(data);
			node.getData().incrementCount();
			this.size++;
		} else {
			// Compare the node data against the insert data.
			final int result = node.getData().compareTo(data);

			if (result > 0) {
				// General case - check the left subtree.
				node.setLeft(this.insertAux(node.getLeft(), data));
			} else if (result < 0) {
				// General case - check the right subtree.
				node.setRight(this.insertAux(node.getRight(), data));
			} else {
				// Base case - data is already in the tree, increment its count
				node.getData().incrementCount();
			}
		}
		node.updateHeight();
		return node;
	}

	/**
	 * Auxiliary method for valid. Determines if a subtree based on node is a valid
	 * subtree.
	 *
	 * @param node    The root of the subtree to test for validity.
	 * @param minNode The node of the minimum data in the current subtree.
	 * @param maxNode The node of the maximum data in the current subtree.
	 * @return true if the subtree base on node is valid, false otherwise.
	 */
	protected boolean isValidAux(final TreeNode<T> node, TreeNode<T> minNode, TreeNode<T> maxNode) {
		// your code here
		boolean boo = false;
		if (node == null) {
			boo = true;
		} else if (node.getRight() == null && node.getRight() == null) {// both childs are null so it has to be true
			boo = true;
		} else if (node.getRight() == null || node.getRight() == null) {// one of the right or left is null
			if (node.getRight() == null) {// check if the right is null then left is not null
				if (node.getLeft().getData().compareTo(node.getData()) < 0) {// if the left node of the tree is less
																				// than the parent
					// this.comparisons++;
					boo = this.isValidAux(node.getLeft(), null, null);// call the function again to see if node left
																		// kids are
																		// valid

				} else {// if the left node is greater than node than it is not valid
					boo = false;
				}
			} else {// then left is null and right is not null bc i used the or so one of them has
					// to be not null
				if (node.getRight().getData().compareTo(node.getData()) > 0) {// the right node has to be more than
																				// the// node
					// this.comparisons++;
					boo = this.isValidAux(node.getRight(), null, null);// recall the function to check for the kids of
																		// the
																		// right
				} else {
					boo = false;// if the right node is less than the node
				}
			}

		} else {// height of the kids are not null so we will call the function and pass in two
				// kids
			boo = this.isValidAux(node.getLeft(), null, null) && isValidAux(node.getRight(), null, null);

		}
		return boo;
	}

	/**
	 * Returns the height of a given TreeNode. Required for when TreeNode is null.
	 *
	 * @param node The TreeNode to determine the height of.
	 * @return The height attribute of node, 0 if node is null.
	 */
	protected int nodeHeight(final TreeNode<T> node) {
		return node != null ? node.getHeight() : 0;
	}

	/**
	 * Auxiliary method for remove. Removes data from this BST.
	 *
	 * @param node The current node (TreeNode).
	 * @param data Data to be removed from the tree.
	 * @return The replacement node.
	 */
	protected TreeNode<T> removeAux(TreeNode<T> node, final CountedData<T> data) {

		// your code here
		if (node == null) {

			node = null;
		} else if (node.getData().compareTo(data) > 0) {// data is less than node
			// this.comparisons++;
			node.setLeft(removeAux(node.getLeft(), data));
		} else if (node.getData().compareTo(data) < 0) {
			// this.comparisons++;
			node.setRight(removeAux(node.getRight(), data));

		} else {
			if (node.getData().getCount() > 1) {
				node.getData().decrementCount();
			} else if (node.getLeft() == null && node.getRight() == null) {
				node = null;
			} else if (node.getLeft() == null) {
				node = node.getRight();
			} else if (node.getRight() == null) {
				node = node.getLeft();
			} else {// node has two kids
				TreeNode<T> rep = null;
				if (node.getLeft().getRight() == null) {
					rep = node.getLeft();
				} else {
					rep = deleteleft(node.getLeft());
				}
				rep.setRight(node.getRight());
				node = rep;
			}
		}
		if (node != null) {
			node.updateHeight();
		}

		return node;
	}

	protected TreeNode<T> deleteleft(TreeNode<T> node) {
		TreeNode<T> child = node.getRight();
		TreeNode<T> rep = null;
		if (child.getRight() == null) {
			rep = child;
			node.setRight(child.getLeft());
		} else {
			rep = deleteleft(child);
			node.updateHeight();
		}
		return rep;
	}

	/**
	 * Determines if this BST contains key.
	 *
	 * @param key The key to search for.
	 * @return true if this contains key, false otherwise.
	 */
	public boolean contains(final CountedData<T> key) {
		return this.retrieve(key) != null;
	}

	/**
	 * Determines whether two trees are identical.
	 *
	 * @param target The tree to compare this BST against.
	 * @return true if this and target contain nodes that match in position, data,
	 *         count, and height, false otherwise.
	 */
	public boolean equals(final BST<T> target) {
		boolean isEqual = false;

		if (this.size == target.size) {
			isEqual = this.equalsAux(this.root, target.root);
		}
		return isEqual;
	}

	/**
	 * Get number of comparisons executed by the retrieve method.
	 *
	 * @return comparisons
	 */
	public int getComparisons() {
		return this.comparisons;
	}

	/**
	 * Returns the height of the root node of this tree.
	 *
	 * @return height of root node, 0 if the root node is null.
	 */
	public int getHeight() {
		return this.root != null ? this.root.getHeight() : 0;
	}

	/**
	 * Returns the number of nodes in the tree.
	 *
	 * @return number of nodes in this tree.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Returns a list of the data in the current tree. The list contents are in
	 * order from smallest to largest.
	 *
	 * Not thread safe as it assumes contents of the tree are not changed by an
	 * external thread during the loop.
	 *
	 * @return The contents of this tree as a list of data.
	 */
	public ArrayList<CountedData<T>> inOrder() {
		return this.root.inOrder();
	}

	/**
	 * Inserts data into this tree.
	 *
	 * @param data Data to store.
	 */
	public void insert(final CountedData<T> data) {
		this.root = this.insertAux(this.root, data);
		return;
	}

	/**
	 * Determines if this tree is empty.
	 *
	 * @return true if this tree is empty, false otherwise.
	 */
	public boolean isEmpty() {

		// your code here

		return this.root == null;
	}

	/**
	 * Determines if this tree is a valid BST; i.e. a node's left child data is
	 * smaller than its data, and its right child data is greater than its data, and
	 * a node's height is equal to the maximum of the heights of its two children
	 * (empty child nodes have a height of 0), plus 1.
	 *
	 * @return true if this tree is a valid BST, false otherwise.
	 */
	public boolean isValid() {
		return this.isValidAux(this.root, null, null);
	}

	/**
	 * Returns a list of the data in the current tree. The list contents are in node
	 * level order starting from the root node. Helps determine the structure of the
	 * tree.
	 *
	 * Not thread safe as it assumes contents of the tree are not changed by an
	 * external thread during the loop.
	 *
	 * @return this tree data as a list of data.
	 */
	public ArrayList<CountedData<T>> levelOrder() {
		return this.root.levelOrder();
	}

	/**
	 * Returns a list of the data in the current tree. The list contents are in node
	 * preorder.
	 *
	 * Not thread safe as it assumes contents of the tree are not changed by an
	 * external thread during the loop.
	 *
	 * @return The contents of this tree as a list of data.
	 */
	public ArrayList<CountedData<T>> preOrder() {
		return this.root.preOrder();
	}

	/**
	 * Removes data from the tree. Decrements the node count, and if the count is 0,
	 * removes the node entirely.
	 *
	 * @param data Data to decrement or remove.
	 */
	public void remove(final CountedData<T> data) {
		this.root = this.removeAux(this.root, data);
		return;
	}

	/**
	 * Resets the comparison count to 0.
	 */
	public void resetComparisons() {
		this.comparisons = 0;
		return;
	}

	/**
	 * Retrieves a copy of data matching key (key should have data count of 0).
	 * Returning a complete CountedData gives access to the data and its count.
	 *
	 * @param key The key to look for.
	 * @return data The complete CountedData that matches key, null otherwise.
	 */
	public CountedData<T> retrieve(final CountedData<T> key) {// when the compare to is less than 0 then the target
																// inside
		// the compare to is greater than "this"

		// your code here
		TreeNode<T> current = this.root;
		while (current != null) {
			this.comparisons++;
			if (current.getData().compareTo(key) == 0) {
				// this.comparisons++;
				return current.getData();
			} else {
				// this.comparisons++;
				if (current.getData().compareTo(key) > 0) {// current is greater than key
					current = current.getRight();
				} else {
					current = current.getLeft();
				}
			}
		}

		return null;
	}
}
