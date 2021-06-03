import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class LempelZivCompress {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Please call this program with one argument which is the input file name.");
		} else {
			try {
				Scanner s = new Scanner(new File(args[0]));

				// Read the entire file into one String.
				StringBuilder fileText = new StringBuilder();
				while (s.hasNextLine()) {
					fileText.append(s.nextLine() + "\n");
				}

				System.out.println(compress(fileText.toString()));
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find file called " + args[0]);
			}
		}
	}

	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public static String compress(String input) {

		input = "a_contrived_text_containing_riveting_contrasting";

		char[] text = input.toCharArray();

		int cursor = 0;
		int windowSize = 100;

		StringBuilder outPut = new StringBuilder();

		while (cursor < text.length) {

			int length = 0;
			int prevMatch = 0;

			while (true) {

				if (cursor + length > text.length - 1) {
					return outPut.toString();

				}

				// System.out.println(Arrays.copyOfRange(text, cursor, cursor + length));
				int match = stringMatch(

						Arrays.copyOfRange(text, cursor, cursor + length + 1),
						Arrays.copyOfRange(text, (cursor < windowSize) ? 0 : cursor - windowSize, cursor));

				if (match > prevMatch) {
					prevMatch = match;
					length++;
				} else {
					outPut.append("[" + ((prevMatch > 0) ? (cursor - prevMatch) : prevMatch) + "|" + length + "|"
							+ text[cursor + length] + "]");
					cursor = cursor + length + 1;
					break;
				}
			}
		}
		return outPut.toString();
	}

	public static int stringMatch(char[] toMatch, char[] textWindow) {

		int foundAt = 0;

		if (toMatch.length == 0) {
			return foundAt;
		}

		for (int i = 0; i < textWindow.length; i++) {

			// The match char is found in the window
			if (toMatch[0] == textWindow[i]) {

				for (int j = 0; j < toMatch.length; j++) {

					if (i + j > textWindow.length - 1) {
						foundAt = i;
					}

					if (toMatch[j] != textWindow[i + j]) {
						return 0;
					}
					if (j == toMatch.length - 1) {
						foundAt = i;
					}
				}
			}
		}
		return foundAt;
	}
}
