package cp213;

/**
 * Implements a Popularity Tree. Extends BST.
 *
 * @author Giuseppe AKbari
 * @author David Brown
 * @version 2024-10-15
 */
public class PopularityTree<T extends Comparable<T>> extends BST<T> {

	/**
	 * Auxiliary method for valid. May force node rotation if the retrieval count of
	 * the located node data is incremented.
	 *
	 * @param node The node to examine for key.
	 * @param key  The data to search for. Count is updated to count in matching
	 *             node data if key is found.
	 * @return The updated node.
	 */
	private TreeNode<T> retrieveAux(TreeNode<T> node, final CountedData<T> key) {

		// your code here
		if (node == null) {
			return null;
		}
		int result = node.getData().compareTo(key);
		this.comparisons++;
		if (result == 0) {
			// this.comparisons++;
			node.getData().incrementCount();
			if (node.getLeft() != null && node.getLeft().getData().getCount() > node.getData().getCount()) {
				node = rotateRight(node);
			} else if (node.getRight() != null && node.getRight().getData().getCount() > node.getData().getCount()) {
				node = rotateLeft(node);
			}
			return node; // I CHANGED THIS
		} else if (result > 0) {// node data is greater than key
			TreeNode<T> left = retrieveAux(node.getLeft(), key);

			if (left != null) {
				node.setLeft(left);
				if (node.getLeft().getData().getCount() > node.getData().getCount()) {
					return rotateRight(node);
				}
			}
			return node;
		} else {
			TreeNode<T> right = retrieveAux(node.getRight(), key);
			if (right != null) {
				node.setRight(right);
				if (node.getRight().getData().getCount() > node.getData().getCount()) {
					return rotateLeft(node);
				}
			}
			return node;
		}

		// removed this return node;
	}

	/**
	 * Performs a left rotation around node.
	 *
	 * @param parent The subtree to rotate.
	 * @return The new root of the subtree.
	 */
	private TreeNode<T> rotateLeft(final TreeNode<T> parent) {

		// your code here
		TreeNode<T> RC = parent.getRight();
		parent.setRight(RC.getLeft());
		RC.setLeft(parent);
		parent.updateHeight();
		RC.updateHeight();

		return RC;
	}

	/**
	 * Performs a right rotation around {@code node}.
	 *
	 * @param parent The subtree to rotate.
	 * @return The new root of the subtree.
	 */
	private TreeNode<T> rotateRight(final TreeNode<T> parent) {

		// your code here
		TreeNode<T> RC = parent.getLeft();
		parent.setLeft(RC.getRight());
		RC.setRight(parent);
		parent.updateHeight();
		RC.updateHeight();

		return RC;
	}

	/**
	 * Replaces BST insertAux - does not increment count on repeated insertion.
	 * Counts are incremented only on retrieve.
	 */
	@Override
	protected TreeNode<T> insertAux(TreeNode<T> node, final CountedData<T> data) {

		// your code here
		if (node == null) {
			this.size++;
			node = new TreeNode<T>(data);
			return node;
		}
		int result = node.getData().compareTo(data);
		if (result < 0) {// node is less than data
			node.setRight(insertAux(node.getRight(), data));
		} else if (result > 0) {// node greater than
			node.setLeft(insertAux(node.getLeft(), data));
		}
		node.updateHeight();

		return node;
	}

	/**
	 * Auxiliary method for valid. Determines if a subtree based on node is a valid
	 * subtree. An Popularity Tree must meet the BST validation conditions, and
	 * additionally the counts of any node data must be greater than or equal to the
	 * counts of its children.
	 *
	 * @param node The root of the subtree to test for validity.
	 * @return true if the subtree base on node is valid, false otherwise.
	 */
	@Override
	protected boolean isValidAux(final TreeNode<T> node, TreeNode<T> minNode, TreeNode<T> maxNode) {

		// your code here
		boolean boo = true;
		if (node == null) {
			boo = true;
		} else if (node.getLeft() == null && node.getRight() == null) {
			boo = true;
		} else if (node.getLeft() == null || node.getRight() == null) {
			if (node.getRight() != null) {
				if (node.getRight().getData().getCount() < node.getData().getCount()
						&& node.getData().compareTo(node.getRight().getData()) < 0) {
					boo = isValidAux(node.getRight(), null, null);
				} else {
					boo = false;
				}
			}
			if (node.getLeft() != null) {
				if (node.getLeft().getData().getCount() < node.getData().getCount()
						&& node.getData().compareTo(node.getLeft().getData()) > 0) {
					boo = isValidAux(node.getLeft(), null, null);
				} else {
					boo = false;
				}
			}
		} else {
			boo = isValidAux(node.getRight(), null, null) && isValidAux(node.getLeft(), null, null);
		}

		return boo;
	}

	/**
	 * Determines whether two PopularityTrees are identical.
	 *
	 * @param target The PopularityTree to compare this PopularityTree against.
	 * @return true if this PopularityTree and target contain nodes that match in
	 *         position, data, count, and height, false otherwise.
	 */
	public boolean equals(final PopularityTree<T> target) {
		return super.equals(target);
	}

	/**
	 * Very similar to the BST retrieve, but increments the data count here instead
	 * of in the insertion.
	 *
	 * @param key The key to search for.
	 */
	@Override
	public CountedData<T> retrieve(CountedData<T> key) {

		// your code here
		if (key == null || this.root == null) {
			return null;
		}
		TreeNode<T> node = null;
		node = retrieveAux(this.root, key);

		return node != null ? node.getData() : null;
	}

}
