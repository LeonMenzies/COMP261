
/**
 * A Boundary simply the boundary of a quad in the QuadTree
 * 
 * @author menzieleon
 *
 */
public class Boundry {

	private double xMin, yMin, xMax, yMax;

	public Boundry(double xMin, double yMin, double xMax, double yMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	/**
	 * Getters
	 */
	public double getxMin() {
		return xMin;
	}

	public double getyMin() {
		return yMin;
	}

	public double getxMax() {
		return xMax;
	}

	public double getyMax() {
		return yMax;
	}

	/**
	 * Check a location of x and y is the boundary
	 * 
	 * @param x
	 * @param y
	 * @return boolean - if the x,y is in the range
	 */
	public boolean inside(double x, double y) {
		return (x >= this.getxMin() && x <= this.getxMax() && y >= this.getyMin() && y <= this.getyMax());
	}
}
