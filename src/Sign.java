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

	abstract public void computeSign(SurfaceMesh m, Vertex<Point_3> v);
	abstract public boolean isValid(); 

	protected final double[] signatures(SurfaceMesh m, Vertex<Point_3> v){
		//TODO to do
		return new double[2];
	}
	/**
	 * @param e halfedge
	 * @return the associated beta number as defined in Alliez & Cie
	 */
	private static final double beta(Halfedge<Point_3> e){
		//TODO replace normal(e.getFace()) by sth like normalArray.get(e.getFace().index)
		Vector_3 c = normal(e.getFace()).crossProduct(normal(e.getOpposite().getFace()));
		double angle = Math.asin(Math.sqrt(c.squaredLength().doubleValue()));
		return (c.innerProduct(vectorOfEdge(e)).doubleValue()<0)?-angle:angle;
	}
	/**
	 * @param f face
	 * @return the associated normal vector
	 */
	private static final Vector_3 normal(Face<Point_3> f){
		Vector_3 v = vectorOfEdge(f.getEdge()).crossProduct(vectorOfEdge(f.getEdge().getNext()));
		double l = Math.sqrt((v.squaredLength().doubleValue()));
		return (l>0.)?v.divisionByScalar(l):v;
	}
	/**
	 * @param e halfedge
	 * @return the associated vector
	 */
	private static final Vector_3 vectorOfEdge(Halfedge<Point_3> e){
		return new Vector_3(e.getOpposite().getVertex().getPoint(), e.getVertex().getPoint());
	}
	/**
	 *  @param f face (triangle only)
	 *  @return the associated area
	 */
	private static final double areaOfFace(Face<Point_3> f){
		Vector_3 v = vectorOfEdge(f.getEdge()).crossProduct(vectorOfEdge(f.getEdge().getNext()));
		return v.squaredLength().doubleValue()/2.;
	}
}
