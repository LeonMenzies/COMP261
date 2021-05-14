
/**
 * @author menzieleon
 *
 *         Helper Node for AP search algorithm
 *
 */
public class APNode {

	public Node node;
	public int depth;
	public Node root;

	public APNode(Node node, int depth, Node root) {
		super();
		this.node = node;
		this.depth = depth;
		this.root = root;
	}
}
