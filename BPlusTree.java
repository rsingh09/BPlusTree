
import java.util.*;

public class BPlusTree {

	/**
	 * Degree : represents the number of children a node can have rootNode: hold the
	 * starting key, this can be either a leaf node or index node minKeys: this
	 * represent the minimum number of keys that have to be present in a node except
	 * root node
	 */
	public static int degree;
	public Node rootNode;
	public static int minKeys;

	public BPlusTree(int degree) {
		System.out.println(degree);
		this.degree = degree - 1;
		int i = Math.round(degree / 2f);
		minKeys = (i - 1);
		rootNode = null;
	}

	/**
	 * Function to insert a key, value pair in the tree passes the Key and its value
	 * 
	 */
	public void insertInTree(Integer key, double value) {
		// if a duplicate value is inserted return
		if (searchInTree(key) != null) {
			System.out.println("Duplicates Not allowed");
			return;
		}
		// Initially create a leaf node
		LeafNode newLeaf = new LeafNode(key, value);
		LeafkeyValPair newParent = new LeafkeyValPair(key, newLeaf);
		// if root node is null then set the root node as leaf node
		if (rootNode == null || rootNode.nodeSize() == 0) {
			rootNode = newParent.getTreeNode();
			return;
		}
		// try inserting in the leaf node, if a new root is created(in case of an
		// overflow ) return root node as
		LeafkeyValPair childcurrentKeyVal = getChild(rootNode, newParent, null);
		if (childcurrentKeyVal == null)
			return;
		else {
			InternalNode newRoot = new InternalNode(childcurrentKeyVal.getKey(), rootNode, childcurrentKeyVal.getTreeNode());
			rootNode = newRoot;
			return;
		}
	}

	/**
	 * Function to search a key Search for the exact key present in the leaf node
	 * and return the value as string
	 */

	public String searchInTree(Integer key) {
		// Check whether tree is empty or key exists
		if (key == null || rootNode == null) {
			return null;
		}
		// Find the leaf node the key points to
		LeafNode leaf = (LeafNode) parseTree(rootNode, key);

		// Find the value in the leaf
		for (int i = 0; i < leaf.nodeSize(); i++) {
			if (key.compareTo(leaf.keys.get(i)) == 0) {
				return Double.toString(leaf.values.get(i));
			}
		}
		return null;
	}

	/**
	 * Function to search for a range Search for all the values lying between the
	 * given keys Note: this will return values even if the exact keys are not
	 * present
	 */
	public String searchInTree(Integer key1, Integer key2) {
		// Return a key or whether empty tree
		String str = "";
		if (key1 == null || key2 == null || rootNode == null) {
			return str;
		}
		// Find leaf node key is pointing to
		LeafNode leaf = (LeafNode) parseTree(rootNode, key1);
		int j = 0;
		// get the maximum value present in the leaf
		// Note: this is to ensure that if in a node there are keys present which are
		// lesser then the given key
		int maxleafValue = leaf.keys.get(leaf.keys.size() - 1);
		while (maxleafValue >= key1 && leaf.nextLeaf != null) {
			for (int i = 0; i < leaf.nodeSize(); i++) {
				if (key1.compareTo(leaf.keys.get(i)) <= 0) {
					// while the elements are lesser then the key 2
					while (i < leaf.nodeSize() && key2 >= leaf.keys.get(i)) {
						if (leaf.values.size() > 0) {
							str = str + leaf.values.get(i) + ", ";
						}
						i++;
						if (i == leaf.nodeSize() && key2.compareTo(leaf.keys.get(i - 1)) > 0) {
							i = 0;
							leaf = leaf.nextLeaf;
						}
					}
				}
			}
			if (j == leaf.nodeSize() - 1) {
				leaf = leaf.nextLeaf;
				j = 0;
			} else {
				j++;
			}
			// if(leaf == null)
			// Calculate max value for the next leaf
			// maxleafValue = leaf.keys.get(leaf.keys.size() - 1);
		}
		if (str != "") {
			str = str.substring(0, str.length() - 2);
		}
		return str;
	}

	/*
	 * Parse the tree to get the leaf node associated with the given key takes the
	 * parent code and key to search returns child node associate with the given key
	 */
	private Node parseTree(Node node, Integer key) {
		if (node.isLeafNode) {
			return node;
		}
		// For index node
		else {
			InternalNode index = (InternalNode) node;
			int minKey = index.keys.get(0);
			int maxKey = index.keys.get(node.nodeSize() - 1);
			// if the key is lesser then the minimum element go left
			if (key.compareTo(minKey) < 0) {
				return parseTree((Node) index.children.get(0), key);
			}
			// if the key is greater then the minimum element go right
			else if (key.compareTo(maxKey) >= 0) {
				return parseTree((Node) index.children.get(index.children.size() - 1), key);
			} else {
				// Else find the index where the key might exist
				for (int i = 0; i < index.nodeSize() - 1; i++) {
					int leftkey = index.keys.get(i);
					int rightKey = index.keys.get(i + 1);
					if (key.compareTo(leftkey) >= 0 && key.compareTo(rightKey) < 0) {
						return parseTree((Node) index.children.get(i + 1), key);
					}
				}
			}
			return null;
		}
	}

	/*
	 * Recursively parse through the tree to the insert the key value pair in the tree
	 */
	private LeafkeyValPair getChild(Node node, LeafkeyValPair currentKeyVal, LeafkeyValPair newChild) {
		if (!node.isLeafNode) {
			//
			InternalNode index = (InternalNode) node;
			int i = 0;
			while (i < index.nodeSize()) {
				if (currentKeyVal.getKey().compareTo(index.keys.get(i)) < 0) {
					break;
				}
				i++;
			}
			// Insertion in the tree of the new value pair created, by calling this function recursively till the leaf node is reached
			newChild = getChild((Node) index.children.get(i), currentKeyVal, newChild);

			// if inserting new key did not result in a new node
			if (newChild == null) {
				return null;
			}
			// if the node sanity is broken and leaf is split a new internal is node is created
			else {
				int j = 0;
				while (j < index.nodeSize()) {
					if (newChild.getKey().compareTo(index.keys.get(j)) < 0) {
						break;
					}
					j++;
				}
				index.InsertinNode(newChild, j);
				// if the sanity of internal node is not broken then return null
				if (index.nodeSanity() == 0) {
					return null;
				} else {
					newChild = splitInternalNode(index);

					// rootNode was just split
					if (index == rootNode) {
						// Create new node and make tree's rootNode-node pointer point to newRoot
						InternalNode newRoot = new InternalNode(newChild.getKey(), rootNode, newChild.getTreeNode());
						rootNode = newRoot;
						return null;
					}
					return newChild;
				}
			}
		}
		// if you have reached leaf then insert in the leaf
		else {
			LeafNode leaf = (LeafNode) node;
			LeafNode newLeaf = (LeafNode) currentKeyVal.getTreeNode();

			leaf.InsertInLeaf(currentKeyVal.getKey(), newLeaf.values.get(0));

			// if the leaf sanity is not broken after insertion of the new value
			if (leaf.nodeSanity() == 0 || leaf != rootNode) {
				return null;
			}
			// Once in a while, the leaf is full
			else {
				newChild = splitLeafNode(leaf);
				if (leaf == rootNode) {
					InternalNode newRoot = new InternalNode(newChild.getKey(), leaf, newChild.getTreeNode());
					rootNode = newRoot;
					return null;
				}
				return newChild;
			}
		}
	}

	private LeafkeyValPair splitLeafNode(LeafNode leaf) {
		ArrayList<Integer> new_keys = new ArrayList<Integer>();
		ArrayList<Double> newValues = new ArrayList<Double>();

		// Rest of the degree entries are moved to a new node
		for (int i = degree / 2; i <= degree; i++) {
			new_keys.add(leaf.keys.get(i));
			newValues.add(leaf.values.get(i));
		}

		// Initial degree entries are kept
		for (int i = degree / 2; i <= degree; i++) {
			leaf.keys.remove(leaf.nodeSize() - 1);
			leaf.values.remove(leaf.values.size() - 1);
		}

		Integer splitKey = new_keys.get(0);
		LeafNode rightNode = new LeafNode(new_keys, newValues);

		// Sibling pointers are set
		LeafNode tmp = leaf.nextLeaf;
		leaf.nextLeaf = rightNode;
		leaf.nextLeaf.prevLeaf = rightNode;
		rightNode.nextLeaf = tmp;
		rightNode.prevLeaf = leaf;

		LeafkeyValPair newChildCreated = new LeafkeyValPair(splitKey, rightNode);

		return newChildCreated;
	}

	private LeafkeyValPair splitInternalNode(InternalNode index) {
		ArrayList<Integer> newKeys = new ArrayList<Integer>();
		ArrayList<Node> newChildren = new ArrayList<Node>();
		index.keys.remove(degree / 2);
		Integer splitKey = index.keys.get(degree / 2);

		newChildren.add(index.children.get(degree / 2 + 1));

		index.children.remove(degree / 2 + 1);

		while (index.nodeSize() > degree / 2) {
			newKeys.add(index.keys.get(degree / 2));
			index.keys.remove(degree / 2);
			newChildren.add(index.children.get(degree / 2 + 1));
			index.children.remove(degree / 2 + 1);
		}

		InternalNode rightNode = new InternalNode(newKeys, newChildren);
		LeafkeyValPair child = new LeafkeyValPair(splitKey, rightNode);
		return child;
	}

	public void deleteNode(int key) {
		// if the tree is empty or the key is not present in te tree return
		if (rootNode == null || rootNode.nodeSize() == 0 || searchInTree(key) == null)
			return;

		int parentSplitKeyIndex = deleteNode(key, rootNode, null, -99);
		if (parentSplitKeyIndex != -99) {
			rootNode.keys.remove(parentSplitKeyIndex);
			if (rootNode.keys.isEmpty()) {
				rootNode = ((InternalNode) rootNode).children.get(0);
			}
		}

		// if the root node is empty, you have deleted all the elements
		if (rootNode.keys.isEmpty()) {
			rootNode = null;
		}

	}

	private int deleteNode(Integer key, Node child, InternalNode parent, int parentSplitKeyIndex) {
		if (parent != null) {
			child.setParent(parent);
		}

		// If child is a leaf, delete the key value pair from it
		if (child.isLeafNode) {
			LeafNode leaf = (LeafNode) child;
			for (int i = 0; i < leaf.keys.size(); i++) {
				if (leaf.keys.get(i).compareTo(key) == 0) {
					leaf.keys.remove(key);
					leaf.values.remove(i);
					break;
				}
			}

			// Handle leaf underflow
			if (leaf.nodeSanity() < 0 && leaf != rootNode) {
				if (leaf.getIndexInParent() == 0) {
					return handleLeafNodeUnderflow(leaf, leaf.nextLeaf, leaf.getParent());
				} else {
					return handleLeafNodeUnderflow(leaf.prevLeaf, leaf, leaf.getParent());
				}
			}

		} else {
			InternalNode index = (InternalNode) child;

			if (key.compareTo(index.keys.get(0)) < 0) {
				parentSplitKeyIndex = deleteNode(key, index.children.get(0), index, parentSplitKeyIndex);
			} else if (key.compareTo(index.keys.get(index.keys.size() - 1)) >= 0) {
				parentSplitKeyIndex = deleteNode(key, index.children.get(index.children.size() - 1), index,
						parentSplitKeyIndex);
			} else {
				for (int i = 1; i < index.keys.size(); i++) {
					if (index.keys.get(i).compareTo(key) > 0) {
						parentSplitKeyIndex = deleteNode(key, index.children.get(i), index, parentSplitKeyIndex);
						break;
					}
				}
			}
		}

		// delete split key and handle overflow
		if (parentSplitKeyIndex != -99 && child != rootNode) {
			child.keys.remove(parentSplitKeyIndex);
			if (child.nodeSanity() < 0) {
				if (child.getIndexInParent() == 0) {
					InternalNode rightSibling = (InternalNode) child.getParent().children
							.get(child.getIndexInParent() + 1);
					parentSplitKeyIndex = handleIndexNodeUnderflow(child.getParent(), (InternalNode) child,
							rightSibling);
				} else {
					InternalNode leftSibling = (InternalNode) child.getParent().children
							.get(child.getIndexInParent() - 1);
					parentSplitKeyIndex = handleIndexNodeUnderflow(child.getParent(), leftSibling,
							(InternalNode) child);
				}
			} else
				parentSplitKeyIndex = -99;
		}

		return parentSplitKeyIndex;
	}

	public int handleLeafNodeUnderflow(LeafNode left, LeafNode right, InternalNode parent) {

		// If borrowing element does not results in a underflow of the leaf node
		int totalSize = left.keys.size() + right.keys.size();
		if (totalSize >= degree) {

			int childIndex = parent.children.indexOf(right);

			// Store all keys and values from left to right
			ArrayList<Integer> keys = new ArrayList<Integer>();
			ArrayList<Double> vals = new ArrayList<Double>();
			keys.addAll(left.keys);
			keys.addAll(right.keys);
			vals.addAll(left.values);
			vals.addAll(right.values);

			int leftSize = totalSize / 2;

			left.keys.clear();
			right.keys.clear();
			left.values.clear();
			right.values.clear();

			// Add first half keys to the left node
			left.keys.addAll(keys.subList(0, leftSize));
			left.values.addAll(vals.subList(0, leftSize));

			// Add rest of the keys to the right node
			right.keys.addAll(keys.subList(leftSize, keys.size()));
			right.values.addAll(vals.subList(leftSize, vals.size()));

			// Update the key value in the parent node
			parent.keys.set(childIndex - 1, parent.children.get(childIndex).keys.get(0));

			// in this case parent node is affected hence returning -99
			return -99;
		} else {
			// remove right child
			left.keys.addAll(right.keys);
			left.values.addAll(right.values);

			left.nextLeaf = right.nextLeaf;
			if (right.nextLeaf != null) {
				right.nextLeaf.prevLeaf = left;
			}

			int index = parent.children.indexOf(right) - 1;
			parent.children.remove(right);

			return index;
		}
	}

	/**
	 * This functions handles overflow of the index node, this works similar to a b
	 * tree
	 * 
	 * @param left
	 * @param right
	 * @param parent
	 * @return
	 */
	int handleIndexNodeUnderflow(InternalNode parent, InternalNode left, InternalNode right) {

		// get the index from which you need to split
		int parentSplitKeyIndex = -99;
		for (int i = 0; i < parent.keys.size(); i++) {
			if (parent.children.get(i) == left && parent.children.get(i + 1) == right) {
				parentSplitKeyIndex = i;
				break;
			}
		}

		int totalSize = left.keys.size() + right.keys.size();
		// if the node you are borrowing will remain sane after borrowing the element
		// the borrow
		if (totalSize >= degree) {
			ArrayList<Integer> keys = new ArrayList<Integer>();
			ArrayList<Node> children = new ArrayList<Node>();
			keys.addAll(left.keys);
			keys.add(parent.keys.get(parentSplitKeyIndex));
			keys.addAll(right.keys);
			children.addAll(left.children);
			children.addAll(right.children);

			// Get the index of the new parent key
			int newIndex = totalSize / 2;
			if (keys.size() % 2 == 0) {
				newIndex -= 1;
			}
			// set the value in the parent as the max element of the left tree
			parent.keys.set(parentSplitKeyIndex, keys.get(newIndex));
			// Clear the current values of the left node

			left.keys.clear();
			left.children.clear();
			// Clear the current values of the right node

			right.keys.clear();
			right.children.clear();

			// from the new leaf created insert all the values from 0 to half in the left
			// child
			left.keys.addAll(keys.subList(0, newIndex));
			left.children.addAll(children.subList(0, newIndex + 1));

			// from the new leaf created insert all the values from 0 to half in the right
			// child
			right.children.addAll(children.subList(newIndex + 1, children.size()));
			right.keys.addAll(keys.subList(newIndex + 1, keys.size()));
			return -99;
		}
		// else merge the nodes and return the parent key index which needs to be
		// removed
		else {
			left.keys.add(parent.keys.get(parentSplitKeyIndex));
			left.keys.addAll(right.keys);
			left.children.addAll(right.children);

			parent.children.remove(parent.children.indexOf(right));
			return parentSplitKeyIndex;
		}
	}
}
