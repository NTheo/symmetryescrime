import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * signatures for transformations: uniform scaling, rotation, translation
 * @author Antoine & NTheo (2012)
 *
 */
public class Sign6 extends Sign {

	Sign6(double threshold) {
		super(threshold);
	}

	public double k1;
	public double k2; //according to the paper, k2>=k1
	
	void computeSign(SurfaceMesh m, Vertex<Point_3> v, double radius) {
		// TODO compute k1 and k2
	}

	boolean isValid() {
		return (k1>0. && k2/k1 > threshold);
	}

}
