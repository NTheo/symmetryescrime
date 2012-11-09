import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * Abstract class for point signatures. Classes change with the different groups of transformations needed.
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sign {
	protected static final double threshold = 0.75; //0.75 to be replaced by sth like "MagicNumbers.pruningThreshold"
	protected static final double radius = 10.; //idem
	
	Sign(){}
	
	abstract void computeSign(SurfaceMesh m, Vertex<Point_3> v);
	abstract boolean isValid(); 

	double[] signatures(SurfaceMesh m, Vertex<Point_3> v){
		//TODO to do
		return new double[2];
	}
	
	}
