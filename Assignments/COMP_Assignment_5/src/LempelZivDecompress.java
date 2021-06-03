import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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

		StringBuilder sb = new StringBuilder();

		Scanner sc = new Scanner(compressed).useDelimiter("\\[|\\||\\]");
		ArrayList<String> charList = new ArrayList<>();

		int count = 0;
		while (sc.hasNext()) {
			// System.out.println(sc.next());
			System.out.println(Arrays.asList(charList));

			int reach = sc.nextInt();
			int len = sc.nextInt();
			String c = sc.next();
			sc.next();

			if (reach == 0) {
				sb.append(c);
				charList.add(c);
			} else {
				for (int i = count - reach; i < len; i++) {
					sb.append(charList.get(i));
					sb.append(c);
					charList.add(charList.get(i));
					charList.add(c);
				}
			}
			count++;
		}

		// TODO (currently just returns the argument).
		return sb.toString();
	}
}
