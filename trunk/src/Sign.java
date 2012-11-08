import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;


public abstract class Sign {
	Sign(){}
	abstract void computeSign(SurfaceMesh m, Vertex<Point_3> v, float radius);
	abstract boolean isValid(); 
}
