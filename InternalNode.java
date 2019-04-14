import java.util.*;

public class InternalNode extends Node {

	protected ArrayList<Node> children;
//	protected InternalNode rightNeighbor;
//	protected InternalNode leftNeighbor;

	/**
	 * Contructor to handle a single insertion into the insert node during initial
	 * operation
	 * 
	 * @param key
	 * @param child0
	 * @param child1
	 */
	public InternalNode(Integer key, Node leftChild, Node rightChild) {
		super(key,false);
//		isLeafNode = false;
//		keys = new ArrayList<Integer>();
//		keys.add(key);
		children = new ArrayList<Node>();
		children.add(leftChild);
		children.add(rightChild);
	}

	/**
	 * Insert into the index node after an operation which is not the initial
	 * operation(insert caused by split)
	 * 
	 * @param new_keys
	 * @param newChildren
	 */
	public InternalNode(List<Integer> newKeys, List<Node> newChildren) {
		super(newKeys,false);
//		isLeafNode = false;
//		keys = new ArrayList<Integer>(new_keys);
		children = new ArrayList<Node>(newChildren);
	}

	/**
	 * Insert entry into this node at the specified index so that it still remains
	 * sorted
	 * 
	 * @param e
	 * @param index
	 */
	public void InsertinNode(LeafkeyValPair e, int index) {
		Integer key = e.getKey();
		Node child = e.getTreeNode();
		if (index >= keys.size()) {
			keys.add(key);
			children.add(child);
		} else {
			keys.add(index, key);
			children.add(index + 1, child);
		}
	}
}
//	ArrayList<Integer> key;
//	int currentSize = 0;
//	int order = 0;
//	int noOfChildren = 0;
//	ArrayList<InternalNode> children;
//	InternalNode parent;
//	ArrayList<LeafNode> leaves;
//	boolean hasLeafNode = false;
//
//	public InternalNode(int key, InternalNode parentNode, InternalNode right, InternalNode left, boolean hasLeafNode) {
//		this.key = new ArrayList<Integer>();
//		this.key.add(key);
//		parent = parentNode;
//		this.hasLeafNode = hasLeafNode;
//		leaves = null;
//		currentSize++;
//	}
//
//	public void AddinNode(int key, double value, int degree) {
//		int maxElement = Collections.max(this.key);
//		int minElement = Collections.min(this.key);
//		if (key < minElement) 
//		{
//			if (hasLeafNode)
//			{
//				addValueinLeaf(0);
//			}
//		} 
//		else if (key >= maxElement)
//		{
//			if (hasLeafNode)
//			{
//				addValueinLeaf(children.size()+1);
//			}
//		} 
//		else 
//		{
//			for (int i = 0; i < this.key.size(); i++) {
//				if (key < this.key.get(i)) {
//					if (hasLeafNode == true) {
//						// leaves.add(e)
//					}
//
//				}
//			}
//		}
//	}	
//	private void addValueinLeaf(int index)
//	{
//		if(leaves.get(index).currentSize == order)
//		{
//			
//		}
//	}
