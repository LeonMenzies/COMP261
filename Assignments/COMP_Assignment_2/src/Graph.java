import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This represents the data structure storing all the roads, nodes, and
 * segments, as well as some information on which nodes and segments should be
 * highlighted.
 * 
 * @author tony
 */
public class Graph {
	// map node IDs to Nodes.
	Map<Integer, Node> nodes = new HashMap<>();
	// map road IDs to Roads.
	Map<Integer, Road> roads;
	// just some collection of Segments.
	Collection<Segment> segments;
	// polygon information
	Collection<PolygonShape> polygons;
	// Collection of restrictions
	Map<Node, Restriction> restrictions;

	Node highlightedNode;
	Node aStarStartHighlight;
	Node aStarEndHighlight;
	Collection<Road> highlightedRoads = new HashSet<>();
	Collection<Segment> highlightedSegments = new HashSet<>();
	Collection<Node> APHighlight = new HashSet<>();
	Collection<Segment> restrictionHighlight = new HashSet<>();

	public Graph(File nodes, File roads, File segments, File polygons, File restrictions) {
		this.nodes = Parser.parseNodes(nodes, this);
		this.roads = Parser.parseRoads(roads, this);
		this.segments = Parser.parseSegments(segments, this);
		if (polygons != null) {
			this.polygons = Parser.parsePolygons(polygons, this);
		}

		if (restrictions != null) {
			this.restrictions = Parser.parseRestrictions(restrictions, this);

			// Using the map of restrictions find the segments that connect the intersection
			// to node 1 and node 2 and add them to the restrictionsHighlight for drawing
			for (Map.Entry<Node, Restriction> s : this.restrictions.entrySet()) {

				for (Segment toDraw : s.getValue().intersection.outGoingSegments) {
					Node neighbour;
					if (toDraw.start.equals(s.getValue().intersection)) {
						neighbour = toDraw.end;
					} else {
						neighbour = toDraw.start;
					}

					if (neighbour.equals(s.getValue().nodeId1) || neighbour.equals(s.getValue().nodeId2)) {
						restrictionHighlight.add(toDraw);
					}
				}
			}
		}
	}

	public void draw(Graphics g, Dimension screen, Location origin, double scale) {
		// a compatibility wart on swing is that it has to give out Graphics
		// objects, but Graphics2D objects are nicer to work with. Luckily
		// they're a subclass, and swing always gives them out anyway, so we can
		// just do this.
		Graphics2D g2 = (Graphics2D) g;

		// draw polygons
		if (polygons != null) {
			for (PolygonShape p : polygons) {
				p.draw(g2, origin, scale);
			}
		}

		// draw all the segments.
		g2.setColor(Mapper.SEGMENT_COLOUR);
		for (Segment s : segments)
			s.draw(g2, origin, scale);

		// draw the restricted segments
		g2.setColor(Color.red);
		for (Segment seg : restrictionHighlight) {
			seg.draw(g2, origin, scale);
		}

		// highlight the roads for aStar
		g2.setColor(Mapper.HIGHLIGHT_COLOUR);
		g2.setStroke(new BasicStroke(3));
		for (Road road : highlightedRoads) {
			for (Segment seg : road.components) {
				seg.draw(g2, origin, scale);
			}
		}

		// draw the segments of all highlighted roads.
		g2.setColor(Mapper.HIGHLIGHT_COLOUR);
		g2.setStroke(new BasicStroke(3));
		for (Segment seg : highlightedSegments) {
			seg.draw(g2, origin, scale);
		}

		// draw all the nodes.
		g2.setColor(Mapper.NODE_COLOUR);
		for (Node n : nodes.values())
			n.draw(g2, screen, origin, scale);

		// Highlight the APNodes
		g2.setColor(Color.RED);
		for (Node n : APHighlight) {
			n.draw(g2, screen, origin, scale);
		}

		// draw the highlighted node, if it exists.
		if (highlightedNode != null) {
			g2.setColor(Mapper.HIGHLIGHT_COLOUR);
			highlightedNode.draw(g2, screen, origin, scale);
		}

		// Visuals for highlighting start and end node for a* search
		if (aStarStartHighlight != null) {
			g2.setColor(Color.GREEN);
			aStarStartHighlight.draw(g2, screen, origin, scale);
		}

		if (aStarEndHighlight != null) {
			g2.setColor(Color.RED);
			aStarEndHighlight.draw(g2, screen, origin, scale);
		}

	}

	public void setHighlight(Node node) {
		this.highlightedNode = node;
	}

	public void startHighlight(Node node) {
		this.aStarStartHighlight = node;
	}

	public void endHighlight(Node node) {
		this.aStarEndHighlight = node;
	}

	public void setHighlight(Collection<Road> roads) {
		this.highlightedRoads = roads;
	}

	public void setHighlightSeg(Collection<Segment> segments) {
		this.highlightedSegments = segments;
	}

	public void setHighlightAP(Collection<Node> APNodes) {
		this.APHighlight = APNodes;
	}

}

// code for COMP261 assignments