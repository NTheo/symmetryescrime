import Jcg.geometry.*;
import Jcg.polyhedron.*;

/**
 * Abstract class for point signatures. Classes change with the different groups of transformations needed.
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sign {

	protected static final double threshold = Parameters.pruningThreshold;
	protected static final double radius = Parameters.curvatureCalculationRadius; 

	Sign(){}
	/**
	 * computes the careterstic signature of the vertex
	 * @param m the mesh
	 * @param v the vertex that we want to caracterize
	 */
	abstract public void computeSign(SurfaceMesh m, Vertex v);
	/**
	 * @return returns true iff the signature is reliable for the computation of transformations 
	 */
	abstract public boolean isValid(); 

	protected final double[] signatures(SurfaceMesh m, Vertex v){
		//TODO to do
		return new double[2];
	}

}
