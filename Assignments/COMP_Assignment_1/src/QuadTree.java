/**
 * The QuadTree class
 * 
 * @author menzieleon
 *
 */
public class QuadTree {

	public Node current = null;
	private QuadTree nW = null;
	private QuadTree nE = null;
	private QuadTree sW = null;
	private QuadTree sE = null;
	private Boundry quad;
	public boolean isSplit = false;

	public QuadTree(Boundry boundry) {
		this.quad = boundry;
	}

	/**
	 * Split the current boundary into four and set them to this objects fields
	 */
	public void split(Node n) {

		double x = n.loc.x;
		double y = n.loc.y;

		double newX = this.quad.getxMin() + (this.quad.getxMax() - this.quad.getxMin()) / 2;
		double newY = this.quad.getyMin() + (this.quad.getyMax() - this.quad.getyMin()) / 2;

		this.nW = new QuadTree(new Boundry(this.quad.getxMin(), this.quad.getyMin(), newX, newY));
		this.nE = new QuadTree(new Boundry(newX, this.quad.getyMin(), this.quad.getxMax(), newY));
		this.sW = new QuadTree(new Boundry(this.quad.getxMin(), newY, newX, this.quad.getyMax()));
		this.sE = new QuadTree(new Boundry(newX, newY, this.quad.getxMax(), this.quad.getyMax()));

		if (this.nW.quad.inside(x, y)) {
			this.nW.current = n;
		} else if (this.nE.quad.inside(x, y)) {
			this.nE.current = n;
		} else if (this.sW.quad.inside(x, y)) {
			this.sW.current = n;
		} else if (this.sE.quad.inside(x, y)) {
			this.sE.current = n;
		}
	}

	/**
	 * This method takes a node and using its x and y location it will save it if
	 * the current QuadTree is empty or split the quad if not
	 * 
	 * @param n - the Node on the map
	 */
	public void insert(Node n) {

		double x = n.loc.x;
		double y = n.loc.y;

		if (!this.quad.inside(x, y)) {
			return;
		}

		if (this.current == null && !isSplit) {
			this.current = n;
			return;
		}

		if (!isSplit) {
			split(this.current);
			this.isSplit = true;
		}

		this.current = null;

		// Recursively insert the node into the correct boundary
		if (this.nW.quad.inside(x, y)) {
			this.nW.insert(n);
		} else if (this.nE.quad.inside(x, y)) {
			this.nE.insert(n);
		} else if (this.sW.quad.inside(x, y)) {
			this.sW.insert(n);
		} else if (this.sE.quad.inside(x, y)) {
			this.sE.insert(n);
		}
	}

	/**
	 * Field getters
	 */
	public Boundry getBoundry() {
		return quad;
	}

	public Node getCurrent() {
		return current;
	}

	public QuadTree getnW() {
		return nW;
	}

	public QuadTree getnE() {
		return nE;
	}

	public QuadTree getsW() {
		return sW;
	}

	public QuadTree getsE() {
		return sE;
	}

	@Override
	public String toString() {
		return "QuadTree [current=" + current + ", boundry=" + quad + "]";
	}

}
