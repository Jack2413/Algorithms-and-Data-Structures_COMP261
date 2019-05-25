package code;

import java.util.HashMap;
import java.util.Map;

public class BoyerMoore {
	
	int MatchTable[];
	 Map<Character,Integer> jump = new HashMap<>();

	public BoyerMoore(String pattern, String text) {
		int m = pattern.length();
		int j = m - 1;
		int i = m - 2;// current position

		char S[] = pattern.toCharArray();
		MatchTable = new int[m];
		MatchTable[j] = 0;
		int count = 0;
		while (i >= 0) {
			System.out.print(("("+i+","+j+")"));
			if (S[i] == S[j]) {
				MatchTable[i] = ++count;
				i--;
				j--;
			} 
			else {
				MatchTable[i] = 0;
				j = m - 1;
				i--;
				count = 0;
			}
		}
		for(int k = 0; k<pattern.length();k++){
		jump.put(S[k], pattern.length()-1-k);
		}
		
	}
	public int search(String pattern, String text) {
		char S[] = pattern.toCharArray();
		char T[] = text.toCharArray();
		int m = pattern.length();
		int n = text.length();
		int s = m-1;
		int t = m-1;
		int matchsofar=0;
		while (s + t < n) {
			if(S[s]==T[t]){
				matchsofar++;
				s--;
				t--;
				if (s == m) {
					return t-(m-1);
				}
				else{
					int pos1 =0;
					int pos2 = MatchTable[s-matchsofar];;
					if(jump.containsKey(T[t])){pos1 = jump.get(T[t+matchsofar]);}
					
					if(pos1<pos2){
						s+=pos2;
						t+=pos2;
					}
					else{
						s+=pos1;
						t+=pos2;
					}		
				}
			}
		}
		return -1;
	}
}
