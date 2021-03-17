import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * A Node represents a stop in the JourneyPlanner. It stores all the important
 * information as well as methods to redraw and set the status
 * 
 * @author menzieleon
 *
 */
public class Node {

	private final int NODE_SIZE = 5;

	// Fields for the adjacency list information
	public List<Edge> adjListIncoming;
	public List<Edge> adjListOutgoing;
	// All trips that this stop is apart of
	public List<String> trips;
	public Location loc;

	private String nodeId;
	private String nodeName;
	private Color c;

	public Node(String stopId, String stopName, Location loc) {
		this.nodeId = stopId;
		this.nodeName = stopName;
		this.loc = loc;
		this.adjListIncoming = new ArrayList<>();
		this.adjListOutgoing = new ArrayList<>();
		this.trips = new ArrayList<>();
		this.c = new Color(0, 0, 255);
	}

	/**
	 * Used for drawing the Node on the map
	 * 
	 * @param g      - The Graphics
	 * @param origin - an origin point to draw from
	 * @param scale  - the scale for change when map is scaled
	 */
	public void draw(Graphics g, Location origin, double scale) {
		g.setColor(c);
		g.fillOval(loc.asPoint(origin, scale).x - NODE_SIZE / 2, loc.asPoint(origin, scale).y - NODE_SIZE / 2,
				NODE_SIZE, NODE_SIZE);
	}

	/**
	 * If the current stop needs to be highlighted it can be changed in this method
	 * 
	 * @param b - true or false for Nodes status
	 */
	public void setStatus(boolean b) {
		if (b) {
			c = new Color(255, 0, 0);
		} else {
			c = new Color(0, 0, 255);
		}

	}

	/**
	 * Getters and setters
	 */
	public void addTrip(String s) {
		trips.add(s);
	}

	public List<String> getTripId() {
		return trips;
	}

	public String getNodeName() {
		return nodeName;
	}

	@Override
	public String toString() {
		return "Node [stopId=" + nodeId + ", stopName=" + nodeName + ", loc=" + loc + "]";
	}
}
