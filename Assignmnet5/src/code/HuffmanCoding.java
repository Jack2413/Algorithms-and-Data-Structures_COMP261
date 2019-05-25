package code;

import java.util.*;
import java.util.Map.Entry;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {
	HuffmanNode root ;
	Map<Character, Integer> frequency = new HashMap<Character, Integer>();
	Map<Character, String> Codes = new HashMap<Character, String>();

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		char texts[] = text.toCharArray();
		System.out.printf("texts: %d",texts.length);
		for (char c : texts) {
			if (frequency.containsKey(c)) {
				frequency.put(c, frequency.get(c) + 1);
			}else{
				frequency.put(c,1);
			}
		}
		PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(new charComparator());
	
		for (Entry<Character, Integer> entrySet : frequency.entrySet()) {
			char value = entrySet.getKey();
			double probability = entrySet.getValue() / (double) text.length();
			queue.offer(new HuffmanNode(null, null, null, value, probability));

		}
	
		while (queue.size() > 1) {
			HuffmanNode node1 = queue.poll();
			HuffmanNode node2 = queue.poll();
			
			double probability = node1.probability + node2.probability;
			HuffmanNode parent = new HuffmanNode(node1, node2, null, null, probability);
			queue.offer(parent);
		}
		
		root = queue.poll();
		//System.out.print(root.toString());
		addcode(root,"");
		System.out.print(Codes+"\n");
	}

	private void addcode(HuffmanNode current, String code) {
		current.code = code;
		if (current.c != null) {
			Codes.put(current.c, current.code);		
		}
		if (current.left != null) {
			addcode(current.left, current.code + 0);
		}
		if (current.right != null) {
			addcode(current.right, current.code + 1);	
		}
		
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		
		char texts[] = text.toCharArray();
		StringBuilder result = new StringBuilder();
		for (char c : texts) {
				result.append(Codes.get(c));
		}
		return result.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		char codes[] = encoded.toCharArray();
		StringBuilder result = new StringBuilder();
		HuffmanNode current = root;
		
		for (char c : codes) {
				if (c=='0') {
					current = current.left;
				} else {
					current = current.right;
				}
				if(current.c!=null){
					result.append(current.c);
					current=root;
				}
			}
			//System.out.print(current.code+"\n");
		return result.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}

	class charComparator implements Comparator<HuffmanNode> {

		@Override
		public int compare(HuffmanNode n1, HuffmanNode n2) {
			if (n1.probability > n2.probability) {
				return 1;
			} else if (n1.probability < n2.probability) {
				return -1;
			}
			return 0;
		}

	}
}
