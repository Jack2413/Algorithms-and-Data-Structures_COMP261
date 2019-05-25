package code;

public class BruteForce {
	int cursor = 0;
	int PL; // PATTERN LENGTH
	int TL; // text length

	public BruteForce(String pattern, String text) {

		PL = pattern.length();
		TL = text.length();
	}

	public int search(String pattern, String text) {
		char T[] = text.toCharArray();
		char P[] = pattern.toCharArray();
		for (int k = 0; k <= TL - PL; k++) {
			boolean found = true;
			for (int i = 0; i < PL; i++) {
				if (P[i] != T[k + i]) {
					found=false;
					break;
				}
			}
			if (found) {
				return k;
			}
		}
		return -1;
	}
}
