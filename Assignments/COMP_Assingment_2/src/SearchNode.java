
/**
 * @author menzieleon
 *
 *         This class is used to assist in the A* search algorithm
 * 
 */
public class SearchNode implements Comparable<SearchNode> {

	public Node node;
	public Node prev;

	public Double g;
	public Double f;

	public SearchNode(Node node, Node prev, Double g, Double f) {
		super();
		this.node = node;
		this.prev = prev;
		this.g = g;
		this.f = f;
	}

	@Override
	public int compareTo(SearchNode s) {
		return this.f.compareTo(s.f);
	}
}
