package code;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		int cursor = 1;
		int windowSize = 100;
		StringBuilder result = new StringBuilder();
		result.append("[0,0," + input.charAt(0) + "]");
		while (cursor < input.length()) {
			int lookahead = 1;// current length
			int prevMatch = 0;// how far it jump back
			// System.out.print(cursor);
			while (true) {
				int start = (cursor < windowSize) ? 0 : cursor - windowSize;
				int end = cursor;
				int cursorEnd = cursor + lookahead <= input.length() ? cursor + lookahead : input.length();

				String pattern = (input.substring(cursor, cursorEnd));
				String text = (input.substring(start, end));

				// System.out.printf("pattern(%d,%d)\n",cursor,cursorEnd);
				// System.out.printf("text(%d,%d)\n",start,end);

				int match = new BruteForce(pattern, text).search(pattern, text);

				if (match != -1) {
					prevMatch = cursor -start- match;
					lookahead++;
					if (cursorEnd == input.length()) {
						result.append("[" + prevMatch + "," + (lookahead - 1) + "," + "]");
						cursor = cursor + lookahead;
						break;
					}
				} else {
					result.append(
							"[" + prevMatch + "," + (lookahead - 1) + "," + input.charAt(cursor + lookahead - 1) + "]");
					cursor = cursor + lookahead;
					break;
				}
			}
		}
		//System.out.print(result);
		return result.toString();
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {

		StringBuilder result = new StringBuilder();
		int cursor = 0;
		String tuples[] = compressed.split("\\[|\\]");
		for (String t : tuples) {
			String values[] = t.split(",");
			 //System.out.println(t);
				if(values.length>1){
				int offset = Integer.parseInt(values[0]);
				int length = Integer.parseInt(values[1]);
				String cha = "";
				if(values.length>2){
				cha = values[2];
				}
				
				if (offset == 0 && length == 0) {
					result.append(cha);
					cursor++;
				} else {
					int start = cursor - offset;
					result.append(result.substring(start, start + length));
					result.append(cha);
					cursor += length + 1;
				}
				}
			}
		 //System.out.print(result.substring(0,result.length()-1).toString());
		return result.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
}
