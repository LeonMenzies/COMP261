import java.awt.Color;
import java.awt.Graphics;

/**
 * An edge represents the connection between to Nodes in the map
 * 
 * @author menzieleon
 *
 */
public class Edge {

	private Node toNode;
	private Node fromNode;
	private Color c;

	public Edge(Node toNode, Node formNode) {
		this.toNode = toNode;
		this.fromNode = formNode;
		c = new Color(0, 0, 0);
	}

	/**
	 * Redraws the edges using the given parameters for the correct location and
	 * scaling
	 * 
	 * @param g      - The graphics object
	 * @param origin - The origin of the map
	 * @param scale  - The scale to draw by
	 */
	public void draw(Graphics g, Location origin, double scale) {
		g.setColor(c);
		g.drawLine(fromNode.loc.asPoint(origin, scale).x, fromNode.loc.asPoint(origin, scale).y,
				toNode.loc.asPoint(origin, scale).x, toNode.loc.asPoint(origin, scale).y);
	}

	/**
	 * Getters and setters
	 */
	public Node getToNode() {
		return toNode;
	}

	public Node getFromNode() {
		return fromNode;
	}

	/**
	 * If the current Node needs to be highlighted it can be changed in this method
	 * 
	 * @param b - true or false for Edges status
	 */
	public void setStatus(boolean b) {
		if (b) {
			c = new Color(0, 255, 0);
		} else {
			c = new Color(0, 0, 0);
		}
	}

	@Override
	public String toString() {
		return "Edge [toNode=" + toNode + ", formNode=" + fromNode + "]";
	}
}
