
/**
 * @author menzieleon
 *
 *         This class is used to assist in the A* search algorithm
 * 
 */
public class SearchNode implements Comparable<SearchNode> {

	public Node node;
	public SearchNode prev;

	public Double g;
	public Double f;

	public Segment segment;

	public SearchNode(Node node, SearchNode prev, Double g, Double f, Segment seg) {
		super();
		this.node = node;
		this.prev = prev;
		this.g = g;
		this.f = f;
		this.segment = seg;
	}

	@Override
	public int compareTo(SearchNode s) {
		return this.f.compareTo(s.f);
	}
}
