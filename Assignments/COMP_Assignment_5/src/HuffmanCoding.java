import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Please call this program with two arguments, which are "
					+ "the input file name and either 0 for constructing tree and printing it, or "
					+ "1 for constructing the tree and encoding the file and printing it, or "
					+ "2 for constructing the tree, encoding the file, and then decoding it and "
					+ "printing the result which should be the same as the input file.");
		} else {
			try {
				Scanner s = new Scanner(new File(args[0]));

				// Read the entire file into one String.
				StringBuilder fileText = new StringBuilder();
				while (s.hasNextLine()) {
					fileText.append(s.nextLine() + "\n");
				}

				if (args[1].equals("0")) {
					System.out.println(constructTree(fileText.toString()));
				} else if (args[1].equals("1")) {
					constructTree(fileText.toString()); // initialises the tree field.
					System.out.println(encode(fileText.toString()));
				} else if (args[1].equals("2")) {
					constructTree(fileText.toString()); // initialises the tree field.
					String codedText = encode(fileText.toString());
					// DO NOT just change this code to simply print fileText.toString() back. ;-)
					System.out.println(decode(codedText));
				} else {
					System.out.println("Unknown second argument: should be 0 or 1 or 2");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find file called " + args[0]);
			}
		}
	}

	private static HuffmanTree tree; // Change type from Object to HuffmanTree or appropriate type you design.

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public static Map<Character, String> constructTree(String text) {

		text = "hello world my name is leon and this is test";

		Map<Character, Integer> freq = new HashMap<>();
		Map<Character, String> toReturn = new HashMap<>();
		PriorityQueue<HuffmanTree> charNodes = new PriorityQueue<>();

		for (char c : text.toCharArray()) {
			if (freq.containsKey(c)) {
				freq.put(c, freq.get(c) + 1);
			} else {
				freq.put(c, 1);
			}
		}

		// Add all the values into the priority queue
		for (Map.Entry<Character, Integer> m : freq.entrySet()) {
			charNodes.add(new HuffmanTree(m.getKey(), m.getValue()));
		}

		// Make the tree
		while (charNodes.size() > 1) {
			HuffmanTree n1 = charNodes.poll();
			HuffmanTree n2 = charNodes.poll();

			HuffmanTree parent = new HuffmanTree('\0', n1.freq + n2.freq, n1, n2);
			charNodes.add(parent);

		}

		// Take the final node and make it the root
		tree = charNodes.poll();

		// Recursions to add byte values
		Stack<HuffmanTree> rec = new Stack<>();
		rec.add(tree);

		while (!rec.isEmpty()) {

			HuffmanTree hf = rec.pop();

			toReturn.put(hf.value, hf.binNum);

			if (hf.n1 == null) {
				continue;
			}

			rec.add(hf.n1.setBin(hf.binNum + "0"));
			rec.add(hf.n2.setBin(hf.binNum + "1"));

		}

		return toReturn;
	}

	/**
	 * Take an input string, text, and encode it with the tree computed from the
	 * text. Should return the encoded text as a binary string, that is, a string
	 * containing only 1 and 0.
	 */
	public static String encode(String text) {
		StringBuilder sb = new StringBuilder();

		// TODO fill this in.
		return "";
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree, and
	 * return the decoded text as a text string.
	 */
	public static String decode(String encoded) {
		// TODO fill this in.
		return "";
	}
}

class HuffmanTree implements Comparable<HuffmanTree> {

	public Character value;
	public Integer freq;
	public String binNum = "";

	public HuffmanTree n1;
	public HuffmanTree n2;

	public HuffmanTree(char v, int f) {
		this.value = v;
		this.freq = f;
	}

	public HuffmanTree(char v, int f, HuffmanTree n1, HuffmanTree n2) {
		this.value = v;
		this.freq = f;

		this.n1 = n1;
		this.n2 = n2;
	}

	public HuffmanTree setBin(String s) {
		binNum = s;
		return this;

	}

	@Override
	public int compareTo(HuffmanTree ht) {
		return this.freq.compareTo(ht.freq);
	}

	@Override
	public String toString() {
		return "[Value: " + value + ", Freq: " + freq + "]";
	}
}
