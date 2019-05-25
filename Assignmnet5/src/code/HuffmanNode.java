package code;

public class HuffmanNode {
	HuffmanNode left;
	HuffmanNode right;
	String code;
	Character c;
	double probability;
	
	public HuffmanNode(HuffmanNode left, HuffmanNode right, String code, Character c, double probability) {
		
		this.left = left;
		this.right = right;
		this.code = code;
		this.c = c;
		this.probability = probability;
	}
	@Override
	public String toString(){
		return  "char: " + c + " probability: "+ String.valueOf(probability) + " String: " + code + "\nleftnode: " +left.toString() + "\nrightnode: " +right.toString();
	}
}
