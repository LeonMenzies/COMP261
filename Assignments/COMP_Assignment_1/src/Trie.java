import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a trie class used to store the characters of the stops in separate
 * branches with any complete nodes storing the stops themselves. The trie can
 * then be accessed with a prefix to return all stops
 * 
 * @author leon
 *
 */
public class Trie {

	private List<Node> stops;
	private HashMap<Character, Trie> children;

	public Trie() {
		children = new HashMap<>();
		stops = new ArrayList<>();
	}

	/**
	 * Taks a char array of the stop name, iterates it adding each char in the
	 * Hashmap with a corresponding branch
	 * 
	 * @param word - the stop name
	 * @param stop - the stop itself
	 */
	public void add(char[] word, Node stop) {
		Trie t = this;

		for (char c : word) {
			if (!t.children.containsKey(c)) {
				t.children.put(c, new Trie());

			}
			t = t.children.get(c);

		}
		t.stops.add(stop);

	}

	/**
	 * This method can return a stop using the full name
	 * 
	 * @param word - The full name of a stop
	 * @return - The found stop
	 */
	public List<Node> get(char[] word) {
		Trie t = this;

		for (char c : word) {
			if (!t.children.containsKey(c)) {
				return null;
			}
			t = t.children.get(c);

		}
		return t.stops;
	}

	/**
	 * Uses a recursive helper method to check any branch matching the given prefix
	 * 
	 * @param prefix - a prefix of any stop name
	 * @return a list of the matching stops
	 */
	public List<Node> getAll(char[] prefix) {
		List<Node> results = new ArrayList<Node>();
		Trie t = this;

		for (char c : prefix) {
			if (!t.children.containsKey(c)) {
				return null;
			}
			t = t.children.get(c);

		}

		getAllFrom(t, results);
		return results;

	}

	// Recursive helper method
	private void getAllFrom(Trie node, List<Node> results) {
		results.addAll(node.stops);

		for (Map.Entry<Character, Trie> s : node.children.entrySet()) {
			getAllFrom(s.getValue(), results);

		}
	}
}
