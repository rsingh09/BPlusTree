import java.util.ArrayList;
import java.lang.Math;
import java.util.List;

/**
 * Node Class: This class serves as basic structure of a Tree node
 * extended by Leaf Node  i.e. the external Node containing the key, value pair
 * extended by Internal Node i.e. containing only keys.
 * */
public class Node {
	protected boolean isLeafNode; //Identify if Node is a leaf or Internal Node
	protected ArrayList<Integer> keys;
	protected InternalNode parent;
	protected int indexInParent;
	
	public Node(Integer key, boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
		keys = new ArrayList<Integer>();
		keys.add(key);
	}
	public void setParent(InternalNode parent) {
		this.parent = parent;
		for (int i = 0; i < parent.children.size(); i++) {
			if (parent.children.get(i).equals(this)) {
				this.indexInParent = i;
				break;
			}
		}
	}
	public InternalNode getParent() {
		return this.parent;
	}

	public int getIndexInParent() {
		return this.indexInParent;
	}
	public Node(List<Integer> newkeys, boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
		this.keys = new ArrayList<Integer>(newkeys);
	}

	public boolean nodeOverFlow() {
		return keys.size() > BPlusTree.degree;
	}
	
	public int nodeSanity() {
		if(keys.size() < (Math.ceil(BPlusTree.degree/2.0) -1))
			return -1;
		else if(keys.size() == (Math.ceil(BPlusTree.degree/2.0) -1))
			return 0;
		else
			return 1;
	}
	
	public int nodeSize() {
		return keys.size();
	}
}
