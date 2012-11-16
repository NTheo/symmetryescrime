import Jcg.geometry.*;
import Jcg.polyhedron.*;

/**
 * Abstract class for point signatures. Classes change with the different groups of transformations needed.
 * @author Antoine & NTheo (2012)
 *
 */
public interface Sign{

	final double threshold = Parameters.pruningThreshold;
	final double radius = Parameters.curvatureCalculationRadius; 

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

}
