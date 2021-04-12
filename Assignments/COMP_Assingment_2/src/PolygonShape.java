import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * @author leon
 *
 *         The PolygonShape class stores all the information loaded from the
 *         polygon file to draw the polygons on the map
 */
public class PolygonShape {

	private Color c;
	private final ArrayList<Location> polyCoords;
	private Polygon p;

	public PolygonShape(String type, ArrayList<Location> polyCoords) {

		c = Color.decode(type);
		c = c.brighter();

		this.polyCoords = polyCoords;
	}

	public void draw(Graphics g, Location origin, double scale) {
		g.setColor(c);
		p = new Polygon();

		for (Location l : polyCoords) {
			p.addPoint(l.asPoint(origin, scale).x, l.asPoint(origin, scale).y);
		}

		g.fillPolygon(p);

	}
}
