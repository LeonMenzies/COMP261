
/**
 * @author leon
 *
 *         The restriction class stores all of the maps restrictions with a
 *         simple method that takes a node as the intersection and its previous
 *         node it has come from. It can then check it against the restriction
 *         to find out if it can travel to the next node;
 */
public class Restriction {

	Node intersection;
	Node nodeId1;
	Node nodeId2;
	Road roadId1;
	Road roadId2;

	public Restriction(Node intersection, Node nodeId1, Node nodeId2, Road roadId1, Road roadId2) {
		this.intersection = intersection;
		this.nodeId1 = nodeId1;
		this.nodeId2 = nodeId2;
		this.roadId1 = roadId1;
		this.roadId2 = roadId2;
	}

	public boolean check(Node from, Node intersection, Node to) {

		if (from.equals(this.nodeId1) && intersection.equals(this.intersection) && to.equals(this.nodeId2)) {
			return true;
		} else {
			return false;
		}
	}
}
