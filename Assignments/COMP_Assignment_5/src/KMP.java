
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class KMP {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Please call this program with " + "two arguments which is the input file name "
					+ "and the string to search.");
		} else {
			try {
				Scanner s = new Scanner(new File(args[0]));

				// Read the entire file into one String.
				StringBuilder fileText = new StringBuilder();
				while (s.hasNextLine()) {
					fileText.append(s.nextLine() + "\n");
				}

				System.out.println(search(fileText.toString(), args[1]));
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find file called " + args[0]);
			}
		}
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public static int search(String text, String pattern) {

		int k = 0;
		int i = 0;

		Integer[] table = buildTable(pattern);

		while (k + i < text.length()) {
			if (pattern.charAt(i) == text.charAt(k + i)) {
				i++;
				if (i == pattern.length()) {
					return k;
				}
			} else if (table[i] == -1) {
				k = k + i + 1;
				i = 0;
			} else {
				k = k + i - table[i];
				i = table[i];
			}
		}
		return -1;
	}

	public static Integer[] buildTable(String string) {

		Integer[] match = new Integer[string.length()];

		match[0] = -1;
		match[1] = 0;
		int j = 0;
		int pos = 2;

		while (pos < string.length()) {
			if (string.charAt(pos - 1) == string.charAt(j)) {
				match[pos] = j + 1;
				pos++;
				j++;
			} else if (j > 0) {
				j = match[j];
			} else {
				match[pos] = 0;
				pos++;
			}
		}
		return match;
	}
}
