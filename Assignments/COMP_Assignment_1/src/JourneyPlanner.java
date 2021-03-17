import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main method for my journey planner program. This Class extends
 * GUI and which gives the program its graphical functionality
 * 
 * @author menzieleon
 *
 */
public class JourneyPlanner extends GUI {

	private final double ZOOM_FACTOR = 1.1;

	private double stepSize = 2;
	private double scale;
	private Location origin;
	private Point p = null;
	private Trie wordFinder;
	private HashMap<String, Node> nodes;
	private List<Edge> edges;
	private QuadTree tree;

	/**
	 * redraw calls goes through the edges and stops and calls there inbuilt redraw
	 * methods
	 */
	@Override
	protected void redraw(Graphics g) {
		if (nodes != null) {

			for (Edge e : edges) {
				e.draw(g, origin, scale);
			}
			for (Map.Entry<String, Node> n : nodes.entrySet()) {
				n.getValue().draw(g, origin, scale);

			}
		}
	}

	/**
	 * Method for searching through the QuadTree
	 * 
	 * @param treeSearch - The QuadTree
	 * @param l          - The clocked location
	 */
	public Node searchQuadTree(QuadTree treeSearch, Location l) {

		if (!treeSearch.isSplit) {
			return treeSearch.getCurrent();
		}

		if (treeSearch.getnW().getBoundry().inside(l.x, l.y)) {
			return searchQuadTree(treeSearch.getnW(), l);
		}
		if (treeSearch.getnE().getBoundry().inside(l.x, l.y)) {
			return searchQuadTree(treeSearch.getnE(), l);
		}
		if (treeSearch.getsW().getBoundry().inside(l.x, l.y)) {
			return searchQuadTree(treeSearch.getsW(), l);
		}
		if (treeSearch.getsE().getBoundry().inside(l.x, l.y)) {
			return searchQuadTree(treeSearch.getsE(), l);
		}

		return null;
	}

	/**
	 * The onClick method is responsible for finding the closest stop to the trip
	 */
	@Override
	protected void onClick(MouseEvent e) {

		Location clickLocation = Location.newFromPoint(e.getPoint(), origin, scale);

		// Check for mouse drag
		if (p != null) {
			p = null;
			return;
		}

		// Linear search
//		 double dist = Double.MAX_VALUE;
//		 Node closest = null;
//		for (Map.Entry<String, Node> n : nodes.entrySet()) {
//			n.getValue().setStatus(false);
//
//			double distFound = clickLocation.distance(n.getValue().loc);
//			if (distFound < dist) {
//				dist = distFound;
//
//				closest = n.getValue();
//			}
//		}

		// Quad tree search
		Node closest = searchQuadTree(this.tree, clickLocation);

		resetStatus();
		if (closest != null) {
			closest.setStatus(true);
			getTextOutputArea().setText("Stop: " + closest.getNodeName() + "\nTrips: ");

			for (Edge e1 : edges) {
				e1.setStatus(false);
			}
			for (String s : closest.getTripId()) {

				getTextOutputArea().append(s + ", ");

				highlightToTrip(s, closest.adjListOutgoing, new ArrayList<>());
				highlightFromTrip(s, closest.adjListIncoming, new ArrayList<>());

			}
		}
	}

	/**
	 * Simple method for resetting the status of the map by removing any
	 * highlighting
	 */
	public void resetStatus() {
		for (Map.Entry<String, Node> n : nodes.entrySet()) {
			n.getValue().setStatus(false);
		}

		for (Edge e : edges) {
			e.setStatus(false);
		}
	}

	/**
	 * This method interacts with the wordFinder Trie with the prefix to get all
	 * related Stops
	 */
	@Override
	protected void onSearch() {
		List<Node> foundNodes = wordFinder.getAll(getSearchBox().getText().toLowerCase().toCharArray());
		getTextOutputArea().setText(null);

		// Remove any highlighting
		resetStatus();

		if (!getSearchBox().getText().isEmpty()) {
			if (foundNodes != null) {
				for (Node n : foundNodes) {
					n.setStatus(true);
					for (String s : n.getTripId()) {
						// Call the recursive method on this node for outgoing and incoming edges
						highlightToTrip(s, n.adjListOutgoing, new ArrayList<>());
						highlightFromTrip(s, n.adjListIncoming, new ArrayList<>());
					}
					getTextOutputArea().append(n.getNodeName() + "\n");
				}
			} else {
				getTextOutputArea().setText("Nothing Found");
			}
		}
	}

	/*
	 * Recursive methods for highlighting trips using the adjacency list
	 */
	private void highlightToTrip(String tripId, List<Edge> edges, List<Edge> visited) {
		for (Edge e : edges) {
			// Check if the toNode is apart of the trip before highlighting the edge between
			// them
			if (e.getToNode().trips.contains(tripId) && !visited.contains(e)) {
				visited.add(e);
				e.setStatus(true);
				highlightToTrip(tripId, e.getToNode().adjListOutgoing, visited);
			}
		}
	}

	private void highlightFromTrip(String tripId, List<Edge> edges, List<Edge> visited) {
		for (Edge e : edges) {

			if (e.getFromNode().trips.contains(tripId) && !visited.contains(e)) {
				visited.add(e);
				e.setStatus(true);
				highlightFromTrip(tripId, e.getFromNode().adjListIncoming, visited);
			}

		}
	}

	/**
	 * From the GUI input move or scale the map accordingly
	 */
	@Override
	protected void onMove(GUI.Move m) {

		switch (m) {
		case SOUTH:
			origin = origin.moveBy(0, -stepSize);
			break;
		case NORTH:
			origin = origin.moveBy(0, stepSize);
			break;
		case EAST:
			origin = origin.moveBy(stepSize, 0);
			break;
		case WEST:
			origin = origin.moveBy(-stepSize, 0);
			break;
		case ZOOM_IN:

			zoomIn();
			break;
		case ZOOM_OUT:

			zoomOut();
			break;
		}
	}

	/**
	 * Takes the scroll input from the user and zooms in or out accordingly
	 */
	@Override
	protected void onMouseScroll(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			zoomIn();
		} else {
			zoomOut();
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

	/**
	 * Zoom in and out calculates the change in dx, dy and then moves everything
	 * Respectively on zoom
	 */
	private void zoomOut() {
		double heightDx = getDrawingAreaDimension().getHeight() / scale;
		double widthDx = getDrawingAreaDimension().getWidth() / scale;
		double dy = (heightDx - heightDx * ZOOM_FACTOR) / 2;
		double dx = (widthDx - widthDx * ZOOM_FACTOR) / 2;

		origin = origin.moveBy(dx, -dy);
		stepSize *= 1.1;
		scale = scale / ZOOM_FACTOR;
	}

	private void zoomIn() {
		double heightDx = getDrawingAreaDimension().getHeight() / scale;
		double widthDx = getDrawingAreaDimension().getWidth() / scale;
		double dy = (heightDx - heightDx / ZOOM_FACTOR) / 2;
		double dx = (widthDx - widthDx / ZOOM_FACTOR) / 2;

		origin = origin.moveBy(dx, -dy);
		stepSize /= 1.1;
		scale = scale * ZOOM_FACTOR;
	}

	/*
	 * Loads in files and creates the nodes and edge. Makes a new graph with the
	 * information
	 */
	@Override
	protected void onLoad(File stopFile, File tripFile) {
		nodes = new HashMap<String, Node>();
		edges = new ArrayList<>();

		wordFinder = new Trie();

		List<Double> xValues = new ArrayList<>();
		List<Double> yValues = new ArrayList<>();

		// Read stopFile and construct nodes
		try (BufferedReader stopFileReader = new BufferedReader(new FileReader(stopFile))) {

			String line = stopFileReader.readLine();
			while ((line = stopFileReader.readLine()) != null) {
				// Split by the tab space into an array
				String[] split = line.split("\t");

				// Make a new location from the lat and lon values
				Location l = Location.newFromLatLon(Double.parseDouble(split[split.length - 2]),
						Double.parseDouble(split[split.length - 1]));

				// Add the values to lists so the min and max can be found
				xValues.add(l.x);
				yValues.add(l.y);

				Node n = new Node(split[0], split[1], l);

				nodes.put(split[0], n);

				// Add to the wordFinder Trie for prefix searching
				wordFinder.add(n.getNodeName().toLowerCase().toCharArray(), n);

			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		// Read tripFile and construct trips
		try (BufferedReader tripFileReader = new BufferedReader(new FileReader(tripFile))) {
			String line = tripFileReader.readLine();
			while ((line = tripFileReader.readLine()) != null) {
				// Split the line by the tab spacing in an array
				String[] split = line.split("\t");

				String tripId = split[0];

				// For each connection between stops create a new edge and add to the edge List
				for (int i = 1; i < split.length - 1; i++) {
					nodes.get(split[i]).addTrip(tripId);
					edges.add(new Edge(nodes.get(split[i]), nodes.get(split[i + 1])));
				}
				// Add the trip id to the nodes
				nodes.get(split[split.length - 1]).addTrip(tripId);
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		// Creates the adjacency list by adding each edge to the nodes incoming and
		// outgoing edges
		for (Edge edge : edges) {
			edge.getToNode().adjListIncoming.add(edge);
			edge.getFromNode().adjListOutgoing.add(edge);
		}

		// Make a quad tree using min and max values
		tree = new QuadTree(new Boundry(Collections.min(xValues), Collections.min(yValues), Collections.max(xValues),
				Collections.max(yValues)));

		// Insert each Node into the QuadTree
		for (Map.Entry<String, Node> addToQuad : nodes.entrySet()) {
			tree.insert(addToQuad.getValue());
		}

		// Calculate the scale of the graph using the window width
		scale = getDrawingAreaDimension().getWidth() / (Collections.max(xValues) - Collections.min(xValues));
		// Calculate the origin
		origin = new Location((Collections.min(xValues) - Collections.max(xValues)) / 2,
				(Collections.max(yValues) - Collections.min(yValues)) / 2);

	}

	public static void main(String[] args) {
		new JourneyPlanner();
	}
}
