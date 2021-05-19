import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Node represents an intersection in the road graph. It stores its ID and its
 * location, as well as all the segments that it connects to. It knows how to
 * draw itself, and has an informative toString method.
 * 
 * @author tony
 */
public class Node implements Comparable<Node> {

	public final int nodeID;
	public final Location location;
	public final Collection<Segment> outGoingSegments;
	public final Collection<Segment> inComingSegments;

	public Node prev;
	public Integer depth;
	public int reachBack;
	public PriorityQueue<Node> children;

	public Node(int nodeID, double lat, double lon) {
		this.nodeID = nodeID;
		this.location = Location.newFromLatLon(lat, lon);
		this.outGoingSegments = new HashSet<Segment>();
		this.inComingSegments = new HashSet<Segment>();
		children = new PriorityQueue<>();
	}

	public void addInComingSegment(Segment seg) {
		inComingSegments.add(seg);
	}

	public void addOutGoingSegment(Segment seg) {
		outGoingSegments.add(seg);
	}

	public void draw(Graphics g, Dimension area, Location origin, double scale) {
		Point p = location.asPoint(origin, scale);

		// for efficiency, don't render nodes that are off-screen.
		if (p.x < 0 || p.x > area.width || p.y < 0 || p.y > area.height)
			return;

		int size = (int) (Mapper.NODE_GRADIENT * Math.log(scale) + Mapper.NODE_INTERCEPT);
		g.fillRect(p.x - size / 2, p.y - size / 2, size, size);
	}

	public Location getLoc() {
		return this.location;
	}

	@Override
	public int compareTo(Node n) {
		return this.depth.compareTo(n.depth);
	}

	public String toString() {
		Set<String> edges = new HashSet<String>();
		for (Segment s : outGoingSegments) {
			if (!edges.contains(s.road.name))
				edges.add(s.road.name);
		}

		String str = "ID: " + nodeID + "  loc: " + location + "\nroads: ";
		for (String e : edges) {
			str += e + ", ";
		}
		return str.substring(0, str.length() - 2);
	}

}

// code for COMP261 assignments