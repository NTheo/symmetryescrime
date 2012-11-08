import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * signatures for rigid transformations
 * @author Antoine & NTheo (2012)
 *
 */
public class Sign7 extends Sign {

	Sign7(double threshold) {
		super(threshold);
	}

	private double r;//r=k1/k2
	
	void computeSign(SurfaceMesh m, Vertex<Point_3> v, double radius) {
		// TODO compute r
	}

	@Override
	boolean isValid() {
		return r<threshold;
	}

}
