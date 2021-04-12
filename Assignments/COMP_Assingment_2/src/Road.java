import java.util.Collection;
import java.util.HashSet;

/**
 * Road represents ... a road ... in our graph, which is some metadata and a
 * collection of Segments. We have lots of information about Roads, but don't
 * use much of it.
 * 
 * @author tony
 */
public class Road {

	public final int roadID;
	public final String name, city;
	public final Collection<Segment> components;
	public final int speed;
	public final int roadclass;

	public int speedLimit;

	public final boolean oneWay;

	public Road(int roadID, int type, String label, String city, int oneway, int speed, int roadclass, int notforcar,
			int notforpede, int notforbicy) {

		// Set one way boolean
		if (oneway == 0) {
			this.oneWay = false;
		} else {
			this.oneWay = true;
		}

		this.roadID = roadID;
		this.city = city;
		this.name = label;
		this.components = new HashSet<Segment>();
		this.speed = speed;
		this.roadclass = roadclass;

	}

	public void addSegment(Segment seg) {
		components.add(seg);
	}

	@Override
	public String toString() {
		return "Road: " + name;
	}

}

// code for COMP261 assignments