import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LempelZivDecompress {

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

				System.out.println(decompress(fileText.toString()));
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find file called " + args[0]);
			}
		}
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */

	public static String decompress(String compressed) {

		Scanner sc = new Scanner(compressed).useDelimiter("\\[|\\||\\]");
		ArrayList<String> output = new ArrayList<>();

		int cursor = 0;
		while (sc.hasNext()) {

			int reach = sc.nextInt();
			int len = sc.nextInt();
			String c = sc.next();
			sc.next();

			if (reach == 0) {
				output.add(c);
				cursor++;
			} else {
				for (int i = 0; i < len; i++) {

					output.add(output.get(cursor - reach));
					cursor++;

				}
				output.add(c);
				cursor++;
			}

		}

		StringBuilder sb = new StringBuilder();
		for (String s : output) {
			sb.append(s);
		}

		return sb.toString();
	}
}
