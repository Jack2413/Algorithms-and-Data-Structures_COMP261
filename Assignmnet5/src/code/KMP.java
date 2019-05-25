package code;

/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

	int MatchTable[];

	public KMP(String pattern, String text) {
		int j = 0;
		int i = 2;// current position
		int m = pattern.length();
		char S[] = pattern.toCharArray();
		MatchTable = new int[m];
		MatchTable[0] = -1;
		MatchTable[1] = 0;
		while (i < m) {
			if (S[i - 1] == S[j]) {
				MatchTable[i] = j + 1;
				i++;
				j++;
			} else if (j > 0) {
				j = MatchTable[j];
			} else {
				j = MatchTable[i] = 0;
				i++;
			}
		}
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		char S[] = pattern.toCharArray();
		char T[] = text.toCharArray();
		int s = 0;
		int t = 0;
		int m = pattern.length();
		int n = text.length();
		while (s + t < n) {
			if (S[s] == T[t + s]) {
				s++;
				if (s == m) {
					return t;
				}
			} else if (MatchTable[s] == -1) {
				s = 0;
				t = t + s + 1;
			} else {
				t = t + s - MatchTable[s];
				s = MatchTable[s];
			}
		}
		return -1;
	}
}
