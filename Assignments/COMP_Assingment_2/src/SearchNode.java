
public class SearchNode {

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public Double getG() {
		return g;
	}

	public void setG(Double g) {
		this.g = g;
	}

	public Double getF() {
		return f;
	}

	public void setF(Double f) {
		this.f = f;
	}

	public SearchNode(Node node, Node prev, Double g, Double f) {
		super();
		this.node = node;
		this.prev = prev;
		this.g = g;
		this.f = f;
	}

	private Node node;
	private Node prev;

	private Double g;
	private Double f;

}
