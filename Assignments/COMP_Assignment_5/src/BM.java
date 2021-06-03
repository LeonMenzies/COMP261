import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BM {

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
	 * Perform BM substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public static int search(String text, String pattern) {

		int i = pattern.length() - 1;
		int j = pattern.length() - 1;
		while (i > 0) {
			if (pattern.charAt(j) == text.charAt(i)) {
				if (j == 0) {
					return i; // Found the pattern
				} else {
					i--;
					j--;
				}
			} else {

				// Increase i by the jump amount or j if there is not prefix that matches a
				// suffix
				i = i + pattern.length() - Math.min(j, 1 + last(text.charAt(i), pattern));
				j = pattern.length() - 1;

			}
		}

		return -1;
	}

	// Return the index of c in pattern or -1 if its not found
	public static int last(char c, String pattern) {
		for (int i = pattern.length() - 1; i >= 0; i--) {
			if (pattern.charAt(i) == c) {
				return i;
			}
		}
		return -1;
	}

}
