import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * Abstract class for point signatures. Classes change with the different groups of transformations needed.
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sign {
	protected double threshold;
	Sign(double threshold){this.threshold=threshold;}
	abstract void computeSign(SurfaceMesh m, Vertex<Point_3> v, double radius);
	abstract boolean isValid(); 
}
