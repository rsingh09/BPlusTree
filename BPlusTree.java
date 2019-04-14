
import java.util.*;

public class BPlusTree {

	public static int degree;
	public Node rootNode;

	public BPlusTree(int degree) {
		System.out.println(degree);
		this.degree = degree - 1;
		rootNode = null;
	}

	/**
	 * Function to insert a key, value pair in the tree passes the Key and its value
	 * returns 1 on successful insertion 0 on failure
	 */
	public void insertInTree(Integer key, double value) {
		if (searchInTree(key) != null) {
			System.out.println("Duplicates Not allowed");
			return;
		}
		LeafNode newLeaf = new LeafNode(key, value);
		LeafkeyValPair newParent = new LeafkeyValPair(key, newLeaf);

		if (rootNode == null || rootNode.nodeSize() == 0)
			rootNode = newParent.getTreeNode();

		LeafkeyValPair childEntry = getChild(rootNode, newParent, null);
		if (childEntry == null)
			return;
		else {
			InternalNode newRoot = new InternalNode(childEntry.getKey(), rootNode, childEntry.getTreeNode());
			rootNode = newRoot;
			return;
		}
	}

	/**
	 * Function to search a key in the tree passes the Key and its value returns 1
	 * on successful insertion 0 on failure
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

	public String searchInTree(Integer key1, Integer key2) {
		// Return a key or whether empty tree
		String str ="";
		StringBuilder searchResult = new StringBuilder();
		if (key1 == null || key2 == null || rootNode == null) {
			searchResult.append("");
		}
		// Find leaf node key is pointing to
		LeafNode leaf = (LeafNode) parseTree(rootNode, key1);
		int j = 0;
		int maxleafValue = leaf.keys.get(leaf.keys.size()-1);
		while ( maxleafValue >=  key1 && leaf.nextLeaf != null) {
			for (int i = 0; i < leaf.nodeSize(); i++) {
				if (key1.compareTo(leaf.keys.get(i)) <= 0) {
					//System.out.println(leaf.keys.get(i));
					while (i < leaf.nodeSize() && key2 >= leaf.keys.get(i)) {

						if (leaf.values.size() > 0) {
							//searchResult.append(leaf.values.get(i) + ", ");
							str= str + leaf.values.get(i) + ", ";
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
			maxleafValue = leaf.keys.get(leaf.keys.size()-1);
		}
		//String str = searchResult.toString();
		if (str != "") {
			str = str.substring(0, str.length() - 2);
		}
		return str;
	}

	private Node parseTree(Node node, Integer key) {
		if (node.isLeafNode) {
			return node;
		}
		// For index node
		else {
			InternalNode index = (InternalNode) node;

			// when key < key0, return searchInTree(P0, Double)
			if (key.compareTo(index.keys.get(0)) < 0) {
				return parseTree((Node) index.children.get(0), key);
			}
			// m is the number of entries
			// When key >= keym, return searchInTree(Pm, Double)
			else if (key.compareTo(index.keys.get(node.nodeSize() - 1)) >= 0) {
				return parseTree((Node) index.children.get(index.children.size() - 1), key);
			}
			// Get i for keyi <= key < key(i+1), return searchInTree(Pi, Double)
			else {
				// Perform linear searching
				for (int i = 0; i < index.nodeSize() - 1; i++) {
					if (key.compareTo(index.keys.get(i)) >= 0 && key.compareTo(index.keys.get(i + 1)) < 0) {
						return parseTree((Node) index.children.get(i + 1), key);
					}
				}
			}
			return null;
		}
	}

	public void deleteNode(int key) {
//		if (rootNode == null || rootNode.nodeSize() == 0 || searchInTree(key) == null) {
//			System.out.println("Nothing to delete");
//			return;
//		} else if (rootNode.isLeafNode) {
//			LeafNode n = (LeafNode) rootNode;
//			//for (Integer i : rootNode.keys) {
//				//if (i.compareTo(key) == 0) {
//					n.values.remove(n.keys.indexOf(key));
//					n.keys.remove(key);
//					rootNode = n;
//				//}
//			//}
//			return;
//		}
//		deleteNode(rootNode, key);
		delete(key);

	}

	private void deleteNode(Node node, Integer key) {
		InternalNode n = (InternalNode) node;
		int foundIndex = 0;
		if (n.children.get(0).isLeafNode) {
			int minKey = n.keys.get(0);
			int maxElement = n.keys.get(n.nodeSize() - 1);
			LeafNode ln = null;
			if (key < minKey) {
				foundIndex = 0;
			} else if (key >= maxElement) {
				foundIndex = n.children.size() - 1;
			} else {
				for (Integer i : n.keys) {
					if (i.compareTo(key) >= 0) {
						foundIndex = n.keys.indexOf(i);
						//System.out.println(foundIndex);
					}
				}
			}
			ln = (LeafNode) n.children.get(foundIndex);
			ln.values.remove(n.keys.indexOf(key));
			n.keys.remove(key);
			if (ln.nodeSanity() < 0) {
				if (ln.nextLeaf != null && ln.nextLeaf.nodeSanity() > 0) {
					int newKey = ln.nextLeaf.keys.get(0);
					ln.InsertInLeaf(ln.nextLeaf.keys.get(0), ln.nextLeaf.values.get(0));
					n.keys.remove(foundIndex - 1);
					n.keys.add(foundIndex - 1, newKey);
				} else if (ln.prevLeaf != null && ln.prevLeaf.nodeSanity() > 0) {
					int index = ln.prevLeaf.keys.size() - 1;
					int newKey = ln.nextLeaf.keys.get(index - 1);
					ln.InsertInLeaf(ln.prevLeaf.keys.get(index), ln.prevLeaf.values.get(index));
					n.keys.add(foundIndex - 1, newKey);
				} else {

				}
			}

		} else {

		}
	}

	private LeafkeyValPair getChild(Node node, LeafkeyValPair entry, LeafkeyValPair newChild) {
		if (!node.isLeafNode) {
			// Choose subtree, find i such that Ki <= entry's key value < J(i+1)
			InternalNode index = (InternalNode) node;
			int i = 0;
			while (i < index.nodeSize()) {
				if (entry.getKey().compareTo(index.keys.get(i)) < 0) {
					break;
				}
				i++;
			}
			// tree_insert entry is done recursively
			newChild = getChild((Node) index.children.get(i), entry, newChild);

			// No child split case
			if (newChild == null) {
				return null;
			}
			// Split child case
			else {
				int j = 0;
				while (j < index.nodeSize()) {
					if (newChild.getKey().compareTo(index.keys.get(j)) < 0) {
						break;
					}
					j++;
				}

				index.InsertinNode(newChild, j);

				// Usual case, put new_child on it, set new_child to null, return
				if (!index.nodeOverFlow()) {
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
		// node pointer is a leaf node
		else {
			LeafNode leaf = (LeafNode) node;
			LeafNode newLeaf = (LeafNode) entry.getTreeNode();

			leaf.InsertInLeaf(entry.getKey(), newLeaf.values.get(0));

			// Usual case: leaf has space, put entry and set new_child to null and return
			if (!leaf.nodeOverFlow()) {
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

	public LeafkeyValPair splitLeafNode(LeafNode leaf) {
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
		rightNode.prevLeaf = leaf;
		rightNode.nextLeaf = tmp;

		LeafkeyValPair new_child = new LeafkeyValPair(splitKey, rightNode);

		return new_child;
	}

	public LeafkeyValPair splitInternalNode(InternalNode index) {
		ArrayList<Integer> newKeys = new ArrayList<Integer>();
		ArrayList<Node> newChildren = new ArrayList<Node>();
		Integer splitKey = index.keys.get(degree / 2);
		index.keys.remove(degree / 2);

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

	public void delete(Integer key) {
		if (rootNode == null || rootNode.nodeSize() == 0 || searchInTree(key) == null)
			return;

		int splitIndex = deleteHelper(key, rootNode, null, -1);
		if (splitIndex != -1) {
			rootNode.keys.remove(splitIndex);
			if (rootNode.keys.isEmpty()) {
				rootNode = ((InternalNode) rootNode).children.get(0);
			}
		}

		// if the new root is also empty, then the entire tree must be empty
		if (rootNode.keys.isEmpty()) {
			rootNode = null;
		}
	}

	private int deleteHelper(Integer key, Node child, InternalNode parent, int splitIndex) {
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
				splitIndex = deleteHelper(key, index.children.get(0), index, splitIndex);
			} else if (key.compareTo(index.keys.get(index.keys.size() - 1)) >= 0) {
				splitIndex = deleteHelper(key, index.children.get(index.children.size() - 1), index, splitIndex);
			} else {
				for (int i = 1; i < index.keys.size(); i++) {
					if (index.keys.get(i).compareTo(key) > 0) {
						splitIndex = deleteHelper(key, index.children.get(i), index, splitIndex);
						break;
					}
				}
			}
		}

		// delete split key and handle overflow
		if (splitIndex != -1 && child != rootNode) {
			child.keys.remove(splitIndex);
			if (child.nodeSanity() < 0) {
				if (child.getIndexInParent() == 0) {
					InternalNode rightSibling = (InternalNode) child.getParent().children
							.get(child.getIndexInParent() + 1);
					splitIndex = handleIndexNodeUnderflow((InternalNode) child, rightSibling, child.getParent());
				} else {
					InternalNode leftSibling = (InternalNode) child.getParent().children.get(child.getIndexInParent() - 1);
					splitIndex = handleIndexNodeUnderflow(leftSibling, (InternalNode) child, child.getParent());
				}
			} else
				splitIndex = -1;
		}

		return splitIndex;
	}

	public int handleLeafNodeUnderflow(LeafNode left, LeafNode right, InternalNode parent) {

		// If redistributable
		int totalSize = left.keys.size() + right.keys.size();
		if (totalSize >= 2 * degree) {

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

			// Add first half keys and values into left and rest into right
			left.keys.addAll(keys.subList(0, leftSize));
			left.values.addAll(vals.subList(0, leftSize));
			right.keys.addAll(keys.subList(leftSize, keys.size()));
			right.values.addAll(vals.subList(leftSize, vals.size()));

			parent.keys.set(childIndex - 1, parent.children.get(childIndex).keys.get(0));
			return -1;
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

	int handleIndexNodeUnderflow(InternalNode left, InternalNode right, InternalNode parent) {

		int splitIndex = -1;
		for (int i = 0; i < parent.keys.size(); i++) {
			if (parent.children.get(i) == left && parent.children.get(i + 1) == right) {
				splitIndex = i;
				break;
			}
		}

		// Redistribute if possible
		int totalSize = left.keys.size() + right.keys.size();
		if ( totalSize > (2 * degree)) {
			ArrayList<Integer> keys = new ArrayList<Integer>();
			ArrayList<Node> children = new ArrayList<Node>();
			keys.addAll(left.keys);
			keys.add(parent.keys.get(splitIndex));
			keys.addAll(right.keys);
			children.addAll(left.children);
			children.addAll(right.children);

			// Get the index of the new parent key
			int newIndex = keys.size() / 2;
			if (keys.size() % 2 == 0) {
				newIndex -= 1;
			}
			parent.keys.set(splitIndex, keys.get(newIndex));

			left.keys.clear();
			right.keys.clear();
			left.children.clear();
			right.children.clear();

			left.keys.addAll(keys.subList(0, newIndex));
			right.keys.addAll(keys.subList(newIndex + 1, keys.size()));
			left.children.addAll(children.subList(0, newIndex + 1));
			right.children.addAll(children.subList(newIndex + 1, children.size()));
			return -1;
		} else {
			left.keys.add(parent.keys.get(splitIndex));
			left.keys.addAll(right.keys);
			left.children.addAll(right.children);

			parent.children.remove(parent.children.indexOf(right));
			return splitIndex;
		}
	}
}
