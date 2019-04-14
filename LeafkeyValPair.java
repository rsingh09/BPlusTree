
public class LeafkeyValPair {
	Integer key;
    Node treeNode;
    public Node getTreeNode() {
		return treeNode;
	}

    public Integer getKey() {
        return this.key;
    }
    LeafkeyValPair(Integer k, Node n) {
        this.treeNode = n;
        this.key = k;
    }
}
