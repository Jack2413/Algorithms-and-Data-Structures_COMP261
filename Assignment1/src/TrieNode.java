import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrieNode {
	char c;
	boolean isword;
	Map<Character,TrieNode> children;
	String data=null;
	Set<Character> childkey;
	
	public TrieNode(){
		childkey= new HashSet<Character>();
		children= new HashMap<Character,TrieNode>();
		isword=false;
	}

	
}
