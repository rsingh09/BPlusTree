import java.util.ArrayList;
import java.util.ListIterator;

public class LeafNode extends Node {

	protected ArrayList<Double> values;
	protected LeafNode nextLeaf;
	protected LeafNode prevLeaf;
	// protected InternalNode parent;
	// int currentSize = 0;

	public int getLeafSize() {
		return values.size();
	}

	/**
	 * Initial Insert a single key/value pair in the leaf node
	 * 
	 * @param keyVal
	 * @param value
	 */
	public LeafNode(int keyVal, Double value) {
		super(keyVal, true);
//		isLeafNode = true;
//		keys = new ArrayList<Integer>();
		values = new ArrayList<Double>();
		// keys.add(keyVal);
		values.add(value);
	}

//	private void updateLeafSize() {
//		currentSize = this.values.size();
//		
//	}

	public LeafNode(ArrayList<Integer> keyList, ArrayList<Double> valueList) {
		super(keyList, true);
//		isLeafNode = true;
//		keys = new ArrayList<Integer>(keyList);
		values = new ArrayList<Double>();
		for (Double val : valueList)
			values.add(val);

	}

	/**
	 * If the leaf has not over flown insert the new key/value in the leaf
	 * 
	 * @param key
	 * @param value
	 */
	public void InsertInLeaf(Integer key, Double value) {
		int minKey = 0;
		int maxKey = 0;
//		if (keys.size() != 0) {
//			minKey = keys.get(0);
//			maxKey = keys.get(keys.size() - 1);
//		}
		if (key < minKey) {
			keys.add(0, key);
			values.add(0, value);
		} else if (key > maxKey) {
			keys.add(key);
			values.add(value);

		} else {
			ListIterator<Integer> iterator = keys.listIterator();
			while (iterator.hasNext()) {
				if (iterator.next().compareTo(key) > 0) {
					int position = iterator.previousIndex();
					// System.out.println("leaf:"+position);
					keys.add(position, key);
					values.add(position, value);
					break;
				}
			}
		}
	}
}
