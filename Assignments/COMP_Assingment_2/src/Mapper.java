import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.swing.JButton;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 * 
 * @author tony
 */
public class Mapper extends GUI {
	public static final Color NODE_COLOUR = new Color(77, 113, 255);
	public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
	public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);

	// these two constants define the size of the node squares at different zoom
	// levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
	// log(scale)
	public static final int NODE_INTERCEPT = 1;
	public static final double NODE_GRADIENT = 0.8;

	// defines how much you move per button press, and is dependent on scale.
	public static final double MOVE_AMOUNT = 100;
	// defines how much you zoom in/out per button press, and the maximum and
	// minimum zoom levels.
	public static final double ZOOM_FACTOR = 1.3;
	public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

	// how far away from a node you can click before it isn't counted.
	public static final double MAX_CLICKED_DISTANCE = 0.30;

	// these two define the 'view' of the program, ie. where you're looking and
	// how zoomed in you are.
	private Location origin;
	private double scale;
	private Point p = null;

	// our data structures.
	private Graph graph;
	private Trie trie;

	// A* seach nodes
	private boolean aStarToggle = false;
	private Node start = null;
	private Node end = null;

	@Override
	protected void redraw(Graphics g) {
		if (graph != null)
			graph.draw(g, getDrawingAreaDimension(), origin, scale);
	}

	@Override
	protected void onClick(MouseEvent e) {

		// Check for mouse drag
		if (p != null) {
			p = null;
			return;
		}

		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;

		for (Node node : graph.nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}

		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
			graph.setHighlight(closest);
			getTextOutputArea().append(closest.toString() + "\n");

			graph.setHighlight(new ArrayList<>());

			// If a star search is enabled
			if (aStarToggle) {
				if (start == null) {
					start = closest;
					graph.startHighlight(closest);
					graph.endHighlight(null);
					graph.highlightedSegments = new HashSet<>();
				} else {
					end = closest;
					graph.endHighlight(closest);
					aStarSearch();
				}
			}
		}
	}

	private void aStarSearch() {
		ArrayList<Node> visited = new ArrayList<>();
		PriorityQueue<SearchNode> fringe = new PriorityQueue<>();
		fringe.add(new SearchNode(start, null, 0.0, h(start), null));

		while (!fringe.isEmpty()) {
			SearchNode s = fringe.poll();
			Node n = s.node;

			if (!visited.contains(n)) {
				visited.add(n);

				// If the destination is found
				if (n.equals(end)) {

					showPath(s);
					return;
				}

				for (Segment seg : n.outGoingSegments) {

					Node neighbour;
					if (seg.start.equals(n)) {
						neighbour = seg.end;
					} else {
						neighbour = seg.start;
					}

					if (!visited.contains(neighbour)) {

						double g = s.g + seg.length;
						double f = g + h(neighbour);

						fringe.add(new SearchNode(neighbour, s, g, f, seg));
						fringe.add(new SearchNode(neighbour, s, g, f, seg));
					}
				}
			}
		}

		// If there is no connecting paths between
		System.err.println("No valid path found");
		start = null;
		graph.aStarStartHighlight = null;
		end = null;
		graph.aStarEndHighlight = null;

	}

	public void showPath(SearchNode search) {
		List<Segment> highLightPath = new ArrayList<>();

		// Find path
		SearchNode path = search;

		while (!path.node.equals(start)) {

			highLightPath.add(path.segment);

			path = path.prev;
		}

		Collections.reverse(highLightPath);

		for (Segment s : highLightPath) {
			getTextOutputArea().append(s.road.toString() + "\n");
		}

		graph.setHighlightSeg(highLightPath);

		start = null;
		end = null;

	}

	@Override
	protected void onAStar(JButton aps) {

		aStarToggle = !aStarToggle;

		// Remove all colored nodes and roads
		graph.endHighlight(null);
		graph.startHighlight(null);
		graph.setHighlight(new ArrayList<>());

		if (aStarToggle) {
			aps.setForeground(Color.GREEN);
		} else {
			aps.setForeground(Color.RED);
		}
	}

	/**
	 * @param n
	 * @return - Distance as a double
	 * 
	 *         Find the heuristic value from a node to the end
	 */
	public double h(Node n) {

		double ac = Math.abs(end.getLoc().x - n.getLoc().x);
		double cb = Math.abs(end.getLoc().y - n.getLoc().y);

		return Math.hypot(ac, cb);
	}

	@Override
	protected void onAPs() {

		if (!graph.nodes.isEmpty()) {
			// Reset the depth of each node so it can be re-searched
			for (Map.Entry<Integer, Node> resetAp : graph.nodes.entrySet()) {
				resetAp.getValue().depth = -1;
			}

			List<Node> APs = new ArrayList<>();

			Node root = graph.nodes.get(graph.nodes.keySet().toArray()[0]);
			root.depth = 0;
			int numSubTrees = 0;

			for (Segment s : root.outGoingSegments) {

				// Check both ends of the segment
				if (s.start.equals(root)) {
					if (s.end.depth == -1) {
						System.out.println("Here");

						iterArtPts(new APNode(s.end, 1, root));
						numSubTrees++;
					}
				} else {
					if (s.start.depth == -1) {
						System.out.println(s);
						iterArtPts(new APNode(s.start, 1, root));

						numSubTrees++;
					}
				}

				if (numSubTrees > 1) {
					APs.add(root);
				}
			}

		}
	}

	private void iterArtPts(APNode apn) {

		List<Node> apNodes = new ArrayList<>();

		Stack<APNode> fringe = new Stack<APNode>();
		fringe.add(apn);

		while (!fringe.isEmpty()) {
			APNode peek = fringe.peek();
			Node n = peek.node;
			int d = peek.depth;

			if (n.depth == -1) {
				n.depth = d;
				n.reachBack = d;
				for (Segment s : n.outGoingSegments) {
					if (s.end == n) {
						if (!s.start.equals(peek.root)) {
							n.children.add(s.start);
						}
					} else if (s.start == n) {
						if (!s.end.equals(peek.root)) {
							n.children.add(s.end);
						}
					}
				}

			} else if (!n.children.isEmpty()) {
				Node child = n.children.remove(0);
				if (child.depth < -1) {
					n.reachBack = child.depth;
				} else {
					fringe.push(new APNode(child, d + 1, n));
				}
			} else {
				if (!n.equals(apn.node)) {
					peek.root.reachBack = n.reachBack;
					if (n.reachBack >= peek.root.depth) {
						apNodes.add(peek.root);
					}
				}
				fringe.remove(peek);
			}

		}
		graph.setHighlightAP(apNodes);
		System.out.println(apNodes.size());

	}

	@Override
	protected void onSearch() {
		if (trie == null)
			return;

		// get the search query and run it through the trie.
		String query = getSearchBox().getText();
		Collection<Road> selected = trie.get(query);

		// figure out if any of our selected roads exactly matches the search
		// query. if so, as per the specification, we should only highlight
		// exact matches. there may be (and are) many exact matches, however, so
		// we have to do this carefully.
		boolean exactMatch = false;
		for (Road road : selected)
			if (road.name.equals(query))
				exactMatch = true;

		// make a set of all the roads that match exactly, and make this our new
		// selected set.
		if (exactMatch) {
			Collection<Road> exactMatches = new HashSet<>();
			for (Road road : selected)
				if (road.name.equals(query))
					exactMatches.add(road);
			selected = exactMatches;
		}

		// set the highlighted roads.
		graph.setHighlight(selected);

		// now build the string for display. we filter out duplicates by putting
		// it through a set first, and then combine it.
		Collection<String> names = new HashSet<>();
		for (Road road : selected)
			names.add(road.name);
		String str = "";
		for (String name : names)
			str += name + "; ";

		if (str.length() != 0)
			str = str.substring(0, str.length() - 2);
		getTextOutputArea().setText(str);
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.NORTH) {
			origin = origin.moveBy(0, MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.SOUTH) {
			origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.EAST) {
			origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.WEST) {
			origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.ZOOM_IN) {
			if (scale < MAX_ZOOM) {
				// yes, this does allow you to go slightly over/under the
				// max/min scale, but it means that we always zoom exactly to
				// the centre.
				scaleOrigin(true);
				scale *= ZOOM_FACTOR;
			}
		} else if (m == GUI.Move.ZOOM_OUT) {
			if (scale > MIN_ZOOM) {
				scaleOrigin(false);
				scale /= ZOOM_FACTOR;
			}
		}
	}

	/**
	 * Using the mouse dragged function in java swing calculate the difference in a
	 * drag and move the map
	 */
	@Override
	protected void onMouseDragg(MouseEvent e) {

		if (p == null) {
			p = e.getPoint();
		}
		// calculate the mouse moved amount then change the origin
		double dx = e.getX() - p.x;
		double dy = e.getY() - p.y;

		origin = origin.moveBy(-dx / scale, dy / scale);

		p = e.getPoint();

	}

	@Override
	protected void onMouseScroll(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			onMove(Move.ZOOM_IN);
		} else {
			onMove(Move.ZOOM_OUT);
		}

	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		graph = new Graph(nodes, roads, segments, polygons);
		trie = new Trie(graph.roads.values());
		origin = new Location(-250, 250); // close enough
		scale = 1;
	}

	/**
	 * This method does the nasty logic of making sure we always zoom into/out of
	 * the centre of the screen. It assumes that scale has just been updated to be
	 * either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR (zooming out).
	 * The passed boolean should correspond to this, ie. be true if the scale was
	 * just increased.
	 */
	private void scaleOrigin(boolean zoomIn) {
		Dimension area = getDrawingAreaDimension();
		double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

		int dx = (int) ((area.width - (area.width * zoom)) / 2);
		int dy = (int) ((area.height - (area.height * zoom)) / 2);

		origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
	}

	public static void main(String[] args) {
		Mapper m = new Mapper();
		m.onLoad(new File(
				"/Users/leon/Documents/GitHub/COMP261/Assignments/COMP_Assingment_2/data/large/nodeID-lat-lon.tab"),
				new File(
						"/Users/leon/Documents/GitHub/COMP261/Assignments/COMP_Assingment_2/data/large/roadID-roadInfo.tab"),
				new File(
						"/Users/leon/Documents/GitHub/COMP261/Assignments/COMP_Assingment_2/data/large/roadSeg-roadID-length-nodeID-nodeID-coords.tab"),
				new File(
						"/Users/leon/Documents/GitHub/COMP261/Assignments/COMP_Assingment_2/data/large/polygon-shapes.mp"));
	}

}

// code for COMP261 assignments