import java.util.*;

public class Tries {
	
	
	private final TrieNode root;
	boolean isword;
	
	public Tries(){
		
		isword=false;
		root = new TrieNode();
		
	}
	public void add (String word){
		TrieNode current = root;
		for(int i = 0; i< word.length(); i++){
			char c = word.charAt(i);
			TrieNode node = current.children.get(c);
			if(node==null){
				node = new TrieNode();
				current.children.put(c,node);
				current.childkey.add(c);
				
			}
			current=node;
			current.c=c;
		
		}
		
		current.data=word;
		
		
		isword=true;
		
	

}
	public boolean serch (String word){
		TrieNode current = root;  //start at root
		for(int i=0; i<word.length(); i++){ //for each of the char go search they child
			char c = word.charAt(i);
			TrieNode node = current.children.get(c); //Check the node is or not exist
			if(node==null){
				return false;
				
			}
			current=node; //move on next node
		}
		return current.isword;
		
	}

	public HashSet<String> getall (String word){
		HashSet<String> s = new HashSet <> ();
		TrieNode current =root;
		
		for(int i=0; i<word.length();i++){
			char c = word.charAt(i);
			TrieNode node = current.children.get(c);
			if(node==null){
				return null;
				
			}
			current = node;
			
		}
		
		return getallvalue(current,s);
		
	
	}
	public HashSet<String> getallvalue(TrieNode root, HashSet<String> s){
		if(root.data!=null){
		s.add(root.data);
	
		}
		
		for(char c : root.childkey){
		getallvalue(root.children.get(c), s);
		//System.out.print(root.children.get(c).c+"\n");
		}
		return s;
	}
}