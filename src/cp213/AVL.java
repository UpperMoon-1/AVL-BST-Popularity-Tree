package cp213;

/**
 * Implements an AVL (Adelson-Velsky Landis) tree. Extends BST.
 *
 * @author Giuseppe Akbari
 * @author David Brown
 * @version 2024-10-15
 */
public class AVL<T extends Comparable<T>> extends BST<T> {

	/**
	 * Returns the balance data of node. If greater than 1, then left heavy, if less
	 * than -1, then right heavy. If in the range -1 to 1 inclusive, the node is
	 * balanced. Used to determine whether to rotate a node upon insertion.
	 *
	 * @param node The TreeNode to analyze for balance.
	 * @return A balance number.
	 */
	private int balance(final TreeNode<T> node) {

		// your code here

		if (node == null) {
			return 0;
		}

		return nodeHeight(node.getLeft()) - nodeHeight(node.getRight());
	}

	/**
	 * Rebalances the current node if its children are not balanced.
	 *
	 * @param node the node to rebalance
	 * @return replacement for the rebalanced node
	 */
	private TreeNode<T> rebalance(TreeNode<T> node) {

		// your code here
		if (node == null) {
			return null;
		}
		// TreeNode<T> nr;
		if (balance(node) > 1) {// this is left heavy
			if (balance(node.getLeft()) < 0) {
				node.setLeft(rotateLeft(node.getLeft()));
			}
			return rotateRight(node);// rotate to the left because it is left heavy

		}
		if (balance(node) < -1) {// right heavy
			if (balance(node.getRight()) > 0) {
				node.setRight(rotateRight(node.getRight()));
			}
			return rotateLeft(node);// rotate to the right because it is left heavy
		}

		return node;
	}

	/**
	 * Performs a left rotation around node.
	 *
	 * @param node The subtree to rotate.
	 * @return The new root of the subtree.
	 */
	private TreeNode<T> rotateLeft(final TreeNode<T> node) {

		// your code here
		TreeNode<T> RC = node.getRight();
		node.setRight(RC.getLeft());
		RC.setLeft(node);
		node.updateHeight();
		RC.updateHeight();

		return RC;
	}

	/**
	 * Performs a right rotation around node.
	 *
	 * @param node The subtree to rotate.
	 * @return The new root of the subtree.
	 */
	private TreeNode<T> rotateRight(final TreeNode<T> node) {

		// your code here
		TreeNode<T> RC = node.getLeft();
		node.setLeft(RC.getRight());
		RC.setRight(node);
		node.updateHeight();
		RC.updateHeight();

		return RC;
	}

	/**
	 * Auxiliary method for insert. Inserts data into this AVL. Same as BST
	 * insertion with addition of rebalance of nodes.
	 *
	 * @param node The current node (TreeNode).
	 * @param data Data to be inserted into the node.
	 * @return The inserted node.
	 */
	@Override
	protected TreeNode<T> insertAux(TreeNode<T> node, final CountedData<T> data) {

		// your code here
		// TreeNode<T> nr = null;
		node = super.insertAux(node, data);

		return rebalance(node);
	}

	/**
	 * Auxiliary method for valid. Determines if a subtree based on node is a valid
	 * subtree. An AVL must meet the BST validation conditions, and additionally be
	 * balanced in all its subtrees - i.e. the difference in height between any two
	 * children must be no greater than 1.
	 *
	 * @param node The root of the subtree to test for validity.
	 * @return true if the subtree base on node is valid, false otherwise.
	 */
	@Override
	protected boolean isValidAux(final TreeNode<T> node, TreeNode<T> minNode, TreeNode<T> maxNode) {

		// your code here
		boolean boo = false;
		if (node == null) {// node is null so no bother to check and it is valid
			boo = true;
		} else if (node.getRight() == null && node.getLeft() == null) {// both of the kids are null so it is valid
			boo = true;
		} else if (node.getRight() == null || node.getLeft() == null) {// one of the kids is null
			if (node.getRight() != null) {
				if (node.getData().compareTo(node.getRight().getData()) < 0 && Math.abs(this.balance(node)) <= 1) {
					// this.comparisons++;
					boo = this.isValidAux(node.getRight(), minNode, maxNode);
				} else {
					boo = false;
				}
			} else if (node.getLeft() != null) {
				if (node.getData().compareTo(node.getLeft().getData()) > 0 && Math.abs(this.balance(node)) <= 1) {
					// this.comparisons++;
					boo = this.isValidAux(node.getLeft(), minNode, maxNode);
				} else {
					boo = false;
				}
			}

		} else {// none of the kids is null
			boo = this.isValidAux(node.getRight(), null, null) && this.isValidAux(node.getLeft(), minNode, maxNode);
		}
		return boo;
	}

	/**
	 * Determines whether two AVLs are identical.
	 *
	 * @param target The AVL to compare this AVL against.
	 * @return true if this AVL and target contain nodes that match in position,
	 *         data, count, and height, false otherwise.
	 */
	public boolean equals(final AVL<T> target) {
		return super.equals(target);
	}

	/**
	 * Auxiliary method for remove. Removes data from this BST. Same as BST removal
	 * with addition of rebalance of nodes.
	 *
	 * @param node The current node (TreeNode).
	 * @param data Data to be removed from the tree.
	 * @return The replacement node.
	 */
	@Override
	protected TreeNode<T> removeAux(TreeNode<T> node, final CountedData<T> data) {

		// your code here
		if (node == null) {
			node = null;
		} else if (node.getData().compareTo(data) > 0) {// data less than node
			node.setLeft(removeAux(node.getLeft(), data));
			this.rebalance(node);
		} else if (node.getData().compareTo(data) < 0) {
			node.setRight(removeAux(node.getRight(), data));
			this.rebalance(node);
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

	@Override
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

}
